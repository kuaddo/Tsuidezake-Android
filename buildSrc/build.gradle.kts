plugins {
    `kotlin-dsl`
    id("jacoco")
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.0")
    implementation("com.google.code.gson:gson:2.8.9")
}
