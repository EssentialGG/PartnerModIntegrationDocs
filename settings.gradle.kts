pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://repo.essential.gg/public")
        maven("https://maven.minecraftforge.net")
    }
}


rootProject.name = "PartnerModIntegrationDocs"

include(":examples:1.8.9-forge")
include(":examples:1.12.2-forge")
include(":examples:1.16.5-forge")
include(":examples:1.20.4-forge")
include(":examples:1.21-fabric")

include(":examples:multiversion")
project(":examples:multiversion").buildFileName = "root.gradle.kts"

listOf(
    "1.8.9-forge",
    "1.12.2-forge",
    "1.16.5-forge",
    "1.16.5-fabric",
    "1.17.1-fabric",
    "1.17.1-forge",
    "1.18.1-fabric",
    "1.18.2-fabric",
    "1.18.2-forge",
    "1.19-fabric",
    "1.19.2-fabric",
    "1.19.2-forge",
    "1.19.3-fabric",
    "1.19.3-forge",
    "1.19.4-fabric",
    "1.19.4-forge",
    "1.20-fabric",
    "1.20.1-fabric",
    "1.20.1-forge",
    "1.20.2-fabric",
    "1.20.2-forge",
    "1.20.4-fabric",
    "1.20.4-forge",
    "1.20.4-neoforge",
    "1.20.6-fabric",
    "1.20.6-forge",
    "1.20.6-neoforge",
    "1.21.1-fabric",
    "1.21.1-forge",
    "1.21.1-neoforge",
    "1.21.3-fabric",
    "1.21.3-forge",
    "1.21.3-neoforge",
    "1.21.4-fabric",
    "1.21.4-forge",
    "1.21.4-neoforge",
    "1.21.5-fabric",
    "1.21.5-forge",
    "1.21.5-neoforge",
).forEach { version ->
    include(":examples:multiversion:$version")
    project(":examples:multiversion:$version").apply {
        projectDir = file("examples/multiversion/versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
