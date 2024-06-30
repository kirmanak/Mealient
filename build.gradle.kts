buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.google.dagger.hiltPlugin)
        classpath(libs.jetbrains.kotlinPlugin)
        classpath(libs.jetbrains.serializationPlugin)
    }
}

plugins {
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.compose.compiler) apply false
}

sonarqube {
    properties {
        property("sonar.projectKey", "kirmanak_Mealient")
        property("sonar.organization", "kirmanak")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${projectDir.path}/app/build/reports/kover/reportRelease.xml"
        )
    }
}

subprojects {
    sonarqube {
        properties {
            property(
                "sonar.androidLint.reportPaths",
                "${projectDir.path}/build/reports/lint-results-debug.xml"
            )

        }
    }
}