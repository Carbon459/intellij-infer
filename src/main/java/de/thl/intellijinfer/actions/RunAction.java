package de.thl.intelijinfer.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import de.thl.intelijinfer.service.InferLauncher;

public class RunAction extends AnAction {
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        InferLauncher.getInstance(project).run();
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
        //Sdk projectSDK = ProjectRootManager.getInstance(project).getProjectSdk();
        //Messages.showMessageDialog(project, projectSDK.getSdkType().getName(), "Greeting", Messages.getInformationIcon());
    }
}