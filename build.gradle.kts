buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.google.servicesPlugin)
        classpath(libs.google.firebase.crashlyticsPlugin)
        classpath(libs.google.dagger.hiltPlugin)
        classpath(libs.jetbrains.kotlinPlugin)
        classpath(libs.jetbrains.serializationPlugin)
        classpath(libs.androidx.navigation.safeArgsPlugin)
    }
}

plugins {
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.rootcoverage)
}

sonarqube {
    properties {
        property("sonar.projectKey", "kirmanak_Mealient")
        property("sonar.organization", "kirmanak")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.androidLint.reportPaths", "build/reports/lint-results-debug.xml")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco.xml")
    }
}

rootCoverage {
    generateXml = true
}