package jp.kuaddo.tsuidezake.di.module

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.PreferenceStorage
import jp.kuaddo.tsuidezake.data.SharedPreferenceStorage
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
