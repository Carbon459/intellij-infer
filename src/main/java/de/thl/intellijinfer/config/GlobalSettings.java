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
    @Property
    private boolean showConsole = false;


    /**
     * Adds a InferInstallation to the global list, which is used by run configurations.
     * @param path The path of the installation
     * @param isDefault if the installation is a default installation
     * @return true, if the installation was added successfully, otherwise false
     */
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

    /**
     * Removes the given installation from the global list.
     * @param ii the given installation
     */
    public void removeInstallation(InferInstallation ii) {
        installations.remove(ii);
    }

    /**
     * Gets the default Installation
     * @return Default Installation. Returns null when there is none.
     */
    @Nullable
    public InferInstallation getDefaultInstallation() {
        return this.getInstallations().stream().filter(InferInstallation::isDefaultInstall).findFirst().orElse(null);
    }

    /**
     * Returns if at least one valid Installation exists
     * @return If a Installation exists, which is confirmed working
     */
    public boolean hasValidInstallation() {
        return this.getInstallations().stream().anyMatch(InferInstallation::isConfirmedWorking);
    }

    /**
     * Gets a valid Infer Installation
     * @return A valid Infer Installation, or null if there is none
     */
    @Nullable
    public InferInstallation getAnyValidInstallation() {
        return this.getInstallations().stream().filter(InferInstallation::isConfirmedWorking).findAny().orElse(null);
    }

    /**
     * Returns the Installation at the given path
     * @param path The given path
     * @return A Installation if it exists at that path, otherwise null
     */
    @Nullable
    public InferInstallation getInstallationFromPath(String path) {
        return this.getInstallations().stream().filter((x) -> x.getPath().equals(path)).findFirst().orElse(null);
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
    public boolean isShowConsole() {
        return showConsole;
    }
    public void setShowConsole(boolean showConsole) {
        this.showConsole = showConsole;
    }
}
