import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(project(":core"))
    implementation(project(":model"))

    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.AndroidX.annotation)
    implementation(Deps.inject)
    implementation(Deps.timber)
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
