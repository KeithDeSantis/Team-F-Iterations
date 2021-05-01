package edu.wpi.cs3733.D21.teamF.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing NodeEntry setters and getters
 */
public class NodeEntryTest {

    @Test
    public void testCustomConstructorAndSettersAndGetters() {
        NodeEntry nodeEntry = new NodeEntry("ID_1", "short_Name");
        assertTrue(nodeEntry.getNodeID().equals("ID_1") && nodeEntry.getShortName().equals("short_Name"));
        nodeEntry.setNodeType("type");
        nodeEntry.setBuilding("build");
        nodeEntry.setFloor("floor");
        nodeEntry.setYCoordinate("y");
        nodeEntry.setXCoordinate("x");
        nodeEntry.setLongName("long");
        assertTrue(nodeEntry.getNodeType().equals("type") &&
                nodeEntry.getLongName().equals("long") &&
                nodeEntry.getBuilding().equals("build") &&
                nodeEntry.getFloor().equals("floor") &&
                nodeEntry.getYCoordinate().equals("y") &&
                nodeEntry.getXCoordinate().equals("x"));
    }
}
