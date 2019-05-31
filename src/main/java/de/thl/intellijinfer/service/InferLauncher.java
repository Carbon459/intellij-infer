package de.thl.intelijinfer.service;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

public class InferLauncher {
    private Project project;

    private static final Logger log = Logger.getInstance("#de.thl.intelij");

    public InferLauncher(Project project) {
        this.project = project;
    }

    public static InferLauncher getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, InferLauncher.class);
    }

    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(project.getBasePath()));

        //String[] commands = {"/bin/sh", "-c", "infer -- " + createJavacParams()};
        String[] commands = {"sh", "-c", "infer -- " + getBuildCmd()};
        processBuilder.command(commands);
        try {
            Process p = processBuilder.start();
            p.waitFor();
            Messages.showMessageDialog(project, "Fertig", "Greeting", Messages.getInformationIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getBuildCmd() {
        String buildTool = "Java"; //TODO buildtool auslesen
        StringBuilder sb = new StringBuilder();

        switch(buildTool) {
            case "Java":
                sb.append("javac -d out/infer ");

                //Get all Class Filepaths
                Collection<VirtualFile> projectJavaFiles = FileBasedIndex.getInstance().getContainingFiles(
                        FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
                for (VirtualFile virtualFile : projectJavaFiles) {
                    sb.append(virtualFile.getCanonicalPath() + " ");
                }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
