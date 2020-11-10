package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.UserSake
import javax.inject.Inject

class GetUserSakeUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Int, UserSake>() {
    override suspend fun execute(parameter: Int): Resource<UserSake> =
        repository.getUserSake(parameter)
}
