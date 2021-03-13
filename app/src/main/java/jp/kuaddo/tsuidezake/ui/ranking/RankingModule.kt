package jp.kuaddo.tsuidezake.ui.ranking

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import jp.kuaddo.tsuidezake.di.ViewModelKey

@Module
abstract class RankingModule {
    @ContributesAndroidInjector
    abstract fun contributeRankingFragment(): RankingFragment

    @Binds
    @IntoMap
    @ViewModelKey(RankingViewModel::class)
    abstract fun bindRankingViewModel(viewModel: RankingViewModel): ViewModel
}
