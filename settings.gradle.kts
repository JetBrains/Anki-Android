pluginManagement {
    val kotlin_repo_url: String? by settings
    val kotlin_version: String? by settings
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        if (!kotlin_repo_url.isNullOrEmpty() && !kotlin_version.isNullOrEmpty()) {
            exclusiveContent {
                forRepository {
                    maven(kotlin_repo_url!!)
                }
                filter {
                    includeVersionByRegex("org.jetbrains.kotlin.*", ".*", kotlin_version!!)
                }
            }
        }
        maven(url = "https://jitpack.io") // only needed for the "amazonappstorepublisher" plugin
    }

}
plugins {
    // Added automatically by Android Studio for Gradle Daemon JVM Criteria
    // https://developer.android.com/studio/releases/past-releases/as-panda-1-release-notes#daemon-jvm-criteria
    // Version catalog references are (mostly) unsupported in settings.gradle.kts
    // https://discuss.gradle.org/t/how-to-use-version-catalog-in-the-root-settings-gradle-kts-file/44603

    // https://github.com/gradle/foojay-toolchains
    // Changelog: https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("com.gradle.develocity") version ("4.3.3")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    val kotlin_version: String by settings
    val kotlin_repo_url: String by settings
    versionCatalogs {
        create("libs") {
            version("kotlin", kotlin_version)
        }
    }
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        exclusiveContent {
            forRepository { maven(kotlin_repo_url) }
            filter {
                includeVersionByRegex("org.jetbrains.kotlin", ".*", kotlin_version)
            }
        }
        maven(url = "https://jitpack.io")
    }
}

// alphabetical ordering rather than dependency-tree ordering to avoid bikeshedding
include(
    ":anki-common",
    ":api",
    ":AnkiDroid",
    ":baselineprofile",
    ":common",
    ":common:android",
    ":compat",
    ":libanki",
    ":lint-rules",
    ":vbpd",
    ":widgets",
)