package de.thl.intellijinfer.actions;

import com.intellij.execution.RunManager;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.service.InferLauncher;

import java.util.List;

public class RunAction extends AnAction {
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        List<RunConfiguration> r = RunManager.getInstance(project).getConfigurationsList(ApplicationConfigurationType.getInstance());
        for(RunConfiguration rc : r) {
            System.out.println(rc);
        }
        //InferLauncher.getInstance(project).run();
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
    }
}