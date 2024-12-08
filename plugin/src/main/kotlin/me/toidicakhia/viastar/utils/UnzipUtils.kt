package me.toidicakhia.viastar.utils

import java.io.*
import java.util.zip.ZipFile


/**
 * UnzipUtils class extracts files and sub-directories of a standard zip file to
 * a destination directory.
 * @author Nitin Prakash
 * @license MIT
 */
object UnzipUtils {

    fun unzip(zipFilePath: File, destDirectory: File) {
        if (!destDirectory.exists())
            destDirectory.mkdirs()

        ZipFile(zipFilePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                if (entry.name.contains("META-INF", true))
                    return@forEach

                zip.getInputStream(entry).use { input ->
                    val filePath = File(destDirectory, entry.name)

                    if (!entry.isDirectory)
                        extractFile(input, filePath.path)
                    else
                        filePath.mkdir()
                }
            }
        }
    }

    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        val bos = BufferedOutputStream(FileOutputStream(destFilePath))
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

    private const val BUFFER_SIZE = 4096
}