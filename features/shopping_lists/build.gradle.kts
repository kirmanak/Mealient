@file:Suppress("UnstableApiUsage")

plugins {
    id("gq.kirmanak.mealient.library")
    alias(libs.plugins.ksp)
    id("gq.kirmanak.mealient.compose")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gq.kirmanak.mealient.shopping_list"
}

dependencies {
    implementation(project(":architecture"))
    implementation(project(":logging"))
    implementation(project(":datasource"))
    implementation(project(":database"))
    implementation(project(":ui"))
    implementation(project(":model_mapper"))

    implementation(libs.android.material.material)
    implementation(libs.androidx.compose.material)

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.androidx.hilt.navigationCompose)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)
}

kapt {
    correctErrorTypes = true
}
