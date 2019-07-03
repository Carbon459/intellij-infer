package de.thl.intellijinfer.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.PlatformUtils;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.service.CLionHelper;
import de.thl.intellijinfer.ui.RunConfigurationEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InferRunConfiguration extends RunConfigurationBase {
    private static final Logger log = Logger.getInstance(InferRunConfiguration.class);
    private static final String PREFIX = "INTELLIJ_INFER-";
    private static final String SELECTED_RUN_CONFIG_NAME = PREFIX + "SELECTED_RUN_CONFIG_NAME";
    private static final String SELECTED_RUN_CONFIG_TYPE = PREFIX + "SELECTED_RUN_CONFIG_TYPE";
    private static final String ADDITIONAL_ARGUMENTS = PREFIX + "ADDITIONAL_ARGUMENTS";
    private static final String CHECKERS = PREFIX + "CHECKERS";
    private static final String REACTIVE_MODE = PREFIX + "REACTIVE_MODE";

    private InferLaunchOptions launchOptions;
    private Project project;

    private String selectedRunConfigName;
    private String selectedRunConfigType;



    InferRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        this.project = project;
        this.launchOptions = new InferLaunchOptions();

        //Make sure that the selected run configuration is shown as valid after loading the project, even if it is not run or changed (which would trigger the validation beforehand)
        new Thread(() -> {
            try {
                while(!project.isInitialized()) Thread.sleep(500);
                this.loadRunConfigInstance();
            } catch(InterruptedException ex) {log.warn("Thread Interrupted: Not Loading the selected run config automatically");}
        }).start();

        if(PlatformUtils.isCLion()) CLionHelper.getInstance(this.project).generateCompileCommands();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        loadRunConfigInstance();
        return new RunConfigurationEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if(launchOptions.getSelectedRunConfig() == null) throw new RuntimeConfigurationException("No Run Configuration Selected");
        if(launchOptions.getSelectedCheckers() == null || launchOptions.getSelectedCheckers().isEmpty()) throw new RuntimeConfigurationException("No Checker selected");
        if(launchOptions.getSelectedInstallation() == null || !launchOptions.getSelectedInstallation().isConfirmedWorking()) throw new RuntimeConfigurationException("No selected Installation or the Installation is invalid");
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        loadRunConfigInstance();
        return new InferRunState(this, executionEnvironment);
    }
    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);
        this.selectedRunConfigName = JDOMExternalizerUtil.readField(element, SELECTED_RUN_CONFIG_NAME);
        this.selectedRunConfigType = JDOMExternalizerUtil.readField(element, SELECTED_RUN_CONFIG_TYPE);
        this.launchOptions.setAdditionalArgs(JDOMExternalizerUtil.readField(element, ADDITIONAL_ARGUMENTS));
        this.launchOptions.setReactiveMode(Boolean.valueOf(JDOMExternalizerUtil.readField(element, REACTIVE_MODE)));

        final String checkerString = JDOMExternalizerUtil.readField(element, CHECKERS);
        if(checkerString != null) {
            List<Checker> newCheckerList = new ArrayList<>();
            Pattern p = Pattern.compile( "\\w+" );
            Matcher m = p.matcher(checkerString);
            while(m.find()) {
                newCheckerList.add(Checker.valueOf(m.group()));
            }
            this.launchOptions.setSelectedCheckers(newCheckerList);
        }
    }

    @Override
    public void writeExternal(@NotNull Element element) throws WriteExternalException {
        super.writeExternal(element);
        if(this.launchOptions.getSelectedRunConfig() != null) {
            JDOMExternalizerUtil.writeField(element, SELECTED_RUN_CONFIG_NAME, this.launchOptions.getSelectedRunConfig().getName());
            JDOMExternalizerUtil.writeField(element, SELECTED_RUN_CONFIG_TYPE, this.launchOptions.getSelectedRunConfig().getType().getDisplayName());
        }
        JDOMExternalizerUtil.writeField(element, ADDITIONAL_ARGUMENTS, this.launchOptions.getAdditionalArgs());
        JDOMExternalizerUtil.writeField(element, REACTIVE_MODE, this.launchOptions.isReactiveMode().toString());

        StringBuilder sb = new StringBuilder();
        for(Checker checker : this.launchOptions.getSelectedCheckers()) {
            sb.append(checker.getName()).append(" ");
        }
        JDOMExternalizerUtil.writeField(element, CHECKERS, sb.toString());
    }

    /**
     * Looks for the Instance of the Run Configuration we need and loads it.
     * Needs to be called at a later time than {@link #readExternal(Element)}, because not all Run Configurations are instantiated at that time.
     */
    private void loadRunConfigInstance() {
        if(launchOptions.getSelectedRunConfig() != null) return;
        for(RunConfiguration rc : RunManager.getInstance(project).getAllConfigurationsList()) {
            if(rc.getType().getDisplayName().equals(this.selectedRunConfigType) && rc.getName().equals(this.selectedRunConfigName)) {
                this.launchOptions.setSelectedRunConfig(rc);
            }
        }
        try {
            this.checkConfiguration();
        } catch(RuntimeConfigurationException ex) {
            log.warn("Selected Run Configuration Instance is invalid: " + ex.getMessage());
        }
    }

    @NotNull
    protected String getInferLaunchCmd() throws ExecutionException {
        return this.launchOptions.buildInferLaunchCmd();
    }

    public InferLaunchOptions getLaunchOptions() {
        return this.launchOptions;
    }

}
