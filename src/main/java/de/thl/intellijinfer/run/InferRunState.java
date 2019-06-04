package de.thl.intellijinfer.run;

import com.intellij.execution.BeforeRunTaskProvider;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class InferRunState extends CommandLineState {
    private static final Logger log = Logger.getInstance("#de.thl.intellijinfer.run.InferRunState");

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
        log.info("Running Infer with Command: " + runCfg.getRunCmd());
        System.out.println("Running Process: " + runCfg.getRunCmd());
        //todo before run task cmake stuff
        GeneralCommandLine commandLine = new GeneralCommandLine("/bin/sh", "-c", runCfg.getRunCmd());
        ProcessHandler ph = new ColoredProcessHandler(commandLine);
        return ph;
    }


}
