package gq.kirmanak.mealient

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal val Project.kotlin: KotlinAndroidProjectExtension
    get() = (this as ExtensionAware).extensions.getByName("kotlin") as KotlinAndroidProjectExtension

internal fun Project.kotlin(configure: Action<KotlinAndroidProjectExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)

internal fun KotlinAndroidProjectExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
    (this as ExtensionAware).extensions.configure("sourceSets", configure)

internal fun Project.library(name: String): Provider<MinimalExternalModuleDependency> {
    return libs.findLibrary(name).get()
}

