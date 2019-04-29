import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;

public class RunAction extends AnAction {
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Sdk projectSDK = ProjectRootManager.getInstance(project).getProjectSdk();
        Messages.showMessageDialog(project, projectSDK.getSdkType().getName(), "Greeting", Messages.getInformationIcon());
    }
}