package de.thl.intellijinfer.ui;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.*;
import com.intellij.ui.components.fields.ExpandableTextField;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.config.PluginConfigurable;
import de.thl.intellijinfer.model.BuildTool;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JComboBox<InferInstallation> inferInstallationComboBox;
    private JComboBox<RunConfiguration> usingRunConfigComboBox;
    private ExpandableTextField additionalArgsTextField;
    private JBPanel checkersJBPanel;
    private JBCheckBox reactiveModeJBCheckBox;
    private JBPanel installPanel;
    private JBList<Checker> checkersJBList;
    private CollectionListModel<Checker> clm;

    public RunConfigurationEditor() {
        this.clm = new CollectionListModel<>();
        this.checkersJBList = new JBList<>(this.clm);
        this.checkersJBList.setEmptyText(ResourceBundle.getBundle("strings").getString("no.checkers.selected"));
        this.installPanel.setLayout(new OverlayLayout(this.installPanel));

        ToolbarDecorator td = ToolbarDecorator
                .createDecorator(checkersJBList)
                .setAddAction(this::checkersAddAction)
                .setRemoveAction(this::checkersRemoveAction)
                .addExtraAction(new AnActionButton(ResourceBundle.getBundle("strings").getString("reset.to.default"), AllIcons.General.TodoDefault) {
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
        reloadInstallationComboBox(inferRC);
    }

    @Override
    protected void applyEditorTo(@NotNull InferRunConfiguration inferRC) throws ConfigurationException {
        if(this.inferInstallationComboBox.isEnabled()) inferRC.getLaunchOptions().setSelectedInstallation((InferInstallation) this.inferInstallationComboBox.getSelectedItem());
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
                        BuildTool.filterUnknownRunConfigurations(runConfigList).toArray(new RunConfiguration[0])
                ));
        if(inferRC.getLaunchOptions().getSelectedRunConfig() != null) usingRunConfigComboBox.setSelectedItem(inferRC.getLaunchOptions().getSelectedRunConfig());
    }

    private void reloadInstallationComboBox(InferRunConfiguration inferRC) {
        inferInstallationComboBox.setModel(
                new DefaultComboBoxModel<>(
                        GlobalSettings.getInstance().getInstallations().toArray(new InferInstallation[0])
                ));
        inferInstallationComboBox.setEnabled(true);
        inferInstallationComboBox.setVisible(true);

        if(inferInstallationComboBox.getItemCount() > 0 && inferRC.getLaunchOptions().getSelectedInstallation() != null) {
            inferInstallationComboBox.setSelectedItem(inferRC.getLaunchOptions().getSelectedInstallation());
        }
        //Show the clickable warning if no Installation is configured
        if(inferInstallationComboBox.getItemCount() == 0) {
            //create the warning label only if it doesnt exist (= only one other component in the installPanel)
            if(installPanel.getComponentCount() == 1) {
                final JBLabel warningLabel = new JBLabel(ResourceBundle.getBundle("strings").getString("warning.no.valid.installation.found.click.here.to.add.one"));
                warningLabel.setForeground(new JBColor(0x0764FF, 0x0652FF));
                warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
                warningLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        ShowSettingsUtil.getInstance().showSettingsDialog(inferRC.getProject(), PluginConfigurable.class);
                    }
                });
                installPanel.add(warningLabel);
                installPanel.revalidate();
            }
            inferInstallationComboBox.setVisible(false);
            inferInstallationComboBox.setEnabled(false);

            //this.installPane.repaint();
        }
    }

    private void checkersAddAction(final AnActionButton button) {
        final InferVersion usingVersion = ((InferInstallation) inferInstallationComboBox.getSelectedItem()).getVersion().isValid() ?
                ((InferInstallation) inferInstallationComboBox.getSelectedItem()).getVersion() :
                new InferVersion(0, 15, 0); //fallback to oldest supported version if no valid version is selected
        final List<Checker> notSelectedCheckers = Checker.getMissingCheckers(clm.getItems(), usingVersion);

        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(notSelectedCheckers)
                .setTitle("Add Checker")
                .setItemChosenCallback((selectedChecker) -> clm.add(selectedChecker))
                .createPopup().show(button.getPreferredPopupPoint());

    }

    private void checkersRemoveAction(final AnActionButton button) {
        final List<Checker> selectedCheckers = checkersJBList.getSelectedValuesList();
        for(Checker checker : selectedCheckers) {
            clm.remove(checker);
        }
    }
}
