package de.thl.intellijinfer.model.buildtool;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class BuildTool {
    /**
     * A list of compilable File Extensions for the supported Languages
     */
    public static final List<String> FILE_EXTENSIONS = Arrays.asList(".c", ".cpp", ".m", ".h", ".java");

    /**
     * Gets the Name of the Build Tool
     * @return Build Tool Name
     */
    @NotNull
    public abstract String getName();

    /**
     * Gets the Build Command for that Build Tool
     * @param project The project, where the Build Tool shall work
     * @return A Build Command
     */
    @Nullable
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
