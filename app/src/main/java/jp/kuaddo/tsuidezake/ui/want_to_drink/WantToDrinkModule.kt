package jp.kuaddo.tsuidezake.ui.want_to_drink

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.kuaddo.tsuidezake.di.ViewModelKey

@Suppress("unused")
@Module
abstract class WantToDrinkModule {

    @ContributesAndroidInjector
    abstract fun contributeWantToDrinkFragment(): WantToDrinkFragment

    @Binds
    @IntoMap
    @ViewModelKey(WantToDrinkViewModel::class)
    abstract fun bindWantToDrinkViewModel(viewModel: WantToDrinkViewModel): ViewModel
}