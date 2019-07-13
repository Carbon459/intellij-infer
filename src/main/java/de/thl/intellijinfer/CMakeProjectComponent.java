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
        //Starting to Generate the Compile Commands at start, because it takes a few more seconds sometimes and we don't want the user to wait when he launches an infer run config
        if(PlatformUtils.isCLion()) CMake.generateCompileCommands(project);
    }
}
