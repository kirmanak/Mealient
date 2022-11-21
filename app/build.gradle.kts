@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.*

plugins {
    id("gq.kirmanak.mealient.application")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.ksp)
    alias(libs.plugins.appsweep)
}

android {
    defaultConfig {
        applicationId = "gq.kirmanak.mealient"
        versionCode = 21
        versionName = "0.3.6"
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
        getByName("debug") {
            ext["enableCrashlytics"] = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
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

    packagingOptions {
        resources.excludes += "DebugProbesKt.bin"
    }
}

dependencies {

    implementation(project(":architecture"))
    implementation(project(":database"))
    implementation(project(":datastore"))
    implementation(project(":datasource"))
    implementation(project(":logging"))
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

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

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

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analyticsKtx)
    implementation(libs.google.firebase.crashlyticsKtx)

    testImplementation(libs.junit)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.robolectric)

    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.coreTesting)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)

    debugImplementation(libs.squareup.leakcanary)
}