package de.thl.intellijinfer.model.buildtool;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildToolFactory {
    public static final List<String> FILE_EXTENSIONS = Arrays.asList(".c", ".cpp", ".m", ".h", ".java");
    private static final List<String> BUILD_TOOLS = Arrays.asList("JavaC", "Maven", "Gradle", "CMake");

    @Nullable
    public static BuildTool createFromName(String name) {
        switch(name) {
            case "JavaC":
                return new JavaC();
            case "Maven":
                return new Maven();
            case "Gradle":
                return new Gradle();
            case "CMake":
                return new CMake();
            default:
                return null;
        }
    }

    /**
     * Gets all Build Tools, which can be used in this project
     * @param project The project
     * @return A list of BuildTools, which can be used
     */
    public static List<BuildTool> getApplicableBuildTools(Project project) {
        List<BuildTool> buildTools = new ArrayList<>();

        for(String s : BUILD_TOOLS) {
            final BuildTool bt = createFromName(s);
            if(bt != null && bt.isUsable(project)) buildTools.add(bt);
        }

        return buildTools;
    }
}
