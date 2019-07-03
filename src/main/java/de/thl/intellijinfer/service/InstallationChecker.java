package de.thl.intellijinfer.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.components.ServiceManager;
import de.thl.intellijinfer.model.InferVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class InstallationChecker {
    public static InstallationChecker getInstance() {
        return ServiceManager.getService(InstallationChecker.class);
    }

    private static final long PROCESS_TIMEOUT = 500; //How long the check waits before it declares the binary the process started from as invalid (in ms)

    /**
     * Checks if the Infer Installation at the given path is valid.
     * @param path Full path to the infer binary
     * @return The Version if the installation is valid, otherwise null
     */
    @Nullable
    public InferVersion checkInfer(@NotNull String path) {
        try {
            Process inferProcess = new ProcessBuilder(path , "--version-json").start();
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inferProcess.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            inferProcess.waitFor(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS);

            if (inferProcess.exitValue() == 0) {
                return parseVersionJson(output.toString());
            }
        } catch(IOException | IllegalThreadStateException ex) { //IllegalThreadStateException means the timeout elapsed without infer finishing returning the version
            return null;
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Nullable
    public InferVersion parseVersionJson(@NotNull String json) {
        try {
            return new Gson().fromJson(json, InferVersion.class);
        } catch(JsonSyntaxException ex) {
            return null;
        }
    }
}
