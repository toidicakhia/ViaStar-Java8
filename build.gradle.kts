plugins {
    `maven-publish`
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
