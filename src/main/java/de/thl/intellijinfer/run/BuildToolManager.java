package de.thl.intellijinfer.run;

import com.intellij.execution.configurations.RunConfiguration;

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
}
