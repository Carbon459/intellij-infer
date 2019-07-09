package de.thl.intellijinfer.actions;

import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.IconLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.service.ResultParser;

import java.nio.file.Paths;

public class RunAction extends AnAction {
    private static final Logger log = Logger.getInstance(RunAction.class);
    public RunAction() {
        super("Run");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        GlobalSettings.getInstance().hasValidInstallation();


        //ResultParser.getInstance(project).parse(Paths.get("C:\\Users\\Carbon\\IdeaProjects\\intelij-infer\\src\\test\\resources\\multipleBugs.json"));
        //if(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project) != null) System.out.println(Arrays.toString(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project).toArray()));
        //System.out.println(BuildManager.getInstance().getFilesChangedSinceLastCompilation(project));
        //Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        //editor.getMarkupModel().addLineHighlighter(2, 20, new TextAttributes());
        /*RangeHighlighter rh = editor.getMarkupModel().addLineHighlighter(1, 20, null);
        rh.setGutterIconRenderer(NavigationGutterIconBuilder
                .create(AllIcons.Actions.Cancel)
                .setTarget(FilenameIndex.getFilesByName(project, "Main.java", GlobalSearchScope.projectScope(project))[0].findElementAt(2))
                .setTooltipText("Laufzeitkomplexität: O(1)")
                .createLineMarkerInfo(FilenameIndex.getFilesByName(project, "Main.java", GlobalSearchScope.projectScope(project))[0]).createGutterRenderer());*/
        //HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);
    }
}