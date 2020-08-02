package jp.kuaddo.tsuidezake.model

data class Ranking(
    val category: String,
    val sakes: List<Drink> // TODO: APIが未実装のためダミーでDrinkを利用。後で修正
)