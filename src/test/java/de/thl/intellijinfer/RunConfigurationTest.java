package de.thl.intellijinfer;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.thl.intellijinfer.model.Checker;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.buildtool.BuildToolFactory;
import de.thl.intellijinfer.run.InferConfigurationType;
import de.thl.intellijinfer.run.InferRunConfiguration;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunConfigurationTest extends LightPlatformCodeInsightFixtureTestCase {

    private InferRunConfiguration irc;
    private InferInstallation testInstall;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.irc = (InferRunConfiguration) new InferConfigurationType().getConfigurationFactories()[0].createTemplateConfiguration(getProject());
        testInstall = TestUtil.createMockInstallation();
    }
    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }



    public void testAddRunConfig() {
        createJavaRC();
    }

    public void testInferLaunchDefault() {
        createJavaRC();

        try {
            TestUtil.assertEqualLaunchArgs(PlatformTestUtil.loadFileText(getTestDataPath() + "defaultInferLaunchCmd.txt"), this.irc.getLaunchOptions().buildInferLaunchCmd(getProject()));
        } catch(IOException ex) {
            ex.printStackTrace();
            fail("Could not load expected file");
        } catch(ExecutionException ex) {
            ex.printStackTrace();
            fail("No Run Configuration selected");
        }
    }

    public void testInferLaunchModified() {
        createJavaRC();

        this.irc.getLaunchOptions().setAdditionalArgs("--debug");

        List<Checker> newCheckers = this.irc.getLaunchOptions().getSelectedCheckers();
        newCheckers.add(Checker.COST);
        this.irc.getLaunchOptions().setSelectedCheckers(newCheckers);

        try {
            TestUtil.assertEqualLaunchArgs(PlatformTestUtil.loadFileText(getTestDataPath() + "modifiedInferLaunchCmd.txt"), this.irc.getLaunchOptions().buildInferLaunchCmd(getProject()));
        } catch(IOException ex) {
            ex.printStackTrace();
            fail("Could not load expected file");
        } catch(ExecutionException ex) {
            ex.printStackTrace();
            fail("No Run Configuration selected");
        }
    }

    private void createJavaRC() {
        this.irc.getLaunchOptions().setUsingBuildTool(BuildToolFactory.getInstanceFromName("JavaC"));
        this.irc.getLaunchOptions().setSelectedInstallation(testInstall);
    }
}
