package de.thl.intellijinfer.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ClionHelper {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.service.ClionHelper");
    private Project project;

    public ClionHelper(Project project) {
        this.project = project;
    }

    public static ClionHelper getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ClionHelper.class);
    }


}
