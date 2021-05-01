package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GoogleAPI {
    //private static GoogleAPI googleAPI;
    public GoogleAPI() {

    }

    private void parseGoogleData(String json){
        //TODO: clarify with zach, do we need to write a parser?
    }

    private String buildUrl(String origin, String destination){
        StringBuilder url = new StringBuilder();
        String urlBase = "https://maps.googleapis.com/maps/api/directions/json";
        url.append(urlBase);
        url.append("?origin=" + origin);
        url.append("&destination=" + destination);
        String apiKey = ""; //TODO: ADD/REMOVE API KEY HERE
        url.append("&key=" + apiKey);

        System.out.println(url.toString());

        return url.toString();
    }

    public String queryAPI(String origin, String destination) throws IOException{
        String requestUrl = buildUrl(origin, destination);
        URL getRequestUrl = new URL(requestUrl);
        StringBuilder apiResponse = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) getRequestUrl.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader data = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String temp;
        while ((temp = data.readLine()) != null){
            apiResponse.append(temp);
        }
        data.close();
        return apiResponse.toString();
    }

    private static class googleSingletonHelper{
        private static final GoogleAPI googleAPI = new GoogleAPI();
    }

    public static GoogleAPI getGoogleAPI(){
        return googleSingletonHelper.googleAPI;
    }
}
