plugins {
    id("gq.kirmanak.mealient.library")
}

android {
    namespace = "gq.kirmanak.mealient.datasource_test"
}

dependencies {
    implementation(project(":datasource"))
}
