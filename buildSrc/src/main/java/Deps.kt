object Deps {
    private const val kotlinVersion = "1.3.72"
    private const val coroutinesVersion = "1.3.7"
    private const val navigationVersion = "2.3.0"
    private const val apolloVersion = "2.1.0"
    const val buildToolsVersion = "29.0.3"

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:4.0.0"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val gms = "com.google.gms:google-services:4.3.3"
        const val safeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
        const val apollo = "com.apollographql.apollo:apollo-gradle-plugin:$apolloVersion"
        const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.5.1.0"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:9.2.1"
        const val versions = "com.github.ben-manes:gradle-versions-plugin:0.28.0"
        const val deployGate = "com.deploygate:gradle:2.2.0"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        const val coroutinesPlayServices =
            "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:2.0.0-beta6"
        const val viewPager = "androidx.viewpager2:viewpager2:1.0.0"
        const val coreKtx = "androidx.core:core-ktx:1.3.0"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"
        const val material = "com.google.android.material:material:1.1.0"

        object Lifecycle {
            private const val version = "2.2.0"

            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
        }

        object Navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
        }
    }

    object Firebase {
        const val analytics = "com.google.firebase:firebase-analytics:17.4.3"
        const val storage = "com.google.firebase:firebase-storage:19.1.1"
    }

    object Dagger {
        private const val version = "2.28.1"

        const val core = "com.google.dagger:dagger:$version"
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"

        object AssistedInject {
            private const val version = "0.5.2"

            const val annotations =
                "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
            const val processor =
                "com.squareup.inject:assisted-inject-processor-dagger2:$version"
        }
    }

    object OkHttp {
        private const val version = "4.7.2"

        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Glide {
        private const val version = "4.11.0"

        const val core = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Apollo {
        const val runtime = "com.apollographql.apollo:apollo-runtime:$apolloVersion"
        const val coroutines = "com.apollographql.apollo:apollo-coroutines-support:$apolloVersion"
    }

    object Groupie {
        private const val version = "2.8.0"

        const val core = "com.xwray:groupie:$version"
        const val viewBinding = "com.xwray:groupie-viewbinding:$version"
    }

    object MaterialDialogs {
        private const val version = "3.3.0"

        const val core = "com.afollestad.material-dialogs:core:$version"
        const val lifecycle = "com.afollestad.material-dialogs:lifecycle:$version"
    }

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:1.2.4"
    const val dataBindingKtx = "com.github.wada811:DataBinding-ktx:4.0.0"
    const val liveEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.4"
    const val inject = "javax.inject:javax.inject:1"

    object Test {
        const val kotlinCoroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        const val androidXCoreTesting = "androidx.arch.core:core-testing:2.1.0"
        const val assertJ = "org.assertj:assertj-core:3.16.1"
        const val mockk = "io.mockk:mockk:1.10.0"
        const val threeTenBp = "org.threeten:threetenbp:1.4.4"
    }

    object JUnit {
        private const val version = "5.6.2"

        const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$version"
        const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$version"
    }

    object Spek {
        private const val version = "2.0.11"

        const val dslJvm = "org.spekframework.spek2:spek-dsl-jvm:$version"
        const val junit5 = "org.spekframework.spek2:spek-runner-junit5:$version"
    }
}