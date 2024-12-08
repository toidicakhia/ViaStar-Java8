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

                register("getLatestSuccessfulBuild", GetLatestSuccessfulBuild::class) {
                    dependsOn("validJobs")
                }

                val checkUpdateTask = register("checkBuildHasUptoDate", CheckBuildHasUptoDate::class) {
                    dependsOn("getLatestSuccessfulBuild")
                }

                register("downloadVia", DownloadViaTask::class) {
                    dependsOn("checkBuildHasUptoDate")

                    onlyIf { !checkUpdateTask.get().hasUptoDate }
                }

                register("updateViaMetadata", UpdateViaMetadata::class)

                register("downgradeVia", DowngradeViaTask::class) {
                    dependsOn("downloadVia")
                    dependsOn("updateViaMetadata")

                    onlyIf { !checkUpdateTask.get().hasUptoDate }
                }
            }
        }
    }
}