package me.toidicakhia.viastar.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import com.google.gson.Gson
import me.toidicakhia.viastar.utils.HttpUtils
import me.toidicakhia.viastar.model.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.*


open class CheckBuildHasUptoDate: DefaultTask() {
	private val gson = Gson()

    @Input
    var hasUptoDate = true

    init {
        group = "ViaStar"
        description = "Get latest Via* build number."
    }

    @TaskAction
    fun run() {
        val latestViaStarFile = File(project.rootDir, "latest_viastar.json")
        val prevViaStarFile = File(project.rootDir, "prev_viastar.json")

        if (!latestViaStarFile.exists() || !prevViaStarFile.exists()) {
            hasUptoDate = false
            return
        }

        val latestViaStarText = latestViaStarFile.bufferedReader().use { it.readText() }
        val latestJsonObject = gson.fromJson(latestViaStarText, JobsMetadata::class.java)

        val prevViaStarText = prevViaStarFile.bufferedReader().use { it.readText() }
        val prevJsonObject = gson.fromJson(prevViaStarText, JobsMetadata::class.java)

        val isOutdatedMap = latestJsonObject.jobs.entries.map {
            var isOutdated = true

            for (prevJob in prevJsonObject.jobs.entries) {
                if (prevJob.key == it.key) {
                    isOutdated = prevJob.value < it.value
                    break
                }
            }

            Pair(it.key, isOutdated)
        }.associate { it }

        hasUptoDate = isOutdatedMap.entries.all { !it.value }
    }

}