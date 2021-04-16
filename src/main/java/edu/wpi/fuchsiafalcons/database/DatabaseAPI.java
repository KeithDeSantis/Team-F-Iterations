package edu.wpi.fuchsiafalcons.database;

import java.sql.*;
import java.lang.*;

public class DatabaseAPI {

    private static DatabaseAPI databaseAPI;

    private Connection conn; //FIXME: CONVERT TO SINGLETON

    /**
     * Method to build prepared sql update statement
     * @param tableName the name of the table wth relevant entry
     * @param colName the name of the column to be modified
     * @param entryType type of entry either node or edge
     * @return complete prepared update query as string
     */
    public String buildUpdateQuery(String tableName, String colName, String entryType) throws NullPointerException
    {
        String query = "";
        if (entryType.equals("node")) {
            query = "UPDATE " + tableName + " SET " + "node" + colName + "=(?) WHERE nodeid=(?)";
        }
        else if (entryType.equals("edge")){
            query = "UPDATE " + tableName + " SET " + "edge" + colName + "=(?) WHERE edgeid=(?)";
        }
        return query;
    }
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

        final PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, id);
        stmt.setInt(2, x);
        stmt.setInt(3, y);
        stmt.setString(4, floor);
        stmt.setString(5, building);
        stmt.setString(6, type);
        stmt.setString(7, longName);
        stmt.setString(8, shortName);

        return stmt.executeUpdate() != 0;//isnt that the same?
    }

    /**
     * Method to add an edge to the L1Edges table
     * @param id the edge ID
     * @param startNode starting node of the edge
     * @param endNode ending node of the edge
     * @return true on success, false otherwise
     * @throws SQLException on sql operation error
     */
    public boolean addEdge(String id, String startNode, String endNode) throws SQLException {
        boolean addSuccess = false;
        String sql = "INSERT INTO L1Edges values(?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        stmt.setString(2, startNode);
        stmt.setString(3, endNode);

        int result = stmt.executeUpdate();
        if (result != 0)
        {
            addSuccess = true;
        }
        return addSuccess;
    }

    /**
     * Method to delete a node from the L1Nodes table
     * @param nodeID the ID of the node to be deleted
     * @return true on success, false otherwise
     * @throws SQLException on failure of sql operation
     */
    public boolean deleteNode(String nodeID) throws SQLException
    {
        boolean deleteSuccess = false;
        String sql = "DELETE FROM L1Nodes WHERE nodeID=(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nodeID);
        int ret = stmt.executeUpdate();
        if (ret != 0)
        {
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    /**
     * Method to delete an edge from the L1Edges table
     * @param edgeID the ID of the edge to be deleted
     * @return true on success, false otherwise
     * @throws SQLException on error with sql db operations
     */
    public boolean deleteEdge(String edgeID) throws SQLException
    {
        boolean deleteSuccess = false;
        String sql = "DELETE FROM L1Edges WHERE edgeID=(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, edgeID);
        int ret = stmt.executeUpdate();
        if (ret != 0)
        {
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    /**
     * Method to update the value of a column of a node or an edge
     * @param tableName name of the table with db entry to be modified
     * @param type either "node" or "edge" specifies which is being changed
     * @param entryID the ID of the entry being modified
     * @param colName the name of the column to be modified
     * @param newVal the new value for the column entry
     * @return true on success, false otherwise
     * @throws SQLException on error with sql operations
     */
    public boolean updateEntry(String tableName, String type, String entryID, String colName, String newVal)
            throws SQLException
    {
        boolean updateSuccess = false;
        boolean coordUpdate = false;
        String sql = "";
        switch (colName)
        {
            case "id":
                sql = buildUpdateQuery(tableName, "id", type);
                break;
            case "x":
                //only nodes have x and y coordinates so the type is hardcoded
                sql = buildUpdateQuery(tableName, "xcoord", "node");
                coordUpdate = true;
                break;
            case "y":
                sql = buildUpdateQuery(tableName, "ycoord", "node");
                coordUpdate = true;
                break;
            case "floor":
                //only nodes have a floor so type is hardcoded
                sql = buildUpdateQuery(tableName, "floor", "node");
                break;
            case "building":
                //only nodes have a building so type is hardcoded
                sql = buildUpdateQuery(tableName, "building", "node");
                break;
            case "type":
                //only nodes have a type so we hardcode
                sql = buildUpdateQuery(tableName, "type", "node");
                break;
            case "longName":
                //only nodes have long and short names
                sql = buildUpdateQuery(tableName, "longname", "node");
                break;
            case "shortName":
                sql = buildUpdateQuery(tableName, "shortname", "node");
                break;
            case "startNode":
                //only edges have start and end nodes
                sql = buildUpdateQuery(tableName, "startnode", "edge");
                break;
            case "endNode":
                sql = buildUpdateQuery(tableName, "endnode", "edge");
                break;
        }
        PreparedStatement stmt = conn.prepareStatement(sql);
        if (coordUpdate) {
            stmt.setInt(1, Integer.parseInt(newVal));
        }
        else {
            stmt.setString(1, newVal);
        }
        stmt.setString(2, entryID);
        int ret = stmt.executeUpdate();
        if (ret != 0){
            updateSuccess = true;
        }

        return updateSuccess;
    }

    private DatabaseAPI(Connection connection) {
        conn = connection;
    }

    public static DatabaseAPI getDatabaseAPI() {
//        if(ConnectionHandler.getConnection() == null)
//            return null;
        if(databaseAPI == null)
            return databaseAPI = new DatabaseAPI(ConnectionHandler.getConnection());
        return databaseAPI;
    }
}
