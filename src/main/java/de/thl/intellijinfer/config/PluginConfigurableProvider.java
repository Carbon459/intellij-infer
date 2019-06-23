package de.thl.intellijinfer.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import org.jetbrains.annotations.Nullable;

public class PluginConfigurableProvider extends ConfigurableProvider {
    @Nullable
    @Override
    public Configurable createConfigurable() {
        return new PluginConfigurable(GlobalSettings.getInstance());
    }
}
