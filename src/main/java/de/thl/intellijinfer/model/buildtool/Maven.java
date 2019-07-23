package de.thl.intellijinfer.model.buildtool;

import com.intellij.execution.ExecutionException;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class Maven extends BuildTool {
    private static final Logger log = Logger.getInstance(Maven.class);
    private final String name = "Maven";

    private static Maven instance;

    public static Maven getInstance() {
        if(instance == null) instance = new Maven();
        return instance;
    }

    private Maven() {}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBuildCmd(Project project) throws ExecutionException {
        if(isUsable(project)) {
            if(FilenameIndex.getFilesByName(project, "pom.xml", GlobalSearchScope.projectScope(project)).length == 0) throw new ExecutionException("Could not find the pom.xml file. Aborting.");

            final File mavenBinary = new File(PluginManager.getPlugin(PluginId.getId("org.jetbrains.idea.maven")).getPath(), "/lib/maven3/bin/mvn");

            //Make sure that the maven binary is executable from everywhere
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            try {
                Files.setPosixFilePermissions(mavenBinary.toPath(), perms);
            } catch(IOException ex) {
                log.warn("Could not change permission for the maven binary", ex);
            }

            return "-- " + mavenBinary.toString() + " clean package";
        }
        else return null;
    }

    @Override
    public boolean isUsable(Project project) {
        return PluginManager.isPluginInstalled(PluginId.getId("org.jetbrains.idea.maven"));
    }
}
