import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class ResponseHandler {
    private String fileName;

    public ResponseHandler(String fileName) {
        this.fileName = fileName;
    }

    public JSONObject parseResponse() {
        if (fileName != null) {
            return parseFromFile();
        }
        return null;
    }

    /**
     * Returns an iterable list of JSONObjects from a JSONArray
     */
    public List<JSONObject> tokenizeArray(JSONArray arrayToMap) {
        List<JSONObject> listObj = new ArrayList<>();

        for (Object obj : arrayToMap) {
            JSONTokener tokener = new JSONTokener(obj.toString());
            JSONObject tokenizedObj = new JSONObject(tokener);
            listObj.add(tokenizedObj);
        }
        return listObj;
    }

    /**
     * Returns a JSONObject from .json FileReader object
     */
    private JSONObject parseFromFile() {
        JSONObject parsedObjectJSON = null;
        try {
            InputStream fileStream = new FileInputStream(this.fileName);
            JSONTokener tokener = new JSONTokener(fileStream);
            parsedObjectJSON = new JSONObject(tokener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parsedObjectJSON;
    }
}
