package gq.kirmanak.mealient.ui.baseurl

import gq.kirmanak.mealient.data.network.NetworkError

data class BaseURLScreenState(
    val error: NetworkError? = null,
    val navigateNext: Boolean = false,
)
