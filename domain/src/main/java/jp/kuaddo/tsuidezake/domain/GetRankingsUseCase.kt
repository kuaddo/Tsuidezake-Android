package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRankingsUseCase @Inject constructor(
    private val repository: Repository
) : FlowUseCase<Unit, Resource<List<Ranking>>>() {
    override fun execute(parameter: Unit): Flow<Resource<List<Ranking>>> =
        repository.getRankings()
            .map { result -> result.map(::sorted) }

    private fun sorted(rankings: List<Ranking>): List<Ranking> = rankings
        .sortedBy(Ranking::displayOrder)
        .map { ranking -> ranking.copy(contents = ranking.contents.sortedBy { it.rank }) }
}
