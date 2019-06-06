package de.thl.intellijinfer.util;

import com.google.common.collect.ImmutableMap;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import de.thl.intellijinfer.service.IdeaHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BuildToolUtil {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.util.BuildToolUtil");

    private static final Map<String, Function<RunConfiguration, String>> supportedRunConfigurations = ImmutableMap.<String, Function<RunConfiguration, String>>builder()
            .put("Application", (rc) -> "-- javac " + IdeaHelper.getInstance(rc.getProject()).getAllJavaFiles())
            .put("MavenRunConfiguration", (rc) -> "-- mvn package")                                               //todo sicherstellen das maven installiert ist
            .put("GradleRunConfiguration", (rc) -> "-- ./gradlew build")
            .put("CMakeRunConfiguration", (rc) -> "--compilation-database cmake-build-debug/compile_commands.json") //todo ordner anpassen
            .build();

    public static List<RunConfiguration> filterUnknownRunConfigurations(List<RunConfiguration> rcList) {
        List<RunConfiguration> newRcList = new ArrayList<>();
        for(int i = 0; i < rcList.size(); i++) {
            if(supportedRunConfigurations.containsKey(rcList.get(i).getType().getId())) {
                newRcList.add(rcList.get(i));
            }
        }
        return newRcList;
    }

    public static String getBuildCmd(RunConfiguration rc) {
        if (rc == null) return null;

        if(supportedRunConfigurations.containsKey(rc.getType().getId())) {
           return supportedRunConfigurations.get(rc.getType().getId()).apply(rc);
        }

        log.error("Unknown Run Configuration Type: " + rc.getType().getId());
        return null;
    }
}
