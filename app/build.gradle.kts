import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import de.mannodermaus.gradle.plugins.junit5.junitPlatform
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.ben-manes.versions")
    id("jacoco")
    id("deploygate")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        applicationId = "jp.kuaddo.tsuidezake"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0.0"
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    androidExtensions.isExperimental = true
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
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
    implementation(fileTree("dir" to "libs", "include" to arrayOf("*.jar")))

    val kotlinVersion = KotlinCompilerVersion.VERSION
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta6")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.2.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime:2.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.2.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.2")

    // Dagger
    api("com.google.dagger:dagger:2.27")
    api("com.google.dagger:dagger-android:2.27")
    api("com.google.dagger:dagger-android-support:2.27")
    kapt("com.google.dagger:dagger-compiler:2.27")
    kapt("com.google.dagger:dagger-android-processor:2.27")
    compileOnly("com.squareup.inject:assisted-inject-annotations-dagger2:0.5.2")
    kapt("com.squareup.inject:assisted-inject-processor-dagger2:0.5.2")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.7.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.8.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.8.1")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.9.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")

    // Groupie
    implementation("com.xwray:groupie:2.8.0")
    implementation("com.xwray:groupie-viewbinding:2.8.0")

    implementation("com.github.wada811:DataBinding-ktx:4.0.0")
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("com.afollestad.material-dialogs:lifecycle:3.3.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.4")
    implementation("org.permissionsdispatcher:permissionsdispatcher:4.7.0")
    kapt("org.permissionsdispatcher:permissionsdispatcher-processor:4.7.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.3")

    testImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.6")
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.assertj:assertj-core:3.16.1")

    // Mockk
    testImplementation("io.mockk:mockk:1.10.0")

    // Spek
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.10")
    testImplementation("org.spekframework.spek2:spek-runner-junit5:2.0.10")

    testImplementation("org.threeten:threetenbp:1.4.4") {
        exclude("com.jakewharton.threetenabp:threetenabp:1.2.1")
    }

    androidTestImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.6")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")

    // JUnit
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("org.assertj:assertj-core:3.16.1")

    androidTestImplementation("org.threeten:threetenbp:1.4.4") {
        exclude("com.jakewharton.threetenabp:threetenabp:1.2.1")
    }

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
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
