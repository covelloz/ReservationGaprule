import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.util.*;
import static spark.Spark.*;


public class SearchRequestService {
    private SearchResponseHandler fakeResponse;
    private List<JSONObject> gapRuleConfig;

    public SearchRequestService() {
        // Setup fake response (read from file)
        String rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String fileName = Paths.get(rootPath, "src", "main", "search-response.json").toString();
        this.fakeResponse = new SearchResponseHandler(fileName);

        // Setup gapRule config
        String configFile = Paths.get(rootPath, "src", "test", "test-gaprule-config.json").toString();
        ResponseHandler configHandler = new ResponseHandler(configFile);
        JSONArray configArray = (JSONArray) configHandler.parseResponse().get("campsites");
        gapRuleConfig = configHandler.tokenizeArray(configArray);
    }

    public static void main(String[] args) {
        SearchRequestService request = new SearchRequestService();

        get("/search/available/from/:startDate/to/:endDate", (req, res) -> {
            // Parse URL parameters
            String startDate = req.params(":startDate");
            String endDate = req.params(":endDate");

            // Prepare response
            Search newSearch = new Search(startDate, endDate);
            List<Reservation> reservations = request.fakeResponse.getReservations();
            List<Campsite> campsites = request.fakeResponse.getCampsites();
            request.fakeResponse.setGapRules(request.gapRuleConfig, campsites);

            // Get available campsites of requested search window
            List<Campsite> availableCampsites = newSearch.getAvailableCampsites(reservations, campsites);
            List<JSONObject> campsiteObjects = new ArrayList<>();

            // Package and send response
            for (Campsite campsite : availableCampsites) {
                JSONObject obj = new JSONObject();
                obj.put("name", campsite.getCampsiteName());
                obj.put("id", campsite.getCampsiteId());
                campsiteObjects.add(obj);
            }
            JSONObject response = new JSONObject();
            response.put("campsites", campsiteObjects);
            return response;
        });
    }
}
