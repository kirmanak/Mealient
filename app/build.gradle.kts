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
    id("com.guardsquare.appsweep") version Dependencies.appsweepVersion
    id("com.google.protobuf") version Dependencies.protobufPluginVersion
    id("com.google.devtools.ksp") version Dependencies.kspPluginVersion
}

android {
    compileSdk = Dependencies.compileSdkVersion

    defaultConfig {
        applicationId = "gq.kirmanak.mealient"
        minSdk = Dependencies.minSdkVersion
        targetSdk = Dependencies.targetSdkVersion
        versionCode = 13
        versionName = "0.2.4"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
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
    coreLibraryDesugaring(libs.android.tools.desugar)

    implementation(libs.android.material.material)

    implementation(libs.androidx.navigation.fragmentKtx)
    implementation(libs.androidx.navigation.runtimeKtx)
    implementation(libs.androidx.navigation.uiKtx)

    implementation(libs.androidx.coreKtx)

    implementation("androidx.appcompat:appcompat:${Dependencies.appcompatVersion}")

    implementation("androidx.constraintlayout:constraintlayout:${Dependencies.contraintLayoutVersion}")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Dependencies.swipeRefreshLayoutVersion}")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Dependencies.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependencies.lifecycleVersion}")

    implementation("com.google.dagger:hilt-android:${Dependencies.hiltVersion}")
    kapt("com.google.dagger:hilt-compiler:${Dependencies.hiltVersion}")
    kaptTest("com.google.dagger:hilt-android-compiler:${Dependencies.hiltVersion}")
    testImplementation("com.google.dagger:hilt-android-testing:${Dependencies.hiltVersion}")

    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Dependencies.retrofitKotlinxSerializationVersion}")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:${Dependencies.okhttpVersion}"))
    implementation("com.squareup.okhttp3:okhttp")
    debugImplementation("com.squareup.okhttp3:logging-interceptor")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Dependencies.kotlinxSerializationVersion}")

    implementation("com.jakewharton.timber:timber:${Dependencies.timberVersion}")

    implementation("androidx.paging:paging-runtime-ktx:${Dependencies.pagingVersion}")
    testImplementation("androidx.paging:paging-common-ktx:${Dependencies.pagingVersion}")

    implementation("androidx.room:room-runtime:${Dependencies.roomVersion}")
    implementation("androidx.room:room-ktx:${Dependencies.roomVersion}")
    implementation("androidx.room:room-paging:${Dependencies.roomVersion}")
    ksp("androidx.room:room-compiler:${Dependencies.roomVersion}")
    testImplementation("androidx.room:room-testing:${Dependencies.roomVersion}")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Dependencies.kotlinxDatetimeVersion}")

    implementation("com.github.bumptech.glide:glide:${Dependencies.glideVersion}")
    implementation("com.github.bumptech.glide:okhttp3-integration:${Dependencies.glideVersion}")
    implementation("com.github.bumptech.glide:recyclerview-integration:${Dependencies.glideVersion}") {
        // Excludes the support library because it's already included by Glide.
        isTransitive = false
    }
    kapt("com.github.bumptech.glide:compiler:${Dependencies.glideVersion}")

    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Dependencies.viewBindingDelegateVersion}")

    implementation("androidx.datastore:datastore-preferences:${Dependencies.datastoreVersion}")
    implementation("androidx.datastore:datastore:${Dependencies.datastoreVersion}")

    implementation("com.google.protobuf:protobuf-javalite:${Dependencies.protobufVersion}")

    implementation("androidx.security:security-crypto:${Dependencies.securityVersion}")

    implementation(platform("com.google.firebase:firebase-bom:${Dependencies.firebaseVersion}"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    testImplementation("junit:junit:${Dependencies.junitVersion}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependencies.coroutinesVersion}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutinesVersion}")

    testImplementation("org.robolectric:robolectric:${Dependencies.robolectricVersion}")

    testImplementation("androidx.test.ext:junit-ktx:${Dependencies.junitKtxVersion}")

    testImplementation("com.google.truth:truth:${Dependencies.truthVersion}")

    testImplementation("io.mockk:mockk:${Dependencies.mockkVersion}")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakcanaryVersion}")

    // https://github.com/ChuckerTeam/chucker/releases
    debugImplementation("com.github.chuckerteam.chucker:library:${Dependencies.chuckerVersion}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Dependencies.protobufVersion}"
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