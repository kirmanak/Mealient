@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import java.io.FileInputStream
import java.util.*

plugins {
    id("gq.kirmanak.mealient.application")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.appsweep)
    id("gq.kirmanak.mealient.compose.app")
}

android {
    defaultConfig {
        applicationId = "gq.kirmanak.mealient"
        versionCode = 31
        versionName = "0.4.2"
        testInstrumentationRunner = "gq.kirmanak.mealient.MealientTestRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")
        resourceConfigurations += listOf("en", "es", "ru", "fr", "nl", "pt", "de")

        buildConfigField(
            "String",
            "ACRA_HOST",
            System.getenv("ACRA_HOST")?.let { "\"$it\"" } ?: "\"\"")
        buildConfigField(
            "String",
            "ACRA_LOGIN",
            System.getenv("ACRA_LOGIN")?.let { "\"$it\"" } ?: "\"\"")
        buildConfigField(
            "String",
            "ACRA_PASSWORD",
            System.getenv("ACRA_PASSWORD")?.let { "\"$it\"" } ?: "\"\"")
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
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

dependencies {

    implementation(project(":architecture"))
    implementation(project(":database"))
    testImplementation(project(":database_test"))
    implementation(project(":datastore"))
    testImplementation(project(":datastore_test"))
    implementation(project(":datasource"))
    testImplementation(project(":datasource_test"))
    implementation(project(":logging"))
    implementation(project(":ui"))
    implementation(project(":features:shopping_lists"))
    implementation(project(":model_mapper"))
    testImplementation(project(":testing"))

    implementation(libs.android.material.material)

    implementation(libs.androidx.navigation.fragmentKtx)
    implementation(libs.androidx.navigation.runtimeKtx)
    implementation(libs.androidx.navigation.uiKtx)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.splashScreen)

    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.constraintLayout)

    implementation(libs.androidx.swipeRefreshLayout)

    implementation(libs.androidx.lifecycle.livedataKtx)
    implementation(libs.androidx.lifecycle.viewmodelKtx)

    implementation(libs.androidx.shareTarget)

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)
    kaptAndroidTest(libs.google.dagger.hiltAndroidCompiler)
    androidTestImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.androidx.paging.runtimeKtx)
    testImplementation(libs.androidx.paging.commonKtx)

    implementation(libs.jetbrains.kotlinx.datetime)

    implementation(libs.bumptech.glide.glide)
    implementation(libs.bumptech.glide.okhttp3)
    implementation(libs.bumptech.glide.recyclerview) {
        // Excludes the support library because it's already included by Glide.
        isTransitive = false
    }
    ksp(libs.bumptech.glide.ksp)

    implementation(libs.kirich1409.viewBinding)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.acra.http)
    implementation(libs.acra.scheduler)

    testImplementation(libs.junit)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.robolectric)

    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.coreTesting)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)

    debugImplementation(libs.squareup.leakcanary)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.kaspersky.kaspresso)
    androidTestImplementation(libs.okhttp3.mockwebserver)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestUtil(libs.androidx.test.orchestrator)
}