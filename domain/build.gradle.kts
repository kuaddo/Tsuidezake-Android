import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("org.jlleitschuh.gradle.ktlint")
}
apply<CommonBuildPlugin>()

dependencies {
    implementation(project(":model"))

    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    api(Deps.inject)
    implementation(Deps.timber)

    testImplementation(Deps.Kotlin.reflect)
    testImplementation(Deps.Test.kotlinCoroutinesTest)
    testImplementation(Deps.JUnit.jupiterApi)
    testImplementation(Deps.JUnit.jupiterEngine)
    testImplementation(Deps.Spek.dslJvm)
    testImplementation(Deps.Spek.junit5)
    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockk)
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}
