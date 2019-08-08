package de.thl.intellijinfer.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class InferInstallation implements Serializable {
    private static final Logger log = Logger.getInstance(InferInstallation.class);
    private static final long PROCESS_TIMEOUT = 500; //How long the check waits before it declares the binary the process started from as invalid (in ms)

    private String path = "infer";
    private InferVersion version;
    private boolean confirmedWorking;
    private boolean defaultInstall = false;

    public InferInstallation() {}

    private InferInstallation(String path, boolean defaultInstall) {
        setPath(path);
        setDefaultInstall(defaultInstall);
    }


    /**
     * Creates a new Infer Installation and checks if its working.
     * @param path The path of the root directory of the infer installation.
     * @param isDefault if the installation which should be created is a default one
     * @return An infer installation, or null if its not valid/working.
     */
    @Nullable
    public static InferInstallation createInferInstallation(String path, boolean isDefault) {
        InferInstallation ii = new InferInstallation(path, isDefault);
        if(ii.confirm()) return ii;
        return null;
    }

    /**
     * Checks if this Installation is valid and gets the version.
     * @return true, if there is a valid infer installation in {@link #path}
     */
    private boolean confirm() {
        this.version = checkInfer(this.getPath());
        if(this.version != null) setConfirmedWorking(true);

        log.info(String.format("Confirmed Infer Installation : %b Version: %s Path: %s Default: %b", confirmedWorking, (this.version == null ? "null" : this.version.toString()), path, defaultInstall));

        return confirmedWorking;
    }

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

            reader.close();
            inferProcess.waitFor(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS);

            if (inferProcess.exitValue() == 0) {
                try {
                    return new Gson().fromJson(output.toString(), InferVersion.class);
                } catch(JsonSyntaxException ex) {
                    return null;
                }
            }
        } catch(IOException | IllegalThreadStateException ex) { //IllegalThreadStateException means the timeout elapsed without infer finishing returning the version
            return null;
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public String toString() {
        return (defaultInstall ? "[Default] " : "") + path + " " + "(" + version + ")";
    }
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        //make sure that the path is to a binary, not the directory of infer
        if(path.endsWith("infer") || path.endsWith(".bat") || path.endsWith(".sh")) this.path = path; //Exception: .bat and .sh endings are used for testing purposes, they shouldn't be changed
        else this.path = path + "/bin/infer";
    }

    public InferVersion getVersion() {
        return version;
    }
    public void setVersion(InferVersion version) {
        this.version = version;
    }
    public boolean isConfirmedWorking() {
        return confirmedWorking;
    }
    public void setConfirmedWorking(boolean confirmedWorking) {
        this.confirmedWorking = confirmedWorking;
    }
    public boolean isDefaultInstall() {
        return defaultInstall;
    }
    public void setDefaultInstall(boolean defaultInstall) {
        this.defaultInstall = defaultInstall;
    }
}
