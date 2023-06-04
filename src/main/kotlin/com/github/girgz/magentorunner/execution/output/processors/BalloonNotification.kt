package com.github.girgz.magentorunner.execution.output.processors

import com.github.girgz.magentorunner.execution.output.OutputProcessorInterface
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

class BalloonNotification : OutputProcessorInterface {
    override fun process(commandOutput: String) {
        // TODO: fix groupId
        Notifications.Bus.notify(
            Notification(
                "Sqlite Viewer",
                "Command succeeded",
                "",
                NotificationType.INFORMATION
            )
        )
    }
}