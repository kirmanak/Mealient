package gq.kirmanak.mealient.data.configuration

import gq.kirmanak.mealient.BuildConfig
import gq.kirmanak.mealient.architecture.configuration.BuildConfiguration
import javax.inject.Inject

class BuildConfigurationImpl @Inject constructor() : BuildConfiguration {

    override fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun versionCode(): Int = BuildConfig.VERSION_CODE
}