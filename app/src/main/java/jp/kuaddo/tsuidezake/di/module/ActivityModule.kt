package jp.kuaddo.tsuidezake.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.kuaddo.tsuidezake.di.ActivityScoped
import jp.kuaddo.tsuidezake.ui.MainActivity
import jp.kuaddo.tsuidezake.ui.drink.DrinkModule
import jp.kuaddo.tsuidezake.ui.ranking.RankingModule
import jp.kuaddo.tsuidezake.ui.want_to_drink.WantToDrinkModule

@Suppress("unused")
@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RankingModule::class, WantToDrinkModule::class, DrinkModule::class])
    abstract fun contributeMainActivity(): MainActivity
}