object Deps {
    private const val kotlinVersion = "1.6.10"
    private const val coroutinesVersion = "1.6.0"
    private const val navigationVersion = "2.3.5"
    private const val apolloVersion = "2.5.11"
    private const val daggerVersion = "2.41"

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:7.1.2"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val gms = "com.google.gms:google-services:4.3.10"
        const val safeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:$daggerVersion"
        const val apollo = "com.apollographql.apollo:apollo-gradle-plugin:$apolloVersion"
        const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.8.0.0"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.2.1"
        const val versions = "com.github.ben-manes:gradle-versions-plugin:0.39.0"
        const val deployGate = "com.deploygate:gradle:2.4.0"
    }

    object Kotlin {
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        const val coroutinesPlayServices =
            "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.4.1"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:2.1.3"
        const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val annotation = "androidx.annotation:annotation:1.3.0"
        const val activityKtx = "androidx.activity:activity-ktx:1.4.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.4.1"
        const val material = "com.google.android.material:material:1.5.0"

        object Lifecycle {
            private const val version = "2.4.1"

            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
        }

        object Navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
        }

        object Room {
            private const val version = "2.4.2"

            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }
    }

    object Firebase {
        const val analytics = "com.google.firebase:firebase-analytics-ktx:20.1.0"
        const val auth = "com.google.firebase:firebase-auth-ktx:21.0.1"
        const val storage = "com.google.firebase:firebase-storage-ktx:20.0.0"
    }

    object Dagger {
        const val core = "com.google.dagger:dagger:$daggerVersion"
        const val compiler = "com.google.dagger:dagger-compiler:$daggerVersion"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$daggerVersion"
            const val compiler = "com.google.dagger:hilt-compiler:$daggerVersion"
        }
    }

    object OkHttp {
        private const val version = "4.9.3"

        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Glide {
        private const val version = "4.13.1"

        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Apollo {
        const val runtime = "com.apollographql.apollo:apollo-runtime:$apolloVersion"
        const val coroutines = "com.apollographql.apollo:apollo-coroutines-support:$apolloVersion"
    }

    object Groupie {
        private const val version = "2.10.0"

        const val core = "com.github.lisawray.groupie:groupie:$version"
        const val viewBinding = "com.github.lisawray.groupie:groupie-viewbinding:$version"
    }

    object MaterialDialogs {
        private const val version = "3.3.0"

        const val core = "com.afollestad.material-dialogs:core:$version"
        const val lifecycle = "com.afollestad.material-dialogs:lifecycle:$version"
    }

    const val gmsAuth = "com.google.android.gms:play-services-auth:20.1.0"
    const val facebookLogin = "com.facebook.android:facebook-login:13.0.0"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:1.3.1"
    const val gson = "com.google.code.gson:gson:2.9.0"
    const val dataBindingKtx = "com.github.wada811:DataBinding-ktx:6.0.0"
    const val liveEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.8.1"
    const val inject = "javax.inject:javax.inject:1"

    object Test {
        const val kotlinCoroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        const val assertJ = "org.assertj:assertj-core:3.22.0"
        const val mockk = "io.mockk:mockk:1.12.2"

        const val robolectric = "org.robolectric:robolectric:4.7.3"
        const val threeTenBp = "org.threeten:threetenbp:1.5.2"

        object AndroidX {
            const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
            const val testRunner = "androidx.test:runner:1.4.0"
            const val jUnit = "androidx.test.ext:junit:1.1.3"
        }

        object JUnit {
            private const val version = "5.8.2"

            const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$version"
            const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$version"
        }

        object Spek {
            private const val version = "2.0.17"

            const val dslJvm = "org.spekframework.spek2:spek-dsl-jvm:$version"
            const val junit5 = "org.spekframework.spek2:spek-runner-junit5:$version"
        }
    }
}
