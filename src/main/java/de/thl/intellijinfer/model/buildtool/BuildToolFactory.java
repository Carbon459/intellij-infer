package de.thl.intellijinfer.model.buildtool;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildToolFactory {
    /**
     * List of all supported Build Tools.
     */
    private static final List<String> BUILD_TOOLS = Arrays.asList("JavaC", "Maven", "Gradle", "CMake");
    /**
     * A list of compilable File Extensions for the supported Languages
     */
    public static final List<String> FILE_EXTENSIONS = Arrays.asList(".c", ".cpp", ".m", ".h", ".java");

    @Nullable
    public static BuildTool createFromName(String name) {
        switch(name) {
            case "JavaC":
                return JavaC.getInstance();
            case "Maven":
                return Maven.getInstance();
            case "Gradle":
                return Gradle.getInstance();
            case "CMake":
                return CMake.getInstance();
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

    /**
     * Gets the preferred Build Tool for the Usage in a generated Run Configuration
     * Automatic Build Tools like Maven and Gradle are always preferred to JavaC
     * @return A preferred BuildTool
     */
    public static BuildTool getPreferredBuildTool(Project project) {
        BuildTool bt = null;
        if(PlatformUtils.isCLion()) bt = CMake.getInstance();
        else {
            if (FilenameIndex.getFilesByName(project, "pom.xml", GlobalSearchScope.projectScope(project)).length > 0) {
                bt = Maven.getInstance();
            }
            else if (FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.projectScope(project)).length > 0) {
                bt = Gradle.getInstance();
            }
            else {
                bt = JavaC.getInstance();
            }
        }
        if(bt != null && bt.isUsable(project)) return bt;
        return null;
    }
}
