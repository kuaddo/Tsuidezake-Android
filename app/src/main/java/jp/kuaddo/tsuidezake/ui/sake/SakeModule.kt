package jp.kuaddo.tsuidezake.ui.sake

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class SakeModule {

    @ContributesAndroidInjector
    abstract fun contributeSakeDetailFragment(): SakeDetailFragment
}
