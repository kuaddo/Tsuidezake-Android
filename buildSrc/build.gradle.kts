plugins {
    `kotlin-dsl`
    id("jacoco")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("com.google.code.gson:gson:2.9.0")
    // Note: JavaPoet is added as workaround to update dagger version to latest.
    // https://github.com/google/dagger/issues/3068
    implementation("com.squareup:javapoet:1.13.0")
}
