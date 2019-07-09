package de.thl.intellijinfer.model.buildtool;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;

public class Gradle extends BuildTool {
    private static final Logger log = Logger.getInstance(Gradle.class);
    private final String name = "Gradle";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBuildCmd(Project project) {
        if(PluginManager.isPluginInstalled(PluginId.getId("org.jetbrains.idea.maven"))) {
            return "-- ./gradlew clean build";
        }
        return null;
    }

    @Override
    public boolean isUsable(Project project) {
        return PluginManager.isPluginInstalled(PluginId.getId("org.jetbrains.plugins.gradle"));
    }
}
