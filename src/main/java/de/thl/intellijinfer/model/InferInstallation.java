package de.thl.intellijinfer.model;

public class InferInstallation {
    private String path;
    private InferVersion version;

    public InferInstallation(String path, InferVersion version) {
        this.path = path;
        this.version = version;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

}
