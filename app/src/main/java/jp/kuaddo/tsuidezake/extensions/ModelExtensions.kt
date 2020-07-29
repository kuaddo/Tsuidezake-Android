package jp.kuaddo.tsuidezake.extensions

import androidx.recyclerview.widget.DiffUtil
import jp.kuaddo.tsuidezake.model.Sake

val Sake.Companion.diffUtil
    get() = object : DiffUtil.ItemCallback<Sake>() {
        override fun areItemsTheSame(oldItem: Sake, newItem: Sake): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Sake, newItem: Sake): Boolean =
            oldItem == newItem
    }