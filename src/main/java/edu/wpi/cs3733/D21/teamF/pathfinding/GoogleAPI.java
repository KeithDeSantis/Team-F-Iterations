package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleAPI {
    //private static GoogleAPI googleAPI;

    /**
     * Constructs a new GoogleAPI
     * @author Declan Murphy
     */
    public GoogleAPI() {

    }

    /**
     * Parses the Google data into a JSONArray of instructions
     * @param json the Google data
     * @author Declan Murphy
     */
    private void parseGoogleData(String json) {
        final JSONArray steps = new JSONObject(json).getJSONArray("steps");
        StringBuilder directions = new StringBuilder();
        for (int i=0; i< steps.length(); i++){
            String temp = steps.getJSONObject(i).getString("html_instructions");
            if (temp != null) {
                directions.append(temp);
            }
        }
        System.out.println(directions);
    }

    /**
     * Builds a URL describing the path between two locations
     * @param origin the starting location
     * @param destination the end location
     * @return the URL of the Google API direction site describing the path from the origin to the destination
     * @author Declan Murphy
     */
    private String buildUrl(String origin, String destination) {
        StringBuilder url = new StringBuilder();
        String urlBase = "https://maps.googleapis.com/maps/api/directions/json";
        url.append(urlBase);
        url.append("?origin=").append(origin);
        url.append("&destination=").append(destination);
        String apiKey = ""; //TODO: ADD/REMOVE API KEY HERE
        url.append("&key=").append(apiKey);
        System.out.println(url);
        return url.toString();
    }

    /**
     * Queries the API with a start and end location
     * @param origin the starting location
     * @param destination the end location
     * @return the API response: a path from the origin to the destination
     * @throws IOException if a problem occurs in openConnection() or getInputStream()
     * @author Declan Murphy, Tony Vuolo (bdane)
     */
    public String queryAPI(String origin, String destination) throws IOException {
        StringBuilder apiResponse = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl(origin, destination)).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader data = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String temp;
        while ((temp = data.readLine()) != null){
            apiResponse.append(temp);
        }
        data.close();
        return apiResponse.toString();
    }

    private static class googleSingletonHelper {
        private static final GoogleAPI googleAPI = new GoogleAPI();
    }

    public static GoogleAPI getGoogleAPI() {
        return googleSingletonHelper.googleAPI;
    }
}
