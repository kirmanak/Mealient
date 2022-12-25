plugins {
    id("gq.kirmanak.mealient.library")
}

android {
    namespace = "gq.kirmanak.mealient.database"
}

dependencies {
    implementation(project(":database"))
}