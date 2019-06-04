package de.thl.intellijinfer.run;

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.RunConfigurationProducerService;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.Constants;
import de.thl.intellijinfer.ui.RunConfigurationEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InferRunConfiguration extends RunConfigurationBase {
    public static final String PREFIX = "INTELLIJ_INFER-";
    public static final String SELECTED_RUN_CONFIG = PREFIX + "SELECTED_RUN_CONFIG";

    private RunConfiguration selectedRunConfig;
    private String selectedRunConfigName;
    private Project project;

    protected InferRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        this.project = project;
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
        this.selectedRunConfigName = JDOMExternalizerUtil.readField(element, SELECTED_RUN_CONFIG);
    }

    @Override
    public void writeExternal(@NotNull Element element) throws WriteExternalException {
        super.writeExternal(element);
        if(this.getSelectedRunConfig() != null) JDOMExternalizerUtil.writeField(element, SELECTED_RUN_CONFIG, this.getSelectedRunConfig().getName());
    }

    public String getRunCmd() {
        return BuildToolManager.getRunCmd(selectedRunConfig);
    }

    public RunConfiguration getSelectedRunConfig() {
        return selectedRunConfig;
    }
    public void setSelectedRunConfig(RunConfiguration rc) {
        this.selectedRunConfig = rc;
    }
    //später als in readexternal ausgeführt da beim laden noch nicht alle run configs bestehen
    private void loadRunConfigInstance() {
        if(this.selectedRunConfig != null) return;
        for(RunConfiguration rc : RunManager.getInstance(project).getAllConfigurationsList()) {
            if(rc.getName().equals(this.selectedRunConfigName)) {
                this.setSelectedRunConfig(rc);
            }
        }
    }

}
