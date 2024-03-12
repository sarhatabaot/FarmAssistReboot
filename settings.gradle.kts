rootProject.name = "FarmAssistReboot"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("spigot-api", "org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
            library("papi-api", "me.clip:placeholderapi:2.11.5")
            library("acf-paper", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("bstats", "org.bstats:bstats-bukkit:3.0.2")
            library("nbt-api", "de.tr7zw:item-nbt-api-plugin:2.12.2")
            library("annotations", "org.jetbrains:annotations:24.1.0")
            library("folia-lib", "com.tcoded:FoliaLib:0.2.5") //move to A248

            version("plugin-yml", "0.6.0")
            plugin("plugin-yml-bukkit", "net.minecrell.plugin-yml.bukkit").versionRef("plugin-yml")

            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
            plugin("lombok", "io.freefair.lombok").version("8.6")

            //Add our in-house messages plugin or find an alternative compat with java8
        }
    }
}