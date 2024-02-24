@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Mealient"

System.setProperty("sonar.gradle.skipCompile", "true")

include(":app")
include(":architecture")
include(":database")
include(":database_test")
include(":datastore")
include(":datastore_test")
include(":logging")
include(":datasource")
include(":datasource_test")
include(":testing")
include(":ui")
include(":model_mapper")
include(":features:shopping_lists")
