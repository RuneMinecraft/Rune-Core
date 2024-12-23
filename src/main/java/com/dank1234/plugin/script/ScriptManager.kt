package com.dank1234.plugin.script

import com.dank1234.plugin.Bootstrap
import com.dank1234.plugin.Main
import com.dank1234.plugin.script.actions.load

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Paths
import java.util.zip.ZipInputStream

class ScriptManager {
    companion object {
        private val scriptsFolder = File(Main.get().dataFolder, "scripts")

        @JvmStatic fun initialize() {
            if (scriptsFolder.exists()) {
                return
            }
            scriptsFolder.mkdirs()

            this.getAllScripts().forEach { file ->
                load(file.path)
            }
        }

        private fun getAllScripts(): List<File> = scriptsFolder.walkTopDown()
            .filter { it.isFile && it.extension == "kts" }
            .toList()

        fun getAllScriptNames(): List<String> = getAllScripts()
            .map { it.relativeTo(scriptsFolder).path.replace(File.separator, "/") }

        fun getAllLoadedScriptNames(): List<String> = Bootstrap.loadedScripts.keys.toList()
        fun getScript(name: String): File? {
            return getAllScripts().firstOrNull { it.relativeTo(scriptsFolder).path == name }
        }

        fun updateScripts() {
            try {
                val path: String = ""
                val connection = URL("https://github.com/RuneMinecraft/CodeX/archive/51706b7a402178b6bb2d5a68bd864d9e42abb798.zip").openConnection()
                connection.connect()

                val inputStream = connection.getInputStream()
                val tempZipFile = File("temp.zip")
                FileOutputStream(tempZipFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                val zipInputStream = ZipInputStream(tempZipFile.inputStream())
                var entry = zipInputStream.nextEntry
                while (entry != null) {
                    if (entry.name.startsWith(path)) {
                        val outputPath = Paths.get("result", entry.name.removePrefix(path)).toFile()
                        if (entry.isDirectory) outputPath.mkdirs()
                        else {
                            outputPath.parentFile.mkdirs()
                            FileOutputStream(outputPath).use { fileOut -> zipInputStream.copyTo(fileOut) }
                        }
                    }
                    entry = zipInputStream.nextEntry
                }
                zipInputStream.closeEntry()
                zipInputStream.close()

                tempZipFile.delete()
            } catch (e: Exception) {
                println("\"Fuck you, said the fuck you guy...\"")
                e.printStackTrace()
            }
        }
    }
}