import static org.junit.Assert.assertEquals;
import org.junit.*;


public class TestCampsite {
    /**
     * Tests that the campsiteId and campsiteName values are parsed and reformatted
     * properly when a Campsite object is instantiated.
     */
    @Test
    public void testGetters() {
        // Default gapRule: 0
        Campsite testCampsite = new Campsite(1, "Cozy Cabin");
        int expectedCampsiteId = 1;

        String expectedCampsiteName = "Cozy Cabin";

        assertEquals(expectedCampsiteId, testCampsite.getCampsiteId());
        assertEquals(expectedCampsiteName, testCampsite.getCampsiteName());

        // Explicitly initialize gapRule: 3
        testCampsite = new Campsite(1, "Cozy Cabin", 3);
        expectedCampsiteId = 1;
        expectedCampsiteName = "Cozy Cabin";
        int expectedGapRule = 3;

        assertEquals(expectedCampsiteId, testCampsite.getCampsiteId());
        assertEquals(expectedCampsiteName, testCampsite.getCampsiteName());
        assertEquals(expectedGapRule, testCampsite.getGapRule());
    }

    /**
     * Tests that the campsite's gapRule values can be
     * properly reset when after a Campsite object is instantiated.
     */
    @Test
    public void testSetters() {
        // GapRule
        Campsite testCampsite = new Campsite(1, "Cozy Cabin");
        int expectedGapRule = 3;

        testCampsite.setGapRule(3);
        assertEquals(expectedGapRule, testCampsite.getGapRule());
    }
}
