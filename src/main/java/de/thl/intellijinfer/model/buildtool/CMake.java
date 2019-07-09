package de.thl.intellijinfer.model.buildtool;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;

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
}
