object Deps {
    private const val kotlinVersion = "1.5.20"
    private const val coroutinesVersion = "1.5.0"
    private const val navigationVersion = "2.3.5"
    private const val apolloVersion = "2.5.9"

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:4.2.1"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val gms = "com.google.gms:google-services:4.3.8"
        const val safeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
        const val apollo = "com.apollographql.apollo:apollo-gradle-plugin:$apolloVersion"
        const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.7.1.1"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.1.0"
        const val versions = "com.github.ben-manes:gradle-versions-plugin:0.39.0"
        const val deployGate = "com.deploygate:gradle:2.4.0"
    }

    object Kotlin {
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        const val coroutinesPlayServices =
            "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.3.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"
        const val coreKtx = "androidx.core:core-ktx:1.5.0"
        const val annotation = "androidx.annotation:annotation:1.2.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.5"
        const val material = "com.google.android.material:material:1.3.0"

        object Lifecycle {
            private const val version = "2.3.1"

            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
        }

        object Navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
        }

        object Room {
            private const val version = "2.3.0"

            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }
    }

    object Firebase {
        const val analytics = "com.google.firebase:firebase-analytics:19.0.0"
        const val auth = "com.google.firebase:firebase-auth:21.0.1"
        const val storage = "com.google.firebase:firebase-storage:20.0.0"
    }

    object Dagger {
        private const val version = "2.37"

        const val core = "com.google.dagger:dagger:$version"
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object OkHttp {
        private const val version = "4.9.1"

        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Glide {
        private const val version = "4.12.0"

        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Apollo {
        const val runtime = "com.apollographql.apollo:apollo-runtime:$apolloVersion"
        const val coroutines = "com.apollographql.apollo:apollo-coroutines-support:$apolloVersion"
    }

    object Groupie {
        private const val version = "2.9.0"

        const val core = "com.xwray:groupie:$version"
        const val viewBinding = "com.xwray:groupie-viewbinding:$version"
    }

    object MaterialDialogs {
        private const val version = "3.3.0"

        const val core = "com.afollestad.material-dialogs:core:$version"
        const val lifecycle = "com.afollestad.material-dialogs:lifecycle:$version"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:1.3.1"
    const val gson = "com.google.code.gson:gson:2.8.7"
    const val dataBindingKtx = "com.github.wada811:DataBinding-ktx:5.0.2"
    const val liveEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"
    const val inject = "javax.inject:javax.inject:1"

    object Test {
        const val kotlinCoroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        const val assertJ = "org.assertj:assertj-core:3.20.2"
        const val mockk = "io.mockk:mockk:1.11.0"

        const val robolectric = "org.robolectric:robolectric:4.5.1"
        const val threeTenBp = "org.threeten:threetenbp:1.5.1"

        object AndroidX {
            const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
            const val testRunner = "androidx.test:runner:1.3.0"
            const val jUnit = "androidx.test.ext:junit:1.1.2"
        }

        object JUnit {
            private const val version = "5.7.2"

            const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$version"
            const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$version"

            const val androidTestRunner = "de.mannodermaus.junit5:android-test-runner:1.2.0"
        }

        object Spek {
            // TODO: Updateはこのissueが解決するまで待つ
            //  https://github.com/spekframework/spek/issues/964
            private const val version = "2.0.15"

            const val dslJvm = "org.spekframework.spek2:spek-dsl-jvm:$version"
            const val junit5 = "org.spekframework.spek2:spek-runner-junit5:$version"
        }
    }
}
