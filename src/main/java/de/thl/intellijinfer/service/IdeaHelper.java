package de.thl.intellijinfer.service;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class IdeaHelper {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.service.IdeaHelper");
    private Project project;

    public IdeaHelper(Project project) {
        this.project = project;
    }

    public static IdeaHelper getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, IdeaHelper.class);
    }

    public String getAllJavaFiles() {
        StringBuilder sb = new StringBuilder();
        //Get all Class Filepaths
        Collection<VirtualFile> projectJavaFiles = FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME, JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        for (VirtualFile virtualFile : projectJavaFiles) {
            sb.append(virtualFile.getCanonicalPath() + " ");
        }
        return sb.toString();
    }
}
