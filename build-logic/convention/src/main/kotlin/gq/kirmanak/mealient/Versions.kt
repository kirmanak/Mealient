package gq.kirmanak.mealient

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

object Versions {
    const val MIN_SDK_VERSION = 23
    const val TARGET_SDK_VERSION = 33
    const val COMPILE_SDK_VERSION = 33
}

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
