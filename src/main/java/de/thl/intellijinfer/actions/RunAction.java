package de.thl.intellijinfer.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.service.ResultParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class RunAction extends AnAction {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.actions.RunAction");
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        List<InferBug> bugList = null;
        try {
            bugList = ResultParser.getInstance(project).readBugList("C:\\Users\\Carbon\\IdeaProjects\\intelij-infer\\src\\test\\resources\\singleBug.json");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        if(bugList != null) {
            //System.out.println(Arrays.toString(ResultParser.getInstance(project).getBugsPerFile(bugList).entrySet().toArray()));
            ResultParser.getInstance(project).mtw.setBugTree(ResultParser.getInstance(project).getBugsPerFile(bugList));
        }

        //if(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project) != null) System.out.println(Arrays.toString(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project).toArray()));
        //System.out.println(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project));
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
    }
}