package de.thl.intelijinfer.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.PlatformUtils;
import de.thl.intelijinfer.service.InferRunner;

public class RunAction extends AnAction {
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        InferRunner ir = InferRunner.getInstance(project);
        ir.run();
        //Sdk projectSDK = ProjectRootManager.getInstance(project).getProjectSdk();
        //Messages.showMessageDialog(project, projectSDK.getSdkType().getName(), "Greeting", Messages.getInformationIcon());
    }
}