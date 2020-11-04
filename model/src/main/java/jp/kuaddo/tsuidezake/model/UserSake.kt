package jp.kuaddo.tsuidezake.model

data class UserSake(
    val sakeDetail: SakeDetail,
    val isAddedToWish: Boolean,
    val isAddedToTasted: Boolean
)
