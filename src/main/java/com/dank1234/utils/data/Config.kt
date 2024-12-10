package com.dank1234.utils.data

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

import kotlin.io.path.createDirectories

object Config {
    val yaml: Yaml

    init {
        val options = DumperOptions().apply {
            indent = 2
            isPrettyFlow = true
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        }

        val representer = Representer(options).apply {
            propertyUtils.isSkipMissingProperties = true
        }

        yaml = Yaml(Constructor(LoaderOptions()), representer, options)
    }

    @Throws(IOException::class) inline fun <reified T> load(file: File): T {
        if (!file.exists()) file.createNewFile()
        FileReader(file).use { reader ->
            return yaml.loadAs(reader, T::class.java)
        }
    }
    @Throws(IOException::class) fun save(file: File, data: Any) {
        if (!file.exists()) {
            file.parentFile?.toPath()?.createDirectories()
            file.createNewFile()
        }
        FileWriter(file).use { writer ->
            yaml.dump(data, writer)
        }
    }
}
