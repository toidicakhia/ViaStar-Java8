package me.toidicakhia.viastar

import me.toidicakhia.viastar.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import java.io.File

class ViaStarPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            tasks {
                register("downloadVia", DownloadViaTask::class)
                register("downgradeVia", DowngradeViaTask::class) {
                    dependsOn("downloadVia")
                }
            }
        }
    }
}