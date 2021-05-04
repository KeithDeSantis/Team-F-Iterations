package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.io.IOException;

public class GoogleDriverCode {
    public static void main(String[] args) throws IOException {
        String data = GoogleAPI.getGoogleAPI().queryAPI("Toronto", "Montreal");
        System.out.println(data);
        GoogleAPI.getGoogleAPI().parseGoogleData(data);
    }
}
