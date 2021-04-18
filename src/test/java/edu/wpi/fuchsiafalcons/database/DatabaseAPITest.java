package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.CSVManager;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.swing.plaf.nimbus.State;
import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;

class DatabaseAPITest {
    //private DatabaseAPI api;
  //  private ConnectionHandler connHandler = new ConnectionHandler();
//    private Connection connection = connHandler.main(false);

    @BeforeEach
    public void setUp() throws Exception
    {
        DatabaseAPI.getDatabaseAPI().populateDB(ConnectionHandler.getConnection(), CSVManager.load("MapfAllnodes.csv"), CSVManager.load("MapfAlledges.csv"));
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

    @Test
    @DisplayName("test dropping non-existent table")
    public void testDropBadTable() throws SQLException{
        assertFalse(DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "test-table"));
    }

    @Test
    @DisplayName("test dropping valid table")
    public void testDropValidTable() throws SQLException{
        assertTrue(DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1Edges"));
    }

    @Test
    @DisplayName("test populate edges method")
    public void testPopulateEdge() throws SQLException{
        ArrayList<String[]> data = new ArrayList<>();
        String[] edgeOneData = {"firstID", "firstStart", "firstEnd"};
        String[] edgeTwoData = {"secondID", "secondStart", "secondEnd"};
        data.add(edgeOneData);
        data.add(edgeTwoData);

        DatabaseAPI.getDatabaseAPI().populateEdges(data);
        assertTrue(DatabaseAPI.getDatabaseAPI().deleteEdge("firstID"));
        assertTrue(DatabaseAPI.getDatabaseAPI().deleteEdge("secondID"));
    }

    @Test
    @DisplayName("test populate nodes method")
    public void testPopulateNodes() throws SQLException{
        ArrayList<String[]> data = new ArrayList<>();
        String[] nodeOneData = {"id1", "1", "1", "test", "test1", "test2", "test3", "test4"};
        String[] nodeTwoData = {"id2", "2", "2", "test", "test5", "test6", "test7", "test8"};
        data.add(nodeOneData);
        data.add(nodeTwoData);

        DatabaseAPI.getDatabaseAPI().populateNodes(data);
        assertTrue(DatabaseAPI.getDatabaseAPI().deleteNode("id1"));
        assertTrue(DatabaseAPI.getDatabaseAPI().deleteNode("id2"));
    }

    @Test
    @DisplayName("test valid table creation")
    public void testValidTableCreation() throws SQLException{
        String sql = "CREATE TABLE DUMMY (TEST INTEGER)";
        DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), sql);
        assertTrue(DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "DUMMY"));
    }

    @Test
    @DisplayName("test invalid table creation")
    public void testInvalidTableCreation() throws SQLException{
        String sql = "invalid query here";
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), sql);
            }
        });
    }

    @Test
    @DisplayName("test generate node entry objects")
    public void testGenNodeEntries() throws SQLException{
        NodeEntry entry = new NodeEntry("test", "1", "1", "f", "b", "t", "l", "s");
        ArrayList<NodeEntry> expected = new ArrayList<>();
        expected.add(entry);
        String sql = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
        DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "L1Nodes");
        DatabaseAPI.getDatabaseAPI().createTable(ConnectionHandler.getConnection(), sql);
        DatabaseAPI.getDatabaseAPI().addNode("test", 1, 1, "f", "b", "t", "l", "s");
        List<NodeEntry> actual = DatabaseAPI.getDatabaseAPI().genNodeEntries(ConnectionHandler.getConnection());
        assertEquals(expected.get(0).getNodeID(), actual.get(0).getNodeID());
    }

    @Test
    @DisplayName("test make sure service request table exists")
    public void testServiceReqTable() throws SQLException{
        assertTrue(DatabaseAPI.getDatabaseAPI().dropTable(ConnectionHandler.getConnection(), "SERVICE_REQUESTS"));
    }

    @Test
    @DisplayName("testing adding one service request")
    public void testOneServiceReq() throws SQLException{
        assertTrue(DatabaseAPI.getDatabaseAPI().addServiceReq("test", "name", "false"));
    }

    @Test
    @DisplayName("test service requests are appropriately added")
    public void testAddServiceReq() throws SQLException{
        String[] reqOne = {"test1", "test person", "false"};
        String[] reqTwo = {"test2", "test person2", "true"};
        List<String[]> reqData = new ArrayList<>();
        reqData.add(reqOne);
        reqData.add(reqTwo);
        DatabaseAPI.getDatabaseAPI().populateReqs(reqData);
        ResultSet rset = null;
        String query = "SELECT * FROM SERVICE_REQUESTS";
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);
        int counter = 0;
        while (rset.next()) {
            if (counter == 0) {
                assertEquals(rset.getString(1), reqOne[0]);
                assertEquals(rset.getString(2), reqOne[1]);
                assertEquals(rset.getString(3), reqOne[2]);
            }
            else {
                assertEquals(rset.getString(1), reqTwo[0]);
                assertEquals(rset.getString(2), reqTwo[1]);
                assertEquals(rset.getString(3), reqTwo[2]);
            }
            counter = counter + 1;
        }
    }

    @Test
    @DisplayName("test editing a service request")
    public void testEditRequest() throws SQLException{
        DatabaseAPI.getDatabaseAPI().addServiceReq("test", "person", "false");
        assertTrue(DatabaseAPI.getDatabaseAPI().editServiceReq("test", "completed", "true"));
    }

    @Test
    @DisplayName("test invalid service request edit")
    public void testInvalidRequestEdit() throws SQLException{
        DatabaseAPI.getDatabaseAPI().addServiceReq("name", "test", "false");
        assertThrows(SQLException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                DatabaseAPI.getDatabaseAPI().editServiceReq("name", "asdf", "1234");
            }
        });
    }

}