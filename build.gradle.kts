import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.lombok)
    alias(libs.plugins.plugin.yml.bukkit)
}

group = "com.github.sarhatabaot.farmassistreboot"
version = "1.4.4"

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(libs.papi.api)

    implementation(libs.nbt.api)
    implementation(libs.bstats)
    implementation(libs.acf.paper)
    implementation(libs.more.paper)
    implementation(libs.annotations)
    implementation(libs.xseries)
}

bukkit {
    main = "com.github.sarhatabaot.farmassistreboot.FarmAssistReboot"
    name = "FarmAssistReboot"
    authors = listOf("Friendly Baron","sarhatabaot")
    website = "https://github.com/sarhatabaot/FarmAssistReboot"
    description = "Allow players to auto-replant crops."
    apiVersion = "1.13"
    softDepend = listOf("PlaceholderAPI")

    permissions {
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
        register("farmassist.wheat") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.sugar_cane") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.nether_wart") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.cocoa") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.carrots") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.potatoes") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.beetroots") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("farmassist.cactus") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }

        register("farmassist.reload") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("farmassist.toggle") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("farmassist.global") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("farmassist.update") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("farmassist.notify.update") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("farmassist.lang") {
            default = BukkitPluginDescription.Permission.Default.OP
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
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}