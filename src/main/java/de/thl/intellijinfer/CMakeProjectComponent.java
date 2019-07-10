package de.thl.intellijinfer;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.PlatformUtils;
import de.thl.intellijinfer.model.buildtool.CMake;

public class CMakeProjectComponent implements ProjectComponent {
    private Project project;

    CMakeProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        if(PlatformUtils.isCLion()) CMake.generateCompileCommands(project);
    }
}
