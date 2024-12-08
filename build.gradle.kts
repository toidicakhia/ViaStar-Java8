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

            file("build/libs").listFiles().filter { !it.name.contains("viastar", true) }.forEach { file ->
                    artifact(file) {
                        val fileNameMetadata = file.nameWithoutExtension.split("-")

                        classifier = fileNameMetadata[0]
                        extension = file.extension
                        version = fileNameMetadata[1]
                    }
                }

            println()
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
    dependsOn("downgradeVia")
}