package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.Sake
import javax.inject.Inject

class GetRecommendedSakesUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Unit, List<Sake>>() {
    override suspend fun execute(parameter: Unit): Resource<List<Sake>> =
        repository.getRecommendedSakes()
            .map { recommendedSakes ->
                recommendedSakes.sortedBy(Ranking.Content::rank)
                    .map { Sake.of(it.sakeDetail) }
            }
}
