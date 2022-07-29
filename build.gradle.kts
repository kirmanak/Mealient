buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Dependencies.androidPluginVersion}")
        classpath("com.google.gms:google-services:${Dependencies.googleServicesVersion}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Dependencies.crashlyticsVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Dependencies.kotlinVersion}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Dependencies.navVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.hiltVersion}")
    }
}

plugins {
    id("org.sonarqube") version Dependencies.sonarqubeVersion
    id("nl.neotech.plugin.rootcoverage") version Dependencies.rootCoverageVersion
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