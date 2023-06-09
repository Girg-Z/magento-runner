package com.github.girgz.magentorunner.ui.quickActions;

import com.intellij.openapi.project.Project;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class QuickActionsFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        QuickActions ToolWindow = new QuickActions();
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(ToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
