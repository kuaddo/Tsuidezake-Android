package jp.kuaddo.tsuidezake.data.local.internal.room.converter

import jp.kuaddo.tsuidezake.model.SuitableTemperature

internal class SuitableTemperatureSetConverters : EnumSetConverters<SuitableTemperature>(
    SuitableTemperature::class.java
) {
    override val SuitableTemperature.storeBit: Int
        get() = when (this) {
            SuitableTemperature.HOT -> 0
            SuitableTemperature.WARM -> 1
            SuitableTemperature.NORMAL -> 2
            SuitableTemperature.COLD -> 3
            SuitableTemperature.ROCK -> 4
        }
}
