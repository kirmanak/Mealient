package gq.kirmanak.mealient.ui.images

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {
    fun loadImage(url: String?, @DrawableRes placeholderId: Int, imageView: ImageView)
}