package jp.kuaddo.tsuidezake.ui.launcher

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class LauncherModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment
}
