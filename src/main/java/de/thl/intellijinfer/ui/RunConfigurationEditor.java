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
    private JBLabel workingDirectoryJBLabel;
    private JBLabel usingRunConfigLabel;
    private JComboBox usingRunConfigComboBox;
    private JBTextField inferArgumentsTextField;
    private TextFieldWithBrowseButton workingDirectoryChooser;

    @Override
    protected void resetEditorFrom(InferRunConfiguration demoRunConfiguration) {
        List<RunConfiguration> runConfigList = RunManager.getInstance(demoRunConfiguration.getProject()).getAllConfigurationsList();
        BuildToolManager.filterUnknownRunConfigurations(runConfigList);
        usingRunConfigComboBox.setModel(new DefaultComboBoxModel(runConfigList.toArray()));

    }

    @Override
    protected void applyEditorTo(InferRunConfiguration demoRunConfiguration) throws ConfigurationException {
        final RunConfiguration selectedRc = (RunConfiguration) usingRunConfigComboBox.getSelectedItem();
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
