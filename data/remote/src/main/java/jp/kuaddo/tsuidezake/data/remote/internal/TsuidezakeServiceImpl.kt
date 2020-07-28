package jp.kuaddo.tsuidezake.data.remote.internal

import android.net.Uri
import com.apollographql.apollo.ApolloClient
import com.google.firebase.storage.FirebaseStorage
import jp.kuaddo.tsuidezake.data.remote.ApiResponse
import jp.kuaddo.tsuidezake.data.remote.SakeQuery
import jp.kuaddo.tsuidezake.data.remote.TsuidezakeService
import jp.kuaddo.tsuidezake.data.remote.toApiResponse
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuitableTemperature
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import jp.kuaddo.tsuidezake.data.remote.type.FoodCategory as ApolloFoodCategory
import jp.kuaddo.tsuidezake.data.remote.type.SuitableTemperature as ApolloSuitableTemperature

internal class TsuidezakeServiceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : TsuidezakeService {

    override suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail> =
        withContext(Dispatchers.IO) {
            apolloClient.query(SakeQuery(id)).toApiResponse { it.sake!!.toSakeDetail() }
        }

    private suspend fun SakeQuery.Sake.toSakeDetail() = SakeDetail(
        id = id,
        name = name,
        description = description,
        brewer = brewer,
        imageUri = getImageUri(imgPath),
        tags = tags.map { it.name!! }, // TODO: nonnull対応後に!!を消す
        suitableTemperatures = suitableTemperatures.map { it.toSuitableTemperature() }.toSet(),
        goodFoodCategories = goodFoodCategories.map { it.toFoodCategory() }.toSet()
    )

    private suspend fun getImageUri(firebaseImagePath: String?): Uri? = runCatching {
        firebaseImagePath?.let { path ->
            FirebaseStorage.getInstance()
                .getReferenceFromUrl(path)
                .downloadUrl
                .await()
        }
    }
        .onFailure { if (it is CancellationException) throw it }
        .getOrNull()


    private fun ApolloSuitableTemperature.toSuitableTemperature() = when (this) {
        ApolloSuitableTemperature.HOT -> SuitableTemperature.HOT
        ApolloSuitableTemperature.WARM -> SuitableTemperature.WARM
        ApolloSuitableTemperature.ROOM -> SuitableTemperature.NORMAL
        ApolloSuitableTemperature.COLD -> SuitableTemperature.COLD
        ApolloSuitableTemperature.ROCK -> SuitableTemperature.ROCK
        is ApolloSuitableTemperature.UNKNOWN__ -> error("Unknown temperature : $rawValue")
    }

    private fun ApolloFoodCategory.toFoodCategory() = when (this) {
        ApolloFoodCategory.MEAT -> FoodCategory.MEAT
        ApolloFoodCategory.SEAFOOD -> FoodCategory.SEAFOOD
        ApolloFoodCategory.DAIRY -> FoodCategory.DAIRY
        ApolloFoodCategory.SNACK -> FoodCategory.SNACK
        is ApolloFoodCategory.UNKNOWN__ -> error("Unknown food category : $rawValue")
    }
}
