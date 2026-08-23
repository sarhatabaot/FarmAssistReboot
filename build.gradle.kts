import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml.bukkit)
}

group = "com.github.sarhatabaot.farmassistreboot"
version = "1.5.1"

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

    testImplementation(libs.spigot.api)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)

    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.mockito.core)
    testRuntimeOnly(libs.mockito.junit.jupiter)
    testRuntimeOnly(libs.mockito.inline)
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

        // H12: per-crop permissions. Crop names must match the keys used
        // by BlockBreakListener (`Permissions.BASE_PERMISSION + material.name()`).
        // Note: PITCHER_PLANT and TORCHFLOWER were missing from the original
        // registration; registered now so the new crops work the same way.
        listOf(
            "wheat", "sugar_cane", "nether_wart", "cocoa", "carrots",
            "potatoes", "beetroots", "cactus", "torch_flower", "pitcher_plant"
        ).forEach { crop ->
            register("farmassist.$crop") {
                default = BukkitPluginDescription.Permission.Default.FALSE
            }
        }

        // Player-facing command permissions (default TRUE so anyone can toggle).
        listOf("toggle", "till")
            .forEach { perm ->
                register("farmassist.$perm") {
                    default = BukkitPluginDescription.Permission.Default.TRUE
                }
            }

        // Admin-only command permissions.
        listOf("reload", "update", "notify.update", "lang", "info")
            .forEach { perm ->
                register("farmassist.$perm") {
                    default = BukkitPluginDescription.Permission.Default.OP
                }
            }

        // H12: was registered as "global" but Commands.ToggleGlobal.PERMISSION
        // is "farmassist.toggle.global". Correct the key.
        register("farmassist.toggle.global") {
            default = BukkitPluginDescription.Permission.Default.OP
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
                "farmassist.torch_flower",
                "farmassist.pitcher_plant",
            )
        }

        // Bypass flags — opt-in, default FALSE.
        listOf("noseeds", "nodrops")
            .forEach { perm ->
                register("farmassist.$perm") {
                    default = BukkitPluginDescription.Permission.Default.FALSE
                }
            }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

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