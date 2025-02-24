plugins {
    id("gg.essential.defaults")
    id("gg.essential.multi-version")
    id("com.gradleup.shadow") version "8.3.5"
}

val essentialAdDep = "gg.essential:ad-$platform:1.0.0"

if (platform.isFabric) {
    dependencies {
        include(essentialAdDep)
    }
} else {
    val essentialAdPackage = "com.example.mod.essentialad"
    val essentialAdPath = essentialAdPackage.replace(".", "/")

    if (platform.isModLauncher) {
        tasks.jar {
            manifest.attributes(
                // The main entry point of the Essential Ad mod are its mixins.
                // Note that you may have to re-declare your own mixin configs here too depending on your build system.
                "MixinConfigs" to "$essentialAdPath/mixins.json,mixins.examplemod.json",
            )
        }
    } else {
        tasks.jar {
            manifest.attributes(
                // The main entry point of the Essential Ad mod is its core mod:
                "FMLCorePlugin" to "$essentialAdPackage.asm.EssentialAdCoreMod",
                // If your mod already has its own core mod, you can have the Essential Ad core mod chain-load it:
                "EssentialAdCoreModDelegate" to "com.example.mod.asm.ExampleModCoreMod",
                // In any case case, you'll likely also want to instruct Forge to load your regular mod, otherwise it'll only
                // load the core mod:
                "FMLCorePluginContainsFMLMod" to "Yes",
            )
        }
    }

    val relocatedEssentialAdJar by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        destinationDirectory.set(layout.buildDirectory.dir("devlibs"))
        archiveFileName.set("essentialad.jar")
        inputs.property("essentialAdPackage", essentialAdPackage)
        val configuration = project.configurations.detachedConfiguration(project.dependencies.create(essentialAdDep))
        dependsOn(configuration)
        from({ configuration.map { zipTree(it) } })
        exclude("mcmod.info", "META-INF/mods.toml", "gg/essential/ad/EssentialAdMod.class")
        relocate("gg.essential.ad", essentialAdPackage)
        filesMatching("gg/essential/ad/mixins.json") {
            filter { it.replace("gg.essential.ad", essentialAdPackage) }
        }
    }

    tasks.jar {
        from(relocatedEssentialAdJar.map { it.archiveFile }.map { zipTree(it) })
    }
}
