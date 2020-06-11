package jp.kuaddo.tsuidezake.data.remote

import jp.kuaddo.tsuidezake.model.SakeDetail

interface TsuidezakeService {
    suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail>
}
