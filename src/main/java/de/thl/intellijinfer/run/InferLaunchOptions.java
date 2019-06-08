package de.thl.intellijinfer.run;

import com.intellij.execution.configurations.RunConfiguration;
import de.thl.intellijinfer.util.BuildToolUtil;

import java.util.List;

public class InferLaunchOptions {
    private RunConfiguration usingRunConfig;
    private String additionalArgs;
    private List<Checker> selectedCheckers;

    public InferLaunchOptions() {
        this.additionalArgs = "";
        this.selectedCheckers = Checker.getDefaultCheckers();
    }

    public String buildInferLaunchCmd() {
        StringBuilder sb = new StringBuilder("infer run ");
        sb.append(additionalArgs).append(" ");
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


}
