import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.apollographql.apollo")
}
apply<CommonBuildPlugin>()

apollo {
    generateKotlinModels.set(true)
    generateAsInternal.set(true)
    sealedClassesForEnumsMatching.set(listOf(".*"))
}

dependencies {
    implementation(project(":core"))
    implementation(project(":model"))
    implementation(project(":data:repository"))

    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.Firebase.storage)
    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.Dagger.Hilt.android)
    kapt(Deps.Dagger.Hilt.compiler)
    implementation(Deps.OkHttp.core)
    implementation(Deps.OkHttp.loggingInterceptor)
    implementation(Deps.Apollo.runtime)
    implementation(Deps.Apollo.coroutines)
    implementation(Deps.timber)

    implementation(kotlin("test-junit"))
    testImplementation(Deps.Test.AndroidX.jUnit)
    testImplementation(Deps.Test.kotlinCoroutinesTest)
    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.OkHttp.mockWebServer)
    testImplementation(Deps.gson)
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
