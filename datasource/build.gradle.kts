plugins {
    id("gq.kirmanak.mealient.library")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    defaultConfig {
        buildConfigField("Boolean", "LOG_NETWORK", "false")
    }
    namespace = "gq.kirmanak.mealient.datasource"
}

dependencies {
    implementation(project(":logging"))
    testImplementation(project(":testing"))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.jetbrains.kotlinx.datetime)

    implementation(libs.jetbrains.kotlinx.serialization)

    implementation(libs.squareup.retrofit)

    implementation(libs.jakewharton.retrofitSerialization)

    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3.okhttp)
    debugImplementation(libs.okhttp3.loggingInterceptor)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)

    debugImplementation(libs.chuckerteam.chucker)
}
