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


open class DowngradeViaTask: DefaultTask() {
    init {
        group = "ViaStar"
        description = "Downgrade Via libraries."
    }

    private fun downgradeJar(tempFolder: File, file: File) {
        println("Downgrading ${file.name}...")
        try {
            val command = listOf("java", 
                "-jar", "JavaDowngrader-Standalone-1.1.2.jar", 
                "-i", file.name, 
                "-o", "../build/libs/${file.name}", 
                "-v", "8"
            )
            val process = ProcessBuilder(command)
                .directory(tempFolder)
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().use { it.readText() }
            println(output)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @TaskAction
    fun run() {
        val tempDir = File(project.rootDir, "temp")
        if (!tempDir.exists())
            throw RuntimeException("Temp Via* folder not found.")

        val jobFiles = tempDir.listFiles().filter { it.name.contains("Via", true) }
        for (file in jobFiles)
            downgradeJar(tempDir, file)
    }

}