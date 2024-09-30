plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml.bukkit)
}

group = "com.github.sarhatabaot"
version = "2.0.0-SNAPSHOT"


dependencies {
    compileOnly(libs.spigot.api) {
        // Old, unsecure dependencies
        exclude("com.googlecode.json-simple")
        exclude("com.google.code.gson", "gson")
        exclude("com.google.guava", "guava")
        exclude("commons-lang", "commons-lang")
        exclude("org.yaml", "snakeyaml")
        exclude("org.avaje", "ebean")
        exclude("net.md-5", "bungeecord-chat")
    }

    implementation(libs.gson)
    implementation(libs.xseries)
    implementation(libs.jetbrains.annotations)
    implementation(libs.caffeine)
    implementation(libs.nbt.api)
    implementation(libs.boosted.yaml)
    implementation(libs.commands)

    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testCompileOnly(libs.spigot.api)
    testRuntimeOnly(libs.junit.engine)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }

}

tasks {
    build {
        dependsOn(shadowJar)

    }

    compileJava {
        options.compilerArgs.add("-parameters")
        options.isFork = true
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("co.aikar.commands", "com.github.sarhatabaot.farmassistreboot.libs.commands")
        relocate("co.aikar.locales", "com.github.sarhatabaot.farmassistreboot.libs.locales")
        relocate("com.cryptomorin.xseries", "com.github.sarhatabaot.farmassistreboot.libs.xseries")
        relocate("com.github.benmanes", "com.github.sarhatabaot.farmassistreboot.libs")
        relocate("de.tr7zw.changeme.nbtapi", "com.github.sarhatabaot.farmassistreboot.libs.nbt")
        relocate("dev.dejvokep.boostedyaml", "com.github.sarhatabaot.farmassistreboot.libs.boostedyaml")

        minimize(){
            exclude(dependency(libs.caffeine.get()))
        }

        exclude("META-INF/**")

        archiveClassifier.set("")
    }

    test {
        useJUnitPlatform()
    }
}

bukkit {
    main = "com.github.sarhatabaot.farmassistreboot.FarmAssistReboot"
    name = "FarmAssistReboot"
    authors = listOf("FriendlyBaron", "sarhatabaot")
    website = "https://github.com/sarhatabaot/FarmAssistReboot"
    description = "Auto replant crops. Customizable."
    softDepend = listOf("PlaceholderAPI")
//    foliaSupported = true todo

}