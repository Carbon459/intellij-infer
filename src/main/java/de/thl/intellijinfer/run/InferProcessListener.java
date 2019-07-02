package de.thl.intellijinfer.run;

import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindowManager;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.service.ResultParser;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InferProcessListener implements ProcessListener {
    private static final Logger log = Logger.getInstance(InferProcessListener.class);
    private Project project;

    InferProcessListener(Project project) { this.project = project; }

    /**
     * Gets called when a Infer Process is terminated
     * @param event Contains information about the terminated process
     */
    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        //todo mehr fehler abhandeln und in eventlog schreiben
        if(event.getExitCode() == 0) {
            log.info("Infer Process terminated successfully: Status Code 0");

            final Path path = Paths.get(project.getBasePath() + "/infer-out/report.json");
            if(Files.exists(path))
                ResultParser.getInstance(project).parse(path);
            else log.error("report.json does not exist after Infer terminated successfully: Check the Infer log");

            //Open the Infer Tool Window, which needs to be done in an AWT Event Dispatcher Thread
            if(!GlobalSettings.getInstance().isShowConsole()) { //if the user wants the console to stay in focus, we dont want to open the infer tool window
                ApplicationManager.getApplication().invokeLater(() -> ToolWindowManager.getInstance(project).getToolWindow("Infer").activate(null, false), ModalityState.any());
            }
        } else {
            log.warn("Infer Process terminated unsuccessfully: Status Code " + event.getExitCode());
            Notifications.Bus.notify(new Notification("Infer", "Failure", "Infer terminated unsuccessfully", NotificationType.ERROR));
        }
    }

    @Override public void startNotified(@NotNull ProcessEvent event) { }
    @Override public void processWillTerminate(@NotNull ProcessEvent event, boolean willBeDestroyed) { }
    @Override public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) { }
}
