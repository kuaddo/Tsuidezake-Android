package jp.kuaddo.tsuidezake.ui.want_to_drink

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class WantToDrinkModule {

    @ContributesAndroidInjector
    abstract fun contributeWantToDrinkFragment(): WantToDrinkFragment
}