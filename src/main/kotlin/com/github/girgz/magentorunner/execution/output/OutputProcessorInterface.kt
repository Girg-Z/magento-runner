package com.github.girgz.magentorunner.execution.output

interface OutputProcessorInterface {
    fun process(commandOutput: String)
}