rootProject.name = "FarmAssistReboot"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://repo.aikar.co/content/groups/aikar/")
    }

    versionCatalogs {
        create("libs") {
            library("spigot-api", "org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
            library("xseries", "com.github.cryptomorin:XSeries:11.2.1")
            library("gson", "com.google.code.gson:gson:2.11.0")

            version("junit", "5.11.0")
            library("junit-api", "org.junit.jupiter","junit-jupiter-api").versionRef("junit")
            library("junit-engine", "org.junit.jupiter","junit-jupiter-engine").versionRef("junit")
            library("junit-params", "org.junit.jupiter","junit-jupiter-params").versionRef("junit")

            library("nbt-api", "de.tr7zw:item-nbt-api:2.13.2")
            library("boosted-yaml", "dev.dejvokep:boosted-yaml:1.3.5")

            library("jetbrains-annotations", "org.jetbrains:annotations:24.0.0")

            library("commands", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("caffeine", "com.github.ben-manes.caffeine:caffeine:2.9.2") // Stuck on 2.9.2 for java 8

            library("bstats", "org.bstats:bstats-bukkit:3.0.2")

            plugin("shadow", "com.gradleup.shadow").version("8.3.0")
            plugin("plugin-yml-bukkit", "net.minecrell.plugin-yml.bukkit").version("0.6.0")
        }
    }
}

