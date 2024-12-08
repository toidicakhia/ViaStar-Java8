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

            file("build/libs").listFiles()?.forEach { file ->
                artifact(file) {
                    classifier = file.nameWithoutExtension.split("-").first()
                    extension = file.extension
                }
            }
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.named("build") {
    dependsOn("downgradeVia")
}

tasks.named("publishToMavenLocal") {
    dependsOn("build")
}