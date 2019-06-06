package de.thl.intellijinfer.ui;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.components.fields.ExtendableTextField;
import de.thl.intellijinfer.util.BuildToolUtil;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JBLabel inferInstallationLabel;
    private JComboBox inferInstallationComboBox;
    private JBLabel argumentsLabel;
    private JBLabel usingRunConfigLabel;
    private JComboBox usingRunConfigComboBox;
    private ExpandableTextField additionalArgsTextField;

    public RunConfigurationEditor() {
    }

    @Override
    protected void resetEditorFrom(InferRunConfiguration inferRunConfiguration) {
        reloadRunConfigComboBoxList(inferRunConfiguration);
        this.additionalArgsTextField.setText(inferRunConfiguration.getAdditionalArgs());
    }

    @Override
    protected void applyEditorTo(InferRunConfiguration inferRunConfiguration) throws ConfigurationException {
        inferRunConfiguration.setSelectedRunConfig((RunConfiguration) usingRunConfigComboBox.getSelectedItem());
        inferRunConfiguration.setAdditionalArgs(this.additionalArgsTextField.getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    private void reloadRunConfigComboBoxList(InferRunConfiguration inferRunConfiguration) {
        List<RunConfiguration> runConfigList = RunManager.getInstance(inferRunConfiguration.getProject()).getAllConfigurationsList();
        usingRunConfigComboBox.setModel(new DefaultComboBoxModel(BuildToolUtil.filterUnknownRunConfigurations(runConfigList).toArray()));
        if(inferRunConfiguration.getSelectedRunConfig() != null) usingRunConfigComboBox.setSelectedItem(inferRunConfiguration.getSelectedRunConfig());
    }
}
