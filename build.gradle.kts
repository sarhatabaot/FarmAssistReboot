import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml.bukkit)
}

group = "com.github.sarhatabaot.farmassistreboot"
version = "1.4.9.0"

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(libs.papi.api)

    implementation(libs.nbt.api)
    implementation(libs.bstats)
    implementation(libs.acf.paper)
    implementation(libs.more.paper)
    implementation(libs.annotations)
    implementation(libs.xseries)
    implementation(libs.boosted.yml)
}

bukkit {
    main = "com.github.sarhatabaot.farmassistreboot.FarmAssistReboot"
    name = "FarmAssistReboot"
    authors = listOf("Friendly Baron", "sarhatabaot")
    website = "https://github.com/sarhatabaot/FarmAssistReboot"
    description = "Allow players to auto-replant crops."
    apiVersion = "1.13"
    softDepend = listOf("PlaceholderAPI")
    foliaSupported = true

    permissions {

        listOf("wheat", "sugar_cane", "nether_wart", "cocoa", "carrots", "potatoes", "beetroots", "cactus")
            .forEach { crop ->
                register("farmassist.$crop") {
                    default = BukkitPluginDescription.Permission.Default.FALSE
                }
            }

        // Admin permissions
        listOf("reload", "toggle", "global", "update", "notify.update", "lang")
            .forEach { perm ->
                register("farmassist.$perm") {
                    default = BukkitPluginDescription.Permission.Default.OP
                }
            }

        register("farmassist.crops") {
            default = BukkitPluginDescription.Permission.Default.TRUE
            children = listOf(
                "farmassist.wheat",
                "farmassist.sugar_cane",
                "farmassist.nether_wart",
                "farmassist.cocoa",
                "farmassist.carrots",
                "farmassist.potatoes",
                "farmassist.beetroots",
                "farmassist.cactus",
            )
        }

        register("farmassist.noseeds") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        minimize()
        archiveClassifier.set("")

        relocate("org.bstats", "com.github.sarhatabaot.farmassistreboot.metrics")
        relocate("co.aikar.commands", "com.github.sarhatabaot.farmassistreboot.acf")
        relocate("co.aikar.locales", "com.github.sarhatabaot.farmassistreboot.locales")
        relocate("de.tr7zw.changeme.nbtapi", "com.github.sarhatabaot.farmassistreboot.nbt")
        relocate("com.cryptomorin.xseries", "com.github.sarhatabaot.farmassistreboot.xseries")
        relocate("dev.dejvokep.boostedyaml", "com.github.sarhatabaot.farmassistreboot.boostedyaml")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}