plugins {
    id("java")
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.faceless"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:7320437640")
}

tasks.shadowJar {
    archiveClassifier.set("shadow")
    manifest {
        attributes["Main-Class"] = "dev.faceless.Main"
        attributes["Description"] = "Test Minestom Server"
    }
}

tasks.assemble{
    dependsOn("shadowJar")
}