package me.toidicakhia.viastar.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class UpdateViaMetadata: DefaultTask() {
    init {
        group = "ViaStar"
        description = "Update Via metadata."
    }

    @TaskAction
    fun run() {
        val latestViaStarFile = File(project.rootDir, "latest_viastar.json")
        val prevViaStarFile = File(project.rootDir, "prev_viastar.json")

        val latestViaStarText = latestViaStarFile.bufferedReader().use { it.readText() }
        prevViaStarFile.writeText(latestViaStarText)
    }

}