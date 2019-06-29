package de.thl.intellijinfer.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class InferInstallation implements Serializable {
    private String path = "/";
    private InferVersion version;
    private boolean confirmedWorking;
    private boolean defaultInstall = false;

    /**
     * Dont use this Constructor! For deserializing use only.
     */
    public InferInstallation() {}

    private InferInstallation(String path, boolean defaultInstall) {
        this.path = path;
        this.defaultInstall = defaultInstall;
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
     * @return If the Installation is valid
     */
    private boolean confirm() {
        //todo confirm installation and get version
        this.version = new InferVersion(0,0,0);
        setConfirmedWorking(true);

        return true;
        //return this.confirmedWorking;
    }

    public String toString() {
        return path + " " + version;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
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
