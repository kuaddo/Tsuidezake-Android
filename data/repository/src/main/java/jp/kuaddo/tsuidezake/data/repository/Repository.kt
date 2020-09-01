package jp.kuaddo.tsuidezake.data.repository

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail

interface Repository {
    suspend fun getRankings(): Resource<List<Ranking>>
    suspend fun getSakeDetail(id: Int): Resource<SakeDetail>
}
