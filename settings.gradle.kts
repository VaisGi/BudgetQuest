pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BudgetQuest"

include(":shared")
include(":androidApp")

// Core Microfeatures
include(":core:domain")
include(":core:database")
include(":core:network")
include(":core:presentation")
include(":core:designsystem")

// Feature Microfeatures
include(":feature:dashboard")
include(":feature:transaction")
include(":feature:budget")
include(":feature:savings")
include(":feature:onboarding")
include(":feature:quests")

