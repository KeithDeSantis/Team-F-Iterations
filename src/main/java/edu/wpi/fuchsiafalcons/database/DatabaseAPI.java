package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.database.entities.IDatabaseTable;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI {


    /*
     * These two private classes were written by Alex Friedman (ahf). I'll have to do something better than this at some point,
     * but this is the start of how we might be able to methodize some stuff.
     */
    //FIXME: DO BETTER
    private static final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
            "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
            "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";

    private static final IDatabaseTable nodeDatabase = new IDatabaseTable(initNodesTable) {

        final String insertString = "INSERT INTO L1Nodes values(?, ?, ?, ?, ?, ?, ?, ?)";

        @Override
        public PreparedStatement getInsertStatement(String[] data) throws SQLException {
            if(data == null || data.length != 8) //FIXME: DO BETTER NULL CHECKS
                return null;

            int xCoordinate;
            int yCoordinate;

            try {
                xCoordinate = Integer.parseInt(data[1]);
                yCoordinate = Integer.parseInt(data[2]);
            }
            catch (Exception e)
            {
                return null; //Number format wrong
            }

            final PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(insertString);


            stmt.setString(1, data[0]);
            stmt.setInt(2, xCoordinate);
            stmt.setInt(3, yCoordinate);
            stmt.setString(4, data[3]);
            stmt.setString(5, data[4]);
            stmt.setString(6, data[5]);
            stmt.setString(7, data[6]);
            stmt.setString(8, data[7]);

            return stmt;
        }
    };


    //FIXME: DO BETTER
    private static final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
            "startNode varchar(200), endNode varchar(200), primary key(edgeID))";

    private static final IDatabaseTable edgesDatabaseTable = new IDatabaseTable(initEdgesTable) {
        final String insertString = "INSERT INTO L1Edges values(?, ?, ?)";

        @Override
        public PreparedStatement getInsertStatement(String[] data) throws SQLException {
            if(data == null || data.length != 3) //FIXME: DO BETTER NULL CHECKS
                return null;

            PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(insertString);
            stmt.setString(1, data[0]);
            stmt.setString(2, data[1]);
            stmt.setString(3, data[2]);

            return stmt;
        }
    };



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
        //FIXME: DO BETTER!!!
        return addNode(new String[] {
                id,
                "" + x,
                "" + y,
                floor,
                building,
                type,
                longName,
                shortName
        });
    }

    public boolean addNode(String[] data) throws SQLException {
        final PreparedStatement stmt = DatabaseAPI.nodeDatabase.getInsertStatement(data);
        return stmt.executeUpdate() != 0;
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
        //FIXME: DO BETTER
        return addEdge(new String[] {id, startNode, endNode});
    }

    public boolean addEdge(String[] data) throws SQLException {
        final PreparedStatement stmt = DatabaseAPI.edgesDatabaseTable.getInsertStatement(data);
        return stmt.executeUpdate() != 0;
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
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
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
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
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
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
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

    public List<NodeEntry> genNodeEntries(Connection conn) throws SQLException {
        List<NodeEntry> nodeEntries = new ArrayList<>();
        String sql = "SELECT * FROM L1Nodes";
        Statement stmt =  conn.createStatement();

        ResultSet resultSet;
        try {

            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e) {
            if(e.getMessage().contains("Table/View 'L1NODES' does not exist."))
                return nodeEntries;
            else
                e.printStackTrace();
            return null;
        }
        while (resultSet.next()) {
            String nodeID = resultSet.getString(1);
            int xCoordinate = resultSet.getInt(2);
            int yCoordinate = resultSet.getInt(3);
            String floor = resultSet.getString(4);
            String building = resultSet.getString(5);
            String type = resultSet.getString(6);
            String longName = resultSet.getString(7);
            String shortName = resultSet.getString(8);

            NodeEntry newEntry = new NodeEntry(nodeID, Integer.toString(xCoordinate), Integer.toString(yCoordinate), floor, building, type, longName, shortName);
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

    private boolean populateData(Connection conn, List<String[]> nodeData, List <String[]> edgeData){
        try {
            createTable(conn, initNodesTable);
            populateTable(DatabaseAPI.nodeDatabase, nodeData);

            createTable(conn, initEdgesTable);
            populateTable(DatabaseAPI.edgesDatabaseTable, edgeData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void populateTable(IDatabaseTable table, List<String[]> data) throws SQLException {
        for (String[] arr : data) {
           table.getInsertStatement(arr).executeUpdate();
        }
    }

    /**
     * Used to drop a table
     * @param conn The SQL Connection
     * @param table The name of the table to drop
     * @return true if the table was dropped, false otherwise.
     * @author Alex Friedman (ahf)
     */
    private boolean dropTable(Connection conn, String table)
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
            if(e.getMessage().contains("'DROP TABLE' cannot be performed on '" + table + "' because it does not exist."))
                return false;

            e.printStackTrace();
            return false;
        }
    }

    public boolean populateDB(Connection conn, List<String[]> nodeData, List<String[]> edgeData) {
        boolean success = false;

        dropTable(conn, "L1NODES");
        dropTable(conn, "L1EDGES");

        success = populateData(conn, nodeData, edgeData);
        return success;
    }


    private DatabaseAPI() {}

    private static class DatabaseSingletonHelper {
        private static final DatabaseAPI databaseAPI = new DatabaseAPI();
    }

    public static DatabaseAPI getDatabaseAPI() {
        return DatabaseSingletonHelper.databaseAPI;
    }
}
