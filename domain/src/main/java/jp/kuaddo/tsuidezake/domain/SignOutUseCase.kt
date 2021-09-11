package jp.kuaddo.tsuidezake.domain

import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.signOut()
}
