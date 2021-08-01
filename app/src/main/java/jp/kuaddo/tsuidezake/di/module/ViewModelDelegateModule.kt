package jp.kuaddo.tsuidezake.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.kuaddo.tsuidezake.delegate.LiveEventSnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.LiveEventToastViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.MediatorLoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.ToastViewModelDelegate

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelDelegateModule {
    companion object {
        @Provides
        fun provideLoadingViewModelDelegate(): LoadingViewModelDelegate =
            MediatorLoadingViewModelDelegate()

        @Provides
        fun provideSnackbarViewModelDelegate(): SnackbarViewModelDelegate =
            LiveEventSnackbarViewModelDelegate()

        @Provides
        fun provideToastViewModelDelegate(): ToastViewModelDelegate =
            LiveEventToastViewModelDelegate()
    }
}
