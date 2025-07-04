plugins {
    id("gg.essential.loom")
    id("gg.essential.defaults")
    // Load the shadow plugin.
    // We don't need to apply it since we don't want the default shadowJar task.
    id("com.gradleup.shadow") version "8.3.5" apply false
}

version = "1.0.0"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

val essentialPartnerModPackage = "com.example.mod.essentialpartnermod"

tasks.jar {
    manifest.attributes(
        // The main entry point of the Essential Partner mod is its core mod:
        "FMLCorePlugin" to "$essentialPartnerModPackage.asm.EssentialPartnerCoreMod",
        // If your mod already has its own core mod, you can have the Essential Partner core mod chain-load it:
        "EssentialPartnerCoreModDelegate" to "com.example.mod.asm.ExampleModCoreMod",
        // In any case, you'll likely also want to instruct Forge to load your regular mod, otherwise it'll only
        // load the core mod:
        "FMLCorePluginContainsFMLMod" to "Yes",
    )
}

val essentialPartnerModDep = "gg.essential:partner-mod-integration-1.12.2-forge:1.0.3"

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
