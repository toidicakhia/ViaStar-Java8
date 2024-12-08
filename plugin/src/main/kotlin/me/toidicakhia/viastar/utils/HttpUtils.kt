package me.toidicakhia.viastar.utils

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.io.File

object HttpUtils {
    private const val DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"

    private fun make(
        url: String,
        method: String,
        body: String = "",
        header: Map<String, String> = emptyMap(),
        allowRedirect: Boolean = true
    ): HttpURLConnection {
        val httpConnection = URL(url).openConnection() as HttpURLConnection

        httpConnection.requestMethod = method
        httpConnection.connectTimeout = 2000
        httpConnection.readTimeout = 10000

        httpConnection.setRequestProperty("User-Agent", DEFAULT_AGENT)
        header.forEach { (key, value) -> httpConnection.setRequestProperty(key, value) }

        httpConnection.instanceFollowRedirects = allowRedirect
        httpConnection.doOutput = true

        if (body.isNotEmpty()) {
            val dataOutputStream = DataOutputStream(httpConnection.outputStream)
            dataOutputStream.writeBytes(body)
            dataOutputStream.flush()
        }

        httpConnection.connect()

        return httpConnection
    }

    private fun request(
        url: String,
        method: String,
        body: String = "",
        header: Map<String, String> = emptyMap(),
        allowRedirect: Boolean = true
    ): Response {
        val connection = make(url, method, body, header, allowRedirect)

        val responseCode = connection.responseCode
        val stream = if (responseCode in 200..299) connection.inputStream else connection.errorStream
        val text = stream?.reader()?.readText() ?: ""
        val headers = connection.headerFields
            .filter { it.key != null }
            .map { (key, value) ->
                if (value.size == 1) {
                    if (key.equals("location", true))
                        key to value.first().replace(" ", "%20")
                    else
                        key to value.first()
                } else key to value.joinToString(", ") { it.trim() }
            }
            .associate { (key, value) -> key to value }

        return Response(responseCode, text, headers)
    }

    fun get(
        url: String,
        header: Map<String, String> = emptyMap(),
        allowRedirect: Boolean = false
    ) = request(url, "GET", header = header, allowRedirect = allowRedirect)

    fun downloadFile(url: String, file: File) {
        println("Downloading ${file.name}...")

        val connection = make(url, "GET", "")
        val responseCode = connection.responseCode
        val stream = if (responseCode in 200..299) connection.inputStream else connection.errorStream
        
        try {
            stream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to download ${file.name}", e)
        }
        
    }

    fun downloadFile(uri: String, file: File, hash: String) {
        if (file.exists() && HashUtils.getCheckSumSHA1FromFile(file) == hash) {
            return
        }

        downloadFile(uri, file)
    }
}