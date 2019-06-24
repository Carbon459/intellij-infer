package de.thl.intellijinfer.model;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public enum BuildTool {

    JAVAC("Application", BuildTool::getJavaCmd),
    MAVEN("MavenRunConfiguration", BuildTool::getMavenCmd),
    GRADLE("GradleRunConfiguration", BuildTool::getGradleCmd),
    CMAKE("CMakeRunConfiguration", BuildTool::getCmakeCmd);

    public static final List<String> COMPILABLE_EXTENSIONS = Arrays.asList(".c", ".cpp", ".m", ".h", ".java");

    private String typeId;
    private Function<RunConfiguration, String> launchCmd;

    private static final Logger log = Logger.getInstance(BuildTool.class);

    BuildTool(String typeId, Function<RunConfiguration, String> launchCmd) {
        this.typeId = typeId;
        this.launchCmd = launchCmd;
    }

    /**
     * Constructs the Run Configuration specific part of an infer launch command (the compiler or build managment tool part)
     * @param rc The Run Configuration, for which the command should be constructed
     * @return The launch command with a leading "--". If the RunConfiguration is null or not supported, the return value is an empty string.
     */
    @Nullable
    public static String getBuildCmd(RunConfiguration rc) {
        if(rc == null) return null;
        for(BuildTool bt: BuildTool.values()) {
            if(bt.typeId.equals(rc.getType().getId())) return bt.launchCmd.apply(rc);
        }
        return null;
    }

    /**
     * Filters all unsupported Run Configurations from a List.
     * See {@link BuildTool} for the List of supported Run Configurations.
     * @param rcList The List of Run Configurations
     * @return A new filtered List of Run Configurations
     */
    public static List<RunConfiguration> filterUnknownRunConfigurations(List<RunConfiguration> rcList) {
        List<RunConfiguration> newRcList = new ArrayList<>();
        for (RunConfiguration rc : rcList) {
            for(BuildTool bt: BuildTool.values()) {
                if(rc.getType().getId().equals(bt.typeId)) newRcList.add(rc);
            }
        }
        return newRcList;
    }


    private static String getJavaCmd(RunConfiguration rc) {
        final Project project = rc.getProject();
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

        return sb.toString();
    }

    private static String getMavenCmd(RunConfiguration rc) {
        if(PluginManager.isPluginInstalled(PluginId.getId("org.jetbrains.idea.maven")))
            return "-- " +
                    (new File(PluginManager.getPlugin(PluginId.getId("org.jetbrains.idea.maven")).getPath(), "/lib/maven3/bin/mvn").toString())
                    + " package";
        else return "-- mvn package";
    }

    private static String getGradleCmd(RunConfiguration rc) {
        return "-- ./gradlew build";
    }

    private static String getCmakeCmd(RunConfiguration rc) {
        return "--compilation-database cmake-build-debug/compile_commands.json";
    }
}
