plugins {
    id("gg.essential.loom")
    id("gg.essential.defaults")
    // Load the shadow plugin.
    // We don't need to apply it since we don't want the default shadowJar task.
    id("com.gradleup.shadow") version "8.3.5" apply false
}

version = "1.0.0"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

val essentialPartnerModPackage = "com.example.mod.essentialpartnermod"

tasks.jar {
    manifest.attributes(
        // The main entry point of the Essential Partner mod are its mixins.
        // Note that you may have to re-declare your own mixin configs here too depending on your build system.
        "MixinConfigs" to "${essentialPartnerModPackage.replace('.', '/')}/mixins.json,mixins.examplemod.json",
    )
}

// Replace "forge" with "neoforge" on NeoForge
val essentialPartnerModDep = "gg.essential:partner-mod-integration-1.20.4-forge:1.0.3"

val relocatedEssentialPartnerModJar by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    destinationDirectory.set(layout.buildDirectory.dir("devlibs"))
    archiveFileName.set("essentialpartner.jar")
    inputs.property("essentialPartnerModPackage", essentialPartnerModPackage)
    val configuration = project.configurations.detachedConfiguration(project.dependencies.create(essentialPartnerModDep))
    dependsOn(configuration)
    from({ configuration.map { zipTree(it) } })
    exclude("mcmod.info", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "pack.mcmeta", "gg/essential/partnermod/EssentialPartnerMod.class")
    relocate("gg.essential.partnermod", essentialPartnerModPackage)
    filesMatching("gg/essential/partnermod/mixins.json") {
        filter { it.replace("gg.essential.partnermod", essentialPartnerModPackage) }
    }
}

tasks.jar {
    from(relocatedEssentialPartnerModJar.map { it.archiveFile }.map { zipTree(it) })
}
