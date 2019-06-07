package de.thl.intellijinfer.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.PlatformUtils;
import de.thl.intellijinfer.service.CLionHelper;
import de.thl.intellijinfer.ui.RunConfigurationEditor;
import de.thl.intellijinfer.util.BuildToolUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InferRunConfiguration extends RunConfigurationBase {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.run.InferRunConfiguration");
    public static final String PREFIX = "INTELLIJ_INFER-";
    public static final String SELECTED_RUN_CONFIG_NAME = PREFIX + "SELECTED_RUN_CONFIG_NAME";
    public static final String SELECTED_RUN_CONFIG_TYPE = PREFIX + "SELECTED_RUN_CONFIG_TYPE";
    public static final String ADDITIONAL_ARGUMENTS = PREFIX + "ADDITIONAL_ARGUMENTS";

    private String selectedRunConfigName;
    private String selectedRunConfigType;

    private String additionalArgs;
    private RunConfiguration selectedRunConfig;

    private Project project;

    protected InferRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        this.project = project;

        //Make sure that the selected run configuration is shown as valid after loading the project, even if it is not run or changed (which would trigger the loading beforehand)
        new Thread(() -> {
            try {
                while(!project.isInitialized()) Thread.sleep(500);
                this.loadRunConfigInstance();
            } catch(InterruptedException ex) {log.warn("Thread Interrupted: Not Loading the selected run config automatically");}
        }).start();

        if(PlatformUtils.isCLion()) CLionHelper.getInstance(this.project).generateCompileCommands();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        loadRunConfigInstance();
        return new RunConfigurationEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if(selectedRunConfig == null) throw new RuntimeConfigurationException("No Run Configuration Selected");
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        loadRunConfigInstance();
        return new InferRunState(this, executionEnvironment);
    }
    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);
        this.selectedRunConfigName = JDOMExternalizerUtil.readField(element, SELECTED_RUN_CONFIG_NAME);
        this.selectedRunConfigType = JDOMExternalizerUtil.readField(element, SELECTED_RUN_CONFIG_TYPE);
        this.additionalArgs = JDOMExternalizerUtil.readField(element, ADDITIONAL_ARGUMENTS);
    }

    @Override
    public void writeExternal(@NotNull Element element) throws WriteExternalException {
        super.writeExternal(element);
        if(this.getSelectedRunConfig() != null) {
            JDOMExternalizerUtil.writeField(element, SELECTED_RUN_CONFIG_NAME, this.getSelectedRunConfig().getName());
            JDOMExternalizerUtil.writeField(element, SELECTED_RUN_CONFIG_TYPE, this.getSelectedRunConfig().getType().getDisplayName());
        }
        JDOMExternalizerUtil.writeField(element, ADDITIONAL_ARGUMENTS, this.additionalArgs);
    }

    //später als in readexternal ausgeführt da beim laden noch nicht alle run configs bestehen. name ist pro type einmalig
    private void loadRunConfigInstance() {
        if(this.selectedRunConfig != null) return;
        for(RunConfiguration rc : RunManager.getInstance(project).getAllConfigurationsList()) {
            if(rc.getType().getDisplayName().equals(this.selectedRunConfigType) && rc.getName().equals(this.selectedRunConfigName)) {
                this.setSelectedRunConfig(rc);
            }
        }
        try {
            this.checkConfiguration();
        } catch(RuntimeConfigurationException ex) {
            log.warn("Selected Run Configuration Instance is invalid");
        }
    }

    public String getBuildCmd() {
        return BuildToolUtil.getBuildCmd(selectedRunConfig);
    }

    public RunConfiguration getSelectedRunConfig() {
        return selectedRunConfig;
    }
    public void setSelectedRunConfig(RunConfiguration rc) {
        this.selectedRunConfig = rc;
    }
    public String getAdditionalArgs() {
        return additionalArgs;
    }
    public void setAdditionalArgs(String additionalArgs) {
        this.additionalArgs = additionalArgs;
    }


}
