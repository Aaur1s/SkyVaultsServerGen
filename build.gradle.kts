val minecraft_version: String by ext
val forge_version: String by ext
val yarn_mappings: String by ext
val mod_version: String by ext
val mod_group: String by ext
val mod_name: String by ext
val mod_id: String by ext
val mod_author: String by ext

plugins {
    java
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.4.3" apply (System.getenv("MODRINTH_TOKEN") != null)
    id("com.matthewprenger.cursegradle") version "1.4.0" apply (System.getenv("CURSEFORGE_TOKEN") != null)
}

base.archivesName = mod_name

version = "$mod_version+$minecraft_version"
group = mod_group

architectury {
    minecraft = minecraft_version
    platformSetupLoomIde()
    forge()
}

loom {
    silentMojangMappingsLicense()
    forge { }
}

repositories {
    mavenCentral()
    maven("https://maven.blamejared.com/")
    maven("https://cursemaven.com")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$minecraft_version-$forge_version")
    modImplementation("curse.maven:vault-hunters-official-mod-458203:5925633")
    modRuntimeOnly("curse.maven:modular-routers-250294:3776175")
    modRuntimeOnly("curse.maven:auxblocks-711533:4358959")
    modRuntimeOnly("curse.maven:ferritecore-429235:4074294")
    modRuntimeOnly("curse.maven:jei-238222:4351311")
    modRuntimeOnly("curse.maven:geckolib-388172:4181370")
    modRuntimeOnly("curse.maven:curios-309927:4418032")
    modRuntimeOnly("curse.maven:quark-243121:3840125")
    modRuntimeOnly("curse.maven:autoreglib-250363:3642382")
    modRuntimeOnly("curse.maven:architects-palette-433862:4498424")
    modRuntimeOnly("curse.maven:architectury-api-419699:4147231")
    modRuntimeOnly("curse.maven:rotten-creatures-371033:4060906")
    modRuntimeOnly("curse.maven:ispawner-533792:5925639")
    modRuntimeOnly("curse.maven:kotlin-for-forge-351264:4513187")
    modRuntimeOnly("curse.maven:sophisticated-backpacks-422301:4515363")
    modRuntimeOnly("curse.maven:sophisticated-core-618298:4489807")
    modRuntimeOnly("curse.maven:alexs-mobs-426558:3853078")
    modRuntimeOnly("curse.maven:citadel-331936:3783096")
    modRuntimeOnly("curse.maven:spark-361579:3824951")
    modRuntimeOnly("curse.maven:embeddium-908741:5322305")
}

tasks.withType<ProcessResources> {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand(mutableMapOf(
            "mod_id" to mod_id,
            "mod_name" to mod_name,
            "mod_author" to mod_author,
            "version" to project.version,
        ))
    }
}

java {
    withSourcesJar()
}