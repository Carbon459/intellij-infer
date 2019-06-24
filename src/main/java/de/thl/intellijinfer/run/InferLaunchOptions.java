package de.thl.intellijinfer.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import de.thl.intellijinfer.model.BuildTool;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;
import de.thl.intellijinfer.service.FileChangeCollector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InferLaunchOptions {
    private static final Logger log = Logger.getInstance(InferLaunchOptions.class);

    private Boolean fullAnalysis; //if a full analysis was already done after loading the current project/creating the run config

    //Saved Infer Configuration Options
    private InferInstallation selectedInstallation;
    private RunConfiguration usingRunConfig;
    private String additionalArgs;
    private List<Checker> selectedCheckers;
    private Boolean reactiveMode;


    InferLaunchOptions() {
        this.selectedInstallation = new InferInstallation("infer", new InferVersion(0,16,0)); //todo nicht konstant
        this.additionalArgs = "";
        this.selectedCheckers = Checker.getDefaultCheckers();
        this.reactiveMode = false;
        this.fullAnalysis = false;
    }

    /**
     * Constructs the final infer launch command
     * @return Infer Launch Command
     */
    @NotNull
    public String buildInferLaunchCmd() throws ExecutionException {
        if(this.usingRunConfig == null) throw new ExecutionException("Infer Execution failed: No Run Configuration selected");
        StringBuilder sb = new StringBuilder(this.selectedInstallation.getPath());

        sb.append(" run ").append(additionalArgs).append(" ");
        for(Checker checker : selectedCheckers) {
            sb.append(checker.getActivationArgument()).append(" ");
        }
        for(Checker checker : Checker.getMissingCheckers(selectedCheckers)) {
            sb.append(checker.getDeactivationArgument()).append(" ");
        }

        if(this.reactiveMode && this.fullAnalysis) {
            if(!FileChangeCollector.changedFiles.isEmpty()) {
                try {
                    final Path file = Paths.get(this.usingRunConfig.getProject().getBasePath() + "/changedfiles.txt");
                    Files.write(file, FileChangeCollector.changedFiles, StandardCharsets.UTF_8);
                    sb.append("--reactive --changed-files-index changedfiles.txt ");
                } catch (IOException ioe) {
                    log.error(ioe);
                }
            }

        }
        this.fullAnalysis = true;
        FileChangeCollector.changedFiles.clear();

        sb.append(BuildTool.getBuildCmd(this.usingRunConfig));
        return sb.toString();
    }

    public RunConfiguration getSelectedRunConfig() {
        return usingRunConfig;
    }
    public void setSelectedRunConfig(RunConfiguration rc) {
        this.usingRunConfig = rc;
    }
    public String getAdditionalArgs() {
        return additionalArgs;
    }
    public void setAdditionalArgs(String additionalArgs) {
        this.additionalArgs = additionalArgs;
    }
    public List<Checker> getSelectedCheckers() {
        return selectedCheckers;
    }
    public void setSelectedCheckers(List<Checker> selectedCheckers) {
        this.selectedCheckers = selectedCheckers;
    }
    public Boolean isReactiveMode() {
        return reactiveMode;
    }
    public void setReactiveMode(Boolean reactiveMode) {
        this.reactiveMode = reactiveMode;
    }
    public InferInstallation getSelectedInstallation() {
        return selectedInstallation;
    }
    public void setSelectedInstallation(InferInstallation selectedInstallation) {
        this.selectedInstallation = selectedInstallation;
    }
}
