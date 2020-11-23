package jp.kuaddo.tsuidezake.data.local.internal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.kuaddo.tsuidezake.data.local.internal.room.converter.FoodCategorySetConverters
import jp.kuaddo.tsuidezake.data.local.internal.room.converter.SuitableTemperatureSetConverters
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity

@Database(
    entities = [SakeEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    SuitableTemperatureSetConverters::class,
    FoodCategorySetConverters::class
)
internal abstract class TsuidezakeDB : RoomDatabase() {
    abstract fun sakeDao(): SakeDao

    companion object {
        private const val dbName = "tsuidezake.db"

        fun createDBInstance(context: Context): TsuidezakeDB = Room
            .databaseBuilder(context, TsuidezakeDB::class.java, dbName)
            .fallbackToDestructiveMigration() // TODO: Release時に消す
            .build()
    }
}
