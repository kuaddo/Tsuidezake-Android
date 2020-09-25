package jp.kuaddo.tsuidezake.data.local.internal.di

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage
import jp.kuaddo.tsuidezake.data.repository.PreferenceStorage

@Module
internal abstract class LocalDataModule {
    @Suppress("unused")
    @Binds
    abstract fun bindPreferenceStorage(impl: SharedPreferenceStorage): PreferenceStorage
}
