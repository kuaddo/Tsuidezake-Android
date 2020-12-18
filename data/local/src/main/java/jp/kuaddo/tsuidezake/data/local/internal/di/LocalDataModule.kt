package jp.kuaddo.tsuidezake.data.local.internal.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.local.internal.LocalDataSourceImpl
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeTagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.TagDao
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.data.repository.PreferenceStorage

@Suppress("unused")
@Module
internal abstract class LocalDataModule {
    @Binds
    abstract fun bindPreferenceStorage(impl: SharedPreferenceStorage): PreferenceStorage

    @Binds
    abstract fun bindLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource

    companion object {
        @LocalDataScope
        @Provides
        fun provideTsuidezakeDB(context: Context): TsuidezakeDB =
            TsuidezakeDB.createDBInstance(context)

        @LocalDataScope
        @Provides
        fun provideSakeDao(db: TsuidezakeDB): SakeDao = db.sakeDao()

        @LocalDataScope
        @Provides
        fun provideTagDao(db: TsuidezakeDB): TagDao = db.tagDao()

        @LocalDataScope
        @Provides
        fun provideSakeTagDao(db: TsuidezakeDB): SakeTagDao = db.sakeTagDao()
    }
}
