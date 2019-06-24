package de.thl.intellijinfer.model;

import java.io.Serializable;

public class InferInstallation implements Serializable {
    private String path = "/";
    private InferVersion version;
    private boolean confirmedWorking;

    public InferInstallation() {}

    public InferInstallation(String path, InferVersion version) {
        this.path = path;
        this.version = version;
    }

    /**
     * Checks if this Installation is valid and gets the version.
     * @return If the Installation is valid
     */
    public boolean confirm() {
        //todo confirm installation and get version



        return this.confirmedWorking;
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
}
