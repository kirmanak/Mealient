@file:Suppress("UnstableApiUsage")

plugins {
    id("gq.kirmanak.mealient.library")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gq.kirmanak.mealient.shopping_list"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompilerExtension.get()
    }
}

dependencies {
    implementation(project(":logging"))
    implementation(project(":datasource"))

    implementation(libs.android.material.material)

    implementation(platform(libs.android.compose.bom))
    implementation(libs.android.compose.material3)
    implementation(libs.android.compose.ui.toolingPreview)
    implementation(libs.android.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.google.accompanist.themeadapter.material3)
    debugImplementation(libs.android.compose.ui.tooling)
    debugImplementation(libs.android.compose.ui.testManifest)
    androidTestImplementation(libs.android.compose.ui.testJunit)
    androidTestImplementation(platform(libs.android.compose.bom))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)
}

kapt {
    correctErrorTypes = true
}
