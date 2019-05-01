package de.thl.intelijinfer.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ResultParser {
    private Project project;

    public ResultParser(Project project) {
        this.project = project;
    }

    public static ResultParser getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ResultParser.class);
    }
}
