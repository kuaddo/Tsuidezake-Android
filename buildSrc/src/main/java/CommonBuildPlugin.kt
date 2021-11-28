import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import groovy.lang.Closure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.fileTree
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.task
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

class CommonBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.github.ben-manes.versions")
        target.plugins.apply("jacoco")

        target.extensions.findByType(BaseExtension::class)?.apply {
            setCompileSdkVersion(Versions.compileSdkVersion)
            buildToolsVersion(Versions.buildToolsVersion)
            defaultConfig {
                minSdkVersion(Versions.minSdkVersion)
                targetSdkVersion(Versions.targetSdkVersion)
                consumerProguardFiles("consumer-rules.pro")
            }
            lintOptions {
                disable("GoogleAppIndexingWarning")
            }
            testOptions {
                unitTests.apply {
                    isIncludeAndroidResources = true
                    @Suppress("UNCHECKED_CAST")
                    all(
                        closureOf<Test> {
                            testLogging { setEvents(TEST_LOGGING_EVENTS) }
                            extensions.configure(JacocoTaskExtension::class.java) {
                                isIncludeNoLocationClasses = true
                                excludes?.add("jdk.internal.*")
                            }
                        } as Closure<Test>
                    )
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

        target.run {
            task("jacocoTestReport", JacocoReport::class) {
                dependsOn("testDebugUnitTest")
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    csv.required.set(false)
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

fun getRejectVersion(candidate: ModuleComponentIdentifier, currentVersion: String): Boolean {
    val isRejectedJacoco = candidate.group == "org.jacoco" && candidate.version != currentVersion
    return isNonStable(candidate.version) || isRejectedJacoco
}

private fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.toUpperCase(Locale.US).contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
