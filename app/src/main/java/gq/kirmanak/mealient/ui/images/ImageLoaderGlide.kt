package gq.kirmanak.mealient.ui.images

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.hilt.android.scopes.FragmentScoped
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class ImageLoaderGlide @Inject constructor(
    private val fragment: Fragment,
) : ImageLoader {

    private val requestManager: RequestManager
        get() = Glide.with(fragment)

    init {
        Timber.v("init called with fragment = ${fragment.javaClass.simpleName}")
    }

    override fun loadImage(url: String?, placeholderId: Int, imageView: ImageView) {
        Timber.v("loadImage() called with: url = $url, placeholderId = $placeholderId, imageView = $imageView")
        requestManager.load(url)
            .placeholder(placeholderId)
            .centerCrop()
            .into(imageView)
    }
}