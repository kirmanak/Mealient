package gq.kirmanak.mealient.ui.activity

import android.os.Bundle
import androidx.annotation.IdRes

data class StartDestinationInfo(
    @IdRes val startDestinationId: Int,
    val startDestinationArgs: Bundle? = null,
)
