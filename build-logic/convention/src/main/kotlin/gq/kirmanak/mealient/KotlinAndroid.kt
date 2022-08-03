@file:Suppress("UnstableApiUsage")

package gq.kirmanak.mealient

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = Versions.COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = Versions.MIN_SDK_VERSION
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        }

        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("android-tools-desugar").get())
        }
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }
}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}