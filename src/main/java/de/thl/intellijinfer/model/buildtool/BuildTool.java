package de.thl.intellijinfer.model.buildtool;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.project.Project;

public abstract class BuildTool {
    /**
     * Gets the Name of the Build Tool
     * @return Build Tool Name
     */
    public abstract String getName();

    /**
     * Gets the Build Command for that Build Tool
     * @param project The project, where the Build Tool shall work
     * @return A Build Command
     */
    public abstract String getBuildCmd(Project project) throws ExecutionException;

    /**
     * Gets if the Build Tool is usable in the given project
     * @param project The given project
     * @return if the build tool is usable
     */
    public abstract boolean isUsable(Project project);

    @Override
    public String toString() {
        return this.getName();
    }
}
