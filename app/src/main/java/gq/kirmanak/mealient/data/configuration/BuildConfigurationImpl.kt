package gq.kirmanak.mealient.data.configuration

import gq.kirmanak.mealient.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigurationImpl @Inject constructor() : BuildConfiguration {

    override fun isDebug(): Boolean = BuildConfig.DEBUG
}