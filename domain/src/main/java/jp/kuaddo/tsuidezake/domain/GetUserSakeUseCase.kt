package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSakeUseCase @Inject constructor(
    private val repository: Repository
) : FlowUseCase<Int, Resource<UserSake>>() {
    override fun execute(parameter: Int): Flow<Resource<UserSake>> =
        repository.getUserSake(parameter)
}
