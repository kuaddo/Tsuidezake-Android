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
        target.extensions.findByType(BaseExtension::class)?.apply {
            setCompileSdkVersion(Versions.compileSdkVersion)
            buildToolsVersion(Deps.buildToolsVersion)
            defaultConfig {
                minSdkVersion(Versions.minSdkVersion)
                targetSdkVersion(Versions.targetSdkVersion)
                consumerProguardFiles("consumer-rules.pro")
            }
            target.tasks.withType<KotlinCompile> {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_1_8.toString()
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
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
    }
}
