package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.utils.CSVManager;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.swing.plaf.nimbus.State;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.*;

class DatabaseAPITest {
    //private DatabaseAPI api;
  //  private ConnectionHandler connHandler = new ConnectionHandler();
//    private Connection connection = connHandler.main(false);

    @BeforeEach
    public void setUp() throws Exception
    {
        DatabaseAPI.getDatabaseAPI().populateDB(ConnectionHandler.getConnection(), CSVManager.load("L1Nodes.csv"), CSVManager.load("L1Edges.csv"));
    }

    @Test
    @DisplayName("Test for adding a dummy node")
    public void testAddNode() throws SQLException
    {
        assertTrue(DatabaseAPI.getDatabaseAPI().addNode("test", 1, 1, "testfloor", "testbuilding", "testtype",
                "testlong", "testshort"));
    }

    @Test
    @DisplayName("Test deleting a node")
    public void testDeleteNode() throws SQLException
    {
        DatabaseAPI.getDatabaseAPI().addNode("test", 1, 1, "testfloor", "testbuilding", "testtype",
                "testlong", "testshort");
        assertTrue(DatabaseAPI.getDatabaseAPI().deleteNode("test"));
    }


    @DisplayName("Test for making sure can't add duplicate node")
    public void testAddDuplicate() throws SQLException
    {
        DatabaseAPI.getDatabaseAPI().addNode("test", 1, 1, "testfloor", "testbuilding", "testtype",
                "testlong", "testshort");
        assertThrows(DerbySQLIntegrityConstraintViolationException.class, () -> DatabaseAPI.getDatabaseAPI().addNode("test", 1, 1, "testfloor", "testbuilding", "testtype",
                "testlong", "testshort"));
    }

    @Test
    @DisplayName("test delete non exsistant node")
    public void testDeleteNonExsistantNode() throws SQLException{
        assertFalse(DatabaseAPI.getDatabaseAPI().deleteNode("test"));
    }

    @Test
    @DisplayName("add edge")
    public void addEdge() throws SQLException{
        assertTrue(DatabaseAPI.getDatabaseAPI().addEdge("test", "b1", "B2"));
        DatabaseAPI.getDatabaseAPI().deleteEdge("test");
    }

    @Test
    @DisplayName("add dulplicate edge")
    public void addDuplicateEdge() throws SQLException{
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                DatabaseAPI.getDatabaseAPI().addEdge("test", "b1", "B2");
                DatabaseAPI.getDatabaseAPI().addEdge("test", "b1", "B2");
            }
        });
    }

    @Test
    @DisplayName("test deleting edge")
    public void deleteEdge() throws SQLException{
        DatabaseAPI.getDatabaseAPI().addEdge("test", "start", "end");
        assertTrue(DatabaseAPI.getDatabaseAPI().deleteEdge("test"));
    }

    @Test
    @DisplayName("test build update query")
    public void testBuildUpdateQuery() throws SQLException{
        String expected = "UPDATE L1Nodes SET nodeid=(?) WHERE nodeid=(?)";
        assertEquals(expected, DatabaseAPI.getDatabaseAPI().buildUpdateQuery("L1Nodes", "id", "node"));
    }

    @Test
    @DisplayName("test incorrect update query build")
    public void testFaultyUpdateQuery() throws NullPointerException{
        assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                DatabaseAPI.getDatabaseAPI().buildUpdateQuery(null, null, null);
            }
        });
    }

    @Test
    @DisplayName("test update node field")
    public void updateNodeID() throws SQLException{
        DatabaseAPI.getDatabaseAPI().addNode("test", 1, 1, "f", "b", "t", "l", "s");
        assertTrue(DatabaseAPI.getDatabaseAPI().updateEntry("L1Nodes", "node", "test",
                "id", "dummyID"));
    }
}