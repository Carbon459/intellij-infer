package de.thl.intellijinfer.model.buildtool;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;
import com.jetbrains.cidr.cpp.cmake.CMakeSettings;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeProfileInfo;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CMake extends BuildTool {
    private static final Logger log = Logger.getInstance(CMake.class);
    private final String name = "CMake";

    private static final String COMPILE_COMMAND_FILE = "compile_commands.json";
    private static final String EXPORT_COMMAND_FILE_ARG = "-DCMAKE_EXPORT_COMPILE_COMMANDS=1";

    private static CMake instance;

    public static CMake getInstance() {
        if(instance == null) instance = new CMake();
        return instance;
    }

    private CMake() {}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBuildCmd(Project project) throws ExecutionException {
        generateCompileCommands(project);

        final CMakeWorkspace workspace = CMakeWorkspace.getInstance(project);

        if(workspace.getProfileInfos().isEmpty()) throw new ExecutionException("Could not create CMake Build Command: No CMake Profile available");

        //comparator for comparing when the compile command file of a cmakeprofile was last modified
        final Comparator<CMakeProfileInfo> comparator = Comparator.comparingLong((y) ->
            new File(getGenerationDir(y, project), COMPILE_COMMAND_FILE).lastModified()
        );

        //getting generation dir where the compile_commands.json from CMake is going to be generated to
        final CMakeProfileInfo cpi = workspace.getProfileInfos().stream()
                .filter((x) -> containsCompileCommands(getGenerationDir(x, project))) //filter out all profiles without generated compile commands
                .max(comparator) //get the profile with the last modified compile commands
                .orElse(null);

        if(cpi == null) throw new ExecutionException("No CMake Profile with generated compile commands file");

        return String.format("--compilation-database %s/%s", getGenerationDir(cpi, project), COMPILE_COMMAND_FILE);
    }

    @Override
    public boolean isUsable(Project project) {
        return PlatformUtils.isCLion();
    }


    /**
     * Generates the compile_commands.json for use with Infer.
     * @param project The project, which Infer is going to analyze
     */
    public static void generateCompileCommands(Project project) {
        final CMakeWorkspace workspace = CMakeWorkspace.getInstance(project);

        List<CMakeSettings.Profile> list = new ArrayList<>(workspace.getSettings().getProfiles());

        for(int i = 0; i < list.size(); i++) {
            final String oldGenerationOptions = list.get(i).getGenerationOptions();
            if(oldGenerationOptions != null && oldGenerationOptions.contains(EXPORT_COMMAND_FILE_ARG)) return;
            list.set(i, list.get(i).withGenerationOptions(EXPORT_COMMAND_FILE_ARG + " " + (oldGenerationOptions == null ? "" : oldGenerationOptions)));
        }
        workspace.getSettings().setProfiles(list);
        workspace.getSettings().setAutoReloadEnabled(true);
        workspace.scheduleReload(true);
    }

    private String getGenerationDir(CMakeProfileInfo cpi, Project project) {
        if(cpi.getProfile().getGenerationDir() == null) {
            //default dir
            return String.format("%s/cmake-build-%s", project.getBasePath(), cpi.getProfile().getName().toLowerCase());
        } else {
            //custom dir set by user
            return  project.getBasePath() + "/" + cpi.getProfile().getGenerationDir().getName();
        }
    }

    private boolean containsCompileCommands(String generationDir) {
        return new File(generationDir, COMPILE_COMMAND_FILE).exists();
    }
}
