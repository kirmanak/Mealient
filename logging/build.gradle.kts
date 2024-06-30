plugins {
    id("gq.kirmanak.mealient.library")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
}

android {
    namespace = "gq.kirmanak.mealient.logging"
}

dependencies {
    implementation(project(":architecture"))

    implementation(libs.google.dagger.hiltAndroid)
    ksp(libs.google.dagger.hiltCompiler)
}