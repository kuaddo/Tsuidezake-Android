package jp.kuaddo.tsuidezake.data.local.internal.room.converter

import jp.kuaddo.tsuidezake.model.FoodCategory

internal class FoodCategorySetConverters : EnumSetConverters<FoodCategory>(
    FoodCategory::class.java
) {
    override val FoodCategory.storeBit: Int
        get() = when (this) {
            FoodCategory.MEAT -> 0
            FoodCategory.SEAFOOD -> 1
            FoodCategory.DAIRY -> 2
            FoodCategory.SNACK -> 3
        }
}
