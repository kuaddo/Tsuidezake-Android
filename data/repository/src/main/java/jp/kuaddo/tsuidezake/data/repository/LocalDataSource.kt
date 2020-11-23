package jp.kuaddo.tsuidezake.data.repository

import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun loadUserSakeFlow(sakeId: Int): Flow<UserSake?>

    suspend fun saveUserSake(userSake: UserSake)
    suspend fun saveSakeDetail(sakeDetail: SakeDetail)
}
