package de.thl.intellijinfer.service;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ClionHelper {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.service.ClionHelper");
    private Project project;

    public ClionHelper(Project project) {
        this.project = project;
    }

    public static ClionHelper getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ClionHelper.class);
    }

    public String getAllCFiles() {
        StringBuilder sb = new StringBuilder();

        Collection<VirtualFile> files = FilenameIndex.getAllFilesByExt(project, ".c");
        for (VirtualFile virtualFile : files) {
            sb.append(virtualFile.getCanonicalPath() + " ");
        }


        return sb.toString();
    }

}
