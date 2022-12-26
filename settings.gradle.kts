@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Mealient"

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
