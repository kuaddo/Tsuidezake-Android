dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(
    ":app",
    ":authui",
    ":core",
    ":model",
    ":domain",
    ":data:auth",
    ":data:local",
    ":data:remote",
    ":data:repository",
    ":testutil"
)
rootProject.name = "Tsuidezake-Android"
