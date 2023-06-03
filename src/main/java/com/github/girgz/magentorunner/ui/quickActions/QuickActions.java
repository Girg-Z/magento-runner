package com.github.girgz.magentorunner.ui.quickActions;

//import com.github.girgz.m2phpstormrunner.execution.CommandExecutor;
import com.intellij.ui.AnimatedIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class QuickActions {
    private JButton flushCacheButton;
    private JButton setupUpgradeButton;
    private JButton diCompileButton;
    private JButton runCronButton;
    private JButton developerModeButton;
    private JButton productionModeButton;
    private JPanel content;

    private final Map<String, JButton> buttonList;

    public QuickActions() {
        buttonList = new HashMap<>();
        buttonList.put("cache:flush", flushCacheButton);
        buttonList.put("setup:upgrade", setupUpgradeButton);
        buttonList.put("setup:di:compile", diCompileButton);
        buttonList.put("cron:run", runCronButton);
        buttonList.put("deploy:mode:set developer", developerModeButton);
        buttonList.put("deploy:mode:set production", productionModeButton);

        ActionListener actionListener = new RunCommand();

        buttonList.forEach((command, button) -> {
            button.setActionCommand(command);
            button.addActionListener(actionListener);
        });
    }

    private class RunCommand implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            disableAllButtons();

            JButton button = (JButton) e.getSource();
            String command = e.getActionCommand();
            button.setIcon(new AnimatedIcon.Default());
            button.setDisabledIcon(new AnimatedIcon.Default());

            new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
//                    CommandExecutor executor = new CommandExecutor();
//                    executor.setCustomCallback((String output) -> {
//                        button.setIcon(null);
//                        enableAllButtons();
//                    });
//                    executor.execute(command);
                    return null;
                }
            }.execute();
        }

        private void disableAllButtons() {
            buttonList.forEach((command, button) -> {
                button.setEnabled(false);
            });
        }

        private void enableAllButtons() {
            buttonList.forEach((command, button) -> {
                button.setEnabled(true);
            });
        }
    }

    public JPanel getContent() {
        return this.content;
    }
}
