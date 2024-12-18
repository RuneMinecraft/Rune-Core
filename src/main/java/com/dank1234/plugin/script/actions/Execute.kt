package com.dank1234.plugin.script.actions
/*
import net.runemc.plugin.script.ScriptManager
import org.bukkit.command.CommandSender
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.FileScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvm.BasicJvmScriptEvaluator
import java.io.File
import kotlin.coroutines.coroutineContext
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvmhost.JvmScriptCompiler

suspend fun execute(scriptManager: ScriptManager, scriptName: String) {
    val scriptFile: File? = scriptManager.getScript(scriptName)
    if (scriptFile == null || !scriptFile.exists() || !scriptFile.canRead()) {
        println("An error occured! [null script was executed]")
        return
    }

    try {
        val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<Any> {
            jvm {
                dependenciesFromCurrentContext(wholeClasspath = true)
            }
        }
        val evaluationConfiguration = ScriptEvaluationConfiguration {
            jvm {
                baseClassLoader(this::class.java.classLoader)
            }
        }
        val scriptSource = FileScriptSource(scriptFile)
        val compiler = JvmScriptCompiler()
        val evaluator = BasicJvmScriptEvaluator()
        val compiledScriptResult = compiler.compilerProxy.compile(scriptSource, compilationConfiguration)

        if (compiledScriptResult is ResultWithDiagnostics.Failure) {
            val errorMessages = compiledScriptResult.reports.joinToString("\n") { it.message }
            println("An error occured! [compiling error]\n$errorMessages")
            return
        }

        val evaluationResult = evaluator(compiledScriptResult.valueOrThrow(), evaluationConfiguration)

        if (evaluationResult is ResultWithDiagnostics.Success) {
            println("Executed the script: $scriptName")
        } else {
            val errorMessages = evaluationResult.reports.joinToString("\n") { it.message }
            println("An error occured! [execution error]")
        }
    } catch (e: Throwable) {
        println("An error occured! [unknown]")
        e.printStackTrace()
    }
}

 */
