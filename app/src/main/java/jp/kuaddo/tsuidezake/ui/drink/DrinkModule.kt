package jp.kuaddo.tsuidezake.ui.drink

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class DrinkModule {

    @ContributesAndroidInjector
    abstract fun contributeDrinkDetailFragment(): DrinkDetailFragment
}