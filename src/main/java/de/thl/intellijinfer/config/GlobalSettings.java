package de.thl.intellijinfer.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Property;
import de.thl.intellijinfer.model.InferInstallation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@State(name = "InferApplicationSettings", storages = {@Storage("$APP_CONFIG$/infer.xml")})
public class GlobalSettings implements PersistentStateComponent<GlobalSettings> {
    //@com.intellij.util.xmlb.annotations.Transient damit nicht serialisiert wird

    @Property
    private List<InferInstallation> installations;

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

    public List<InferInstallation> getInstallations() {
        return installations;
    }

    public void setInstallations(List<InferInstallation> installations) {
        this.installations = installations;
    }
}
