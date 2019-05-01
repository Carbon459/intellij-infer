package de.thl.intelijinfer.service;

import com.intellij.openapi.components.ServiceManager;

public class Installer {
    private String workingDir = System.getProperty("java.io.tmpdir");

    public static Installer getInstance() {
        return ServiceManager.getService(Installer.class);
    }

    public boolean isInstalled() {
        return true;
    }
}
