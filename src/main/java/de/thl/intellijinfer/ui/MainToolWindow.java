package de.thl.intellijinfer.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.treeStructure.Tree;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.service.ResultParser;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.List;
import java.util.Map;

public class MainToolWindow {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.ui.MainToolWindow");

    private JPanel MainToolWindowContent;
    private Tree issueList;

    private Project project;


    public MainToolWindow(ToolWindow toolWindow, Project project) {
        this.project = project;
        ResultParser.getInstance(project).mtw = this;

        issueList.addTreeSelectionListener(e -> {
            final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) issueList.getLastSelectedPathComponent();

            if(node.getUserObject() instanceof InferBug || node.getUserObject() instanceof InferBug.BugTrace) {
                LogicalPosition pos;
                String fileName;

                if(node.getUserObject() instanceof InferBug) {
                    final InferBug bug = (InferBug) node.getUserObject();
                    pos = new LogicalPosition(bug.getLine()-1, bug.getColumn());
                    fileName = bug.getFile();
                } else {
                    final InferBug.BugTrace bug = (InferBug.BugTrace) node.getUserObject();
                    pos = new LogicalPosition(bug.getLineNumber()-1, bug.getColumnNumber());
                    fileName = bug.getFilename();
                }

                PsiFile[] fileArray = FilenameIndex.getFilesByName(project, fileName , GlobalSearchScope.projectScope(project));
                if(fileArray.length != 1) {
                    log.warn("Could not find or to many selected file(s) to navigate to: " + fileName);
                    return;
                }
                fileArray[0].navigate(true);

                editor.getCaretModel().moveToLogicalPosition(pos);
                editor.getScrollingModel().scrollTo(pos, ScrollType.CENTER);
            }
        });
    }

    public JPanel getContent() {
        return MainToolWindowContent;
    }

    /**
     * Draws the given bugMap to the Infer Tool Window
     * @param bugMap keys are filenames, while the values are lists of infer bugs
     */
    public void setBugTree(Map<String, List<InferBug>> bugMap) {
        if(bugMap == null) return;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Infer Analysis Result:");

        //todo effizienter
        for (Map.Entry<String, List<InferBug>> entry : bugMap.entrySet()) {
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(entry.getKey());
            for(InferBug bug : entry.getValue()) {
                DefaultMutableTreeNode bugNode = new DefaultMutableTreeNode(bug);
                for(InferBug.BugTrace trace : bug.getBugTrace()) {
                    DefaultMutableTreeNode bugtraceNode = new DefaultMutableTreeNode(trace);
                    bugNode.add(bugtraceNode);
                }
                fileNode.add(bugNode);
            }
            root.add(fileNode);
        }

        TreeModel tm = new DefaultTreeModel(root);
        issueList.setModel(tm);
    }
}
