plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
}

gradlePlugin {
    // Define the plugin
    val plugin by plugins.creating {
        id = "me.toidicakhia.viastar"
        implementationClass = "me.toidicakhia.viastar.ViaStarPlugin"
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}