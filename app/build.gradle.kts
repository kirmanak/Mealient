@file:Suppress("UnstableApiUsage")

import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.android.application")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.appsweep)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ksp)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "gq.kirmanak.mealient"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 13
        versionName = "0.2.4"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        buildConfigField("Boolean", "LOG_NETWORK", "false")
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
            isTestCoverageEnabled = true
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

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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

    namespace = "gq.kirmanak.mealient"

    packagingOptions {
        resources.excludes += "DebugProbesKt.bin"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.tools.desugar)

    implementation(libs.android.material.material)

    implementation(libs.androidx.navigation.fragmentKtx)
    implementation(libs.androidx.navigation.runtimeKtx)
    implementation(libs.androidx.navigation.uiKtx)

    implementation(libs.androidx.coreKtx)

    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.constraintLayout)

    implementation(libs.androidx.swipeRefreshLayout)

    implementation(libs.androidx.lifecycle.livedataKtx)
    implementation(libs.androidx.lifecycle.viewmodelKtx)

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.squareup.retrofit)

    implementation(libs.jakewharton.retrofitSerialization)

    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3.okhttp)
    debugImplementation(libs.okhttp3.loggingInterceptor)

    implementation(libs.jetbrains.kotlinx.serialization)

    implementation(libs.jakewharton.timber)

    implementation(libs.androidx.paging.runtimeKtx)
    testImplementation(libs.androidx.paging.commonKtx)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    implementation(libs.jetbrains.kotlinx.datetime)

    implementation(libs.bumptech.glide.glide)
    implementation(libs.bumptech.glide.okhttp3)
    implementation(libs.bumptech.glide.recyclerview) {
        // Excludes the support library because it's already included by Glide.
        isTransitive = false
    }
    kapt(libs.bumptech.glide.compiler)

    implementation(libs.kirich1409.viewBinding)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.datastore)

    implementation(libs.google.protobuf.javalite)

    implementation(libs.androidx.security.crypto)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analyticsKtx)
    implementation(libs.google.firebase.crashlyticsKtx)

    testImplementation(libs.junit)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.robolectric)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)

    debugImplementation(libs.squareup.leakcanary)

    debugImplementation(libs.chuckerteam.chucker)
}

protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}