package jp.kuaddo.tsuidezake.ui.drink

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@AssistedModule
@Module(includes = [AssistedInject_DrinkModule::class])
abstract class DrinkModule {

    @ContributesAndroidInjector
    abstract fun contributeDrinkDetailFragment(): DrinkDetailFragment
}
