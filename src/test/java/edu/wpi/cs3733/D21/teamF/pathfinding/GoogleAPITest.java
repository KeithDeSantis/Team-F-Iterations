package edu.wpi.cs3733.D21.teamF.pathfinding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GoogleAPITest {
    @BeforeEach
    public void setUp(){
        System.out.println("I hate Google");
    }

    @Test
    @DisplayName("testing finding distance")
    public void testFindDistance() throws IOException {
        GoogleAPI.getGoogleAPI().findDistance("Toronto", "Montreal");
    }
}