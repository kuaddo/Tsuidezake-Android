plugins {
    `kotlin-dsl`
    id("jacoco")
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
}
