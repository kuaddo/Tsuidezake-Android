package jp.kuaddo.tsuidezake.data.local.internal.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.repository.PreferenceStorage

@Suppress("unused")
@Module
internal abstract class LocalDataModule {
    @Binds
    abstract fun bindPreferenceStorage(impl: SharedPreferenceStorage): PreferenceStorage

    companion object {
        @LocalDataScope
        @Provides
        fun provideTsuidezakeDB(context: Context): TsuidezakeDB =
            TsuidezakeDB.createDBInstance(context)
    }
}
