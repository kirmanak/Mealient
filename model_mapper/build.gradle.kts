plugins {
    id("gq.kirmanak.mealient.library")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gq.kirmanak.mealient.model_mapper"
}

dependencies {
    implementation(project(":database"))
    testImplementation(project(":database_test"))
    implementation(project(":datasource"))
    testImplementation(project(":datasource_test"))
    implementation(project(":datastore"))
    testImplementation(project(":datastore_test"))
    testImplementation(project(":testing"))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)
}
