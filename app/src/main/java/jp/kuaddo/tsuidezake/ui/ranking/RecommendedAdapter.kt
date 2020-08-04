package jp.kuaddo.tsuidezake.ui.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.kuaddo.tsuidezake.databinding.ItemRecommendedBinding
import jp.kuaddo.tsuidezake.extensions.diffUtil
import jp.kuaddo.tsuidezake.model.Sake
import jp.kuaddo.tsuidezake.ui.common.DataBoundListAdapter

class RecommendedAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClickItem: (sake: Sake) -> Unit
) : DataBoundListAdapter<Sake, ItemRecommendedBinding>(lifecycleOwner, Sake.diffUtil) {
    override fun getItemCount(): Int = DUMMY_OFFSET + super.getItemCount() + DUMMY_OFFSET

    override fun getItem(position: Int): Sake {
        val actualSize = currentList.size
        return super.getItem((position + actualSize - DUMMY_OFFSET) % actualSize)
    }

    override fun createBinding(parent: ViewGroup): ItemRecommendedBinding =
        ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemRecommendedBinding, item: Sake, position: Int) {
        binding.sakeName = item.name
        val isClickable = getClickable(position)
        binding.root.setOnClickListener(
            if (isClickable) View.OnClickListener { onClickItem(item) }
            else null
        )
        binding.root.isClickable = isClickable
    }

    /**
     * 右端(左端)に到達した際にジャンプしなければならない位置を計算。
     * ジャンプの必要がない場合にnull、必要がある場合にその位置を返す
     */
    fun getJumpPosition(sourcePosition: Int): Int? {
        val actualSize = currentList.size
        return when {
            sourcePosition < DUMMY_OFFSET -> actualSize + sourcePosition
            sourcePosition >= actualSize + DUMMY_OFFSET -> sourcePosition - actualSize
            else -> null
        }
    }

    private fun getClickable(position: Int): Boolean =
        position in DUMMY_OFFSET until currentList.size + DUMMY_OFFSET

    companion object {
        // 前後の要素をチラ見せするので、一つ余分にoffsetが必要
        private const val DUMMY_OFFSET = 2
        const val START_INDEX = DUMMY_OFFSET
    }
}
