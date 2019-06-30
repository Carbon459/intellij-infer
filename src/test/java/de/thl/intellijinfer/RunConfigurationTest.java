package de.thl.intellijinfer;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;
import de.thl.intellijinfer.run.InferConfigurationType;
import de.thl.intellijinfer.run.InferRunConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunConfigurationTest extends LightPlatformCodeInsightFixtureTestCase {

    private InferRunConfiguration irc;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.irc = (InferRunConfiguration) new InferConfigurationType().getConfigurationFactories()[0].createTemplateConfiguration(getProject());

    }
    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testDefaultInferLaunchCmd() {
        RunConfiguration javaRC = new ApplicationConfigurationType().getConfigurationFactories()[0].createTemplateConfiguration(getProject());
        this.irc.getLaunchOptions().setSelectedRunConfig(javaRC);

        InferInstallation ii = InferInstallation.createInferInstallation("infer", true);
        ii.setVersion(new InferVersion(0, 16,0 ));
        this.irc.getLaunchOptions().setSelectedInstallation(ii);

        try {
            final List<String> expected = Arrays.asList(PlatformTestUtil.loadFileText(getTestDataPath() + "defaultInferLaunchCmd.txt").split(" "));
            final List<String> actual = Arrays.asList(this.irc.getLaunchOptions().buildInferLaunchCmd().split(" "));

            assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
        } catch(IOException ex) {
            ex.printStackTrace();
            fail("Could not load expected file");
        } catch(ExecutionException ex) {
            ex.printStackTrace();
            fail("No Run Configuration selected");
        }
    }

    public void testModifiedInferLaunchCmd() {
        RunConfiguration javaRC = new ApplicationConfigurationType().getConfigurationFactories()[0].createTemplateConfiguration(getProject());
        this.irc.getLaunchOptions().setSelectedRunConfig(javaRC);

        InferInstallation ii = InferInstallation.createInferInstallation("infer", true);
        ii.setVersion(new InferVersion(0, 16,0 ));
        this.irc.getLaunchOptions().setSelectedInstallation(ii);

        this.irc.getLaunchOptions().setAdditionalArgs("--debug");

        List<Checker> newCheckers = this.irc.getLaunchOptions().getSelectedCheckers();
        newCheckers.add(Checker.COST);
        this.irc.getLaunchOptions().setSelectedCheckers(newCheckers);

        try {
            final List<String> expected = Arrays.asList(PlatformTestUtil.loadFileText(getTestDataPath() + "modifiedInferLaunchCmd.txt").split(" "));
            final List<String> actual = Arrays.asList(this.irc.getLaunchOptions().buildInferLaunchCmd().split(" "));

            assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
        } catch(IOException ex) {
            ex.printStackTrace();
            fail("Could not load expected file");
        } catch(ExecutionException ex) {
            ex.printStackTrace();
            fail("No Run Configuration selected");
        }
    }

}
