package de.thl.intellijinfer.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum Checker {
    //DEFAULT SINCE VERSION 0.15.0
    BIABDUCTION("biabduction", Category.DEFAULT, new InferVersion(0, 15, 0)),                               //the separation logic based bi-abduction analysis using the checkers framework
    FRAGMENT_RETAINS_VIEW("fragment-retains-view", Category.DEFAULT, new InferVersion(0, 15, 0)),           //detects when Android fragments are not explicitly nullified before becoming unreabable
    LINTERS("linters", Category.DEFAULT, new InferVersion(0, 15, 0)),                                       //syntactic linters
    LIVENESS("liveness", Category.DEFAULT, new InferVersion(0, 15, 0)),                                     //the detection of dead stores and unused variables
    OWNERSHIP("ownership", Category.DEFAULT, new InferVersion(0, 15, 0)),                                   //the detection of C++ lifetime bugs
    RACERD("racerd", Category.DEFAULT, new InferVersion(0, 15, 0)),                                         //the RacerD thread safety analysis
    SIOF("siof", Category.DEFAULT, new InferVersion(0, 15, 0)),                                             //the Static Initialization Order Fiasco analysis
    UNINIT("uninit", Category.DEFAULT, new InferVersion(0, 15, 0)),                                         //checker for use of uninitialized values

    //OPTIONAL SINCE VERSION 0.15.0
    ANNOTATION_REACHABILITY("annotation-reachability", Category.OPTIONAL, new InferVersion(0, 15, 0)),      //the annotation reachability checker. Given a pair of source and sink annotation, e.g. @PerformanceCritical and @Expensive, this checker will warn whenever some method annotated with @PerformanceCritical calls, directly or indirectly, another method annotated with @Expensive
    BUFFEROVERRUN("bufferoverrun", Category.OPTIONAL, new InferVersion(0, 15, 0)),                          //the buffer overrun analysis
    COST("cost", Category.OPTIONAL, new InferVersion(0, 15, 0)),                                            //checker for performance cost analysis
    ERADICATE("eradicate", Category.OPTIONAL, new InferVersion(0, 15, 0)),                                  //the eradicate @Nullable checker for Java annotations
    IMMUTABLE_CAST("immutable-cast", Category.OPTIONAL, new InferVersion(0, 15, 0)),                        //the detection of object cast from immutable type to mutable type. For instance, it will detect cast from ImmutableList to List, ImmutableMap to Map, and ImmutableSet to Set.
    PRINTF_ARGS("printf-args", Category.OPTIONAL, new InferVersion(0, 15, 0)),                              //the detection of mismatch between the Java printf format strings and the argument types. For, example, this checker will warn about the type error in `printf("Hello %d", "world")`
    QUANDARY("quandary", Category.OPTIONAL, new InferVersion(0, 15, 0)),                                    //the quandary taint analysis
    STARVATION("starvation", Category.OPTIONAL, new InferVersion(0, 15, 0)),                                //starvation analysis

    //OPTIONAL SINCE VERSION 0.16.0
    CLASS_LOADS("class-loads", Category.OPTIONAL, new InferVersion(0,16,0)),                                //Java class loading analysis
    LOOP_HOISTING("loop-hoisting", Category.OPTIONAL, new InferVersion(0, 16, 0)),                          //checker for loop-hoisting

    //EXPERIMENTAL SINCE VERSION 0.15.0
    LITHO("litho", Category.EXPERIMENTAL, new InferVersion(0, 15, 0)),                                      //Experimental checkers supporting the Litho framework

    //EXPERIMENTAL SINCE VERSION 0.16.0
    NULLSAFE("nullsafe", Category.EXPERIMENTAL, new InferVersion(0, 16, 0)),                                //nullable type checker (incomplete: use --eradicate for now)
    PULSE("pulse", Category.EXPERIMENTAL, new InferVersion(0, 16, 0)),                                      //C++ lifetime analysis
    PURITY("purity", Category.EXPERIMENTAL, new InferVersion(0, 16, 0)),                                    //Purity analysis
    QUANDARY_BO("quandaryBO", Category.EXPERIMENTAL, new InferVersion(0,16,0)),                             //The quandaryBO tainted buffer access

    //SINCE VERSION 0.17.0
    INEFFICIENT_KEYSET_ITERATOR("inefficient-keyset-iterator", Category.OPTIONAL, new InferVersion(0,17,0));

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
    public String getName() {
        return super.toString();
    }

    public String toString() {
        switch(this.category) {
            case DEFAULT:
                return "[Default] " + super.toString();
            case OPTIONAL:
                return "[Optional] " + super.toString();
            case EXPERIMENTAL:
                return "[Experimental] " + super.toString();
        }
        return super.toString();
    }

    @NotNull
    public static List<Checker> getDefaultCheckers() {
        List<Checker> defaultChecker = new ArrayList<>();
        for(Checker checker : Checker.values()) {
            if(checker.isDefault()) defaultChecker.add(checker);
        }

        return defaultChecker;
    }

    /**
     * Gets a List of Checkers supported by the given version and not included in the given list.
     * @param checkers The given list of checkers
     * @param version The given version
     * @return A diff of the given checkers list and all checkers for this version
     */
    @NotNull
    public static List<Checker> getMissingCheckers(List<Checker> checkers, InferVersion version) {
        List<Checker> missingCheckers = new ArrayList<>();
        for(Checker checker : Checker.values()) {
            if(!checkers.contains(checker) && checker.getSinceVersion().compareTo(version) <= 0) missingCheckers.add(checker);
        }
        return missingCheckers;
    }
}
