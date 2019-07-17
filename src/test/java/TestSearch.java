import java.util.*;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;


public class TestSearch {

    private SearchResponseHandler testResponse;
    private List<JSONObject> gapRuleConfig;

    /**
     * Set file location for JSON response input file: test-search-response.json
     */
    @Before
    public void setUp() {
        String rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String testFile = Paths.get(rootPath, "src", "test", "test-search-response.json").toString();
        testResponse = new SearchResponseHandler(testFile);

        String configFile = Paths.get(rootPath, "src", "test", "test-gaprule-config.json").toString();
        ResponseHandler configHandler = new ResponseHandler(configFile);
        JSONArray configArray = (JSONArray) configHandler.parseResponse().get("campsites");
        gapRuleConfig = configHandler.tokenizeArray(configArray);
    }

    /**
     * Tests that the start and end date values are parsed and reformatted
     * properly when the Search object is instantiated.
     */
    @Test
    public void testGetters() {
        Search testSearch = new Search("2018-06-06", "2018-06-08");
        String expectedStartDate = "06/06/2018";
        String expectedEndDate = "06/08/2018";

        assertEquals(expectedStartDate, testSearch.getStartDate());
        assertEquals(expectedEndDate, testSearch.getEndDate());
    }

    /**
     * Tests that the start and end date can be reset
     * properly after the Search object has been instantiated.
     */
    @Test
    public void testSetters() {
        Search testSearch = new Search("2018-06-06", "2018-06-08");
        String expectedStartDate = "06/10/2018";
        String expectedEndDate = "06/12/2018";

        testSearch.setStartDate("2018-06-10");
        testSearch.setEndDate("2018-06-12");

        assertEquals(expectedStartDate, testSearch.getStartDate());
        assertEquals(expectedEndDate, testSearch.getEndDate());
    }

    /**
     * Tests that only getAvailableCampsites only returns
     * campsites that will not violate gapRule when reserved for Search's start/end dates.
     */
    @Test
    public void getAvailableCampsites() {
        List<Reservation> reservations = testResponse.getReservations();
        List<Campsite> campsites = testResponse.getCampsites();

        // Set gapRules from test-gaprule-config.json
        testResponse.setGapRules(gapRuleConfig, campsites);

        // Default case provided
        int[] expectedCampsiteIds = new int[]{2,4,5};

        Search testSearch = new Search("2018-06-04", "2018-06-06");
        List<Campsite> availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }

        // All Campsites
        expectedCampsiteIds = new int[]{1,2,3,4,5};

        testSearch = new Search("2018-06-15", "2018-06-20");
        availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }

        // Only campsite {5}
        // Overlaps with reservations from campsites {1,2,3,4}
        expectedCampsiteIds = new int[]{5};

        testSearch = new Search("2018-06-06", "2018-06-10");
        availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }

        // Only campsites {4,5}
        // Overlaps with reservations from campsites {1,2,3}
        expectedCampsiteIds = new int[]{4,5};

        testSearch = new Search("2018-06-02", "2018-06-04");
        availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }

        // Only campsite {3, 5}
        // No overlaps but 1 day gap with {1,2,4}
        expectedCampsiteIds = new int[]{3,5};

        testSearch = new Search("2018-06-05", "2018-06-05");
        availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }

        // Only campsite {4, 5}
        // No overlaps but 1 day gap with {1,2,3}
        expectedCampsiteIds = new int[]{4,5};

        testSearch = new Search("2018-06-05", "2018-06-06");
        availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }

        // No overlap but fails with campsites {2, 3}
        expectedCampsiteIds = new int[]{1,4,5};

        testSearch = new Search("2018-06-11", "2018-06-14");
        availableCampsites = testSearch.getAvailableCampsites(reservations, campsites);
        for (int i=0; i < availableCampsites.size(); i++) {
            assertEquals(expectedCampsiteIds[i], availableCampsites.get(i).getCampsiteId());
        }
    }
}
