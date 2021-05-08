package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleDriverCode {
    public static void main(String[] args) throws IOException {
        String[] data = GoogleAPI.getGoogleAPI().queryAPI("997 Main St. Norwell, MA 02061", "BWH Visitor Parking, Francis Street, Boston, MA");
        System.out.println(data[0]);
        System.out.println(data[1]);
        String address = GoogleAPI.getGoogleAPI().parseClosestParkingLot("997 Main St. Norwell MA, 02061");
        System.out.println("Closest parking lot " + address);
    }
}
