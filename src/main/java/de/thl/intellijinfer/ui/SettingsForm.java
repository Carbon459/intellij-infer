package de.thl.intellijinfer.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.model.InferInstallation;

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
    private static final Logger log = Logger.getInstance(SettingsForm.class);

    private JPanel mainPanel;
    private TextFieldWithBrowseButton pathChooser;
    private JBLabel getInferHereJBLabel;
    private JButton addAndCheckInstallationButton;
    private JBCheckBox showInferConsoleJBCheckBox;
    private JBPanel validInstallationsPanel;
    private JPanel listWithToolBox;

    private JBList<InferInstallation> installationJBList;

    /**
     * The IDE shows a reset button while this is true
     */
    private boolean modified = false;

    public SettingsForm() {
        getInferHereJBLabel.setForeground(new JBColor(0x0645AD, 0x0652FF));

        //CLicked on the File Chooser Icon by the text box
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

        //Clicked on the get infer link
        getInferHereJBLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Desktop.getDesktop().browse(URI.create(ResourceBundle.getBundle("strings").getString("infer.getting.started.url")));
                } catch (IOException ex) {
                    log.warn("Couldn't open Infer getting started page in default browser");
                }
            }
        });

        //Clicked Add Installation Button
        addAndCheckInstallationButton.addActionListener(e -> {
            final boolean success = GlobalSettings.getInstance().addInstallation(pathChooser.getText(), false);
            if(success) refreshInstallationList();
            else showAddInstallationError();
        });

        showInferConsoleJBCheckBox.addActionListener(e -> modified = true);
    }

    private void showAddInstallationError() {
        final Color oldBg = pathChooser.getBackground();
        pathChooser.setBackground(JBColor.red);
        addAndCheckInstallationButton.setEnabled(false);
        pathChooser.setText(ResourceBundle.getBundle("strings").getString("invalid.installation.selected"));
        Timer timer = new Timer(3000, actionEvent -> {
            pathChooser.setBackground(oldBg);
            addAndCheckInstallationButton.setEnabled(true);
            pathChooser.setText("");
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void refreshInstallationList() {
        ((DefaultListModel<InferInstallation>) this.installationJBList.getModel()).clear();
        for(InferInstallation ii : GlobalSettings.getInstance().getInstallations()) {
            ((DefaultListModel<InferInstallation>) this.installationJBList.getModel()).addElement(ii);
        }
    }

    /**
     * Creates UI components, which cannot be created by the ui designer
     */
    private void createUIComponents() {
        installationJBList = new JBList<>();

        ToolbarDecorator td = ToolbarDecorator
                .createDecorator(installationJBList)
                .disableAddAction()
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
}
