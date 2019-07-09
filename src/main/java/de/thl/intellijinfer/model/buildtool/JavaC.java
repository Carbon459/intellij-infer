package de.thl.intellijinfer.model.buildtool;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.PlatformUtils;
import com.intellij.util.indexing.FileBasedIndex;

import java.io.File;
import java.util.Collection;

public class JavaC extends BuildTool {
    private static final Logger log = Logger.getInstance(JavaC.class);
    private final String name = "JavaC";

    private static JavaC instance;

    public static JavaC getInstance() {
        if(instance == null) instance = new JavaC();
        return instance;
    }

    private JavaC() {}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBuildCmd(Project project) {
        StringBuilder sb = new StringBuilder("-- javac -d out/infer -cp \"");
        //Get all libraries
        Module[] modules = ModuleManager.getInstance(project).getModules();

        for (Module module : modules) {
            if(ModuleType.get(module).getId().equals("JAVA_MODULE")) {
                ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(library -> {
                    for(VirtualFile vf : library.getFiles(OrderRootType.CLASSES)) {
                        sb.append(vf.getCanonicalPath()).append(":");
                    }
                    return true;
                });
            }
        }
        sb.append("\"");

        //Get all classes
        final FileType javaFileType = FileTypeManager.getInstance().findFileTypeByName("JAVA");
        if(javaFileType != null) {
            Collection<VirtualFile> projectJavaFiles = FileBasedIndex.getInstance().getContainingFiles(
                    FileTypeIndex.NAME, javaFileType, GlobalSearchScope.projectScope(project));
            for (VirtualFile vf : projectJavaFiles) {
                sb.append(" ").append(vf.getCanonicalPath());
            }
        }

        //Create the directory for the compiled class files if it doesnt exist
        new File(project.getBasePath() + "/out/infer").mkdirs();

        return sb.toString();
    }

    @Override
    public boolean isUsable(Project project) {
        return PlatformUtils.isIntelliJ();
    }
}
