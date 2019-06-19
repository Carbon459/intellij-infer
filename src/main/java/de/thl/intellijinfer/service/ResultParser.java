package de.thl.intellijinfer.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.ui.MainToolWindow;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ResultParser {
    private Project project;

    public MainToolWindow mtw; //todo ändern!!!!!

    public ResultParser(Project project) {
        this.project = project;
    }

    public static ResultParser getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ResultParser.class);
    }

    /**
     * Reads a result.json from an Infer analysis and deserializes it into a list of InferBug objects
     * @param jsonPath The Path of the result.json
     * @return A list of InferBugs
     * @throws IOException If the json file couldnt be read
     */
    public List<InferBug> readBugList(String jsonPath) throws IOException {
        final String json = new String(Files.readAllBytes(Paths.get(jsonPath)));
        Type targetClassType = new TypeToken<ArrayList<InferBug>>(){}.getType();
        Collection<InferBug> targetCollection = new Gson().fromJson(json, targetClassType);
        return new ArrayList<>(targetCollection);
    }

    public Map<String, List<InferBug>> getBugsPerFile(List<InferBug> bugList) {
        Map<String, List<InferBug>> map = new HashMap<>();
        for(InferBug bug : bugList) {
            if(map.containsKey(bug.getFile())) {
                map.get(bug.getFile()).add(bug);
            }
            else {
                final List<InferBug> list = new ArrayList<InferBug>();
                list.add(bug);
                map.put(bug.getFile(), list);
            }
        }

        return map;
    }

}
