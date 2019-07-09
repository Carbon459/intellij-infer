package de.thl.intellijinfer.actions;

import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.model.buildtool.BuildToolFactory;
import de.thl.intellijinfer.run.InferConfigurationType;
import de.thl.intellijinfer.run.InferRunConfiguration;

public class RunAction extends AnAction {
    private static final Logger log = Logger.getInstance(RunAction.class);
    private static final String GENERATED_CONFIG_NAME = "Generated Infer Config";
    public RunAction() {
        super();
    }

    public void actionPerformed(AnActionEvent event) {
        if(!GlobalSettings.getInstance().hasValidInstallation()) {
            log.error("No valid Infer Installation: Go to the Infer Settings and add one");
            return;
        }

        final RunManagerImpl runManager = (RunManagerImpl) RunManager.getInstance(event.getProject());

        RunnerAndConfigurationSettings rcs = null;

        //Find a already generated config
        for(RunnerAndConfigurationSettings rcSettings : runManager.getConfigurationSettingsList(InferConfigurationType.class)) {
            if(rcSettings.getConfiguration().getName().equals(GENERATED_CONFIG_NAME)) rcs = rcSettings;
        }
        //Generate a Config if we didnt find one
        if(rcs == null) {
            rcs = generateInferRunConfig(runManager);
            runManager.addConfiguration(rcs);
        }

        runManager.setSelectedConfiguration(rcs);
        RunnerAndConfigurationSettings runnerAndConfigurationSettings = new RunnerAndConfigurationSettingsImpl(runManager, rcs.getConfiguration(), false);
        ProgramRunnerUtil.executeConfiguration(runnerAndConfigurationSettings, DefaultRunExecutor.getRunExecutorInstance());
    }

    private RunnerAndConfigurationSettings generateInferRunConfig(RunManagerImpl runManager) {
        final ConfigurationFactory inferFactory = runManager.getFactory("InferRunConfiguration", "Infer configuration factory");
            if(inferFactory != null) {
                RunnerAndConfigurationSettings rcs = runManager.createConfiguration(GENERATED_CONFIG_NAME, inferFactory);

                InferRunConfiguration inferRC = (InferRunConfiguration) rcs.getConfiguration();
                inferRC.getLaunchOptions().setUsingBuildTool(BuildToolFactory.getPreferredBuildTool(runManager.getProject()));
                inferRC.getLaunchOptions().setSelectedInstallation(GlobalSettings.getInstance().getAnyValidInstallation());

                return rcs;
            }
            return null;
    }
}



//Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

//Notifications.Bus.notify(new Notification("Infer", "Failure", "Infer terminated unsuccessfully", NotificationType.ERROR));
//ResultParser.getInstance(project).parse(Paths.get("C:\\Users\\Carbon\\IdeaProjects\\intelij-infer\\src\\test\\resources\\multipleBugs.json"));

//editor.getMarkupModel().addLineHighlighter(2, 20, new TextAttributes());
        /*RangeHighlighter rh = editor.getMarkupModel().addLineHighlighter(1, 20, null);
        rh.setGutterIconRenderer(NavigationGutterIconBuilder
                .create(AllIcons.Actions.Cancel)
                .setTarget(FilenameIndex.getFilesByName(project, "Main.java", GlobalSearchScope.projectScope(project))[0].findElementAt(2))
                .setTooltipText("Laufzeitkomplexität: O(1)")
                .createLineMarkerInfo(FilenameIndex.getFilesByName(project, "Main.java", GlobalSearchScope.projectScope(project))[0]).createGutterRenderer());*/
//HintManager.getInstance().showErrorHint(editor,"jaja", 5, 5, HintManager.DEFAULT, HintManager.HIDE_BY_TEXT_CHANGE, 10);