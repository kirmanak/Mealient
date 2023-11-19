plugins {
    id("gq.kirmanak.mealient.library")
    alias(libs.plugins.ksp)
    id("gq.kirmanak.mealient.compose")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gq.kirmanak.mealient.ui"
}

dependencies {
    implementation(project(":logging"))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.android.material.material)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)
}