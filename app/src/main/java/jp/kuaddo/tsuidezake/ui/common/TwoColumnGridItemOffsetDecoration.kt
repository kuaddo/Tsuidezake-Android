package jp.kuaddo.tsuidezake.ui.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class TwoColumnGridItemOffsetDecoration(context: Context, offsetDp: Int) : ItemDecoration() {

    private val offset = (context.resources.displayMetrics.density * offsetDp).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) % 2 == 0)
            outRect.set(offset, offset / 2, offset / 2, offset / 2)
        else
            outRect.set(offset / 2, offset / 2, offset, offset / 2)
    }
}