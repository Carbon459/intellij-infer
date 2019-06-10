package de.thl.intellijinfer.run;

import com.intellij.compiler.server.BuildManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import de.thl.intellijinfer.util.BuildToolUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class InferLaunchOptions {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.run.InferLaunchOptions");
    private final List<String> COMPILABLE_EXTENSIONS = Arrays.asList(".c", ".cpp", ".m", ".h", ".java");

    private RunConfiguration usingRunConfig;
    private String additionalArgs;
    private List<Checker> selectedCheckers;
    private Boolean reactiveMode;

    public InferLaunchOptions() {
        this.additionalArgs = "";
        this.selectedCheckers = Checker.getDefaultCheckers();
        this.reactiveMode = false;
    }

    public String buildInferLaunchCmd() {
        StringBuilder sb = new StringBuilder("infer run ");

        sb.append(additionalArgs).append(" ");
        for(Checker checker : selectedCheckers) {
            sb.append(checker.getActivationArgument()).append(" ");
        }
        for(Checker checker : Checker.getMissingCheckers(selectedCheckers)) {
            sb.append(checker.getDeactivationArgument()).append(" ");
        }

        if(this.reactiveMode) { //todo sicherstellen das es vorher eine komplette analyse gab
            List<String> changedFilesList = BuildManager.getInstance().getFilesChangedSinceLastCompilation(usingRunConfig.getProject());
            //only keep compilable files
            if(changedFilesList != null) {
                changedFilesList.removeIf((file) -> COMPILABLE_EXTENSIONS.stream().noneMatch(file::endsWith));

                System.out.println(Arrays.toString(changedFilesList.toArray()));
                if(!changedFilesList.isEmpty()) {
                    try {
                        Path file = Paths.get(this.usingRunConfig.getProject().getBasePath() + "/changedfiles.txt");
                        Files.write(file, changedFilesList, StandardCharsets.UTF_8);
                        sb.append("--reactive --changed-files-index changedfiles.txt ");
                    } catch (IOException ioe) {
                        log.error(ioe);
                    }
                }
            }
        }

        sb.append(BuildToolUtil.getBuildCmd(this.usingRunConfig));
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
}
