package jp.kuaddo.tsuidezake.ui.ranking

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class RankingModule {

    @ContributesAndroidInjector
    abstract fun contributeRankingFragment(): RankingFragment
}