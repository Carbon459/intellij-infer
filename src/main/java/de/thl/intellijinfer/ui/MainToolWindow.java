package de.thl.intellijinfer.ui;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class MainToolWindow {
    private JPanel MainToolWindowContent;
    private Tree issueList;


    public MainToolWindow(ToolWindow toolWindow) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("File1.java");
        DefaultMutableTreeNode subnode = new DefaultMutableTreeNode("Error 1: .....");
        DefaultMutableTreeNode subnode2 = new DefaultMutableTreeNode("Error 2: ....");
        root.add(subnode);
        root.add(subnode2);
        TreeModel tm = new DefaultTreeModel(root);
        issueList.setModel(tm); //Lieber custom treemodel
    }

    public JPanel getContent() {
        return MainToolWindowContent;
    }
}
