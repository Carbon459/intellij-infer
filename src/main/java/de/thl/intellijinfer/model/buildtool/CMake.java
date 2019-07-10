package de.thl.intellijinfer.model.buildtool;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;
import com.jetbrains.cidr.cpp.cmake.CMakeSettings;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;

import java.util.ArrayList;
import java.util.List;

public class CMake extends BuildTool {
    private static final Logger log = Logger.getInstance(CMake.class);
    private final String name = "CMake";

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
    public String getBuildCmd(Project project) { //todo unterscheiden debug/run
        return "--compilation-database cmake-build-debug/compile_commands.json";
    }

    @Override
    public boolean isUsable(Project project) {
        return PlatformUtils.isCLion();
    }


    /**
     * Generates the compile_commands.json for use with Infer.
     * @param project The project, which Infer is going to analyze
     */
    public void generateCompileCommands(Project project) {
        List<CMakeSettings.Profile> list = new ArrayList<>(CMakeWorkspace.getInstance(project).getSettings().getProfiles());
        for(int i = 0; i < list.size(); i++) {
            final String oldGenerationOptions = list.get(i).getGenerationOptions();
            if(oldGenerationOptions != null && oldGenerationOptions.contains("-DCMAKE_EXPORT_COMPILE_COMMANDS=1")) return;
            list.set(i, list.get(i).withGenerationOptions("-DCMAKE_EXPORT_COMPILE_COMMANDS=1 " + oldGenerationOptions));
        }
        CMakeWorkspace.getInstance(project).getSettings().setProfiles(list);
        CMakeWorkspace.getInstance(project).getSettings().setAutoReloadEnabled(true);
        CMakeWorkspace.getInstance(project).scheduleReload(true);
    }
}
