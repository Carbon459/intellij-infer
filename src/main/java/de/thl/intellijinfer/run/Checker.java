package de.thl.intellijinfer.run;

import de.thl.intellijinfer.config.InferVersion;

import java.util.ArrayList;
import java.util.List;

public enum Checker {
    //DEFAULT SINCE VERSION 0.16.0
    ANNOTATION_REACHABILITY("annotation-reachability", Category.DEFAULT, new InferVersion(0, 16, 0)),
    BIABDUCTION("biabduction", Category.DEFAULT, new InferVersion(0, 16, 0)),
    FRAGMENT_RETAINS_VIEW("fragment-retains-view", Category.DEFAULT, new InferVersion(0, 16, 0)),
    IMMUTABLE_CAST("immutable-cast", Category.DEFAULT, new InferVersion(0, 16, 0)),
    LINTERS("linters", Category.DEFAULT, new InferVersion(0, 16, 0)),
    LIVENESS("liveness", Category.DEFAULT, new InferVersion(0, 16, 0)),
    OWNERSHIP("ownership", Category.DEFAULT, new InferVersion(0, 16, 0)),
    PRINTF_ARGS("printf-args", Category.DEFAULT, new InferVersion(0, 16, 0)),
    RACERD("racerd", Category.DEFAULT, new InferVersion(0, 16, 0)),
    SIOF("siof", Category.DEFAULT, new InferVersion(0, 16, 0)),
    UNINIT("uninit", Category.DEFAULT, new InferVersion(0, 16, 0)),

    //OPTIONAL SINCE VERSION 0.16.0
    ERADICATE("eradicate", Category.OPTIONAL, new InferVersion(0, 16, 0)),
    COST("cost", Category.OPTIONAL, new InferVersion(0, 16, 0)),
    CHECK_NULLABLE("check-nullable", Category.OPTIONAL, new InferVersion(0, 16, 0)),
    BUFFEROVERRUN("bufferoverrun", Category.OPTIONAL, new InferVersion(0, 16, 0)),
    QUANDRY("quandary", Category.OPTIONAL, new InferVersion(0, 16, 0)),
    STARVATION("starvation", Category.OPTIONAL, new InferVersion(0, 16, 0)),
    SUGGEST_NULLABLE("suggest-nullable", Category.OPTIONAL, new InferVersion(0, 16, 0)),

    //EXPERIMENTAL SINCE VERSION 0.16.0
    LITHO("litho", Category.EXPERIMENTAL, new InferVersion(0, 16, 0));

    enum Category {DEFAULT, OPTIONAL, EXPERIMENTAL}

    private String argument;
    private Category category;
    private InferVersion sinceVersion;

    Checker(String arg, Category category, InferVersion sinceVersion) {
        this.argument = arg;
        this.category = category;
        this.sinceVersion = sinceVersion;
    }

    public String getActivationArgument() {
        return "--" + this.argument;
    }
    public String getDeactivationArgument() {
        return "--no-" + this.argument;
    }
    public InferVersion getSinceVersion() {
        return sinceVersion;
    }

    public Boolean isDefault() {
        return this.category == Category.DEFAULT;
    }

    public static List<Checker> getDefaultCheckers() {
        List<Checker> defaultChecker = new ArrayList<>();
        for(Checker checker : Checker.values()) {
            if(checker.isDefault()) defaultChecker.add(checker);
        }
        return defaultChecker;
    }
    public static List<Checker> getMissingCheckers(List<Checker> checkers) {
        List<Checker> missingCheckers = new ArrayList<>();
        for(Checker checker : Checker.values()) {
            if(!checkers.contains(checker)) missingCheckers.add(checker);
        }
        return missingCheckers;
    }
}
