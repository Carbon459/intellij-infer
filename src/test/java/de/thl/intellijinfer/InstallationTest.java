package de.thl.intellijinfer;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.thl.intellijinfer.config.GlobalSettings;
import de.thl.intellijinfer.model.InferInstallation;
import de.thl.intellijinfer.model.InferVersion;
import jdk.nashorn.internal.objects.Global;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstallationTest extends LightPlatformCodeInsightFixtureTestCase {

    private GlobalSettings settings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        settings = GlobalSettings.getInstance();

        //Remove all previously added installations
        settings.getInstallations().removeIf((x) -> true);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testAddInstallation() {
        assertEquals(0, settings.getInstallations().size());
        settings.addInstallation(TestUtil.getInferVersionTestPath(), false);
        assertEquals(1, settings.getInstallations().size());
        assertTrue(settings.getInstallations().get(0).isConfirmedWorking());

        //Check version
        final InferVersion iv = settings.getInstallations().get(0).getVersion();
        assertNotNull(iv);
        assertEquals(0, iv.getMajor());
        assertEquals(16, iv.getMinor());
        assertEquals(0, iv.getPatch());
    }

    public void testRemoveInstallation() {
        settings.addInstallation(TestUtil.getInferVersionTestPath(), false);
        assertEquals(1, settings.getInstallations().size());
        settings.removeInstallation(settings.getInstallations().get(0));
        assertEquals(0, settings.getInstallations().size());
    }

}

