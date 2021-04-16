package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.utils.CSVManager;

import javax.xml.crypto.Data;
import java.sql.*;

public class TestDriverCode {
    public static void main(String[] args) throws Exception {
        //ConnectionHandler c = new ConnectionHandler();
        //Connection conn = c.main(false);
        PopulateDB p = new PopulateDB();
        p.main(ConnectionHandler.getConnection(), CSVManager.load("L1Nodes.csv"), CSVManager.load("L1Edges.csv"));
        //DatabaseAPI api = new DatabaseAPI(conn);
        DatabaseAPI api = DatabaseAPI.getDatabaseAPI();
        boolean testAdd = api.addNode("TEST", 1, 1, "FLOOR", "BUILD", "TYPE",
                "TESTLONG", "TESTSHORT");
        boolean testDelete = api.deleteNode("TEST");
        boolean testAddEdge = api.addEdge("TESTEDGE", "start", "end");
        boolean testDeleteEdge = api.deleteEdge("TESTEDGE");
        boolean dummyNode = api.addNode("TEST", 1, 1, "f", "b", "t",
                "long", "short");
        boolean updateNode = api.updateEntry("L1Nodes", "node", "TEST",
                "id", "DECLAN");
       // conn.close();
        ConnectionHandler.getConnection().close();
    }
}
