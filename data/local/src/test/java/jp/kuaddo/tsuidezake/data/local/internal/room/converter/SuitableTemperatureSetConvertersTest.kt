package jp.kuaddo.tsuidezake.data.local.internal.room.converter

import jp.kuaddo.tsuidezake.data.local.powerSet
import jp.kuaddo.tsuidezake.model.SuitableTemperature
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object SuitableTemperatureSetConvertersTest : Spek({
    describe("Encoded and then decoded value is original value.") {
        val converter = SuitableTemperatureSetConverters()
        SuitableTemperature.values().toList().powerSet().forEach { targetEnumSet ->
            it(targetEnumSet.toString()) {
                val encodedValue = converter.encode(targetEnumSet)
                val decodedValue = converter.decode(encodedValue)
                assertThat(decodedValue).isEqualTo(targetEnumSet)
            }
        }
    }
})
