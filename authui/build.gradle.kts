import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

android {
    defaultConfig {
        val props = Properties().apply {
            load(FileInputStream(File(projectDir, "secret.properties")))
        }
        buildConfigField(
            "String",
            "GOOGLE_SIGN_IN_TOKEN",
            "\"${props.getProperty("googleSignInToken")}\""
        )
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))

    implementation(Deps.AndroidX.Lifecycle.runtime)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.Firebase.auth)
    implementation(Deps.gmsAuth)
    implementation(Deps.facebookLogin)

    implementation(Deps.Dagger.Hilt.android)
    kapt(Deps.Dagger.Hilt.compiler)

    implementation(Deps.timber)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        getRejectVersion(candidate, currentVersion)
    }
    outputFormatter = dependencyUpdatesFormatter
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
