package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PopulateDB {
    public List<NodeEntry> genNodeEntries(Connection conn) throws SQLException {
        List<NodeEntry> nodeEntries = new ArrayList<>();
        String sql = "SELECT * FROM L1Nodes";
        Statement stmt =  conn.createStatement();

        ResultSet rset = null;
        try {

            rset = stmt.executeQuery(sql);
        } catch (SQLException e) {
            if(e.getMessage().contains("Table/View 'L1NODES' does not exist."))
                return nodeEntries;
            else
                e.printStackTrace();
            return null;
        }
        while (rset.next()) {
            String nodeID = rset.getString(1);
            int xCoord = rset.getInt(2);
            int yCoord = rset.getInt(3);
            String floor = rset.getString(4);
            String building = rset.getString(5);
            String type = rset.getString(6);
            String longName = rset.getString(7);
            String shortName = rset.getString(8);

            NodeEntry newEntry = new NodeEntry(nodeID, Integer.toString(xCoord), Integer.toString(yCoord), floor, building, type, longName, shortName);
            nodeEntries.add(newEntry);
        }
        return nodeEntries;
    }

    private void createTable(Connection conn, String createCMD) throws SQLException {
        //create the tables

        Statement initStmt = conn.createStatement();
        initStmt.execute(createCMD);

        initStmt.close();

    }

    private void populateNodes(List<String[]> data) throws SQLException //I'll get back to that
    {
        //This here is the tricky part b/c of the formatting....
        for (String[] arr : data) {
            final int x = Integer.parseInt(arr[1]);
            final int y = Integer.parseInt(arr[2]);
            DatabaseAPI.getDatabaseAPI().addNode(arr[0], x, y, arr[3], arr[4], arr[5], arr[6], arr[7]);
        }
    }

    private void populateEdges(List<String[]> data) throws SQLException //I'll get back to that
    {
        //This here is the tricky part b/c of the formatting....
        for (String[] arr : data) {
            DatabaseAPI.getDatabaseAPI().addEdge(arr[0], arr[1], arr[2]);
        }
    }

    private boolean populateData(Connection conn, List<String[]> nodeData, List <String[]> edgeData) throws Exception {
        try {
            final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                    "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                    "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
            createTable(conn, initNodesTable);
            populateNodes(nodeData);


            final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                    "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
            createTable(conn, initEdgesTable);

            populateEdges(edgeData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }



    /**
     * Used to drop a table
     * @param conn The SQL Connection
     * @param table The name of the table to drop
     * @return true if the table was dropped, false otherwise.
     * @author Alex Friedman (ahf)
     */
    private final boolean dropTable(Connection conn, String table)
    {
        try
        {
            String sql = "drop table " + table;
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();

            return true;
        }
        catch (SQLException e)
        {
            //System.out.println("MSG: " + e.getMessage());
            if(e.getMessage().contains("'DROP TABLE' cannot be performed on '" + table + "' because it does not exist."))
                return false;

            e.printStackTrace();
            return false;
        }
    }

    public boolean main(Connection conn, List<String[]> nodeData, List<String[]> edgeData) throws Exception {
        boolean success = false;
        //ConnectionHandler connectionHandler = new ConnectionHandler();
        //Connection conn = connectionHandler.main(false);
        //List<String[]> data = new ArrayList<String[]>();
        /*
        List<String[]> data1 = new ArrayList<String[]>();
        try {
          //  data = CSVManager.load(nodeFile);
            data1 = CSVManager.load(edgeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        dropTable(conn, "L1NODES");
        dropTable(conn, "L1EDGES");

        success = populateData(conn, nodeData, edgeData);
        //conn.close();
        return success;
    }
}
