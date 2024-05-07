@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("gq.kirmanak.mealient.application")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
    id("gq.kirmanak.mealient.compose.app")
}

android {
    defaultConfig {
        applicationId = "gq.kirmanak.mealient"
        versionCode = 35
        versionName = "0.4.6"
        testInstrumentationRunner = "gq.kirmanak.mealient.MealientTestRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
        resourceConfigurations += listOf("en", "es", "ru", "fr", "nl", "pt", "de")
    }

    signingConfigs {
        create("release") {
            rootProject.file("keystore.properties").also { keystorePropertiesFile ->
                if (keystorePropertiesFile.canRead()) {
                    val keystoreProperties = Properties()
                    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                    keyAlias = keystoreProperties.getProperty("keyAlias")
                    keyPassword = keystoreProperties.getProperty("keyPassword")
                    storeFile = file(keystoreProperties.getProperty("storeFile"))
                    storePassword = keystoreProperties.getProperty("storePassword")
                } else {
                    println("Unable to read keystore.properties")
                }
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    namespace = "gq.kirmanak.mealient"

    packaging {
        resources.excludes += "DebugProbesKt.bin"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("pixel2api30").apply {
                    device = "Pixel 2"
                    apiLevel = 30
                    systemImageSource = "aosp"
                }
            }
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

ksp {
    arg("compose-destinations.generateNavGraphs", "false")
}

dependencies {
    implementation(project(":architecture"))
    implementation(project(":database"))
    implementation(project(":datastore"))
    implementation(project(":datasource"))
    implementation(project(":logging"))
    implementation(project(":ui"))
    implementation(project(":features:shopping_lists"))
    implementation(project(":model_mapper"))
    implementation(libs.android.material.material)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewmodelKtx)
    implementation(libs.androidx.shareTarget)
    implementation(libs.androidx.compose.materialIconsExtended)
    implementation(libs.google.dagger.hiltAndroid)
    implementation(libs.androidx.paging.runtimeKtx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.hilt.navigationCompose)
    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)

    debugImplementation(libs.squareup.leakcanary)

    kover(project(":model_mapper"))
    kover(project(":features:shopping_lists"))
    kover(project(":ui"))
    kover(project(":logging"))
    kover(project(":architecture"))
    kover(project(":database"))
    kover(project(":datastore"))
    kover(project(":datasource"))

    kapt(libs.google.dagger.hiltCompiler)

    kaptTest(libs.google.dagger.hiltAndroidCompiler)

    kaptAndroidTest(libs.google.dagger.hiltAndroidCompiler)

    testImplementation(project(":datasource_test"))
    testImplementation(project(":database_test"))
    testImplementation(project(":datastore_test"))
    testImplementation(project(":testing"))
    testImplementation(libs.androidx.paging.commonKtx)
    testImplementation(libs.junit)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.coreTesting)
    testImplementation(libs.google.truth)
    testImplementation(libs.io.mockk)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.kaspersky.kaspresso)
    androidTestImplementation(libs.kaspersky.kaspresso.compose)
    androidTestImplementation(libs.okhttp3.mockwebserver)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.google.dagger.hiltAndroidTesting)

    androidTestUtil(libs.androidx.test.orchestrator)
}

koverReport {
    filters {
        excludes {
            classes(
                "gq.kirmanak.mealient.datastore.recipe.AddRecipeInput*", // generated by data store
                "*ComposableSingletons*", // generated by Compose
                "gq.kirmanak.mealient.database.AppDb_Impl*", // generated by Room
                "*Dao_Impl*", // generated by Room
                "*Hilt_*", // generated by Hilt
            )
            packages(
                "gq.kirmanak.mealient*.destinations", // generated by Compose destinations
            )
            annotatedBy(
                "androidx.compose.ui.tooling.preview.Preview",
                "gq.kirmanak.mealient.ui.preview.ColorSchemePreview",
                "androidx.compose.runtime.Composable",
                "dagger.Module",
                "dagger.internal.DaggerGenerated",
            )
        }
        includes {
            packages("gq.kirmanak.mealient")
        }
    }
    androidReports("release") {
        verify {
            rule {
                minBound(30)
            }
        }
    }
}
