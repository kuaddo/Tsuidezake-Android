import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import de.mannodermaus.gradle.plugins.junit5.junitPlatform
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.ben-manes.versions")
    id("jacoco")
    id("deploygate")
}
apply<CommonBuildPlugin>()

android {
    defaultConfig {
        applicationId = "jp.kuaddo.tsuidezake"
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    androidExtensions.isExperimental = true
    lintOptions {
        disable("GoogleAppIndexingWarning")
    }
    testOptions {
        unitTests.apply {
            all {
                extensions.configure(JacocoTaskExtension::class.java) {
                    isIncludeNoLocationClasses = true
                }
                testLogging {
                    setEvents(listOf("passed", "skipped", "failed", "standardOut", "standardError"))
                }
            }
        }
        junitPlatform {
            filters {
                includeEngines("spek2")
            }
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":model"))
    implementation(project(":data:auth"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":data:repository"))

    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.reflect)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesAndroid)

    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.swipeRefresh)
    implementation(Deps.AndroidX.constraint)
    implementation(Deps.AndroidX.viewPager)
    implementation(Deps.AndroidX.coreKtx)
    implementation(Deps.AndroidX.fragmentKtx)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.Lifecycle.viewModelKtx)
    implementation(Deps.AndroidX.Lifecycle.liveDataKtx)
    implementation(Deps.AndroidX.Lifecycle.commonJava8)
    implementation(Deps.AndroidX.Navigation.fragmentKtx)
    implementation(Deps.AndroidX.Navigation.uiKtx)

    implementation(Deps.Firebase.analytics)

    api(Deps.Dagger.core)
    api(Deps.Dagger.android)
    api(Deps.Dagger.androidSupport)
    kapt(Deps.Dagger.compiler)
    kapt(Deps.Dagger.androidProcessor)
    compileOnly(Deps.Dagger.AssistedInject.annotations)
    kapt(Deps.Dagger.AssistedInject.processor)

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

    testImplementation(Deps.Kotlin.reflect)
    testImplementation(Deps.Test.kotlinCoroutinesTest)
    testImplementation(Deps.Test.androidXCoreTesting)

    testImplementation(Deps.JUnit.jupiterApi)
    testImplementation(Deps.JUnit.jupiterEngine)

    testImplementation(Deps.Spek.dslJvm)
    testImplementation(Deps.Spek.junit5)

    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockk)

    testImplementation(Deps.Test.threeTenBp) {
        exclude(Deps.threeTenAbp)
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

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

ktlint {
    android.set(true)
    outputColorName.set("RED")

    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

fun com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions.all(block: Test.() -> Unit) =
    all(KotlinClosure1<Any, Test>({ (this as Test).apply(block) }, owner = this))
