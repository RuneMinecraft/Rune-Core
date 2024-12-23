package com.dank1234.plugin.script.actions

import com.dank1234.plugin.script.ScriptManager
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvm.BasicJvmScriptEvaluator
import java.io.File
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvmhost.JvmScriptCompiler

fun execute(scriptName: String) {
    val scriptFile: File? = ScriptManager.getScript(scriptName)
    if (scriptFile == null || !scriptFile.exists() || !scriptFile.canRead()) {
        println("An error occured! [null script was executed]")
        return
    }

    try {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<Any> {
            jvm {
                dependenciesFromCurrentContext( wholeClasspath = true, unpackJarCollections = true)
            }
        }
        ScriptEvaluationConfiguration {
            jvm {
                baseClassLoader(this::class.java.classLoader)
            }
        }
        val scriptSource = FileScriptSource(scriptFile)
        val compiler = JvmScriptCompiler()
        val compiledScriptResult = compiler.compilerProxy.compile(scriptSource, compilationConfiguration)

        if (compiledScriptResult is ResultWithDiagnostics.Failure) {
            val errorMessages = compiledScriptResult.reports.joinToString("\n") { it.message }
            println("An error occured! [compiling error]\n$errorMessages")
            return
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}
