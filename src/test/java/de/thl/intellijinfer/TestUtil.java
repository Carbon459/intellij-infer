package de.thl.intellijinfer;

import com.intellij.openapi.util.SystemInfo;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestUtil {

    /**
     * Gets the correct path for the Infer Version Test Script
     * @return A relative path to the Test Script
     */
    public static String getInferVersionTestPath() {
        if(SystemInfo.isWindows) return "src/test/resources/inferVersionTest.bat";
        else return "src/test/resources/inferVersionTest.sh";
    }

    /**
     * Creates a Mock Infer Installation
     * @return A validated {@link InferInstallation}
     */
    public static InferInstallation createMockInstallation() {
        InferInstallation testInstall = new InferInstallation();
        testInstall.setConfirmedWorking(true);
        testInstall.setVersion(new InferVersion(0, 16, 0));
        return testInstall;
    }

    /**
     * Asserts if the actual Launch Arguments are equal to the expected Arguments
     * @param expected The expected launch arguments
     * @param actual the actual launch arguments
     */
    public static void assertEqualLaunchArgs(String expected, String actual) {
        final List<String> expectedSplit = Arrays.asList(expected.split(" "));
        final List<String> actualSplit = Arrays.asList(actual.split(" "));

        assertTrue(expectedSplit.containsAll(actualSplit) && actualSplit.containsAll(expectedSplit));
    }

}
