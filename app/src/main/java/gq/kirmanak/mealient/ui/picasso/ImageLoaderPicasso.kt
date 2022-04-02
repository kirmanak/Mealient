package gq.kirmanak.mealient.ui.picasso

import android.widget.ImageView
import com.squareup.picasso.Picasso
import gq.kirmanak.mealient.ui.ImageLoader
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoaderPicasso @Inject constructor(
    private val picasso: Picasso
) : ImageLoader {

    override fun loadImage(url: String?, placeholderId: Int, imageView: ImageView) {
        Timber.v("loadImage() called with: url = $url, placeholderId = $placeholderId, imageView = $imageView")
        val width = imageView.measuredWidth
        val height = imageView.measuredHeight
        Timber.d("loadImage: width = $width, height = $height")
        picasso.load(url).apply {
            placeholder(placeholderId)
            if (width > 0 && height > 0) resize(width, height).centerCrop()
            into(imageView)
        }
    }
}