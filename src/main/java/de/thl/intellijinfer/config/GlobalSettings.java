package de.thl.intellijinfer.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Property;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@State(name = "InferApplicationSettings", storages = {@Storage("$APP_CONFIG$/infer.xml")})
public class GlobalSettings implements PersistentStateComponent<GlobalSettings> {
    //@com.intellij.util.xmlb.annotations.Transient damit nicht serialisiert wird

    @Property
    private List<InferInstallation> installations = new LinkedList<>(Arrays.asList(new InferInstallation("test", new InferVersion(0,1,2)), new InferInstallation("test2", new InferVersion(0,2,3))));


    public boolean addInstallation(String path) {
        InferInstallation ii = new InferInstallation(path, new InferVersion(0,0,0));
        if(ii.confirm()) {
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
