package de.thl.intellijinfer.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.service.ResultParser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.List;
import java.util.Map;

public class MainToolWindow {
    private JPanel MainToolWindowContent;
    private Tree issueList;

    private Project project;


    public MainToolWindow(ToolWindow toolWindow, Project project) {
        this.project = project;
        ResultParser.getInstance(project).mtw = this;
    }

    public JPanel getContent() {
        return MainToolWindowContent;
    }

    public void setBugTree(Map<String, List<InferBug>> bugMap) {
        if(bugMap == null) return;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Infer Analysis Result:");

        //todo effizienter
        for (Map.Entry<String, List<InferBug>> entry : bugMap.entrySet()) {
            final DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(entry.getKey());
            for(InferBug bug : entry.getValue()) {
                final DefaultMutableTreeNode subsubnode = new DefaultMutableTreeNode(bug);
                for(InferBug.BugTrace trace : bug.getBugTrace()) {
                    final DefaultMutableTreeNode subsubsubnode = new DefaultMutableTreeNode(trace);
                    subsubnode.add(subsubsubnode);
                }
                subnode.add(subsubnode);
            }
            root.add(subnode);
        }

        TreeModel tm = new DefaultTreeModel(root);
        issueList.setModel(tm);
    }
}
