plugins {
    id("gg.essential.loom")
    id("gg.essential.defaults")
    id("com.gradleup.shadow") version "8.3.5"
}

version = "1.0.0"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    maven(url = "https://repo.essential.gg/public")
}

dependencies {
    include("gg.essential:ad-1.21-fabric:1.0.0")
}
