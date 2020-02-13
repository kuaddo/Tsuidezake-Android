package jp.kuaddo.tsuidezake.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.di.ViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}