package jp.kuaddo.tsuidezake.ui.wish

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.kuaddo.tsuidezake.di.ViewModelKey

@Module
abstract class WishModule {
    @ContributesAndroidInjector
    abstract fun contributeWishFragment(): WishFragment

    @Binds
    @IntoMap
    @ViewModelKey(WishViewModel::class)
    abstract fun bindWishViewModel(viewModel: WishViewModel): ViewModel
}
