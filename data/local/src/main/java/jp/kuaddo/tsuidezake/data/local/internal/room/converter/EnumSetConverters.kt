package jp.kuaddo.tsuidezake.data.local.internal.room.converter

import androidx.room.TypeConverter

internal abstract class EnumSetConverters<T : Enum<T>>(private val clazz: Class<T>) {
    protected abstract val T.storeBit: Int

    @TypeConverter
    fun encode(enums: Set<T>?): Int? = enums?.sumBy { 1 shl it.storeBit }

    @TypeConverter
    fun decode(value: Int?): Set<T>? {
        value ?: return null
        return clazz.enumConstants
            ?.filter { value ushr it.storeBit and 1 == 1 }
            ?.toSet()
    }
}
