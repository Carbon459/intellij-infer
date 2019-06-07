package de.thl.intellijinfer.ui;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.components.fields.ExpandableTextField;
import de.thl.intellijinfer.run.Checker;
import de.thl.intellijinfer.util.BuildToolUtil;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.stream.Stream;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JComboBox inferInstallationComboBox;
    private JComboBox usingRunConfigComboBox;
    private ExpandableTextField additionalArgsTextField;

    public RunConfigurationEditor() {
    }

    @Override
    protected void resetEditorFrom(InferRunConfiguration inferRC) {
        reloadRunConfigComboBoxList(inferRC);
        additionalArgsTextField.setText(inferRC.getLaunchOptions().getAdditionalArgs());
    }

    @Override
    protected void applyEditorTo(InferRunConfiguration inferRC) throws ConfigurationException {
        inferRC.getLaunchOptions().setSelectedRunConfig((RunConfiguration) usingRunConfigComboBox.getSelectedItem());
        inferRC.getLaunchOptions().setAdditionalArgs(additionalArgsTextField.getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    private void reloadRunConfigComboBoxList(InferRunConfiguration inferRC) {
        List<RunConfiguration> runConfigList = RunManager.getInstance(inferRC.getProject()).getAllConfigurationsList();
        usingRunConfigComboBox.setModel(new DefaultComboBoxModel(BuildToolUtil.filterUnknownRunConfigurations(runConfigList).toArray()));
        if(inferRC.getLaunchOptions().getSelectedRunConfig() != null) usingRunConfigComboBox.setSelectedItem(inferRC.getLaunchOptions().getSelectedRunConfig());
    }

    private void getAllCheckers() {
        for(Checker checker : Checker.values()) {
            //todo.... Stream.of(...) vlt
        }
    }
}
