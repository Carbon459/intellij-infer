package de.thl.intellijinfer.run;

import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.util.PlatformUtils;
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration;
import de.thl.intellijinfer.service.IdeaHelper;
import org.jetbrains.idea.maven.execution.MavenRunConfiguration;
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BuildToolManager {
    private final static List<String> runConfigurationWhitelist = new LinkedList<>(Arrays.asList("Application", "GradleRunConfiguration", "MavenRunConfiguration"));

    public static void filterUnknownRunConfigurations(List<RunConfiguration> rcList) {
        for(RunConfiguration rc : rcList) {
            if(!runConfigurationWhitelist.contains(rc.getType().getId())) {
                rcList.remove(rc);
            }
        }
    }

    public static String getRunCmd(RunConfiguration rc) {
        System.out.println(rc.getType().getDisplayName());
        if (rc == null) return null;

        final ConfigurationType javaType = ConfigurationTypeUtil.findConfigurationType("ApplicationConfiguration");
        final ConfigurationType mavenType = ConfigurationTypeUtil.findConfigurationType("MavenRunConfiguration");
        final ConfigurationType gradleType = ConfigurationTypeUtil.findConfigurationType("GradleRunConfiguration");
        final ConfigurationType cmakeType = ConfigurationTypeUtil.findConfigurationType("CMakeAppRunConfiguration");

        if (javaType != null && rc.getType().getDisplayName().equals(javaType.getDisplayName())) {
            return "infer run -- javac " + IdeaHelper.getInstance(rc.getProject()).getAllJavaFiles();
        } else if (mavenType != null && rc.getType().getDisplayName().equals(mavenType.getDisplayName())) {
            return "infer run -- mvn package";
        } else if (gradleType != null && rc.getType().getDisplayName().equals(gradleType.getDisplayName())) {
            return "infer run -- gradle build";
        } else if (cmakeType != null && rc.getType().getDisplayName().equals(cmakeType.getDisplayName())) {
            System.out.println("CMAKEAPPRUNCONFIGURATION FOUND");
            return "infer run --compilation-database build/compile_commands.json";
        }
        PlatformUtils.isCLion(); //todo
        return null;
    }
}
