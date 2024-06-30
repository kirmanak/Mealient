plugins {
    id("gq.kirmanak.mealient.library")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
}

android {
    namespace = "gq.kirmanak.mealient.architecture"
}

dependencies {
    implementation(libs.google.dagger.hiltAndroid)
    ksp(libs.google.dagger.hiltCompiler)

    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.coreTesting)
    testImplementation(libs.google.truth)
    testImplementation(project(":testing"))
}