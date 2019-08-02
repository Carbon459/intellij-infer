package de.thl.intellijinfer.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class InferConfigurationType implements ConfigurationType {
    @Override
    @NotNull
    public String getDisplayName() {
        return "Infer";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Analyze the sourcecode using the Infer static analyzer";
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/META-INF/pluginIcon.svg");
    }

    @NotNull
    @Override
    public String getId() {
        return "InferRunConfiguration";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new InferConfigurationFactory(this)};
    }
}
