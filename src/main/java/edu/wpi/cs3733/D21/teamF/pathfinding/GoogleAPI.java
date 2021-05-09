package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public String parseClosestParkingLot(String origin) throws IOException {
        String address = "";
        String[] parkingLots = {"0 Francis St, Boston, MA 02115", "15-51 New Whitney St, Boston, MA 02115"};
        int time = 0;
        for (String s : parkingLots){
            String[] results = GoogleAPI.getGoogleAPI().queryAPI(origin, s);
            int temp = Integer.parseInt(results[1].split(" ")[0]);
            if (temp > time){
                time = temp;
                address = s;
            }
        }
        return address;
    }

    /**
     * URL Encodes parameter
     * @param param the string to url encode
     * @return the encoded string
     * @throws UnsupportedEncodingException with error on encoding
     */
    public String urlEncode(String param) throws UnsupportedEncodingException {
        return URLEncoder.encode(param, String.valueOf(StandardCharsets.UTF_8));
    }

    /**
     * Parses the Google data into a String of directional instructions
     * @param json the Google data
     * @author Declan Murphy
     */
    public String[] parseGoogleData(String json) {
        final JSONObject parsed = new JSONObject(json);
        final JSONObject route = (JSONObject) (parsed.getJSONArray("routes")).get(0);
        final JSONObject leg = (JSONObject) (route.getJSONArray("legs")).get(0);
        final JSONObject durationArray = leg.getJSONObject("duration");
        String travelTime = durationArray.getString("text");
        final JSONArray steps = leg.getJSONArray("steps");
        StringBuilder directions = new StringBuilder();
        for (int i=0; i<steps.length(); i++){
            String temp = ((JSONObject) steps.get(i)).getString("html_instructions");
            if (temp != null) {
                directions.append(temp).append("\n");
            }
        }
        String results = directions.toString();
        results = results.replaceAll("\\<.*?\\>", "");
        return new String[]{results, travelTime};
    }

    /**
     * Builds a URL describing the path between two locations
     * @param origin the starting location
     * @param destination the end location
     * @return the URL of the Google API direction site describing the path from the origin to the destination
     * @author Declan Murphy
     */
    private String buildUrl(String origin, String destination) throws UnsupportedEncodingException {
        StringBuilder url = new StringBuilder();
        String urlBase = "https://maps.googleapis.com/maps/api/directions/json";
        url.append(urlBase);
        url.append("?origin=").append(GoogleAPI.getGoogleAPI().urlEncode(origin));
        url.append("&destination=").append(GoogleAPI.getGoogleAPI().urlEncode(destination));
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
    public String[] queryAPI(String origin, String destination) throws IOException {
        StringBuilder apiResponse = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl(origin, destination)).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader data = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String temp;
        while ((temp = data.readLine()) != null){
            apiResponse.append(temp).append("\n");
        }
        data.close();
        return GoogleAPI.getGoogleAPI().parseGoogleData(apiResponse.toString());
    }

    private static class googleSingletonHelper {
        private static final GoogleAPI googleAPI = new GoogleAPI();
    }

    public static GoogleAPI getGoogleAPI() {
        return googleSingletonHelper.googleAPI;
    }
}
