object Versions {
    const val targetSdkVersion = 31
    const val compileSdkVersion = 31
    const val minSdkVersion = 23
    const val buildToolsVersion = "30.0.3"
    val versionCode: Int
        get() = majorVersion * 10000 + minorVersion * 100 + patchVersion
    val versionName: String
        get() = "$majorVersion.$minorVersion.$patchVersion"

    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 0
}
