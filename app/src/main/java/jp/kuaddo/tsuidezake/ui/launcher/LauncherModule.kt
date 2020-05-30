package jp.kuaddo.tsuidezake.ui.launcher

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
internal abstract class LauncherModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment
}
