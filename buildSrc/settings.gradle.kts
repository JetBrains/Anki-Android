// SPDX-License-Identifier: GPL-3.0-or-later

import java.util.Properties

val rootGradleProperties =
    Properties().apply {
        file("../gradle.properties").inputStream().use { load(it) }
    }

// buildSrc doesn't inherit the root gradle.properties, but does see -P / ORG_GRADLE_PROJECT_* overrides
fun rootProperty(name: String): String? = providers.gradleProperty(name).orNull ?: rootGradleProperties.getProperty(name)

val kotlinVersion = rootProperty("kotlin_version") ?: error("kotlin_version is missing from the root gradle.properties")
val kotlinRepoUrl = rootProperty("kotlin_repo_url")?.takeIf { it.isNotBlank() }

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
