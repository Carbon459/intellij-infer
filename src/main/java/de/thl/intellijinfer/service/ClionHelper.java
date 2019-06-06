package de.thl.intellijinfer.service;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemBeforeRunTask;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration;
import com.jetbrains.cidr.cpp.execution.CMakeBeforeRunTaskProviderMixin;
import com.jetbrains.cidr.cpp.execution.CMakeBuildBeforeRunTaskProvider;
import com.jetbrains.cidr.cpp.execution.external.run.CLionExternalBuildBeforeRunTask;
import com.jetbrains.cidr.cpp.execution.external.run.CLionExternalBuildBeforeRunTaskProvider;
import de.thl.intellijinfer.run.CMakeBeforeRunTaskProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ClionHelper {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.service.ClionHelper");
    private Project project;

    public ClionHelper(Project project) {
        this.project = project;
    }

    public static ClionHelper getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ClionHelper.class);
    }

    public void createCMakeBeforeRunTask(RunConfiguration inferRc, RunConfiguration cmakeRc) {
        CMakeAppRunConfiguration clonedRc = (CMakeAppRunConfiguration) cmakeRc.clone();
        clonedRc.setProgramParameters("-DCMAKE_EXPORT_COMPILE_COMMANDS=1");
        BeforeRunTask task = new CMakeBeforeRunTaskProvider(this.project).createTask(cmakeRc);
        List<BeforeRunTask> beforeRunTasks = RunManagerEx.getInstanceEx(this.project).getBeforeRunTasks(inferRc);
        beforeRunTasks.add(task);
        RunManagerEx.getInstanceEx(this.project).setBeforeRunTasks(inferRc, beforeRunTasks, true);
    }

    public void runCMake(CMakeAppRunConfiguration cmake) throws ExecutionException {
        final String oldParams = cmake.getProgramParameters();
        cmake.setProgramParameters(oldParams + " -DCMAKE_EXPORT_COMPILE_COMMANDS=1");
        ExecutionEnvironmentBuilder.create(project, DefaultRunExecutor.getRunExecutorInstance(), cmake).buildAndExecute();
        cmake.setProgramParameters(oldParams);
    }


}
