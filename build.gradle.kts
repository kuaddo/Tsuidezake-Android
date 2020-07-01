buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Deps.GradlePlugin.android)
        classpath(Deps.GradlePlugin.kotlin)
        classpath(Deps.GradlePlugin.gms)
        classpath(Deps.GradlePlugin.safeArgs)
        classpath(Deps.GradlePlugin.apollo)
        classpath(Deps.GradlePlugin.junit5)
        classpath(Deps.GradlePlugin.ktlint)
        classpath(Deps.GradlePlugin.versions)
        classpath(Deps.GradlePlugin.deployGate)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://dl.bintray.com/lisawray/maven")
        maven("https://www.jitpack.io")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
