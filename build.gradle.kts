import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.20"
    `java-library`
    `maven-publish`
}

group = "com.github.marcoral.versatia"
version = "0.0.1"

// Only the Paper API (for Player and OfflinePlayer), JDK types and coroutines for await(); plugins have VersatiaAPI and Paper on their classpath already
dependencies {
    compileOnlyApi("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    api(kotlin("stdlib"))
    // Plugins get kotlinx-coroutines through VersatiaAPI and, on the server, from VersatiaCore
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
}

// Paper 1.21.4 requires Java 21
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "VersatiaModule-Database"
            from(components["java"])
        }
    }
}
