package jp.kuaddo.tsuidezake.data.repository

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake

interface TsuidezakeService {
    suspend fun getRankings(): ApiResponse<List<Ranking>>
    suspend fun getRecommendedSakes(): ApiResponse<List<Ranking.Content>>
    suspend fun getWishList(): ApiResponse<List<SakeDetail>>
    suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail>
    suspend fun getUserSake(id: Int): ApiResponse<UserSake>

    suspend fun addSakeToWishList(id: Int): ApiResponse<UserSake>
    suspend fun removeSakeFromWishList(id: Int): ApiResponse<UserSake>
    suspend fun addSakeToTastedList(id: Int): ApiResponse<UserSake>
    suspend fun removeSakeFromTastedList(id: Int): ApiResponse<UserSake>
}
