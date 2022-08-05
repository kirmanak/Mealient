plugins {
    id("gq.kirmanak.mealient.library")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "gq.kirmanak.mealient.logging"
}

dependencies {
    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
}