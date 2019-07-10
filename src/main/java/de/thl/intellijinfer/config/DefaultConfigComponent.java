package de.thl.intellijinfer.config;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.util.PlatformUtils;
import de.thl.intellijinfer.model.buildtool.CMake;
import org.jetbrains.annotations.NotNull;

public class DefaultConfigComponent implements BaseComponent {
    @NotNull
    @Override
    public String getComponentName() {
        return "ConfigComponent";
    }

    @Override
    public void initComponent() {
        System.out.println("--- IDE Start ---");
        GlobalSettings.getInstance().addInstallation("infer", true);
    }

    @Override
    public void disposeComponent() {
        System.out.println("--- IDE Stop ---");
    }
}
