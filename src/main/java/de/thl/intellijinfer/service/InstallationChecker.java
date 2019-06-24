package de.thl.intellijinfer.service;

import com.google.gson.Gson;
import com.intellij.openapi.components.ServiceManager;
import de.thl.intellijinfer.model.InferVersion;

public class InstallationChecker {
    public static InstallationChecker getInstance() {
        return ServiceManager.getService(InstallationChecker.class);
    }

    public InferVersion checkInfer() {
        //infer --version-json
        String testJson = "{\n" +
                "\"major\": 0,\n" +
                "\"minor\": 16,\n" +
                "\"patch\": 0,\n" +
                "\"commit\": \"4a91616\",\n" +
                "\"branch\": \"HEAD\",\n" +
                "\"tag\": \"v0.16.0\"\n" +
                "}";

        InferVersion version = new Gson().fromJson(testJson, InferVersion.class);



        return version;
    }
}
