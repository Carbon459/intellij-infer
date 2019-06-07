package de.thl.intellijinfer.run;

import de.thl.intellijinfer.config.InferVersion;

public enum Checker {
    //DEFAULT SINCE VERSION 0.16.0
    ANNOTATION_REACHABILITY("annotation-reachability", true, new InferVersion(0, 16, 0)),
    BIABDUCTION("biabduction", true, new InferVersion(0, 16, 0)),
    FRAGMENT_RETAINS_VIEW("fragment-retains-view", true, new InferVersion(0, 16, 0)),
    IMMUTABLE_CAST("immutable-cast", true, new InferVersion(0, 16, 0)),
    LINTERS("linters", true, new InferVersion(0, 16, 0)),
    LIVENESS("liveness", true, new InferVersion(0, 16, 0)),
    OWNERSHIP("ownership", true, new InferVersion(0, 16, 0)),
    PRINTF_ARGS("printf-args", true, new InferVersion(0, 16, 0)),
    RACERD("racerd", true, new InferVersion(0, 16, 0)),
    SIOF("siof", true, new InferVersion(0, 16, 0)),
    UNINIT("uninit", true, new InferVersion(0, 16, 0)),

    //OPTIONAL SINCE VERSION 0.16.0
    ERADICATE("eradicate", false, new InferVersion(0, 16, 0)),
    COST("cost", false, new InferVersion(0, 16, 0)),
    CHECK_NULLABLE("check-nullable", false, new InferVersion(0, 16, 0)),
    BUFFEROVERRUN("bufferoverrun", false, new InferVersion(0, 16, 0)),
    QUANDRY("quandary", false, new InferVersion(0, 16, 0)),
    STARVATION("starvation", false, new InferVersion(0, 16, 0)),
    SUGGEST_NULLABLE("suggest-nullable", false, new InferVersion(0, 16, 0)),

    //EXPERIMENTAL SINCE VERSION 0.16.0
    LITHO("--litho", false, new InferVersion(0, 16, 0));

    private String argument;
    private Boolean defaultChecker;
    private InferVersion sinceVersion;

    Checker(String arg, Boolean defaultChecker, InferVersion sinceVersion) {
        this.argument = arg;
        this.defaultChecker = defaultChecker;
        this.sinceVersion = sinceVersion;
    }

    public String getActivationArgument() {
        return "--" + this.argument;
    }
    public String getDeactivationArgument() {
        return "--no-" + this.argument;
    }

    public Boolean isDefault() {
        return this.defaultChecker;
    }
}
