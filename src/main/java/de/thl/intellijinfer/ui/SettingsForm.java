package de.thl.intellijinfer.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.GridBag;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.model.InferInstallation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ResourceBundle;

public class SettingsForm {
    private JPanel mainPanel;
    private TextFieldWithBrowseButton pathChooser;
    private JBLabel getInferHereJBLabel;
    private JButton addAndCheckInstallationButton;
    private JBCheckBox showInferConsoleJBCheckBox;
    private JBPanel validInstallationsPanel;
    private JPanel listWithToolBox;

    private JBList<InferInstallation> installationJBList;

    private boolean modified = false;

    public SettingsForm() {
        getInferHereJBLabel.setForeground(new JBColor(0x0645AD, 0x0652FF));

        pathChooser.addActionListener(e -> FileChooser.chooseFile(
                FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                ProjectUtil.guessCurrentProject(mainPanel),
                LocalFileSystem.getInstance().findFileByPath(pathChooser.getText().isEmpty() ? "/" : pathChooser.getText()), //where the file chooser starts
                (dir) -> pathChooser.setText(dir.getPath())));

        pathChooser.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {modified = true;}
            @Override public void removeUpdate(DocumentEvent e) {modified = true;}
            @Override public void changedUpdate(DocumentEvent e) {modified = true;}
        });

        getInferHereJBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Desktop.getDesktop().browse(URI.create(ResourceBundle.getBundle("strings").getString("infer.getting.started.url")));
                } catch (IOException ex) {}
            }
        });

        addAndCheckInstallationButton.addActionListener(e -> {
            GlobalSettings.getInstance().addInstallation(pathChooser.getText(), false);
            refreshInstallationList();
        });

        showInferConsoleJBCheckBox.addActionListener(e -> modified = true);
    }

    private void refreshInstallationList() {
        ((DefaultListModel<InferInstallation>) this.installationJBList.getModel()).clear();
        for(InferInstallation ii : GlobalSettings.getInstance().getInstallations()) {
            ((DefaultListModel<InferInstallation>) this.installationJBList.getModel()).addElement(ii);
        }
    }

    private void showAddResult(boolean success) {
        if(success) {
            addAndCheckInstallationButton.setIcon(AllIcons.RunConfigurations.TestPassed);
        } else {
            addAndCheckInstallationButton.setIcon(AllIcons.RunConfigurations.TestFailed);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public boolean isModified() {
        return modified;
    }
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    public String getPath() {
        return this.pathChooser.getText();
    }
    public void setPath(String path) {
        this.pathChooser.setText(path);
    }
    public boolean isShowConsole() {
        return showInferConsoleJBCheckBox.isSelected();
    }
    public void setShowConsole(boolean selected) {
        showInferConsoleJBCheckBox.setSelected(selected);
    }

    /**
     * Creates UI components, which cannot be created by the ui designer
     */
    private void createUIComponents() {
        installationJBList = new JBList<>();

        ToolbarDecorator td = ToolbarDecorator
                .createDecorator(installationJBList)
                /*.setAddAction((e) -> {
                    GlobalSettings.getInstance().addInstallation(pathChooser.getText(), false);
                    refreshInstallationList();
                })*/.disableAddAction()
                .setRemoveAction((e) -> {
                    GlobalSettings.getInstance().removeInstallation(installationJBList.getSelectedValue());
                    refreshInstallationList();
                })
                .disableUpDownActions();
        listWithToolBox = td.createPanel();

        installationJBList.getEmptyText().setText(ResourceBundle.getBundle("strings").getString("no.installation.to.show"));
        installationJBList.setModel(new DefaultListModel<>());
        refreshInstallationList();
    }
}
