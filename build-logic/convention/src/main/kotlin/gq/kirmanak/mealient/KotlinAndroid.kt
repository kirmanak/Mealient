@file:Suppress("UnstableApiUsage")

package gq.kirmanak.mealient

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
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

        lint {
            disable += listOf("ObsoleteLintCustomCheck", "IconMissingDensityFolder")
            enable += listOf(
                "ConvertToWebp",
                "DuplicateStrings",
                "EasterEgg",
                "ExpensiveAssertion",
                "IconExpectedSize",
                "ImplicitSamInstance",
                "InvalidPackage",
                "KotlinPropertyAccess",
                "LambdaLast",
                "MinSdkTooLow",
                "NegativeMargin",
                "NoHardKeywords",
                "Registered",
                "RequiredSize",
                "UnknownNullness",
                "WrongThreadInterprocedural"
            )
        }

        buildFeatures {
            viewBinding = true
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }

        buildTypes {
            getByName("debug") {
                enableUnitTestCoverage = true
            }
        }

        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("android-tools-desugar").get())
        }
    }
}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}