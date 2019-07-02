package de.thl.intellijinfer.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.service.ResultParser;

public class RunAction extends AnAction {
    private static final Logger log = Logger.getInstance(RunAction.class);
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        ResultParser.getInstance(project).parse("C:\\Users\\Carbon\\IdeaProjects\\intelij-infer\\src\\test\\resources\\multipleBugs.json");
        //if(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project) != null) System.out.println(Arrays.toString(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project).toArray()));
        //System.out.println(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project));
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
    }
}