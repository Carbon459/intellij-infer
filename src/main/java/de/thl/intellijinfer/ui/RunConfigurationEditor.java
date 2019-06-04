package de.thl.intellijinfer.ui;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.task.impl.ExecutionEnvironmentProviderImpl;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import de.thl.intellijinfer.run.BuildToolManager;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JBLabel inferInstallationLabel;
    private JComboBox inferInstallationComboBox;
    private JBLabel argumentsLabel;
    private JBLabel usingRunConfigLabel;
    private JComboBox usingRunConfigComboBox;
    private JBTextField inferArgumentsTextField;

    public RunConfigurationEditor() {

    }

    @Override
    protected void resetEditorFrom(InferRunConfiguration inferRunConfiguration) {
        List<RunConfiguration> runConfigList = RunManager.getInstance(inferRunConfiguration.getProject()).getAllConfigurationsList();
        BuildToolManager.filterUnknownRunConfigurations(runConfigList);
        usingRunConfigComboBox.setModel(new DefaultComboBoxModel(runConfigList.toArray()));
        if(inferRunConfiguration.getSelectedRunConfig() != null) usingRunConfigComboBox.setSelectedItem(inferRunConfiguration.getSelectedRunConfig());
    }

    @Override
    protected void applyEditorTo(InferRunConfiguration inferRunConfiguration) throws ConfigurationException {
        final RunConfiguration selectedRc = (RunConfiguration) usingRunConfigComboBox.getSelectedItem();
        inferRunConfiguration.setSelectedRunConfig(selectedRc);
        //ExecutionEnvironment ex = ExecutionEnvironmentBuilder.create(DefaultRunExecutor.getRunExecutorInstance(), selectedRc).build();
        //System.out.println(ex);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
