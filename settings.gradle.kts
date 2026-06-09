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
    resolutionStrategy {
        // TODO try to find another plugin for this functionality?
        eachPlugin {
            if (requested.id.id == "app.brant.amazonappstorepublisher") {
                useModule("com.github.BrantApps.gradle-amazon-app-store-publisher:amazonappstorepublisher:master-SNAPSHOT")
            }
        }
    }
}

dependencyResolutionManagement {
    val kotlin_version: String by settings
    versionCatalogs {
        create("libs") {
            version("kotlin", kotlin_version)
        }
    }
    // TODO enforce repositories declared here, currently it clashes with robolectricDownloader.gradle
    //  which uses a local maven repository
//     repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//        kotlin_repo_url?.also { maven(it) }
//        maven(url = "https://jitpack.io")
//    }
}

include(":lint-rules", ":api", ":AnkiDroid", ":testlib", ":common", ":libanki", ":libanki:testutils", ":vbpd")