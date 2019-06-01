package de.thl.intellijinfer.service;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.ApplicationInfoProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

public class InferLauncher {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.service.InferLauncher");
    private Project project;
    private BuildTool buildtool;
    private boolean hasJavaSDK;

    public InferLauncher(Project project) {
        this.project = project;
        hasJavaSDK = ProjectRootManager.getInstance(project).getProjectSdk() != null;
    }

    public static InferLauncher getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, InferLauncher.class);
    }

    enum BuildTool{
        JAVAC, CLANG, MAVEN, GRADLE, MAKE, CMAKE
    }

    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(project.getBasePath()));

        this.buildtool = getBuildTool();

        String[] commands = {"sh", "-c", getBuildCmd(buildtool)};
        processBuilder.command(commands);

        try {
            if(buildtool.equals(BuildTool.CMAKE)) {
                ProcessBuilder processBuilder2 = new ProcessBuilder();
                processBuilder2.directory(new File(project.getBasePath() + "/cmake-build-debug"));
                processBuilder2.command("cmake -DCMAKE_EXPORT_COMPILE_COMMANDS=1 ..");
                Process p2 = processBuilder2.start();
                p2.waitFor();
            }
            Process p = processBuilder.start();
            p.waitFor();
            Messages.showMessageDialog(project, "Fertig", "Greeting", Messages.getInformationIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BuildTool getBuildTool() {
        Sdk sdk = ProjectRootManager.getInstance(project).getProjectSdk();

        if(hasJavaSDK && sdk.getSdkType().getName() == "JavaSDK") {
            if (FilenameIndex.getFilesByName(project, "pom.xml", GlobalSearchScope.projectScope(project)).length > 0) {
                return BuildTool.MAVEN;
            }
            else if (FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.projectScope(project)).length > 0) {
                return BuildTool.GRADLE;
            } else {
                return BuildTool.JAVAC;
            }
        }

        if (FilenameIndex.getFilesByName(project, "CMakeLists.txt", GlobalSearchScope.projectScope(project)).length > 0) {
            return BuildTool.CMAKE;
        } else if (FilenameIndex.getFilesByName(project, "makefile", GlobalSearchScope.projectScope(project)).length > 0) {
            return BuildTool.MAKE;
        } else {
            return BuildTool.CLANG;
        }

    }


    public String getBuildCmd(BuildTool bt) {
        StringBuilder sb = new StringBuilder();

        switch(bt) { //todo für javac, maven, gradle, cmake, make installation prüfen
            case JAVAC:
                sb.append("infer run -- javac -d out/infer ");
                sb.append(IdeaHelper.getInstance(project).getAllJavaFiles());
                break;
            case MAVEN:
                sb.append("infer run -- mvn package");
                break;
            case GRADLE:
                sb.append("infer run -- gradle build");
                break;
            case CLANG:
                sb.append("infer run -- clang -o ");
                sb.append(ClionHelper.getInstance(project).getAllCFiles());
                break;
            case CMAKE:
                sb.append("infer run --compilation-database cmake-build-debug/compile_commands.json");
                break;
            case MAKE:
                sb.append("infer run -- make");
                break;
            default:
                log.error("Unsupported Build Command");
                break;
        }
        System.out.println(sb.toString());
        log.info("Build Cmd: " + sb.toString());
        return sb.toString();
    }
}
