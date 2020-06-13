package jp.kuaddo.tsuidezake.data.local.internal.di

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.local.PreferenceStorage
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage

@Module
internal abstract class LocalDataModule {
    @Binds
    abstract fun bindPreferenceStorage(impl: SharedPreferenceStorage): PreferenceStorage
}
