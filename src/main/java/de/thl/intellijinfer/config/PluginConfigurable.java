package de.thl.intellijinfer.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import de.thl.intellijinfer.ui.SettingsForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginConfigurable implements Configurable {
    private GlobalSettings settings;
    private SettingsForm form;

    PluginConfigurable(GlobalSettings settings) {
        this.settings = settings;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Infer";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if(this.form == null) {
            this.form = new SettingsForm();
            reset();
        }
        return this.form.getMainPanel();
    }

    @Override
    public boolean isModified() {
        if(this.form != null) return this.form.isModified();
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        if(this.form == null) return;
        this.form.setModified(false);

        this.settings.setShowConsole(this.form.isShowConsole());

        /*final File inferDir = new File(this.form.getPath());
        if(!inferDir.exists()) throw new ConfigurationException("Directory does not exist");
        if(!inferDir.isDirectory()) throw new ConfigurationException("Given path is not a directory");*/


    }

    @Override
    public void reset() {
        this.form.setShowConsole(settings.isShowConsole());
        this.form.setModified(false);
    }
}
