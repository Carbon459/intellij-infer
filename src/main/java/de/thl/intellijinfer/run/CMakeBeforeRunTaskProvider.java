package de.thl.intellijinfer.run;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemBeforeRunTask;
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemBeforeRunTaskProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CMakeBeforeRunTaskProvider extends ExternalSystemBeforeRunTaskProvider {
    public static final Key<ExternalSystemBeforeRunTask> ID = Key.create("Infer.BeforeRunTask");
    private static final ProjectSystemId SYSTEM_ID = new ProjectSystemId("INFER");

    public CMakeBeforeRunTaskProvider(Project project) {
        super(SYSTEM_ID, project, ID);
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/META-INF/pluginIcon.svg");
    }

    @Nullable
    @Override
    public Icon getTaskIcon(ExternalSystemBeforeRunTask task) {
        return IconLoader.getIcon("/META-INF/pluginIcon.svg");
    }

    @Nullable
    @Override
    public ExternalSystemBeforeRunTask createTask(@NotNull RunConfiguration runConfiguration) {
        return new ExternalSystemBeforeRunTask(ID, SYSTEM_ID);
    }
}
