package jp.kuaddo.tsuidezake.data.remote.internal

import android.net.Uri
import com.apollographql.apollo.ApolloClient
import com.google.firebase.storage.FirebaseStorage
import jp.kuaddo.tsuidezake.data.remote.AddSakeToTastedListMutation
import jp.kuaddo.tsuidezake.data.remote.AddSakeToWishListMutation
import jp.kuaddo.tsuidezake.data.remote.ApiResponse
import jp.kuaddo.tsuidezake.data.remote.RankingsQuery
import jp.kuaddo.tsuidezake.data.remote.RecommendedSakeQuery
import jp.kuaddo.tsuidezake.data.remote.RemoveSakeFromTastedListMutation
import jp.kuaddo.tsuidezake.data.remote.RemoveSakeFromWishListMutation
import jp.kuaddo.tsuidezake.data.remote.SakeQuery
import jp.kuaddo.tsuidezake.data.remote.TsuidezakeService
import jp.kuaddo.tsuidezake.data.remote.WishListQuery
import jp.kuaddo.tsuidezake.data.remote.fragment.ContentFragment
import jp.kuaddo.tsuidezake.data.remote.fragment.SakeDetailFragment
import jp.kuaddo.tsuidezake.data.remote.toApiResponse
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.Ranking
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
    override suspend fun getRankings(): ApiResponse<List<Ranking>> = withContext(Dispatchers.IO) {
        apolloClient.query(RankingsQuery()).toApiResponse { response ->
            response.getRankings.map { it.toRanking() }
        }
    }

    override suspend fun getRecommendedSakes(): ApiResponse<List<Ranking.Content>> =
        withContext(Dispatchers.IO) {
            apolloClient.query(RecommendedSakeQuery()).toApiResponse { response ->
                response.getRecommendedSakes
                    .map { it.fragments.contentFragment.toContent() }
            }
        }

    override suspend fun getWishList(): ApiResponse<List<SakeDetail>> =
        withContext(Dispatchers.IO) {
            apolloClient.query(WishListQuery()).toApiResponse { response ->
                response.wishList
                    .map { it.fragments.sakeDetailFragment.toSakeDetail() }
            }
        }

    override suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail> =
        withContext(Dispatchers.IO) {
            apolloClient.query(SakeQuery(id)).toApiResponse { response ->
                response.sake!!.fragments
                    .sakeDetailFragment
                    .toSakeDetail()
            }
        }

    override suspend fun addSakeToWishList(id: Int): ApiResponse<List<SakeDetail>> =
        withContext(Dispatchers.IO) {
            apolloClient.mutate(AddSakeToWishListMutation(id)).toApiResponse { response ->
                response.addWishSake
                    .map { it.fragments.sakeDetailFragment.toSakeDetail() }
            }
        }

    override suspend fun removeSakeFromWishList(id: Int): ApiResponse<List<SakeDetail>> =
        withContext(Dispatchers.IO) {
            apolloClient.mutate(RemoveSakeFromWishListMutation(id)).toApiResponse { response ->
                response.removeWishSake
                    .map { it.fragments.sakeDetailFragment.toSakeDetail() }
            }
        }

    override suspend fun addSakeToTastedList(id: Int): ApiResponse<List<SakeDetail>> =
        withContext(Dispatchers.IO) {
            apolloClient.mutate(AddSakeToTastedListMutation(id)).toApiResponse { response ->
                response.addTastedSake
                    .map { it.fragments.sakeDetailFragment.toSakeDetail() }
            }
        }

    override suspend fun removeSakeFromTastedList(id: Int): ApiResponse<List<SakeDetail>> =
        withContext(Dispatchers.IO) {
            apolloClient.mutate(RemoveSakeFromTastedListMutation(id)).toApiResponse { response ->
                response.removeTastedSake
                    .map { it.fragments.sakeDetailFragment.toSakeDetail() }
            }
        }
}

private suspend fun RankingsQuery.GetRanking.toRanking() = Ranking(
    displayOrder = displayOrder,
    category = category,
    contents = contents.map { it.fragments.contentFragment.toContent() }
)

private suspend fun ContentFragment.toContent() = Ranking.Content(
    rank = rank,
    sakeId = sake.id,
    name = sake.name,
    imageUri = getImageUri(sake.imgPath)
)

private suspend fun SakeDetailFragment.toSakeDetail() = SakeDetail(
    id = id,
    name = name,
    description = description,
    region = region,
    brewer = brewer,
    imageUri = getImageUri(imgPath),
    tags = tags.map { it.name },
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
