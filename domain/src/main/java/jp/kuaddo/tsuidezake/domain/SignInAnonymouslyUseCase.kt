package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SuccessResource
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Unit, Unit>() {
    override suspend fun execute(parameter: Unit): Resource<Unit> =
        if (repository.signInAnonymously()) SuccessResource(Unit)
        else ErrorResource(SIGN_IN_ERROR_MESSAGE, null)

    companion object {
        private const val SIGN_IN_ERROR_MESSAGE = "Sign in error"
    }
}
