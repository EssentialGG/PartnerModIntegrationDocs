# Essential Mod Partner Program Documentation

Welcome to the Essential Mod Partner Program documentation.

(TODO introduction about the Partner Mod/links to wiki if appropriate)

Latest Partner Integration Mod Version:
<img alt="version badge" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.essential.gg%2Fpublic%2Fgg%2Fessential%2Fad-1.12.2-forge%2Fmaven-metadata.xml">

Refer to the [changelog](#ad-mod-changelog) for changes.

## Supported Minecraft Versions

There are certain Minecraft versions where the changes do not affect the Partner Mod, so for these versions
you are required to bundle the Partner Mod for a different Minecraft versions. These version mismatches
are bolded in the fourth column of this table.

| Minecraft Version | Fabric | Forge | Partner Mod Minecraft Version |
|-------------------|:------:|:-----:|-------------------------------|
| 1.8.9             |   ❌    |   ✅   | 1.8.9                         |
| 1.12.2            |   ❌    |   ✅   | 1.12.2                        |
| 1.16.5            |   ✅    |   ✅   | 1.16.5                        |
| 1.17.1            |   ✅    |   ✅   | 1.17.1                        |
| 1.18              |   ✅    |   ❌   | **1.18.1**                    |
| 1.18.1            |   ✅    |   ❌   | 1.18.1                        |
| 1.18.2            |   ✅    |   ✅   | 1.18.2                        |
| 1.19              |   ✅    |   ❌   | 1.19                          |
| 1.19.1            |   ✅    |   ❌   | **1.19.2**                    |
| 1.19.2            |   ✅    |   ✅   | 1.19.2                        |
| 1.19.3            |   ✅    |   ✅   | 1.19.3                        |
| 1.19.4            |   ✅    |   ✅   | 1.19.4                        |
| 1.20              |   ✅    |   ❌   | 1.20                          |
| 1.20.1            |   ✅    |   ✅   | 1.20.1                        |
| 1.20.2            |   ✅    |   ✅   | 1.20.2                        |
| 1.20.4            |   ✅    |   ✅   | 1.20.4                        |
| 1.20.6            |   ✅    |   ❌   | 1.20.6                        |
| 1.21              |   ✅    |   ❌   | 1.21                          |
| 1.21.1            |   ✅    |   ❌   | **1.21**                      |
| 1.21.2            |   ✅    |   ❌   | **1.21.3**                    |
| 1.21.3            |   ✅    |   ❌   | 1.21.3                        |
| 1.21.4            |   ✅    |   ❌   | 1.21.4                        |

## Bundling the Partner Mod

Bundling the Partner Mod is different on Fabric and Forge. See the sections below
for the details of how to bundle the Partner Mod on each modloader.

Examples are provided in the [examples](examples) directory. There is also a "multiversion" example
in [examples/multiversion](examples/multiversion) that supports all of the above versions
(using the Essential Gradle Toolkit/ReplayMod preprocessor multiversion approach).

### Bundling the Partner Mod on Fabric

On Fabric, the Partner Mod is bundled using Fabric's jar-in-jar mechanism. Fabric Loader will automatically select
the latest version available out of all the versions bundled by mods. This can be done as follows.

```kotlin
repositories {
    // If you use a groovy buildscript instead of a Kotlin one, use {} instead of ().
    // If you use the defaults plugin from Essential Gradle Toolkit, the repository is likely already added.
    maven(url = "https://repo.essential.gg/public")
}

dependencies {
    // Replace ${mcVersion} with the Minecraft version
    // Replace ${partnerModVersion} with the Partner Mod version
    // Refer to the Supported Versions table for both of these
    include("gg.essential:partner-mod-integration-${mcVersion}-fabric:${partnerModVersion}")
}
```

See [examples/1.21.1-fabric](examples/1.21-fabric) for a full example.

### Bundling the Partner Mod on Forge

On Forge, you must include a relocated version of the Partner Mod within your mod. At startup, these relocated versions
will negotiate to ensure only the latest version of the Partner Mod is used.

This process is slightly different for legacy forge (1.8.9 & 1.12.2) and modern forge (1.16+).

<details>
<summary>1.8.9 and 1.12.2 Forge</summary>

An example using the Kotlin buildscript can be found in [examples/1.12.2-forge](examples/1.12.2-forge)
and an example using the Groovy buildscript can be found in [examples/1.8.9-forge](examples/1.8.9-forge).

The following highlights the important sections (using the Kotlin buildscript, if using the Groovy buildscript
refer to the respective example).

```kotlin
plugins {
    // Load the shadow plugin.
    // We don't need to apply it since we don't want the default shadowJar task.
    id("com.gradleup.shadow") version "8.3.5" apply false
}

repositories {
    // If you use a groovy buildscript instead of a Kotlin one, use {} instead of ().
    // If you use the defaults plugin from Essential Gradle Toolkit, the repository is likely already added.
    maven(url = "https://repo.essential.gg/public")
}

// Replace this with a package within your mod package
val essentialPartnerModPackage = "com.example.mod.essentialpartnermod"

tasks.jar {
    manifest.attributes(
        // The main entry point of the Essential Partner mod is its core mod:
        "FMLCorePlugin" to "$essentialPartnerModPackage.asm.EssentialPartnerCoreMod",
        // If your mod already has its own core mod, you can have the Essential Partner core mod chain-load it:
        "EssentialPartnerCoreModDelegate" to "com.example.mod.asm.ExampleModCoreMod",
        // In any case case, you'll likely also want to instruct Forge to load your regular mod, otherwise it'll only
        // load the core mod:
        "FMLCorePluginContainsFMLMod" to "Yes",
    )
}

// Replace ${mcVersion} with the Minecraft version
// Replace ${partnerModVersion} with the Partner Mod version
// Refer to the Supported Versions table for both of these
val essentialPartnerModDep = "gg.essential:partner-mod-integration-${mvVersion}-forge:${partnerModVersion}"

// Relocate Essential Ad into your mod's package
val relocatedEssentialPartnerModJar by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    destinationDirectory.set(layout.buildDirectory.dir("devlibs"))
    archiveFileName.set("essentialpartner.jar")
    inputs.property("essentialPartnerModPackage", essentialPartnerModPackage)
    val configuration = project.configurations.detachedConfiguration(project.dependencies.create(essentialPartnerModDep))
    dependsOn(configuration)
    from({ configuration.map { zipTree(it) } })
    exclude("mcmod.info", "META-INF/mods.toml", "gg/essential/partnermod/EssentialPartnerMod.class")
    relocate("gg.essential.partnermod", essentialPartnerModPackage)
    filesMatching("gg/essential/partnermod/mixins.json") {
        filter { it.replace("gg.essential.partnermod", essentialPartnerModPackage) }
    }
}

// Include the relocated classes into your jar
tasks.jar {
    from(relocatedEssentialPartnerModJar.map { it.archiveFile }.map { zipTree(it) })
}
```

</details>

<details>
<summary>1.16.5+ Forge</summary>

An example using the Kotlin buildscript can be found in [examples/1.20.4-forge](examples/1.20.4-forge)
and an example using the Groovy buildscript can be found in [examples/1.16.5-forge](examples/1.16.5-forge).

The following highlights the important sections (using the Kotlin buildscript, if using the Groovy buildscript
refer to the respective example).

```kotlin
// Apply the shadow plugin
plugins {
    // Load the shadow plugin.
    // We don't need to apply it since we don't want the default shadowJar task.
    id("com.gradleup.shadow") version "8.3.5" apply false
}

repositories {
    // If you use a groovy buildscript instead of a Kotlin one, use {} instead of ().
    // If you use the defaults plugin from Essential Gradle Toolkit, the repository is likely already added.
    maven(url = "https://repo.essential.gg/public")
}

// Replace this with a package within your mod package
val essentialPartnerModPackage = "com.example.mod.essentialpartnermod"

tasks.jar {
    manifest.attributes(
        // The main entry point of the Essential Partner mod are its mixins.
        // Note that you may have to re-declare your own mixin configs here too depending on your build system.
        "MixinConfigs" to "${essentialPartnerModPackage.replace(".", "/")}/mixins.json,mixins.examplemod.json",
    )
}

// Replace ${mcVersion} with the Minecraft version
// Replace ${partnerModVersion} with the Partner Mod version
// Refer to the Supported Versions table for both of these
val essentialPartnerModDep = "gg.essential:partner-mod-integration-${mcVersion}-forge:${partnerModVersion}"

// Relocate Essential Ad into your mod's package
val relocatedEssentialPartnerModJar by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    destinationDirectory.set(layout.buildDirectory.dir("devlibs"))
    archiveFileName.set("essentialpartner.jar")
    inputs.property("essentialPartnerModPackage", essentialPartnerModPackage)
    val configuration = project.configurations.detachedConfiguration(project.dependencies.create(essentialPartnerModDep))
    dependsOn(configuration)
    from({ configuration.map { zipTree(it) } })
    exclude("mcmod.info", "META-INF/mods.toml", "gg/essential/partnermod/EssentialPartnerMod.class")
    relocate("gg.essential.partnermod", essentialPartnerModPackage)
    filesMatching("gg/essential/partnermod/mixins.json") {
        filter { it.replace("gg.essential.partnermod", essentialPartnerModPackage) }
    }
}

// Include the relocated classes into your jar
tasks.jar {
    from(relocatedEssentialPartnerModJar.map { it.archiveFile }.map { zipTree(it) })
}
```

</details>

## Support

Open a ticket in the Essential Partner discord to get support on bundling the Essential Mod Partner Integration.

## Partner Mod Changelog

### v1.0.0 - 2025-XX-XX
- Initial Release
