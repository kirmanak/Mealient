plugins {
    id("gq.kirmanak.mealient.library")
    alias(libs.plugins.ksp)
    id("gq.kirmanak.mealient.compose")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gq.kirmanak.mealient.ui"
}

dependencies {
    implementation(project(":logging"))

    implementation(libs.google.dagger.hiltAndroid)
    ksp(libs.google.dagger.hiltCompiler)
    kspTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.android.material.material)
    implementation(libs.androidx.compose.material)

    implementation(libs.androidx.paging.compose)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)
}