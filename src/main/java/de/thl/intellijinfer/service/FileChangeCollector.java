package de.thl.intellijinfer.service;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import de.thl.intellijinfer.model.BuildTool;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//info:  List<String> changedFilesList = BuildManager.getInstance().getFilesChangedSinceLastCompilation(usingRunConfig.getProject()); gibt es, aber ungewünschtes verhalten (reset nur bei echter kompilierung)
public class FileChangeCollector implements  FileDocumentManagerListener{
    public static List<String> changedFiles = new ArrayList<>();

    /**
     * Listener for Document saving events. Collects all changed compilable files into a list.
     * @param document the document, which is about to be saved
     */
    public void beforeDocumentSaving(@NotNull Document document) {
        Pattern r = Pattern.compile("(?<=DocumentImpl\\[file://).*?(?=\\])"); //Matches everything between "DocumentImpl[file://" and "]"
        Matcher m = r.matcher(document.toString());

        if (m.find()) {
            if(m.group(0) != null && BuildTool.COMPILABLE_EXTENSIONS.stream().anyMatch((ext) -> m.group(0).endsWith(ext))) {
                changedFiles.add(m.group(0));
            }
        }
    }

}
