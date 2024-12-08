package me.toidicakhia.viastar.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.google.gson.Gson
import me.toidicakhia.viastar.utils.HttpUtils
import me.toidicakhia.viastar.model.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.*


open class GetLatestSuccessfulBuild: DefaultTask() {
	private val gson = Gson()

    init {
        group = "ViaStar"
        description = "Get latest Via* build number."
    }

    private fun getLatestBuildNumber(job: String): Int {
        val ciUrl = "https://ci.viaversion.com/job/$job/api/json?tree=lastSuccessfulBuild[number,url]"
        val ciResponse = HttpUtils.get(ciUrl)

        if (ciResponse.statusCode != 200)
            throw RuntimeException("Failed when getting ${job}\'s CI.")

        val ciObject = gson.fromJson(ciResponse.text, JenkinsLastSuccessBuild::class.java)
        return ciObject.lastSuccessfulBuild.number
    }

    @TaskAction
    fun run() {
        val viaNeedDowngradeFile = File(project.rootDir, "vianeeddowngrade.txt")

        val text = viaNeedDowngradeFile.bufferedReader().use { it.readText() }
        val jobs = text.split("\n").map { it.trim() }
        val jobsLatestBuild = jobs.map { Pair(it, getLatestBuildNumber(it)) }.associate { it }

        val latestViaStarFile = File(project.rootDir, "latest_viastar.json")
        val jsonText = gson.toJson(JobsMetadata(jobsLatestBuild))
        latestViaStarFile.writeText(jsonText)
    }

}