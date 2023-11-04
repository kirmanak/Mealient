plugins {
    id("gq.kirmanak.mealient.library")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    defaultConfig {
        buildConfigField("Boolean", "LOG_NETWORK", "true")
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

    api(libs.jetbrains.kotlinx.datetime)

    implementation(libs.jetbrains.kotlinx.serialization)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3.okhttp)
    debugImplementation(libs.okhttp3.loggingInterceptor)

    implementation(libs.ktor.core)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.encoding)
    implementation(libs.ktor.negotiation)
    implementation(libs.ktor.json)
    implementation(libs.ktor.okhttp)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)

    debugImplementation(libs.chuckerteam.chucker)
}
