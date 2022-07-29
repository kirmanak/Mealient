@file:Suppress("UnstableApiUsage")

import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.android.application")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.guardsquare.appsweep") version Dependencies.appsweep_version
    id("com.google.protobuf") version Dependencies.protobuf_plugin_version
}

android {
    compileSdk = Dependencies.compile_sdk_version

    defaultConfig {
        applicationId = "gq.kirmanak.mealient"
        minSdk = Dependencies.min_sdk_version
        targetSdk = Dependencies.target_sdk_version
        versionCode = 13
        versionName = "0.2.4"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }

        buildConfigField("Boolean", "LOG_NETWORK", "false")
    }

    signingConfigs {
        create("release") {
            rootProject.file("keystore.properties").also { keystorePropertiesFile ->
                if (keystorePropertiesFile.canRead()) {
                    val keystoreProperties = Properties()
                    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                    keyAlias = keystoreProperties.getProperty("keyAlias")
                    keyPassword = keystoreProperties.getProperty("keyPassword")
                    storeFile = file(keystoreProperties.getProperty("storeFile"))
                    storePassword = keystoreProperties.getProperty("storePassword")
                } else {
                    println("Unable to read keystore.properties")
                }
            }
        }
    }

    buildTypes {
        getByName("debug") {
            ext["enableCrashlytics"] = false
            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    lint {
        disable += listOf("ObsoleteLintCustomCheck", "IconMissingDensityFolder")
        enable += listOf(
            "ConvertToWebp",
            "DuplicateStrings",
            "EasterEgg",
            "ExpensiveAssertion",
            "IconExpectedSize",
            "ImplicitSamInstance",
            "InvalidPackage",
            "KotlinPropertyAccess",
            "LambdaLast",
            "MinSdkTooLow",
            "NegativeMargin",
            "NoHardKeywords",
            "Registered",
            "RequiredSize",
            "UnknownNullness",
            "WrongThreadInterprocedural"
        )
    }

    namespace = "gq.kirmanak.mealient"

    packagingOptions {
        resources.excludes += "DebugProbesKt.bin"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

tasks.withType<Test>().configureEach {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Dependencies.desugar_version}")

    implementation("com.google.android.material:material:${Dependencies.material_version}")

    implementation("androidx.navigation:navigation-fragment-ktx:${Dependencies.nav_version}")
    implementation("androidx.navigation:navigation-runtime-ktx:${Dependencies.nav_version}")
    implementation("androidx.navigation:navigation-ui-ktx:${Dependencies.nav_version}")

    implementation("androidx.core:core-ktx:${Dependencies.core_ktx_version}")

    implementation("androidx.appcompat:appcompat:${Dependencies.appcompat_version}")

    implementation("androidx.constraintlayout:constraintlayout:${Dependencies.contraint_layout_version}")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Dependencies.swipe_refresh_layout_version}")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Dependencies.lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependencies.lifecycle_version}")

    implementation("com.google.dagger:hilt-android:${Dependencies.hilt_version}")
    kapt("com.google.dagger:hilt-compiler:${Dependencies.hilt_version}")
    kaptTest("com.google.dagger:hilt-android-compiler:${Dependencies.hilt_version}")
    testImplementation("com.google.dagger:hilt-android-testing:${Dependencies.hilt_version}")

    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofit_version}")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Dependencies.retrofit_kotlinx_serialization_version}")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:${Dependencies.okhttp_version}"))
    implementation("com.squareup.okhttp3:okhttp")
    debugImplementation("com.squareup.okhttp3:logging-interceptor")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Dependencies.kotlinx_serialization_version}")

    implementation("com.jakewharton.timber:timber:${Dependencies.timber_version}")

    implementation("androidx.paging:paging-runtime-ktx:${Dependencies.paging_version}")
    testImplementation("androidx.paging:paging-common-ktx:${Dependencies.paging_version}")

    implementation("androidx.room:room-runtime:${Dependencies.room_version}")
    implementation("androidx.room:room-ktx:${Dependencies.room_version}")
    implementation("androidx.room:room-paging:${Dependencies.room_version}")
    kapt("androidx.room:room-compiler:${Dependencies.room_version}")
    testImplementation("androidx.room:room-testing:${Dependencies.room_version}")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Dependencies.kotlinx_datetime_version}")

    implementation("com.github.bumptech.glide:glide:${Dependencies.glide_version}")
    implementation("com.github.bumptech.glide:okhttp3-integration:${Dependencies.glide_version}")
    implementation("com.github.bumptech.glide:recyclerview-integration:${Dependencies.glide_version}") {
        // Excludes the support library because it's already included by Glide.
        isTransitive = false
    }
    kapt("com.github.bumptech.glide:compiler:${Dependencies.glide_version}")

    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Dependencies.view_binding_delegate_version}")

    implementation("androidx.datastore:datastore-preferences:${Dependencies.datastore_version}")
    implementation("androidx.datastore:datastore:${Dependencies.datastore_version}")

    implementation("com.google.protobuf:protobuf-javalite:${Dependencies.protobuf_version}")

    implementation("androidx.security:security-crypto:${Dependencies.security_version}")

    implementation(platform("com.google.firebase:firebase-bom:${Dependencies.firebase_version}"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    testImplementation("junit:junit:${Dependencies.junit_version}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependencies.coroutines_version}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutines_version}")

    testImplementation("org.robolectric:robolectric:${Dependencies.robolectric_version}")

    testImplementation("androidx.test.ext:junit-ktx:${Dependencies.junit_ktx_version}")

    testImplementation("com.google.truth:truth:${Dependencies.truth_version}")

    testImplementation("io.mockk:mockk:${Dependencies.mockk_version}")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakcanary_version}")

    // https://github.com/ChuckerTeam/chucker/releases
    debugImplementation("com.github.chuckerteam.chucker:library:${Dependencies.chucker_version}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Dependencies.protobuf_version}"
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