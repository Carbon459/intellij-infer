package de.thl.intellijinfer.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.model.InferBug;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResultParser {
    private Project project;

    public ResultParser(Project project) {
        this.project = project;
    }

    public static ResultParser getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ResultParser.class);
    }

    public List<InferBug> getBugList(String jsonPath) throws IOException {
        final String json = new String(Files.readAllBytes(Paths.get(jsonPath)));
        Type targetClassType = new TypeToken<ArrayList<InferBug>>(){}.getType();
        Collection<InferBug> targetCollection = new Gson().fromJson(json, targetClassType);
        return new ArrayList<>(targetCollection);
    }
}
