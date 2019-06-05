package de.thl.intellijinfer.run;

import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import de.thl.intellijinfer.service.IdeaHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BuildToolManager {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.run.BuildToolManager");
    private final static List<String> runConfigurationWhitelist = new LinkedList<>(Arrays.asList("Application", "GradleRunConfiguration", "MavenRunConfiguration", "CMakeRunConfiguration"));

    public static List<RunConfiguration> filterUnknownRunConfigurations(List<RunConfiguration> rcList) {
        List<RunConfiguration> newRcList = new LinkedList<>();
        newRcList.addAll(rcList);
        for(RunConfiguration rc : newRcList) {
            if(!runConfigurationWhitelist.contains(rc.getType().getId())) {
                newRcList.remove(rc);
            }
        }
        return newRcList;
    }

    public static String getRunCmd(RunConfiguration rc) {
        if (rc == null) return null;

        final ConfigurationType javaType = ConfigurationTypeUtil.findConfigurationType("ApplicationConfiguration");
        final ConfigurationType mavenType = ConfigurationTypeUtil.findConfigurationType("MavenRunConfiguration");
        final ConfigurationType gradleType = ConfigurationTypeUtil.findConfigurationType("GradleRunConfiguration");
        final ConfigurationType cmakeType = ConfigurationTypeUtil.findConfigurationType("CMakeRunConfiguration");

        if (javaType != null && rc.getType().getDisplayName().equals(javaType.getDisplayName())) {
            return "infer run -- javac " + IdeaHelper.getInstance(rc.getProject()).getAllJavaFiles();
        } else if (mavenType != null && rc.getType().getDisplayName().equals(mavenType.getDisplayName())) { //todo sicherstellen das maven installiert ist
            return "infer run -- mvn package";
        } else if (gradleType != null && rc.getType().getDisplayName().equals(gradleType.getDisplayName())) {
            return "infer run -- ./gradlew build";
        } else if (cmakeType != null && rc.getType().getDisplayName().equals(cmakeType.getDisplayName())) {
            return "infer run --compilation-database build/compile_commands.json";
        }
        log.error("Unknown Run Configuration Type");
        return null;
    }
}
