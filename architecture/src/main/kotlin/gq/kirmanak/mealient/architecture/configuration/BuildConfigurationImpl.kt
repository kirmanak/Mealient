package gq.kirmanak.mealient.architecture.configuration

import androidx.viewbinding.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigurationImpl @Inject constructor() : BuildConfiguration {

    @get:JvmName("_isDebug")
    private val isDebug by lazy { BuildConfig.DEBUG }

    override fun isDebug(): Boolean = isDebug
}