@file:Suppress("UnstableApiUsage")

plugins {
    id("gq.kirmanak.mealient.library")
    alias(libs.plugins.ksp)
    id("gq.kirmanak.mealient.compose")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "gq.kirmanak.mealient.shopping_list"
}

ksp {
    arg("compose-destinations.generateNavGraphs", "false")
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
    implementation(libs.androidx.compose.materialIconsExtended)
    implementation(libs.google.dagger.hiltAndroid)
    implementation(libs.androidx.hilt.navigationCompose)
    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)

    ksp(libs.google.dagger.hiltCompiler)

    kspTest(libs.google.dagger.hiltAndroidCompiler)

    testImplementation(project(":testing"))
    testImplementation(libs.google.dagger.hiltAndroidTesting)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.google.truth)
    testImplementation(libs.io.mockk)
}
