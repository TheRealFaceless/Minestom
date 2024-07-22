plugins {
    id ("java")
    id ("io.freefair.lombok") version "8.6"
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.faceless"
version = "1.0"


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:1f34e60ea6")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

tasks.shadowJar {
    archiveClassifier.set("shadow")
    manifest {
        attributes["Main-Class"] = "dev.faceless.Main"
    }
    destinationDirectory.set(File("C:\\Users\\Faceless\\Desktop\\Minestom"))
}

tasks.assemble{
    dependsOn("shadowJar")
}