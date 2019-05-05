package de.thl.intelijinfer.actions;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.PlatformUtils;
import com.intellij.util.indexing.FileBasedIndex;
import de.thl.intelijinfer.service.InferRunner;

import java.util.Collection;

public class RunAction extends AnAction {
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        InferRunner.getInstance(project).run();

        //Sdk projectSDK = ProjectRootManager.getInstance(project).getProjectSdk();
        //Messages.showMessageDialog(project, projectSDK.getSdkType().getName(), "Greeting", Messages.getInformationIcon());
    }
}