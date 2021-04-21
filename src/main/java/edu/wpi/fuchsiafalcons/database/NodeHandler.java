package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class NodeHandler {

    /**
     * API method to add a node into the L1Nodes table
     *
     * @param id        String ID of the node
     * @param x         x-coordinate of the node
     * @param y         y-coordinate of the node
     * @param floor     which floor the node is on
     * @param building  which building the node is in
     * @param type      node type
     * @param longName  long name for node
     * @param shortName short name for node
     * @return true on success, false otherwise
     * @throws SQLException on sql operation error
     */
    public boolean addNode(String id, int x, int y, String floor, String building, String type, String longName, String shortName) throws SQLException {

        final String sql = "INSERT INTO L1Nodes values(?, ?, ?, ?, ?, ?, ?, ?)";

        final PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);


        stmt.setString(1, id);
        stmt.setInt(2, x);
        stmt.setInt(3, y);
        stmt.setString(4, floor);
        stmt.setString(5, building);
        stmt.setString(6, type);
        stmt.setString(7, longName);
        stmt.setString(8, shortName);

        return stmt.executeUpdate() != 0;
    }

        /**
         * Method to delete a node from the L1Nodes table
         *
         * @param nodeID the ID of the node to be deleted
         * @return true on success, false otherwise
         * @throws SQLException on failure of sql operation
         */
        public boolean deleteNode(String nodeID) throws SQLException {
            boolean deleteSuccess = false;
            String sql = "DELETE FROM L1Nodes WHERE nodeID=(?)";
            PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
            stmt.setString(1, nodeID);
            int ret = stmt.executeUpdate();
            if (ret != 0) {
                deleteSuccess = true;
            }
            return deleteSuccess;
        }
    /**
     * method to generate a list of node entry objects from the DB
     *
     * @param conn the active DB conection object to use
     * @return list of node entry objects
     * @throws SQLException if error occurs performing DB operations
     */
    public List<NodeEntry> genNodeEntries(Connection conn) throws SQLException {
        List<NodeEntry> nodeEntries = new ArrayList<>();
        String sql = "SELECT * FROM L1Nodes";
        Statement stmt = conn.createStatement();

        ResultSet rset = null;
        try {
            rset = stmt.executeQuery(sql);
        } catch (SQLException e) {
            if (e.getMessage().contains("Table/View 'L1NODES' does not exist."))
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

    /**
     * Method to get a specific node from the database
     * @param conn the current connection object
     * @param requestNodeID the requested node ID
     * @return a NodeEntry object for the requested node
     * @throws SQLException on error performing DB operations
     */
    public NodeEntry getNode(Connection conn, String requestNodeID) throws SQLException {
        final String sql = "SELECT * FROM L1Nodes WHERE nodeID=(?)";
        final PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, requestNodeID);

        ResultSet rset;
        try {
            rset = stmt.executeQuery();
        } catch (SQLException e) {
            if (e.getMessage().contains("Table/View 'L1NODES' does not exist."))
                return null;
            else
                e.printStackTrace();
            return null;
        }
        while (rset.next()) {
            final String nodeID = rset.getString(1);
            final int xCoord = rset.getInt(2);
            final int yCoord = rset.getInt(3);
            final String floor = rset.getString(4);
            final String building = rset.getString(5);
            final String type = rset.getString(6);
            final String longName = rset.getString(7);
            final String shortName = rset.getString(8);

            return new NodeEntry(nodeID, Integer.toString(xCoord), Integer.toString(yCoord), floor, building, type, longName, shortName);
            // nodeEntries.add(newEntry);
        }
        //return nodeEntries;
        return null;
    }

    /**
     * method to add all the nodes to the databases passed from populateDB()
     *
     * @param data lit of string arrays containing all the node data
     * @throws SQLException on error performing database operations
     */
    public void populateNodes(List<String[]> data) throws SQLException //I'll get back to that
    {
        //This here is the tricky part b/c of the formatting....
        for (String[] arr : data) {
            final int x = Integer.parseInt(arr[1].trim());
            final int y = Integer.parseInt(arr[2].trim());
            DatabaseAPI.getDatabaseAPI().addNode(arr[0], x, y, arr[3], arr[4], arr[5], arr[6], arr[7]);
        }
    }


}
