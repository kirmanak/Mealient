plugins {
    id("gq.kirmanak.mealient.library")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "gq.kirmanak.mealient.architecture"
}

dependencies {
    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)

    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.coreTesting)
    testImplementation(libs.google.truth)
    testImplementation(project(":testing"))
}