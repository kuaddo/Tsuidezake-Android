package jp.kuaddo.tsuidezake.ui.drink

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.kuaddo.tsuidezake.di.ViewModelKey

@Suppress("unused")
@Module
abstract class DrinkModule {

    @ContributesAndroidInjector
    abstract fun contributeDrinkDetailFragment(): DrinkDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(DrinkDetailViewModel::class)
    abstract fun bindMainViewModel(viewModel: DrinkDetailViewModel): ViewModel
}