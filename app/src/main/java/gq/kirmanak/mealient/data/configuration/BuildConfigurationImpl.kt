package gq.kirmanak.mealient.data.configuration

import gq.kirmanak.mealient.BuildConfig
import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigurationImpl @Inject constructor() : BuildConfiguration {

    @get:JvmName("_isDebug")
    private val isDebug by lazy { BuildConfig.DEBUG }

    private val versionCode by lazy { BuildConfig.VERSION_CODE }

    override fun isDebug(): Boolean = isDebug

    override fun versionCode(): Int = versionCode
}