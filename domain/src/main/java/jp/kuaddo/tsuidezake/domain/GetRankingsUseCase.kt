package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import javax.inject.Inject

class GetRankingsUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Unit, List<Ranking>>() {
    override suspend fun execute(parameter: Unit): Resource<List<Ranking>> =
        repository.getRankings()
            .map { rankings ->
                rankings.sortedBy(Ranking::displayOrder)
                    .map { ranking ->
                        ranking.copy(contents = ranking.contents.sortedBy(Ranking.Content::rank))
                    }
            }
}
