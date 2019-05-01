package de.thl.intelijinfer.config;

import de.thl.intelijinfer.ui.MainToolWindow;
import org.jetbrains.annotations.NotNull;

public class ConfigComponentImpl implements ConfigComponent{
    public void ConfigComponentImpl() {}

    @NotNull
    @Override
    public String getComponentName() {
        return "ConfigComponent";
    }

    @Override
    public void initComponent() {
        System.out.println("IDE Start");
    }

    @Override
    public void disposeComponent() {
        System.out.println("IDE Stop");
    }
}
