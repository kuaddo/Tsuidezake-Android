package jp.kuaddo.tsuidezake.data.remote

import com.apollographql.apollo.ApolloClient
import jp.kuaddo.tsuidezake.SakeQuery
import jp.kuaddo.tsuidezake.model.SakeDetail
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
        suitableTemperatures = suitableTemperatures,
        goodFoodCategories = goodFoodCategories
    )
}
