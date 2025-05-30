import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

rootProject.name = "for-interviews"

dependencyResolutionManagement {
    repositoriesMode = FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}
