package jp.kuaddo.tsuidezake.data.local.internal.di

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.local.PreferenceStorage
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage

@Module
internal abstract class LocalDataModule {
    @Suppress("unused")
    @Binds
    abstract fun bindPreferenceStorage(impl: SharedPreferenceStorage): PreferenceStorage
}
