package gq.kirmanak.mealient

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

object Versions {
    const val MIN_SDK_VERSION = 26
    const val TARGET_SDK_VERSION = 34
    const val COMPILE_SDK_VERSION = 34
}

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
