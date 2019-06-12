package de.thl.intellijinfer.util;

import com.google.common.collect.ImmutableMap;
import com.intellij.execution.configurations.RunConfiguration;
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
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BuildToolUtil {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.util.BuildToolUtil");

    private static final Map<String, Function<RunConfiguration, String>> supportedRunConfigurations = ImmutableMap.<String, Function<RunConfiguration, String>>builder()
            .put("Application", (rc) -> "-- javac -d out/infer " + getJavaCArgs(rc.getProject()))
            .put("MavenRunConfiguration", (rc) -> "-- mvn package")                                               //todo sicherstellen das maven installiert ist
            .put("GradleRunConfiguration", (rc) -> "-- ./gradlew build")
            .put("CMakeRunConfiguration", (rc) -> "--compilation-database cmake-build-debug/compile_commands.json") //todo ordner anpassen
            .build();

    /**
     * Filters all unsupported Run Configurations from a List.
     * See {@link BuildToolUtil#supportedRunConfigurations} for the List of supported Run Configurations.
     * @param rcList The List of Run Configurations
     * @return A new filtered List of Run Configurations
     */
    public static List<RunConfiguration> filterUnknownRunConfigurations(List<RunConfiguration> rcList) {
        List<RunConfiguration> newRcList = new ArrayList<>();
        for(int i = 0; i < rcList.size(); i++) {
            if(supportedRunConfigurations.containsKey(rcList.get(i).getType().getId())) {
                newRcList.add(rcList.get(i));
            }
        }
        return newRcList;
    }

    /**
     * Constructs the Run Configuration specific part of an infer launch command (the compiler or build managment tool part)
     * @param rc The Run Configuration, for which the command should be constructed
     * @return The launch command with a leading "--". If the RunConfiguration is null or not supported, the return value is an empty string.
     */
    public static String getBuildCmd(RunConfiguration rc) {
        if (rc == null) return "";

        if(supportedRunConfigurations.containsKey(rc.getType().getId())) {
           return supportedRunConfigurations.get(rc.getType().getId()).apply(rc);
        }

        log.error("Unknown Run Configuration Type: " + rc.getType().getId());
        return "";
    }

    private static String getJavaCArgs(Project project) {
        StringBuilder sb = new StringBuilder("-cp \"");
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

        return sb.toString();
    }
}
