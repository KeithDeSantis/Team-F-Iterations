package edu.wpi.cs3733.D21.teamF.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EdgeEntryTest {

    @Test
    public void testConstructorAndSetters(){
        EdgeEntry edgeEntry = new EdgeEntry("Start_End", "Start", "End");
        assertTrue(edgeEntry.getEdgeID().equals("Start_End") &&
                edgeEntry.getStartNode().equals("Start") &&
                edgeEntry.getEndNode().equals("End"));

        edgeEntry.setEdgeID("Start2_End2");
        assertTrue(edgeEntry.getEdgeID().equals("Start2_End2") &&
                edgeEntry.getStartNode().equals("Start2") &&
                edgeEntry.getEndNode().equals("End2"));

        edgeEntry.setStartNode("Start3");
        assertTrue(edgeEntry.getEdgeID().equals("Start3_End2") &&
                edgeEntry.getStartNode().equals("Start3") &&
                edgeEntry.getEndNode().equals("End2"));

        edgeEntry.setEndNode("End3");
        assertTrue(edgeEntry.getEdgeID().equals("Start3_End3") &&
                edgeEntry.getStartNode().equals("Start3") &&
                edgeEntry.getEndNode().equals("End3"));

    }
}
