import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.KotlinClosure1
import org.gradle.kotlin.dsl.fileTree
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.task
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class CommonBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.github.ben-manes.versions")
        target.plugins.apply("jacoco")

        target.extensions.findByType(BaseExtension::class)?.apply {
            setCompileSdkVersion(Versions.compileSdkVersion)
            buildToolsVersion(Deps.buildToolsVersion)
            defaultConfig {
                minSdkVersion(Versions.minSdkVersion)
                targetSdkVersion(Versions.targetSdkVersion)
                consumerProguardFiles("consumer-rules.pro")
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
            lintOptions {
                disable("GoogleAppIndexingWarning")
            }
            testOptions {
                unitTests.all {
                    testLogging { setEvents(TEST_LOGGING_EVENTS) }
                    extensions.configure(JacocoTaskExtension::class.java) {
                        isIncludeNoLocationClasses = true
                    }
                }
            }

            @Suppress("UnstableApiUsage")
            when (this) {
                is BaseAppModuleExtension -> {
                    buildFeatures {
                        viewBinding = true
                        dataBinding = true
                    }
                }
                is LibraryExtension -> {
                    buildFeatures {
                        buildConfig = false
                    }
                }
            }
        }

        target.tasks.withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
        target.run {
            task("jacocoTestReport", JacocoReport::class) {
                dependsOn("testDebugUnitTest")
                reports {
                    xml.isEnabled = true
                    html.isEnabled = true
                    csv.isEnabled = false
                }
                sourceDirectories.setFrom("$projectDir/src/main/java")
                classDirectories.setFrom(
                    fileTree(
                        "dir" to ".",
                        "includes" to listOf("**/tmp/kotlin-classes/debug/**"),
                        "excludes" to listOf(
                            // Android
                            "**/R.class",
                            "**/R$*.class",
                            "**/BuildConfig.*",
                            "**/Manifest*.*",
                            "**/*Test*.*",
                            "**/*Spec*.*",
                            "android/**/*.*",
                            "**/*Application.*",

                            // Dagger
                            "**/*Dagger*Component*.*",
                            "**/*Module.*",
                            "**/*Module$*.*",
                            "**/*MembersInjector*.*",
                            "**/*_Factory*.*",
                            "**/*Provide*Factory*.*"
                        )
                    )
                )
                executionData.setFrom(files("$buildDir/jacoco/testDebugUnitTest.exec"))
            }
        }
    }

    private fun UnitTestOptions.all(block: Test.() -> Unit) =
        all(KotlinClosure1<Any, Test>({ (this as Test).apply(block) }, owner = this))

    companion object {
        private val TEST_LOGGING_EVENTS = listOf(
            "passed",
            "skipped",
            "failed",
            "standardOut",
            "standardError"
        )
    }
}

const val dependencyUpdatesFormatter = "json"

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
