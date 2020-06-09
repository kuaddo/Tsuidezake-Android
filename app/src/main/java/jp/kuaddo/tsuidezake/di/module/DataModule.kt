package jp.kuaddo.tsuidezake.di.module

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.local.PreferenceStorage
import jp.kuaddo.tsuidezake.data.local.SharedPreferenceStorage
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun providePreferenceStorage(
        preferenceStorage: SharedPreferenceStorage
    ): PreferenceStorage
}
