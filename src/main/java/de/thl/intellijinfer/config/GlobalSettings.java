package de.thl.intellijinfer.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Property;
import de.thl.intellijinfer.model.InferInstallation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(name = "InferApplicationSettings", storages = {@Storage("$APP_CONFIG$/infer.xml")})
public class GlobalSettings implements PersistentStateComponent<GlobalSettings> {
    //@com.intellij.util.xmlb.annotations.Transient damit nicht serialisiert wird

    @Property
    private List<InferInstallation> installations = new ArrayList<>();


    public boolean addInstallation(String path, boolean isDefault) {
        //check if a default installation already exists
        if(isDefault && this.getInstallations().stream().anyMatch(InferInstallation::isDefaultInstall)) return false;

        InferInstallation ii = InferInstallation.createInferInstallation(path, isDefault);
        if(ii != null) {
            installations.add(ii);
            return true;
        }
        return false;
    }




    public static GlobalSettings getInstance() {
        return ApplicationManager.getApplication().getComponent(GlobalSettings.class);
    }

    @Nullable
    @Override
    public GlobalSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @NotNull
    public List<InferInstallation> getInstallations() {
        return installations;
    }

    public void setInstallations(List<InferInstallation> installations) {
        this.installations = installations;
    }
}
