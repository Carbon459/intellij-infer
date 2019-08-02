package de.thl.intellijinfer.run;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.project.Project;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.model.buildtool.BuildToolFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InferConfigurationFactory extends ConfigurationFactory {
    private static final String FACTORY_NAME = "Infer configuration factory";

    InferConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    @NotNull
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new InferRunConfiguration(project, this, "Infer");
    }

    @Override
    @NotNull
    public String getName() {
        return FACTORY_NAME;
    }

    /**
     * Creates a working Infer Run Configuration
     * @param runManager The RunManager of the project where it should be generated
     * @param name The name of the run configuration
     * @return A valid Run Configuration, or null if it could not be created
     */
    @Nullable
    public static RunnerAndConfigurationSettings createValidConfiguration(RunManagerImpl runManager, String name) {
        if(!GlobalSettings.getInstance().hasValidInstallation()) return null;
        final ConfigurationFactory inferFactory = runManager.getFactory("InferRunConfiguration", "Infer configuration factory");
        if(inferFactory != null) {
            RunnerAndConfigurationSettings rcs = runManager.createConfiguration(name, inferFactory);

            InferRunConfiguration inferRC = (InferRunConfiguration) rcs.getConfiguration();
            inferRC.getLaunchOptions().setUsingBuildTool(BuildToolFactory.getPreferredBuildTool(runManager.getProject()));
            inferRC.getLaunchOptions().setSelectedInstallation(GlobalSettings.getInstance().getAnyValidInstallation());

            return rcs;
        }
        return null;
    }
}
