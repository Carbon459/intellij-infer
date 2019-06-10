package de.thl.intellijinfer.service;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import org.jetbrains.annotations.NotNull;

public class FileChangeCollector implements  FileDocumentManagerListener{
    public FileChangeCollector() {}

    public void beforeDocumentSaving(@NotNull Document document) {
        //System.out.println(document);
    }
}
