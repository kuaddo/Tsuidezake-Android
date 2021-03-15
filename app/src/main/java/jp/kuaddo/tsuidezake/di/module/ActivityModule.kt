package jp.kuaddo.tsuidezake.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.kuaddo.tsuidezake.di.ActivityScoped
import jp.kuaddo.tsuidezake.ui.MainActivity
import jp.kuaddo.tsuidezake.ui.launcher.LauncherActivity
import jp.kuaddo.tsuidezake.ui.launcher.LauncherModule
import jp.kuaddo.tsuidezake.ui.ranking.RankingModule
import jp.kuaddo.tsuidezake.ui.sake.SakeModule
import jp.kuaddo.tsuidezake.ui.wish.WishModule

@Module
abstract class ActivityModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [LauncherModule::class])
    abstract fun contributeLauncherActivity(): LauncherActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            RankingModule::class,
            WishModule::class,
            SakeModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}
