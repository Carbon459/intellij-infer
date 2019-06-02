package de.thl.intellijinfer.ui;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import de.thl.intellijinfer.run.BuildToolManager;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
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
        System.out.println("resetEditorFrom" + inferRunConfiguration.getId());
        List<RunConfiguration> runConfigList = RunManager.getInstance(inferRunConfiguration.getProject()).getAllConfigurationsList();
        System.out.println(runConfigList.size());
        BuildToolManager.filterUnknownRunConfigurations(runConfigList);
        usingRunConfigComboBox.setModel(new DefaultComboBoxModel(runConfigList.toArray()));
    }

    @Override
    protected void applyEditorTo(InferRunConfiguration inferRunConfiguration) throws ConfigurationException {
        final RunConfiguration selectedRc = (RunConfiguration) usingRunConfigComboBox.getSelectedItem();
        final String runCmd = BuildToolManager.getRunCmd(selectedRc);

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
