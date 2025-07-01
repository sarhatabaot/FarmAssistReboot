rootProject.name = "FarmAssistReboot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://repo.codemc.org/repository/maven-public")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://mvn-repo.arim.space/lesser-gpl3/")
        maven("https://nexuslite.gcnt.net/repos/other/")
    }

    versionCatalogs {
        create("libs") {
            library("spigot-api", "org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
            library("papi-api", "me.clip:placeholderapi:2.11.6")
            library("acf-paper", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("bstats", "org.bstats:bstats-bukkit:3.1.0")
            library("nbt-api", "de.tr7zw:item-nbt-api:2.14.1")
            library("annotations", "org.jetbrains:annotations:26.0.2")
            library("more-paper", "space.arim.morepaperlib:morepaperlib:0.4.4")
            library("xseries", "com.github.cryptomorin:XSeries:13.0.0")
            version("plugin-yml", "0.6.0")
            plugin("plugin-yml-bukkit", "net.minecrell.plugin-yml.bukkit").versionRef("plugin-yml")

            library("boosted-yml", "dev.dejvokep:boosted-yaml:1.3.7")

            plugin("shadow", "com.gradleup.shadow").version("8.3.8")

            //Add our in-house messages plugin or find an alternative compat with java8
        }
    }
}