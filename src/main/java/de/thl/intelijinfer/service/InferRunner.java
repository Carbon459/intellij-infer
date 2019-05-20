package de.thl.intelijinfer.service;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

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
        //String[] commands = {"/bin/sh", "-c", "infer -- " + createJavacParams()};
        String[] commands = {"sh", "-c", "infer -- " + createJavacParams()};
        processBuilder.command(commands);
        try {
            Process p = processBuilder.start();
            p.waitFor();
            Messages.showMessageDialog(project, "Fertig", "Greeting", Messages.getInformationIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @NotNull
    private String createJavacParams() {
        StringBuilder sb = new StringBuilder("javac -d out/infer ");

        //Get all Class Filepaths
        Collection<VirtualFile> projectJavaFiles = FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        for (VirtualFile virtualFile : projectJavaFiles) {
            sb.append(virtualFile.getCanonicalPath() + " ");
        }

        System.out.println(sb.toString());
        return sb.toString();
    }
}
