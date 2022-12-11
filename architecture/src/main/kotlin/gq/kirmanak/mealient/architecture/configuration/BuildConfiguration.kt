package gq.kirmanak.mealient.architecture.configuration

interface BuildConfiguration {

    fun isDebug(): Boolean

    fun versionCode(): Int
}