package gq.kirmanak.mealie.ui

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import javax.inject.Inject

class ImageLoader @Inject constructor() {
    fun loadImage(url: String?, @DrawableRes placeholderId: Int, imageView: ImageView) {
        with(Glide.with(imageView)) {
            if (url.isNullOrBlank()) clear(imageView)
            else load(url).placeholder(placeholderId).into(imageView)
        }
    }
}