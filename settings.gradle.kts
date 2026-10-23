plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
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
        exclusiveContent {
            forRepository {
                ivy {
                    url = uri("https://github.com/sarhatabaot/WatchWolf-Tester/releases/download/")
                    patternLayout {
                        artifact("v[revision]/[artifact]-[revision].[ext]")
                    }
                    metadataSources {
                        artifact()
                    }
                }
            }
            filter {
                includeModule("dev.watchwolf", "watchwolf-tester")
            }
        }
    }

    versionCatalogs {
        create("libs") {
            library("spigot-api", "org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
            library("papi-api", "me.clip:placeholderapi:2.12.3")
            library("acf-paper", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("bstats", "org.bstats:bstats-bukkit:3.2.1")
            library("nbt-api", "de.tr7zw:item-nbt-api:2.16.0")
            library("annotations", "org.jetbrains:annotations:26.1.0")
            library("more-paper", "space.arim.morepaperlib:morepaperlib:0.5.3")
            library("xseries", "com.github.cryptomorin:XSeries:13.7.1")
            version("plugin-yml", "0.6.0")
            plugin("plugin-yml-bukkit", "net.minecrell.plugin-yml.bukkit").versionRef("plugin-yml")

            library("boosted-yml", "dev.dejvokep:boosted-yaml:1.3.7")

            plugin("shadow", "com.gradleup.shadow").version("8.3.0")

            version("junit", "5.8.1")
            library("junit-api", "org.junit.jupiter","junit-jupiter-api").versionRef("junit")
            library("junit-engine", "org.junit.jupiter","junit-jupiter-engine").versionRef("junit")
            library("junit-params", "org.junit.jupiter","junit-jupiter-params").versionRef("junit")
            library("junit-launcher", "org.junit.platform:junit-platform-launcher:1.8.1")
            library("watchwolf-tester", "dev.watchwolf:watchwolf-tester:0.4.1")
            library("snakeyaml", "org.yaml:snakeyaml:1.21")

            // Testing: Mockito for unit tests (Bukkit API mocking)
            version("mockito", "4.11.0")
            library("mockito-core", "org.mockito","mockito-core").versionRef("mockito")
            library("mockito-inline", "org.mockito","mockito-inline").versionRef("mockito")
            library("mockito-junit-jupiter", "org.mockito","mockito-junit-jupiter").versionRef("mockito")

            //Add our in-house messages plugin or find an alternative compat with java8
        }
    }
}
