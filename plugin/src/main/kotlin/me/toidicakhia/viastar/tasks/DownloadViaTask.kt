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


open class DownloadViaTask: DefaultTask() {
	private val gson = Gson()
    private val JOBS = arrayOf("ViaVersion", "ViaBackwards", "ViaRewind")
    private val JVM_DOWNGRADER_URL = "https://github.com/RaphiMC/JavaDowngrader/releases/download/v1.1.2/JavaDowngrader-Standalone-1.1.2.jar"

    init {
        group = "Bloom Gradle"
        description = "Download Via libraries."
    }

    private fun downloadJvmDowngrader(tempDir: File) {
        val downgraderFile = File(tempDir, "JavaDowngrader-Standalone-1.1.2.jar")
        HttpUtils.downloadFile(JVM_DOWNGRADER_URL, downgraderFile)
    }

    private fun downloadVia(tempFolder: File, job: String) {
        val ciUrl = "https://ci.viaversion.com/job/$job/api/json?tree=lastSuccessfulBuild[number,url]"
        val ciResponse = HttpUtils.get(ciUrl)

        if (ciResponse.statusCode != 200)
            throw RuntimeException("Failed when getting $job's CI.")

        val ciObject = gson.fromJson(ciResponse.text, JenkinsJob::class.java)
        val buildUrl = ciObject.lastSuccessfulBuild.url
        val buildApiUrl = "$buildUrl/api/json?tree=artifacts[relativePath,fileName]"
        val buildResponse = HttpUtils.get(buildApiUrl)

        if (buildResponse.statusCode != 200)
            throw RuntimeException("Failed when getting $job's latest build.")

        val buildObject = gson.fromJson(buildResponse.text, JenkinsBuild::class.java)
        val artifact = buildObject.artifacts.first()

        val artifactUrl = "$buildUrl/artifact/${artifact.relativePath}"
        val artifactFile = File(tempFolder, artifact.fileName)
        HttpUtils.downloadFile(artifactUrl, artifactFile)
    }

    @TaskAction
    fun download() {
        println("Downloading Via libraries...")

        val tempDir = File(project.rootDir, "temp")
        if (!tempDir.exists())
            tempDir.mkdirs()

        if (tempDir.exists())
            tempDir.deleteRecursively()

        tempDir.mkdirs()

    	for (job in JOBS)
            downloadVia(tempDir, job)
        downloadJvmDowngrader(tempDir)
    }

}