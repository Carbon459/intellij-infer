package de.thl.intellijinfer.run;

import com.intellij.execution.JavaExecutionUtil;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.ShortenCommandLine;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.JavaApplicationRunConfigurationImporter;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration;
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
        if (rc == null) return null;

        final ConfigurationType mavenType = ConfigurationTypeUtil.findConfigurationType("MavenRunConfiguration");
        final ConfigurationType gradleType = ConfigurationTypeUtil.findConfigurationType("GradleRunConfiguration");

        if (rc instanceof ApplicationConfiguration) {
            System.out.println(((ApplicationConfiguration) rc));
            return "infer run -- javac ...";
        } else if (mavenType != null && rc.getType().getDisplayName().equals(mavenType.getDisplayName())) {
            return "infer run -- mvn package";
        } else if (gradleType != null && rc.getType().getDisplayName().equals(gradleType.getDisplayName())) {
            return "infer run -- gradle build";
        } /*else if (rc instanceof CMakeAppRunConfiguration) {
            return "..."; //TODO Cmake
        }*/
        return null;
    }
}
