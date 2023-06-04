package com.github.girgz.magentorunner.execution

import com.github.girgz.magentorunner.execution.output.ProcessorFactory
import com.intellij.execution.ExecutionException
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessOutputType
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.util.Key
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import java.io.File
import java.util.function.BiConsumer

class CommandExecutor {
    var customCallback: BiConsumer<Int, String>? = null
    var isResultWindowAutoOpen: Boolean = true

    fun execute(command: String) {
        // Cool
        // TODO: Learn more
        DataManager.getInstance().dataContextFromFocusAsync.then {
            val project = it.getData(PlatformDataKeys.PROJECT)!!
            val type = ConfigurationTypeUtil.findConfigurationType("PhpLocalRunConfigurationType")
            val runManager = RunManager.getInstance(project)
            val runnerAndConfigurationSettings = runManager.createConfiguration(
                "Magento Runner",
                type!!.configurationFactories[0]
            )

            val executor = DefaultRunExecutor.getRunExecutorInstance()
            val conf = runnerAndConfigurationSettings.configuration as PhpScriptRunConfiguration

            conf.settings.path = project.basePath + File.separator + "bin" + File.separator + "magento"
            conf.settings.setScriptParameters(command)

            // Enable-Disable result window auto open
            runnerAndConfigurationSettings.isActivateToolWindowBeforeRun = isResultWindowAutoOpen

            val runner = ProgramRunner.getRunner(executor.id, conf)
            val environment = ExecutionEnvironment(executor, runner!!, runnerAndConfigurationSettings, project)
            try {
                // TODO: Take a look at that
                // Can I use lambda expression instead of nested class ??
                // On the JVM, if the object is an instance of a functional Java interface
                // (that means a Java interface with a single abstract method),
                // you can create it using a lambda expression prefixed with the type of the interface
                // https://kotlinlang.org/docs/nested-classes.html#anonymous-inner-classes

                runner.execute(environment, Callback(command, customCallback))
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }
    }

    private class Callback(private val command: String, private val customCallback: BiConsumer<Int, String>?) :
        ProgramRunner.Callback {
        override fun processStarted(descriptor: RunContentDescriptor) {
            descriptor.processHandler!!.addProcessListener(
                object : ProcessAdapter() {
                    private val stringBuilder = StringBuilder()
                    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                        // This will be executed multiple times (1: input, 2: output, 3: exit code)
                        if (ProcessOutputType.isStdout(outputType)) {
                            stringBuilder.append(event.text)
                        }
                    }

                    override fun processTerminated(event: ProcessEvent) {
                        if (customCallback != null) {
                            customCallback.accept(event.exitCode, stringBuilder.toString())
                        } else {
                            val outputProcessor = ProcessorFactory().create(command)
                            outputProcessor.process(stringBuilder.toString())
                        }
                    }
                }
            )
        }
    }
}