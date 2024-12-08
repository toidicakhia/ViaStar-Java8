package me.toidicakhia.viastar.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import com.google.gson.Gson
import me.toidicakhia.viastar.utils.HttpUtils
import me.toidicakhia.viastar.model.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.net.URL


open class ValidJobs: DefaultTask() {
    private val gson = Gson()
    private val HOMEPAGE_URI = "https://ci.viaversion.com/api/json?tree=jobs[name,url]"

    init {
        group = "ViaStar"
        description = "Validate jobs from \"vianeeddowngrade.txt\" file."
    }

    private fun getJobsAvailable(): List<String> {
        val homepageResponse = HttpUtils.get(HOMEPAGE_URI)

        if (homepageResponse.statusCode != 200)
            throw RuntimeException("Failed when get CI's homepage.")

        val homepageObject = gson.fromJson(homepageResponse.text, Jenkins::class.java)
        return homepageObject.jobs.map { it.name }
    }

    @TaskAction
    fun run() {
        val viaNeedDowngradeFile = File(project.rootDir, "vianeeddowngrade.txt")

        val text = viaNeedDowngradeFile.bufferedReader().use { it.readText() }
        val jobs = text.split("\n").map { it.trim() }.toList()
        val jobsAvailable = getJobsAvailable()

        val validatedJobs = mutableListOf<String>()

        for (job in jobs) {
            for (jobAvailable in jobsAvailable) {
                if (job.equals(jobAvailable, true)) {
                    validatedJobs.add(job)
                    break
                }
            }
        }

        val validatedJobsText = validatedJobs.joinToString("\n")
        viaNeedDowngradeFile.writeText(validatedJobsText)
    }

}