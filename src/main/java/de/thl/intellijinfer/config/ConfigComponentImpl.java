package de.thl.intellijinfer.config;

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
        System.out.println("--- IDE Start ---");
    }

    @Override
    public void disposeComponent() {
        System.out.println("--- IDE Stop ---");
    }
}
