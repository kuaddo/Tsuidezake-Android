import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import de.mannodermaus.gradle.plugins.junit5.junitPlatform
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("de.mannodermaus.android-junit5")
    id("com.apollographql.apollo")
}
apply<CommonBuildPlugin>()

apollo {
    generateKotlinModels.set(true)
    generateAsInternal.set(true)
    sealedClassesForEnumsMatching.set(listOf(".*"))
}

android {
    testOptions.junitPlatform.filters {
        includeEngines("spek2")
    }
}

dependencies {
    implementation(project(":model"))
    implementation(project(":data:repository"))

    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.Firebase.storage)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.OkHttp.core)
    implementation(Deps.OkHttp.loggingInterceptor)
    implementation(Deps.Apollo.runtime)
    implementation(Deps.Apollo.coroutines)
    implementation(Deps.timber)

    testImplementation(Deps.Test.kotlinCoroutinesTest)
    testImplementation(Deps.Test.JUnit.jupiterApi)
    testImplementation(Deps.Test.JUnit.jupiterEngine)
    testImplementation(Deps.Test.Spek.dslJvm)
    testImplementation(Deps.Test.Spek.junit5)
    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.OkHttp.mockWebServer)
    testImplementation(Deps.gson)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
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
