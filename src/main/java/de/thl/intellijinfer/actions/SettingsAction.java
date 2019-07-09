package de.thl.intellijinfer.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import de.thl.intellijinfer.config.PluginConfigurable;
import org.jetbrains.annotations.NotNull;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), PluginConfigurable.class);
    }
}
