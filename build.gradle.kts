buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath(kotlin("gradle-plugin", "1.3.61"))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.1")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.5.1.0")
        classpath("com.deploygate:gradle:2.0.2")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
