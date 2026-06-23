pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.1.18"
}

gitHooks {
    preCommit {
        tasks("ktlintCheck", "detekt")
    }

    hook("pre-push") {
        tasks("clean", "assembleDebug", "koverVerifyDebug")
    }

    createHooks(true)
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RewardStamp"
include(":app")
 