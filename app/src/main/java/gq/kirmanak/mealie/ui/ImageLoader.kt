package gq.kirmanak.mealie.ui

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {
    fun loadImage(url: String?, @DrawableRes placeholderId: Int, imageView: ImageView)
}