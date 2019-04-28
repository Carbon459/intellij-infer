import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;

public class HelloAction extends AnAction {
    public HelloAction() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        //Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
    }
}