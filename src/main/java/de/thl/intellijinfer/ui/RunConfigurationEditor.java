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
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;
import de.thl.intellijinfer.model.buildtool.BuildTool;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.ResourceBundle;

public class RunConfigurationEditor extends SettingsEditor<InferRunConfiguration> {
    private JPanel mainPanel;
    private JComboBox<InferInstallation> inferInstallationComboBox;
    private JComboBox<BuildTool> usingBuildToolComboBox;
    private ExpandableTextField additionalArgsTextField;
    private JBCheckBox reactiveModeJBCheckBox;
    private JBPanel installPanel;
    private JPanel checkersJPanel;
    private JBList<Checker> checkersJBList;
    private CollectionListModel<Checker> checkersListModel;

    public RunConfigurationEditor() {
        this.installPanel.setLayout(new OverlayLayout(this.installPanel));

        //Reset the Checker list to default if installation was changed
        inferInstallationComboBox.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                checkersListModel.replaceAll(Checker.getDefaultCheckers());
            }
        });
    }

    @Override
    protected void resetEditorFrom(@NotNull InferRunConfiguration inferRC) {
        reloadBuildToolComboBoxList(inferRC);
        additionalArgsTextField.setText(inferRC.getLaunchOptions().getAdditionalArgs());
        this.checkersListModel.replaceAll(inferRC.getLaunchOptions().getSelectedCheckers());
        this.reactiveModeJBCheckBox.setSelected(inferRC.getLaunchOptions().isReactiveMode());
        reloadInstallationComboBox(inferRC);
    }

    @Override
    protected void applyEditorTo(@NotNull InferRunConfiguration inferRC) throws ConfigurationException {
        if(this.inferInstallationComboBox.isEnabled()) inferRC.getLaunchOptions().setSelectedInstallation((InferInstallation) this.inferInstallationComboBox.getSelectedItem());
        inferRC.getLaunchOptions().setUsingBuildTool((BuildTool) usingBuildToolComboBox.getSelectedItem());
        inferRC.getLaunchOptions().setAdditionalArgs(additionalArgsTextField.getText());
        inferRC.getLaunchOptions().setSelectedCheckers(this.checkersListModel.toList());
        inferRC.getLaunchOptions().setReactiveMode(this.reactiveModeJBCheckBox.isSelected());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    private void reloadBuildToolComboBoxList(InferRunConfiguration inferRC) {
        usingBuildToolComboBox.setModel(new DefaultComboBoxModel<>(
                inferRC.getLaunchOptions().getAvailableBuildTools().toArray(new BuildTool[0])
        ));

        if(inferRC.getLaunchOptions().getUsingBuildTool() != null) usingBuildToolComboBox.setSelectedItem(inferRC.getLaunchOptions().getUsingBuildTool());
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
        final List<Checker> notSelectedCheckers = Checker.getMissingCheckers(checkersListModel.getItems(), usingVersion);

        JBPopupFactory.getInstance()
                .createPopupChooserBuilder(notSelectedCheckers)
                .setTitle("Add Checker")
                .setItemChosenCallback((selectedChecker) -> checkersListModel.add(selectedChecker))
                .createPopup().show(button.getPreferredPopupPoint());

    }

    private void checkersRemoveAction(final AnActionButton button) {
        final List<Checker> selectedCheckers = checkersJBList.getSelectedValuesList();
        for(Checker checker : selectedCheckers) {
            checkersListModel.remove(checker);
        }
    }

    /**
     * Creates UI components, which cannot be created by the ui designer
     */
    private void createUIComponents() {
        this.checkersListModel = new CollectionListModel<>();
        this.checkersJBList = new JBList<>(this.checkersListModel);
        this.checkersJBList.setEmptyText(ResourceBundle.getBundle("strings").getString("no.checkers.selected"));

        ToolbarDecorator td = ToolbarDecorator
                .createDecorator(checkersJBList)
                .setAddAction(this::checkersAddAction)
                .setRemoveAction(this::checkersRemoveAction)
                .addExtraAction(new AnActionButton(ResourceBundle.getBundle("strings").getString("reset.to.default"), AllIcons.General.TodoDefault) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        checkersListModel.replaceAll(Checker.getDefaultCheckers());
                    }
                })
                .disableUpDownActions();
        checkersJPanel = td.createPanel();
    }
}
