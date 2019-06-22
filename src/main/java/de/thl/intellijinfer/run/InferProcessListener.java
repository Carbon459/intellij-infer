package de.thl.intellijinfer.run;

import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import de.thl.intellijinfer.service.ResultParser;
import org.jetbrains.annotations.NotNull;

public class InferProcessListener implements ProcessListener {
    private static final Logger log = Logger.getInstance(InferProcessListener.class);
    private Project project;

    InferProcessListener(Project project) { this.project = project; }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        if(event.getExitCode() == 0) {
            log.info("Infer Process terminated successfully: Status Code 0");
            ResultParser.getInstance(project).parse(project.getBasePath() + "/infer-out/result.json");
        } else {
            log.error("Infer Process terminated unsuccessfully: Status Code " + event.getExitCode());
        }
    }

    @Override public void startNotified(@NotNull ProcessEvent event) { }
    @Override public void processWillTerminate(@NotNull ProcessEvent event, boolean willBeDestroyed) { }
    @Override public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) { }
}
