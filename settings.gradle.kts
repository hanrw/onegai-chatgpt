import de.fayard.refreshVersions.core.StabilityLevel

rootProject.name = "onegai-chatgpt"

pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }

    plugins {
        // See https://jmfayard.github.io/refreshVersions
        id("de.fayard.refreshVersions").version("0.51.0")
    }
}

plugins {
    id("de.fayard.refreshVersions")
}

refreshVersions {
    // ignore all non-stable releases
    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }

    // Or maybe you want to see alpha versions if you are already using an alpha version, otherwise you want to see only stable versions
    rejectVersionIf {
        candidate.stabilityLevel.isLessStableThan(current.stabilityLevel)
    }
}

// gradle version >= 7.2
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")
