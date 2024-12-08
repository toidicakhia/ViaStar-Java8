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
                register("defaultJobsIfNotExist", DefaultJobsIfNotExists::class)

                register("validJobs", ValidJobs::class) {
                    dependsOn("defaultJobsIfNotExist")
                }

                register("updateViaMetadata", UpdateViaMetadata::class) {
                    dependsOn("validJobs")
                }

                register("getLatestSuccessfulBuild", GetLatestSuccessfulBuild::class) {
                    dependsOn("updateViaMetadata")
                }

                val checkUpdateTask = register("checkBuildHasUptoDate", CheckBuildHasUptoDate::class) {
                    dependsOn("getLatestSuccessfulBuild")
                }

                register("downloadVia", DownloadViaTask::class) {
                    dependsOn("checkBuildHasUptoDate")

                    onlyIf { !checkUpdateTask.get().hasUptoDate }
                }

                register("downgradeVia", DowngradeViaTask::class) {
                    dependsOn("downloadVia")

                    onlyIf { !checkUpdateTask.get().hasUptoDate }
                }

                register("getViaDowngraded") {
                    group = "ViaStar"
                    description = "Get Via* downgraded"

                    dependsOn("updateViaMetadata")
                    dependsOn("downgradeVia")
                }
            }
        }
    }
}