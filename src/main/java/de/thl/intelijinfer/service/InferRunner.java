package de.thl.intelijinfer.service;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class InferRunner {
    private Project project;

    public InferRunner(Project project) {
        this.project = project;
    }

    public static InferRunner getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, InferRunner.class);
    }

    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(project.getBasePath()));
        processBuilder.command("infer -- javac -d bin " + project.getBasePath() + "/src/*.java");
        try {
            Process p = processBuilder.start();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
