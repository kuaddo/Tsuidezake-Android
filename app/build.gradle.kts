import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
    id("org.jlleitschuh.gradle.ktlint")
    id("deploygate")
}
apply<CommonBuildPlugin>()

android {
    defaultConfig {
        applicationId = "jp.kuaddo.tsuidezake"
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArgument(
            "runnerBuilder",
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
        )
    }
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("keystore/debug.keystore")

            val instance = runCatching {
                Class.forName("Keystore").kotlin.objectInstance
            }.getOrNull()
            if (instance == null) {
                // CI build
                storePassword = System.getenv("STORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            } else {
                // local build
                fun getProperty(name: String): String =
                    instance::class.members.first { it.name == name }.call() as String

                storePassword = getProperty("STORE_PASSWORD")
                keyAlias = getProperty("KEY_ALIAS")
                keyPassword = getProperty("KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        val appName = "ついで酒"
        getByName("debug") {
            resValue("string", "app_name", "$appName-debug")

            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isTestCoverageEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            resValue("string", "app_name", appName)

            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core"))
    implementation(project(":model"))
    implementation(project(":domain"))
    implementation(project(":data:auth"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":data:repository"))

    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesAndroid)

    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.swipeRefresh)
    implementation(Deps.AndroidX.constraint)
    implementation(Deps.AndroidX.viewPager)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.activityKtx)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.Lifecycle.viewModelKtx)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    implementation(Deps.AndroidX.Lifecycle.commonJava8)
    implementation(Deps.AndroidX.Navigation.fragmentKtx)
    implementation(Deps.AndroidX.Navigation.uiKtx)

    implementation(Deps.Firebase.analytics)

    implementation(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.Dagger.Hilt.android)
    kapt(Deps.Dagger.Hilt.compiler)

    implementation(Deps.Glide.core)
    kapt(Deps.Glide.compiler)

    implementation(Deps.Groupie.core)
    implementation(Deps.Groupie.viewBinding)

    implementation(Deps.MaterialDialogs.core)
    implementation(Deps.MaterialDialogs.lifecycle)

    implementation(Deps.timber)
    implementation(Deps.threeTenAbp)
    implementation(Deps.liveEvent)
    implementation(Deps.dataBindingKtx)
    debugImplementation(Deps.leakCanary)

    testImplementation(project(":testutil"))
    testImplementation(Deps.Test.kotlinCoroutinesTest)
    testImplementation(Deps.Test.AndroidX.coreTesting)

    testImplementation(Deps.Test.JUnit.jupiterApi)
    testImplementation(Deps.Test.JUnit.jupiterEngine)

    testImplementation(Deps.Test.Spek.dslJvm)
    testImplementation(Deps.Test.Spek.junit5)

    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockk)

    testImplementation(Deps.Test.threeTenBp) {
        exclude(Deps.threeTenAbp)
    }

    androidTestRuntimeOnly(Deps.Test.JUnit.androidTestRunner)
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

tasks.register<DependencyUpdatesReportMergerTask>("mergeDependencyUpdatesReports") {
    projectRootDirPath = projectDir.parent
}
