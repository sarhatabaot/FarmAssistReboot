plugins {
    java
    alias(libs.plugins.shadow)
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
        minimize()

        exclude("META-INF/**")

        archiveClassifier.set("")
    }

    test {
        useJUnitPlatform()
    }
}


//tasks.withType(JavaCompile).configureEach {
//    options.encoding = 'UTF-8'
//
//    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
//        options.release.set(targetJavaVersion)
//    }
//}
