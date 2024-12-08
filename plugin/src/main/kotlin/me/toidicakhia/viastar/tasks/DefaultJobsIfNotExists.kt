package me.toidicakhia.viastar.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

open class DefaultJobsIfNotExists: DefaultTask() {
    init {
        group = "ViaStar"
        description = "Generate default jobs if \"vianeeddowngrade.txt\" file is not exist."
    }

    @TaskAction
    fun run() {
        val viaNeedDowngradeFile = File(project.rootDir, "vianeeddowngrade.txt")

        if (!viaNeedDowngradeFile.exists()) {
            val defaultJobsText = listOf("ViaVersion", "ViaBackwards", "ViaRewind").joinToString("\n")
            viaNeedDowngradeFile.writeText(defaultJobsText)
        }
    }

}