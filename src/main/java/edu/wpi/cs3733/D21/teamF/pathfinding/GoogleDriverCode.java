package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.io.IOException;

public class GoogleDriverCode {
    public void runDriverCode() throws IOException {
        GoogleAPI.getGoogleAPI().queryAPI("Toronto", "Montreal");
    }
}
