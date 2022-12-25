package gq.kirmanak.mealient

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    val variants = when (commonExtension) {
        is BaseAppModuleExtension -> commonExtension.applicationVariants
        is LibraryExtension -> commonExtension.libraryVariants
        else -> throw IllegalStateException("Unsupported extension type")
    }

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            val version = libs.findVersion("composeKotlinCompilerExtension")
            kotlinCompilerExtensionVersion = version.get().toString()
        }

        // Add compose-destinations generated code to Gradle source sets
        variants.all {
            kotlin.sourceSets {
                getByName(name) {
                    kotlin.srcDir("build/generated/ksp/$name/kotlin")
                }
            }
        }

        dependencies {
            val bom = library("androidx-compose-bom")
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", library("androidx-compose-material3"))
            add("implementation", library("androidx-compose-ui-toolingPreview"))
            add("implementation", library("androidx-compose-runtime-livedata"))
            add("implementation", library("androidx-lifecycle-viewmodelCompose"))
            add("implementation", library("google-accompanist-themeadapter-material3"))
            add("debugImplementation", library("androidx-compose-ui-tooling"))
            add("debugImplementation", library("androidx-compose-ui-testManifest"))
            add("androidTestImplementation", library("androidx-compose-ui-testJunit"))
            add("implementation", library("composeDestinations-core"))
            add("ksp", library("composeDestinations-ksp"))
        }
    }
}

private fun Project.library(name: String): Provider<MinimalExternalModuleDependency> {
    return libs.findLibrary(name).get()
}

private val Project.kotlin: KotlinAndroidProjectExtension
    get() = (this as ExtensionAware).extensions.getByName("kotlin") as KotlinAndroidProjectExtension

private fun KotlinAndroidProjectExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
    (this as ExtensionAware).extensions.configure("sourceSets", configure)


