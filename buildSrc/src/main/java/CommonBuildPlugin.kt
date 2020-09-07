import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class CommonBuildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.github.ben-manes.versions")

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
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
