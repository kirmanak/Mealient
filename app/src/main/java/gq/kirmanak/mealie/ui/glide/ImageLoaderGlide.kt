package gq.kirmanak.mealie.ui.glide

import android.widget.ImageView
import androidx.annotation.DrawableRes
import gq.kirmanak.mealie.ui.ImageLoader
import javax.inject.Inject

class ImageLoaderGlide @Inject constructor() : ImageLoader {
    override fun loadImage(url: String?, @DrawableRes placeholderId: Int, imageView: ImageView) {
        GlideApp.with(imageView)
            .load(url)
            .centerCrop()
            .placeholder(placeholderId)
            .into(imageView)
    }
}