package jp.kuaddo.tsuidezake.model

data class ObsoleteRanking(
    val category: String,
    val sakes: List<Drink> // TODO: APIが未実装のためダミーでDrinkを利用。後で修正
)