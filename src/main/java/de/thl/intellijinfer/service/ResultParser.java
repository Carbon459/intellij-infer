package de.thl.intellijinfer.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.model.InferVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ResultParser {
    private static final Logger log = Logger.getInstance(ResultParser.class);
    private Project project;

    private Map<String, List<InferBug>> bugsPerFile;
    private PropertyChangeSupport changes = new PropertyChangeSupport( this );

    public ResultParser(Project project) {
        this.project = project;
    }

    public static ResultParser getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ResultParser.class);
    }

    /**
     * Parses a given infer result file. The result file has a default name of 'result.json'. The MainToolWindow is notified, so it can show the results.
     * @param resultPath The Path of the file in the json format
     * @return A Map of the rearranged Results. Mainly for testing purposes.
     */
    @Nullable
    public Map<String, List<InferBug>> parse(String resultPath) {
        try {
            rearrangeBugList(
                    readBugList(resultPath));
            return getBugsPerFile();
        } catch (IOException e) {
            log.error("Could not parse given result file", e);
        }
        return null;
    }

    /**
     * Reads a result.json from an Infer analysis and deserializes it into a list of InferBug objects
     * @param jsonPath The Path of the result.json
     * @return A list of InferBugs
     * @throws IOException If the json file couldnt be read
     */
    private List<InferBug> readBugList(String jsonPath) throws IOException {
        final String json = new String(Files.readAllBytes(Paths.get(jsonPath)));
        Type targetClassType = new TypeToken<ArrayList<InferBug>>(){}.getType();
        Collection<InferBug> targetCollection = new Gson().fromJson(json, targetClassType);
        return new ArrayList<>(targetCollection);
    }

    /**
     * Rearranges the buglist from the format infer delievers to a map with the filenames as keys and a list of bugs from that file as value
     * @param bugList The buglist, deserialized from the infer result.json
     */
    private void rearrangeBugList(List<InferBug> bugList) {
        Map<String, List<InferBug>> map = new HashMap<>();
        for(InferBug bug : bugList) {
            if(map.containsKey(bug.getFile())) {
                map.get(bug.getFile()).add(bug);
            }
            else {
                final List<InferBug> list = new ArrayList<>();
                list.add(bug);
                map.put(bug.getFile(), list);
            }
        }

        setBugsPerFile(map);
    }

    public Map<String, List<InferBug>> getBugsPerFile() {
        return bugsPerFile;
    }

    private void setBugsPerFile(Map<String, List<InferBug>> bugsPerFile) {
        Map<String, List<InferBug>> oldMap = this.bugsPerFile;
        this.bugsPerFile = bugsPerFile;
        changes.firePropertyChange("bugsPerFile", oldMap, bugsPerFile);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

}
