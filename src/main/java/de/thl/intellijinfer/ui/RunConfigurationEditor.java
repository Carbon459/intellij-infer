package de.thl.intellijinfer.ui;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import de.thl.intellijinfer.config.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JBLabel inferInstallationLabel;
    private JComboBox inferInstallationComboBox;
    private JBLabel argumentsLabel;
    private JBLabel workingDirectoryJBLabel;
    private JBLabel buildToolLabel;
    private JComboBox buildToolComboBox;
    private JBTextField inferArgumentsTextField;
    private TextFieldWithBrowseButton workingDirectoryChooser;

    @Override
    protected void resetEditorFrom(InferRunConfiguration demoRunConfiguration) {

    }

    @Override
    protected void applyEditorTo(InferRunConfiguration demoRunConfiguration) throws ConfigurationException {

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
