package jp.kuaddo.tsuidezake.data.remote

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail

interface TsuidezakeService {
    suspend fun getRankings(): ApiResponse<List<Ranking>>
    suspend fun getSakeDetail(id: Int): ApiResponse<SakeDetail>
}
