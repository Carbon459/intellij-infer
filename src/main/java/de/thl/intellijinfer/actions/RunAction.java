package de.thl.intellijinfer.actions;

import com.intellij.compiler.server.BuildManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import java.util.Arrays;

public class RunAction extends AnAction {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.actions.RunAction");
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        //if(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project) != null) System.out.println(Arrays.toString(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project).toArray()));
        //System.out.println(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project));
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
    }
}