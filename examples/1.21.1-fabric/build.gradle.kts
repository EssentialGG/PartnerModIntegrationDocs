plugins {
    id("gg.essential.loom")
    id("gg.essential.defaults")
    // Load the shadow plugin.
    // We don't need to apply it since we don't want the default shadowJar task.
    id("com.gradleup.shadow") version "8.3.5" apply false
}

version = "1.0.0"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    maven(url = "https://repo.essential.gg/public")
}

dependencies {
    include("gg.essential:partner-mod-integration-1.21.1-fabric:1.0.1")
}
