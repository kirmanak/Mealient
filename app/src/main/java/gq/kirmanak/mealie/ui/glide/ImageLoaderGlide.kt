package gq.kirmanak.mealie.ui.glide

import android.widget.ImageView
import androidx.annotation.DrawableRes
import gq.kirmanak.mealie.ui.ImageLoader
import javax.inject.Inject

class ImageLoaderGlide @Inject constructor() : ImageLoader {
    override fun loadImage(url: String?, @DrawableRes placeholderId: Int, imageView: ImageView) {
        with(GlideApp.with(imageView)) {
            if (url.isNullOrBlank()) clear(imageView)
            else load(url).placeholder(placeholderId).into(imageView)
        }
    }
}