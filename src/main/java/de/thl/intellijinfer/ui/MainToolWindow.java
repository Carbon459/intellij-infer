package de.thl.intellijinfer.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.treeStructure.Tree;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.model.ResultListEntry;
import de.thl.intellijinfer.service.ResultParser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainToolWindow {
    private static final Logger log = Logger.getInstance(MainToolWindow.class);

    private JPanel MainToolWindowContent;
    private Tree issueList;

    private Project project;


    MainToolWindow(ToolWindow toolWindow, Project project) {
        this.project = project;

        issueList.getEmptyText().setText(ResourceBundle.getBundle("strings").getString("no.bug.list.to.show"));
        issueList.setModel(new DefaultTreeModel(null));

        ResultParser.getInstance(project).addPropertyChangeListener(evt -> {
            if(evt.getNewValue() != null && evt.getPropertyName().equals("bugsPerFile")) {
                drawBugTree((Map<String, List<InferBug>>)evt.getNewValue());
            }
        });

        issueList.setCellRenderer(new ColoredTreeCellRenderer() {
            @Override
            public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                //is a bug or bugtrace (tree depth 3 or 4)
                if(((DefaultMutableTreeNode)value).getUserObject() instanceof ResultListEntry) {
                    final ResultListEntry bug = (ResultListEntry) ((DefaultMutableTreeNode)value).getUserObject();
                    append("Line: " + bug.getLine(), SimpleTextAttributes.SYNTHETIC_ATTRIBUTES);
                    if(bug.getColumn() >= 0) //sometimes infer returns a -1 as column number
                        append(" Column: " + bug.getColumn(), SimpleTextAttributes.SYNTHETIC_ATTRIBUTES);
                    append(" " + bug.toString());
                }
                //is the top most entry (tree depth 1)
                else if(row == 0){
                    append(value.toString(), new SimpleTextAttributes(SimpleTextAttributes.STYLE_BOLD, null));
                    setIcon(AllIcons.Actions.ListFiles);
                }
                //filenames (tree depth 2)
                else {
                    append(value.toString(), SimpleTextAttributes.ERROR_ATTRIBUTES);
                }
            }
        });

        issueList.addTreeSelectionListener(e -> ApplicationManager.getApplication().invokeLater(() -> {
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            final DefaultMutableTreeNode node = (DefaultMutableTreeNode) issueList.getLastSelectedPathComponent();

            if(editor == null || node == null) return;
            if(node.getUserObject() instanceof ResultListEntry) {
                final ResultListEntry bug = (ResultListEntry) node.getUserObject();
                LogicalPosition pos = new LogicalPosition(
                        bug.getLine() > 0 ? bug.getLine() - 1 : 0,
                        bug.getColumn() > 0 ? bug.getColumn() - 1 : 0); // -1 because LogicalPosition starts to count at 0;
                String fileName = bug.getFileName();

                PsiFile[] fileArray = FilenameIndex.getFilesByName(project, fileName , GlobalSearchScope.projectScope(project));
                if(fileArray.length != 1) {
                    log.warn("Could not find or to many selected file(s) to navigate to: " + fileName);
                    return;
                }
                fileArray[0].navigate(true);

                editor = FileEditorManager.getInstance(project).getSelectedTextEditor(); //get the new editor because we just changed it
                if(editor == null) {
                    log.warn("No editor found. Not jumping to line " + bug.getLine());
                    return;
                }

                editor.getScrollingModel().scrollTo(pos, ScrollType.CENTER);
                editor.getCaretModel().moveToLogicalPosition(pos);
            }
        }));
    }

    public JPanel getContent() {
        return MainToolWindowContent;
    }

    /**
     * Draws the given bugMap to the Infer Tool Window
     * @param bugMap keys are filenames, while the values are lists of infer bugs
     */
    private void drawBugTree(Map<String, List<InferBug>> bugMap) {
        if(bugMap == null) return;

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(String.format(ResourceBundle.getBundle("strings").getString("infer.analysis.result.bugs.found"), bugMap.values().stream().mapToInt(List::size).sum()));

        //todo effizienter
        for (Map.Entry<String, List<InferBug>> entry : bugMap.entrySet()) {
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(String.format(ResourceBundle.getBundle("strings").getString("bugs.found"), entry.getKey(), entry.getValue().size()));
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
