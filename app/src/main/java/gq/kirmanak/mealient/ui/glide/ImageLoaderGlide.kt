package gq.kirmanak.mealient.ui.glide

import android.widget.ImageView
import androidx.annotation.DrawableRes
import gq.kirmanak.mealient.ui.ImageLoader
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