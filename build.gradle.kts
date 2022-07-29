buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Dependencies.android_plugin_version}")
        classpath("com.google.gms:google-services:${Dependencies.google_services_version}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Dependencies.crashlytics_version}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Dependencies.kotlin_version}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Dependencies.nav_version}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.hilt_version}")
    }
}

plugins {
    id("org.sonarqube") version Dependencies.sonarqube_version
    id("nl.neotech.plugin.rootcoverage") version Dependencies.root_coverage_version
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