repositories {
    mavenCentral()
}

plugins {
    id("java")
    kotlin("jvm") version "1.6.10"
    id("me.toidicakhia.viastar")
}

tasks.named("build") {
    dependsOn("downgradeVia")
}