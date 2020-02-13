package jp.kuaddo.tsuidezake.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util

abstract class SimpleBitmapTarget(
    private val width: Int = Target.SIZE_ORIGINAL,
    private val height: Int = Target.SIZE_ORIGINAL
) :
    Target<Bitmap> {
    override fun onLoadStarted(placeholder: Drawable?) {}

    override fun onLoadFailed(errorDrawable: Drawable?) {}

    final override fun getSize(cb: SizeReadyCallback) {
        if (!Util.isValidDimensions(width, height))
            throw IllegalArgumentException("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: $width and height: $height")
        cb.onSizeReady(width, height)
    }

    override fun getRequest(): Request? = null

    override fun onStop() {}

    override fun setRequest(request: Request?) {}

    override fun removeCallback(cb: SizeReadyCallback) {}

    override fun onLoadCleared(placeholder: Drawable?) {}

    override fun onStart() {}

    override fun onDestroy() {}
}