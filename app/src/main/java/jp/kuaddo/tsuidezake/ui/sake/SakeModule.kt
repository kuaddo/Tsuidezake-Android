package jp.kuaddo.tsuidezake.ui.sake

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SakeModule {
    @ContributesAndroidInjector
    abstract fun contributeSakeDetailFragment(): SakeDetailFragment
}
