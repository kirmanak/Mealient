plugins {
    `kotlin-dsl`
}

group = "gq.kirmanak.mealient.buildlogic"

dependencies {
    implementation(libs.jetbrains.kotlinPlugin)
    implementation(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gq.kirmanak.mealient.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "gq.kirmanak.mealient.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}
