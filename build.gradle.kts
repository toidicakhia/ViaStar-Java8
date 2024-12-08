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
            group = "me.toidicakhia"
            version = "dev"

            file("build/libs").listFiles()?.filter { !it.name.contains("viastar", true) }?.forEach {
                artifact(it) {
                    val fileNameMetadata = it.nameWithoutExtension.split("-")

                    classifier = fileNameMetadata[0]
                    extension = it.extension
                    version = fileNameMetadata[1]
                }
            }
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.named("build") {
    dependsOn("getViaDowngraded")
}