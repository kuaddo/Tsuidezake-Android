package jp.kuaddo.tsuidezake.data.repository

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail

interface Repository {
    suspend fun getRankings(): Resource<List<Ranking>>
    suspend fun getRecommendedSakes(): Resource<List<Ranking.Content>>
    suspend fun getWishList(): Resource<List<SakeDetail>>
    suspend fun getSakeDetail(id: Int): Resource<SakeDetail>

    suspend fun addSakeToWishList(id: Int): Resource<List<SakeDetail>>
    suspend fun removeSakeFromWishList(id: Int): Resource<List<SakeDetail>>
    suspend fun addSakeToTastedList(id: Int): Resource<List<SakeDetail>>
    suspend fun removeSakeFromTastedList(id: Int): Resource<List<SakeDetail>>
}
