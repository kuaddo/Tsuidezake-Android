package jp.kuaddo.tsuidezake.data.remote

import com.apollographql.apollo.ApolloClient
import jp.kuaddo.tsuidezake.SakeQuery
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuitableTemperature
import jp.kuaddo.tsuidezake.type.SuitableTemparature
import javax.inject.Inject

class TsuidezakeService @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail> =
        apolloClient.query(SakeQuery(id)).toApiResponse { it.sake!!.toSakeDetail() }

    private fun SakeQuery.Sake.toSakeDetail() = SakeDetail(
        id = id,
        name = name,
        description = description,
        brewer = brewer,
        imageUrl = imgPath,
        tags = tags.map { it.name!! }, // TODO: nonnull対応後に!!を消す
        suitableTemperatures = suitableTemperatures.map { it.toSuitableTemperature() },
        goodFoodCategories = goodFoodCategories.map { it.toFoodCategory() }
    )

    private fun SuitableTemparature.toSuitableTemperature() = when (this) {
        SuitableTemparature.HOT -> SuitableTemperature.HOT
        SuitableTemparature.WARM -> SuitableTemperature.WARM
        SuitableTemparature.ROOM -> SuitableTemperature.NORMAL
        SuitableTemparature.COLD -> SuitableTemperature.COLD
        SuitableTemparature.ROCK -> SuitableTemperature.ROCK
        SuitableTemparature.UNKNOWN__ -> error("Unknown temperature.")
    }

    private fun jp.kuaddo.tsuidezake.type.FoodCategory.toFoodCategory() = when (this) {
        jp.kuaddo.tsuidezake.type.FoodCategory.MEAT -> FoodCategory.MEAT
        jp.kuaddo.tsuidezake.type.FoodCategory.SEAFOOD -> FoodCategory.SEAFOOD
        jp.kuaddo.tsuidezake.type.FoodCategory.DAIRY -> FoodCategory.DAIRY
        jp.kuaddo.tsuidezake.type.FoodCategory.SNACK -> FoodCategory.SNACK
        jp.kuaddo.tsuidezake.type.FoodCategory.UNKNOWN__ -> error("Unknown food category.")
    }
}
