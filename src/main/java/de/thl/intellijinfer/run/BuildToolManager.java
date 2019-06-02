package de.thl.intellijinfer.run;

import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BuildToolManager {
    private final static List<String> runConfigurationWhitelist = new LinkedList<String>(Arrays.asList("Application", "GradleRunConfiguration", "Maven"));

    public static void filterUnknownRunConfigurations(List<RunConfiguration> rcList) {
        for(RunConfiguration rc : rcList) {
            if(!runConfigurationWhitelist.contains(rc.getType().getId())) {
                rcList.remove(rc);
            }
        }
    }

    public static String getRunCmd(RunConfiguration rc) {
        if(rc == null) return null;
        if(rc instanceof ApplicationConfiguration) {
            System.out.println(((ApplicationConfiguration)rc).getProgramParameters());
        }

        return null;
    }
}
