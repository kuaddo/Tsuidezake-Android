package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow

interface Repository {
    val isAccountInitialized: Flow<Boolean>

    suspend fun signInAnonymously(): Boolean

    fun getRankings(): Flow<Resource<List<Ranking>>>
    fun getRecommendedSakes(): Flow<Resource<List<Ranking.Content>>>
    fun getWishList(): Flow<Resource<List<SakeDetail>>>
    fun getSakeDetail(id: Int): Flow<Resource<SakeDetail>>
    fun getUserSake(id: Int): Flow<Resource<UserSake>>

    suspend fun addSakeToWishList(id: Int): Resource<Unit>
    suspend fun removeSakeFromWishList(id: Int): Resource<Unit>
    suspend fun addSakeToTastedList(id: Int): Resource<Unit>
    suspend fun removeSakeFromTastedList(id: Int): Resource<Unit>
}
