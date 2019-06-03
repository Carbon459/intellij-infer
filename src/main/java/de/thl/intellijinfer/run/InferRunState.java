package de.thl.intellijinfer.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.jetbrains.annotations.NotNull;

public class InferRunState extends CommandLineState {

    private ExecutionEnvironment ee;
    private InferRunConfiguration runCfg;

    protected InferRunState(InferRunConfiguration runCfg, ExecutionEnvironment environment) {
        super(environment);
        this.ee = environment;
        this.runCfg = runCfg;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        System.out.println("Running Process: " + runCfg.buildCmd);
        GeneralCommandLine commandLine = new GeneralCommandLine("/bin/sh", "-c", runCfg.buildCmd);

        ConfigurationTypeUtil.findConfigurationType("MavenRunConfiguration");
        ProcessHandler ph = new ColoredProcessHandler(commandLine);
        return ph;
    }


}
