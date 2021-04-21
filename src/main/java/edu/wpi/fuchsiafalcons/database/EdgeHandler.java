package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.EdgeEntry;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class EdgeHandler {

    /**
     * Method to add an edge to the L1Edges table
     *
     * @param id        the edge ID
     * @param startNode starting node of the edge
     * @param endNode   ending node of the edge
     * @return true on success, false otherwise
     * @throws SQLException on sql operation error
     */
    public boolean addEdge(String id, String startNode, String endNode) throws SQLException {
        boolean addSuccess = false;
        String sql = "INSERT INTO L1Edges values(?, ?, ?)";

        final PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);


        stmt.setString(1, id);
        stmt.setString(2, startNode);
        stmt.setString(3, endNode);

        return stmt.executeUpdate() != 0;
        //return DatabaseAPI2.buildInsertQuery(id, startNode, endNode, addSuccess, sql);
    }

    /**
     * Method to delete an edge from the L1Edges table
     *
     * @param edgeID the ID of the edge to be deleted
     * @return true on success, false otherwise
     * @throws SQLException on error with sql db operations
     */
    public boolean deleteEdge(String edgeID) throws SQLException {
        boolean deleteSuccess = false;
        String sql = "DELETE FROM L1Edges WHERE edgeID=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, edgeID);
        int ret = stmt.executeUpdate();
        if (ret != 0) {
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    /**
     * method to generate all the edge entry objects for the edge editor
     *
     * @param conn the active connection object to use
     * @return lits of edge entry objects
     * @throws SQLException if error occurs performing DB operations
     */
    public List<EdgeEntry> genEdgeEntries(Connection conn) throws SQLException {
        List<EdgeEntry> edgeEntries = new ArrayList<>();
        String sql = "SELECT * FROM L1Edges";
        Statement stmt = conn.createStatement();
        ResultSet rset = null;

        rset = stmt.executeQuery(sql);
        while (rset.next()) {
            String edgeID = rset.getString(1);
            String startNode = rset.getString(2);
            String endNode = rset.getString(3);

            EdgeEntry newEntry = new EdgeEntry(edgeID, startNode, endNode);
            edgeEntries.add(newEntry);
        }
        return edgeEntries;
    }

    /**
     * Method to add all the edges in the list of string arrays passed to populateDB
     *
     * @param data list of string arrays containing edge data
     * @throws SQLException on error executing DB operations
     */
    public void populateEdges(List<String[]> data) throws SQLException //I'll get back to that
    {
        //This here is the tricky part b/c of the formatting....
        for (String[] arr : data) {
            DatabaseAPI.getDatabaseAPI().addEdge(arr[0], arr[1], arr[2]);
        }
    }

}
