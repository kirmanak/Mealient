plugins {
    id("gq.kirmanak.mealient.library")
}

android {
    namespace = "gq.kirmanak.mealient.datastore_test"
}

dependencies {
    implementation(project(":datastore"))
}
