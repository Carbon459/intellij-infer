package de.thl.intelijinfer.actions;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.LightweightHint;
import com.intellij.util.PlatformUtils;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.ui.UIUtil;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import de.thl.intelijinfer.service.InferRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;

public class RunAction extends AnAction {
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        //InferRunner.getInstance(project).run();
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
        //Sdk projectSDK = ProjectRootManager.getInstance(project).getProjectSdk();
        //Messages.showMessageDialog(project, projectSDK.getSdkType().getName(), "Greeting", Messages.getInformationIcon());
    }
}