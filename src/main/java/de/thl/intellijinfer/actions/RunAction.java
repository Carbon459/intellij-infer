package de.thl.intellijinfer.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import de.thl.intellijinfer.config.InferVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RunAction extends AnAction {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.actions.RunAction");
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);

        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
    }
}