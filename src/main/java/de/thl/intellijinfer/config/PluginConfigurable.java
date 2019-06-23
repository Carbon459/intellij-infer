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

    /**
     * Creates new Swing form that enables user to configure the settings.
     * Usually this method is called on the EDT, so it should not take a long time.
     * <p>
     * Also this place is designed to allocate resources (subscriptions/listeners etc.)
     *
     * @return new Swing form to show, or {@code null} if it cannot be created
     * @see #disposeUIResources
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        if(this.form == null) this.form = new SettingsForm();
        return this.form.getMainPanel();
    }

    /**
     * Indicates whether the Swing form was modified or not.
     * This method is called very often, so it should not take a long time.
     *
     * @return {@code true} if the settings were modified, {@code false} otherwise
     */
    @Override
    public boolean isModified() {
        return false;
    }

    /**
     * Stores the settings from the Swing form to the configurable component.
     * This method is called on EDT upon user's request.
     *
     * @throws ConfigurationException if values cannot be applied
     */
    @Override
    public void apply() throws ConfigurationException {

    }
}
