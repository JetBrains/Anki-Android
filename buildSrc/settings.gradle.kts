// SPDX-License-Identifier: GPL-3.0-or-later

import java.util.Properties

val rootGradleProperties =
    Properties().apply {
        file("../gradle.properties").inputStream().use { load(it) }
    }
val kotlinVersion =
    rootGradleProperties.getProperty("kotlin_version")
        ?: error("kotlin_version is missing from the root gradle.properties")
val kotlinRepoUrl = rootGradleProperties.getProperty("kotlin_repo_url")?.takeIf { it.isNotBlank() }

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
            version("kotlin", kotlinVersion)
        }
    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        if (kotlinRepoUrl != null) {
            exclusiveContent {
                forRepository {
                    maven(kotlinRepoUrl)
                }
                filter {
                    includeVersionByRegex("org.jetbrains.kotlin.*", ".*", kotlinVersion)
                }
            }
        }
    }
}
