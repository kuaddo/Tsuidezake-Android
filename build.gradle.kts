buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
        classpath(kotlin("gradle-plugin", "1.3.72"))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.2")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.5.1.0")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.2.1")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.28.0")
        classpath("com.deploygate:gradle:2.1.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/lisawray/maven")
        maven(url = "https://www.jitpack.io")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
