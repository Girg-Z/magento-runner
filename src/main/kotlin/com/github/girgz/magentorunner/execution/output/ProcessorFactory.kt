package com.github.girgz.magentorunner.execution.output

import com.github.girgz.magentorunner.execution.output.processors.BalloonNotification

class ProcessorFactory {
    fun create(command: String?): OutputProcessorInterface {
        return when (command){
            else -> BalloonNotification()
        }
    }
}