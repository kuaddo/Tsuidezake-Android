package jp.kuaddo.tsuidezake.data.remote

import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuitableTemperature
import jp.kuaddo.tsuidezake.model.Tag
import jp.kuaddo.tsuidezake.model.UserSake

internal val SAKE_DETAIL1 = SakeDetail(
    id = 1,
    name = "秘幻　吟醸酒",
    description = "おいしいお酒",
    region = "草津",
    brewer = "浅間酒造",
    imageUri = "converted:gs://tsuidezake.appspot.com/sakes/1.png",
    tags = listOf(
        Tag(id = 1, name = "辛口"),
        Tag(id = 2, name = "アルコール度数：中")
    ),
    suitableTemperatures = setOf(
        SuitableTemperature.HOT,
        SuitableTemperature.WARM,
        SuitableTemperature.COLD,
        SuitableTemperature.ROCK
    ),
    goodFoodCategories = setOf(
        FoodCategory.DAIRY,
        FoodCategory.SNACK
    )
)

internal val SAKE_DETAIL2 = SakeDetail(
    id = 2,
    name = "酒2",
    description = "酒2 description",
    region = "草津",
    brewer = "浅間酒造",
    imageUri = "converted:gs://tsuidezake.appspot.com/sakes/2.png",
    tags = emptyList(),
    suitableTemperatures = setOf(
        SuitableTemperature.HOT,
        SuitableTemperature.COLD
    ),
    goodFoodCategories = setOf(
        FoodCategory.MEAT,
        FoodCategory.SEAFOOD
    )
)

internal val USER_SAKE1 = UserSake(
    sakeDetail = SAKE_DETAIL1,
    isAddedToWish = true,
    isAddedToTasted = true
)
