buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.61"))
    }
}

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.61"
    application
}

application {
    mainClassName = "com.telemetry.rocks.c4.ArchitectureKt"
}
dependencies {
    implementation("cc.catalysts.structurizr:structurizr-kotlin:1.3.0.3")
    implementation("com.structurizr:structurizr-plantuml:1.3.5")
}
