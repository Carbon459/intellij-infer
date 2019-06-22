package de.thl.intellijinfer.ui;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.fields.ExpandableTextField;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.util.BuildToolUtil;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JComboBox inferInstallationComboBox;
    private JComboBox<RunConfiguration> usingRunConfigComboBox;
    private ExpandableTextField additionalArgsTextField;
    private JBPanel checkersJBPanel;
    private JBCheckBox reactiveModeJBCheckBox;
    private JBList<Checker> checkersJBList;
    private CollectionListModel<Checker> clm;

    public RunConfigurationEditor() {
        this.clm = new CollectionListModel<>();
        this.checkersJBList = new JBList<>(this.clm);
        this.checkersJBList.setEmptyText("No Checkers selected");

        ToolbarDecorator td = ToolbarDecorator
                .createDecorator(checkersJBList)
                .setAddAction(this::checkersAddAction)
                .setRemoveAction(this::checkersRemoveAction)
                .addExtraAction(new AnActionButton("Reset to Default", AllIcons.General.TodoDefault) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        clm.replaceAll(Checker.getDefaultCheckers());
                    }
                })
                .disableUpDownActions();
        checkersJBPanel.add(td.createPanel(), BorderLayout.CENTER);
    }

    @Override
    protected void resetEditorFrom(@NotNull InferRunConfiguration inferRC) {
        reloadRunConfigComboBoxList(inferRC);
        additionalArgsTextField.setText(inferRC.getLaunchOptions().getAdditionalArgs());
        this.clm.replaceAll(inferRC.getLaunchOptions().getSelectedCheckers());
        this.reactiveModeJBCheckBox.setSelected(inferRC.getLaunchOptions().isReactiveMode());
    }

    @Override
    protected void applyEditorTo(InferRunConfiguration inferRC) throws ConfigurationException {
        inferRC.getLaunchOptions().setSelectedRunConfig((RunConfiguration) usingRunConfigComboBox.getSelectedItem());
        inferRC.getLaunchOptions().setAdditionalArgs(additionalArgsTextField.getText());
        inferRC.getLaunchOptions().setSelectedCheckers(this.clm.toList());
        inferRC.getLaunchOptions().setReactiveMode(this.reactiveModeJBCheckBox.isSelected());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    private void reloadRunConfigComboBoxList(InferRunConfiguration inferRC) {
        List<RunConfiguration> runConfigList = RunManager.getInstance(inferRC.getProject()).getAllConfigurationsList();
        usingRunConfigComboBox.setModel(
                new DefaultComboBoxModel<>(
                        BuildToolUtil.filterUnknownRunConfigurations(runConfigList).toArray(new RunConfiguration[0])
                ));
        if(inferRC.getLaunchOptions().getSelectedRunConfig() != null) usingRunConfigComboBox.setSelectedItem(inferRC.getLaunchOptions().getSelectedRunConfig());
    }

    private void checkersAddAction(final AnActionButton button) {
        final List<Checker> notSelectedCheckers = Checker.getMissingCheckers(this.clm.getItems());

        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(notSelectedCheckers)
                .setTitle("Add Checker")
                .setItemChosenCallback((selectedChecker) -> this.clm.add(selectedChecker))
                .createPopup().show(button.getPreferredPopupPoint());

    }

    private void checkersRemoveAction(final AnActionButton button) {
        final List<Checker> selectedCheckers = this.checkersJBList.getSelectedValuesList();
        for(Checker checker : selectedCheckers) {
            this.clm.remove(checker);
        }
    }
}
