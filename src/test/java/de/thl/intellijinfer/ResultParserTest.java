package de.thl.intellijinfer;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import de.thl.intellijinfer.model.InferBug;
import de.thl.intellijinfer.service.ResultParser;

import java.io.IOException;
import java.util.List;

public class ResultParserTest extends LightPlatformCodeInsightFixtureTestCase {

    private ResultParser rp;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.rp = ResultParser.getInstance(getProject());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/";
    }

    public void testParseSingleBug() {
        final String singleBugJsonPath = getTestDataPath() + "singleBug.json";

        List<InferBug> bugList = null;

        try {
            bugList = this.rp.getBugList(singleBugJsonPath);
        } catch(IOException ex) {
            ex.printStackTrace();
            fail("Failed reading file " + singleBugJsonPath);
        }

        assertNotNull(bugList);
        assertEquals(1, bugList.size());

        final InferBug bug = bugList.get(0);
        assertEquals("NULL_DEREFERENCE", bug.getBugType());
        assertEquals(12, bug.getLine());

        assertEquals(3, bug.getBugTrace().size());
        assertEquals(11, bug.getBugTrace().get(1).getLineNumber());
    }
}
