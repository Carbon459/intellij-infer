import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBList;

import javax.swing.*;

public class MainToolWindow {
    private JPanel MainToolWindowContent;
    private JBList issueList;

    public MainToolWindow(ToolWindow toolWindow) {
        DefaultListModel<String> model = new DefaultListModel();
        issueList.setModel(model);
        model.addElement("test");
        model.addElement("test2");
        model.addElement("test3");
    }

    public JPanel getContent() {
        return MainToolWindowContent;
    }
}
