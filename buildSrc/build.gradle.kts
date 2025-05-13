repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://maven.architectury.dev/")
    maven("https://repo.essential.gg/public")
    maven("https://maven.minecraftforge.net")
}

dependencies {
    implementation("gg.essential:essential-gradle-toolkit:0.6.7")
    implementation("gg.essential:architectury-loom:1.7.30")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
}
