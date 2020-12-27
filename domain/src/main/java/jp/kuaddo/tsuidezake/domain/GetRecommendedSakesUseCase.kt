package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.Sake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecommendedSakesUseCase @Inject constructor(
    private val repository: Repository
) : FlowUseCase<Unit, Resource<List<Sake>>>() {
    override fun execute(parameter: Unit): Flow<Resource<List<Sake>>> =
        repository.getRecommendedSakes()
            .map { result ->
                result.map { recommendedSakes ->
                    recommendedSakes.sortedBy(Ranking.Content::rank)
                        .map { Sake.of(it.sakeDetail) }
                }
            }
}
