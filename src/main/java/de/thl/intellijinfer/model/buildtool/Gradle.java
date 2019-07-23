package de.thl.intellijinfer.model.buildtool;

import com.intellij.execution.ExecutionException;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

public class Gradle extends BuildTool {
    private static final Logger log = Logger.getInstance(Gradle.class);
    private final String name = "Gradle";

    private static Gradle instance;

    public static Gradle getInstance() {
        if(instance == null) instance = new Gradle();
        return instance;
    }

    private Gradle() {}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBuildCmd(Project project) throws ExecutionException {
        if(isUsable(project)) {
            if(FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.projectScope(project)).length == 0) throw new ExecutionException("Could not find the build.gradle file. Aborting.");
            return "-- ./gradlew clean build";
        }
        return null;
    }

    @Override
    public boolean isUsable(Project project) {
        return PluginManager.isPluginInstalled(PluginId.getId("org.jetbrains.plugins.gradle"));
    }
}
