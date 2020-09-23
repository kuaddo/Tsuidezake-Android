package jp.kuaddo.tsuidezake.data.repository

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail

interface TsuidezakeService {
    suspend fun getRankings(): ApiResponse<List<Ranking>>
    suspend fun getRecommendedSakes(): ApiResponse<List<Ranking.Content>>
    suspend fun getWishList(): ApiResponse<List<SakeDetail>>
    suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail>

    suspend fun addSakeToWishList(id: Int): ApiResponse<List<SakeDetail>>
    suspend fun removeSakeFromWishList(id: Int): ApiResponse<List<SakeDetail>>
    suspend fun addSakeToTastedList(id: Int): ApiResponse<List<SakeDetail>>
    suspend fun removeSakeFromTastedList(id: Int): ApiResponse<List<SakeDetail>>
}