package jp.kuaddo.tsuidezake.data.repository

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun loadUserSakeFlow(sakeId: Int): Flow<UserSake?>
    fun loadSakeDetailFlow(sakeId: Int): Flow<SakeDetail?>
    fun loadWishList(): Flow<List<SakeDetail>>
    fun loadRecommendedSakes(): Flow<List<Ranking.Content>>

    suspend fun saveUserSake(userSake: UserSake)
    suspend fun saveSakeDetail(sakeDetail: SakeDetail)
    suspend fun saveWishList(wishList: List<SakeDetail>)
    suspend fun saveRecommendedSakes(contents: List<Ranking.Content>)
}
