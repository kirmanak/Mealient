plugins {
    id("gq.kirmanak.mealient.library")
}

android {
    namespace = "gq.kirmanak.mealient.database_test"
}

dependencies {
    implementation(project(":database"))
}