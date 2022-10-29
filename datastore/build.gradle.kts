plugins {
    id("gq.kirmanak.mealient.library")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "gq.kirmanak.mealient.datastore"
}

dependencies {
    implementation(project(":logging"))

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.datastore)

    implementation(libs.google.protobuf.javalite)

    implementation(libs.androidx.security.crypto)

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltCompiler)
    kaptTest(libs.google.dagger.hiltAndroidCompiler)
    testImplementation(libs.google.dagger.hiltAndroidTesting)

    implementation(libs.jetbrains.kotlinx.datetime)

    implementation(libs.jetbrains.kotlinx.coroutinesAndroid)
    testImplementation(libs.jetbrains.kotlinx.coroutinesTest)

    testImplementation(libs.androidx.test.junit)

    testImplementation(libs.google.truth)

    testImplementation(libs.io.mockk)
}

protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                val java by registering {
                    option("lite")
                }
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}
