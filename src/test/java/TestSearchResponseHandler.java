import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.*;


public class TestSearchResponseHandler {
    private SearchResponseHandler testResponse;

    /**
     * Set file location for JSON response input file: test-search-response.json
     */
    @Before
    public void setUp() {
        String rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String fileName = Paths.get(rootPath, "src", "test", "test-search-response.json").toString();
        testResponse = new SearchResponseHandler(fileName);
    }

    /**
     * Tests that the SearchResponseHandler getSearch() method
     * returns an accurately parsed and instantiated Search object.
     */
    @Test
    public void testGetSearch() {
        String expectedStartDate = "06/04/2018";
        String expectedEndDate = "06/06/2018";

        Search testSearch = testResponse.getSearch();
        assertEquals(expectedStartDate, testSearch.getStartDate());
        assertEquals(expectedEndDate, testSearch.getEndDate());
    }

    /**
     * Tests that the SearchResponseHandler getCampsites() method
     * returns an accurately parsed and instantiated list of Campsite objects.
     */
    @Test
    public void testGetCampsites() {
        int[] expectedCampsiteIds = new int[]{1,2,3,4,5};
        String[] expectedCampsiteNames = new String[] {
                "Cozy Cabin",
                "Comfy Cabin",
                "Rustic Cabin",
                "Rickety Cabin",
                "Cabin in the Woods"
        };

        List<Campsite> testCampsites = testResponse.getCampsites();
        for (int i = 0; i < testCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], testCampsites.get(i).getCampsiteId());
            assertEquals(expectedCampsiteNames[i], testCampsites.get(i).getCampsiteName());
        }
    }

    /**
     * Tests that the SearchResponseHandler getReservations() method
     * returns an accurately parsed and instantiated list of Reservation objects.
     */
    @Test
    public void testGetReservations() {
        int[] expectedCampsiteIds = new int[]{1,1,2,2,2,3,3,4};
        String[] expectedCampsiteStartDates = new String[] {
                "06/01/2018",
                "06/08/2018",
                "06/01/2018",
                "06/02/2018",
                "06/07/2018",
                "06/01/2018",
                "06/08/2018",
                "06/07/2018"
        };
        String[] expectedCampsiteEndDates = new String[] {
                "06/03/2018",
                "06/10/2018",
                "06/01/2018",
                "06/03/2018",
                "06/09/2018",
                "06/02/2018",
                "06/09/2018",
                "06/10/2018"
        };

        List<Reservation> testReservations = testResponse.getReservations();
        for (int i = 0; i < testReservations.size(); i++) {
            assertEquals(expectedCampsiteIds[i], testReservations.get(i).getCampsiteId());
            assertEquals(expectedCampsiteStartDates[i], testReservations.get(i).getStartDate());
            assertEquals(expectedCampsiteEndDates[i], testReservations.get(i).getEndDate());
        }
    }

    /**
     * Tests that the SearchResponseHandler setGapRules() method
     * accurately sets campsites' gapRule.
     */
    @Test
    public void testSetGapRules() {
        int[] expectedGapRules = new int[]{1,2,3,4,5};

        // Set gapRule equal to id
        List<JSONObject> gapRules = new ArrayList<>();
        for (int i = 1; i < 6; i ++) {
            JSONObject rule = new JSONObject();
            rule.put("id", i);
            rule.put("gapRule", i);
            gapRules.add(rule);
        }

        List<Campsite> testCampsites = testResponse.getCampsites();
        testResponse.setGapRules(gapRules, testCampsites);

        for (int i = 0; i < testCampsites.size(); i++) {
            assertEquals(expectedGapRules[i], testCampsites.get(i).getGapRule());
        }
    }
}
