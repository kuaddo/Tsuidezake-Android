object Versions {
    const val targetSdkVersion = 29
    const val compileSdkVersion = 29
    const val minSdkVersion = 21
    val versionCode: Int
        get() = majorVersion * 10000 + minorVersion * 100 + patchVersion
    val versionName: String
        get() = "$majorVersion.$minorVersion.$patchVersion"

    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 0
}