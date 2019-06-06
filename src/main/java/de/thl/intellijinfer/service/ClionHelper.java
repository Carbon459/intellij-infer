package de.thl.intellijinfer.service;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.jetbrains.cidr.cpp.cmake.CMakeSettings;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClionHelper {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.service.ClionHelper");
    private Project project;

    public ClionHelper(Project project) {
        this.project = project;
    }

    public static ClionHelper getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ClionHelper.class);
    }

    public void generateCompileCommands() {
        List<CMakeSettings.Profile> list = new ArrayList<>(CMakeWorkspace.getInstance(project).getSettings().getProfiles());
        for(int i = 0; i < list.size(); i++) {
            String oldGenerationOptions = list.get(i).getGenerationOptions();
            if(oldGenerationOptions != null && oldGenerationOptions.contains("-DCMAKE_EXPORT_COMPILE_COMMANDS=1")) return;
            list.set(i, list.get(i).withGenerationOptions("-DCMAKE_EXPORT_COMPILE_COMMANDS=1 " + oldGenerationOptions));
        }
        CMakeWorkspace.getInstance(project).getSettings().setProfiles(list);
        CMakeWorkspace.getInstance(project).getSettings().setAutoReloadEnabled(true);
        //CMakeWorkspace.getInstance(project).scheduleReload(true);
    }

}
