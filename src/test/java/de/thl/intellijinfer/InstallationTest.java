package de.thl.intellijinfer;


import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.thl.intellijinfer.model.InferVersion;
import de.thl.intellijinfer.service.InstallationChecker;

public class InstallationTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testVersionChecker() {
    final String testJson = "{\n" +
                "\"major\": 0,\n" +
                "\"minor\": 16,\n" +
                "\"patch\": 0,\n" +
                "\"commit\": \"4a91616\",\n" +
                "\"branch\": \"HEAD\",\n" +
                "\"tag\": \"v0.16.0\"\n" +
                "}";

       final InferVersion iv = InstallationChecker.getInstance().parseVersionJson(testJson);
       assertEquals(0, iv.getMajor());
       assertEquals(16, iv.getMinor());
       assertEquals(0, iv.getPatch());
    }
}

