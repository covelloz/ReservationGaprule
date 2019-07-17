import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;


class SearchResponseHandler extends ResponseHandler {

    public SearchResponseHandler(String fileName) { super(fileName); }

    /**
     * Returns a Search object from JSON Response
     */
    public Search getSearch() {
        JSONObject jo = parseResponse();
        JSONArray ja = new JSONArray();
        ja.put(jo.get("search"));

        List<JSONObject> listObj = tokenizeArray(ja);
        String startDate = listObj.get(0).get("startDate").toString();
        String endDate = listObj.get(0).get("endDate").toString();
        return new Search(startDate, endDate);
    }

    /**
     * Returns a list of Campsite objects from JSON Response
     */
    public List<Campsite> getCampsites() {
        JSONObject jo = parseResponse();
        JSONArray ja = (JSONArray) jo.get("campsites");
        List<JSONObject> listObj = tokenizeArray(ja);

        List<Campsite> campsiteList = new ArrayList<Campsite>();
        for (JSONObject obj : listObj) {
            int campsiteId = Integer.parseInt(obj.get("id").toString());
            String name = obj.get("name").toString();

            Campsite campsite = new Campsite(campsiteId, name);
            campsiteList.add(campsite);
        }
        return campsiteList;
    }

    /**
     * Returns a list of Reservation objects from JSON Response
     */
    public List<Reservation> getReservations() {
        JSONObject jo = parseResponse();
        JSONArray ja = (JSONArray) jo.get("reservations");
        List<JSONObject> listObj = tokenizeArray(ja);

        List<Reservation> reservationList = new ArrayList<>();
        for (JSONObject obj : listObj) {
            int campsiteId = Integer.parseInt(obj.get("campsiteId").toString());
            String startDate = obj.get("startDate").toString();
            String endDate = obj.get("endDate").toString();

            Reservation reservation = new Reservation(campsiteId, startDate, endDate);
            reservationList.add(reservation);
        }
        return reservationList;
    }

    /**
     * Sets a campsite's gapRule from a JSON Array
     */
    public void setGapRules(List<JSONObject> gapRuleConfig, List<Campsite> campsites) {
        for(JSONObject obj : gapRuleConfig) {
            int campsiteId = Integer.parseInt(obj.get("id").toString());
            int gapRule = Integer.parseInt(obj.get("gapRule").toString());

            for(Campsite campsite : campsites) {
                if (campsite.getCampsiteId() == campsiteId) {
                    campsite.setGapRule(gapRule);
                }
            }
        }
    }
}
