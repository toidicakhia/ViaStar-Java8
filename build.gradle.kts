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
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/toidicakhia/ViaStar-Java8")
            credentials {
                username = System.getenv("GITHUB_USERNAME") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
    }
}

tasks.named("build") {
    dependsOn("getViaDowngraded")
}