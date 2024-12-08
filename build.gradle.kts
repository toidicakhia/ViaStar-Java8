plugins {
    `maven-publish`
    id("me.toidicakhia.viastar")
    java
}

publishing {
    publications {
        create<MavenPublication>("mavenLocal") {
            from(components["java"])
            artifactId = "viastar-java8"
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.named("build") {
    dependsOn("getViaDowngraded")
}

tasks.named("publishToMavenLocal") {
    dependsOn("getViaDowngraded")
}