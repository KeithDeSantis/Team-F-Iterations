package edu.wpi.fuchsiafalcons.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI2 {

    NodeHandler NodeHandler = new NodeHandler();
    EdgeHandler EdgeHandler = new EdgeHandler();

    /**
     * Method to build prepared sql update statement
     *
     * @param tableName the name of the table wth relevant entry
     * @param colName   the name of the column to be modified
     * @param entryType type of entry either node or edge
     * @return complete prepared update query as string
     */
    public String buildUpdateQuery(String tableName, String colName, String entryType) throws NullPointerException {
        String query = "";
        if (entryType.equals("node")) {
            query = "UPDATE " + tableName + " SET " + "node" + colName + "=(?) WHERE nodeid=(?)";
        } else if (entryType.equals("edge") && colName.equals("id")) {
            query = "UPDATE " + tableName + " SET " + "edge" + colName + "=(?) WHERE edgeid=(?)";
        } else if (entryType.equals("edge")) {
            query = "UPDATE " + tableName + " SET " + colName + "=(?) WHERE edgeid=(?)";
        }
        return query;
    }

    /**
     * Method to update the value of a column of a node or an edge
     *
     * @param tableName name of the table with db entry to be modified
     * @param type      either "node" or "edge" specifies which is being changed
     * @param entryID   the ID of the entry being modified
     * @param colName   the name of the column to be modified
     * @param newVal    the new value for the column entry
     * @return true on success, false otherwise
     * @throws SQLException on error with sql operations
     */
    public boolean updateEntry(String tableName, String type, String entryID, String colName, String newVal)
            throws SQLException {
        boolean updateSuccess = false;
        boolean coordUpdate = false;
        String sql = "";
        switch (colName) {
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
        } else {
            stmt.setString(1, newVal);
        }
        stmt.setString(2, entryID);
        int ret = stmt.executeUpdate();
        if (ret != 0) {
            updateSuccess = true;
        }

        return updateSuccess;
    }

    /**
     * Method to make a query to edit table with 3 columns (NEEDS TO BE REFACTORED FOR ALL TABLES)
     *
     * @param first   first column
     * @param second  second column
     * @param third   third column
     * @param success boolean for return value
     * @return true on success, false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean genEditQuery(String first, String second, String third, boolean success) throws SQLException {
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(third);
        stmt.setString(1, second);
        stmt.setString(2, first);
        int ret = stmt.executeUpdate();

        if (ret != 0) {
            success = true;
        }
        return success;
    }

    /**
     * Method to return list of all users in the DB (needed for the UI)
     *
     * @return list of strings with all the usernames
     * @throws SQLException on error performing DB operations
     */
    public ArrayList<String> listAllUsers() throws SQLException {
        ArrayList<String> allUsernames = new ArrayList<>();
        String query = "SELECT * FROM USERS";
        ResultSet rset;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);
        while (rset.next()) {
            allUsernames.add(rset.getString(2));
        }
        return allUsernames;
    }

    /**
     * method to construct add query for tables with 3 columns (NEEDS REFACTORING FOR ALL INSERTIONS)
     *
     * @param first   first string value in column 1
     * @param second  second value for column 2
     * @param third   third value for column 3
     * @param success the boolean value for the return
     * @param sql     the variable to hold the query
     * @return boolean for success or not
     * @throws SQLException
     */
    public boolean buildInsertQuery(String first, String second, String third, boolean success, String sql) throws SQLException {
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, first);
        stmt.setString(2, second);
        stmt.setString(3, third);

        int result = stmt.executeUpdate();
        if (result != 0) {
            success = true;
        }
        return success;
    }


    /**
     * method to create a table
     *
     * @param conn      connection object to the DB to use
     * @param createCMD the string query to create the table
     * @throws SQLException if error occurs while creating the table
     */
    public void createTable(Connection conn, String createCMD) throws SQLException {
        Statement initStmt = conn.createStatement();
        initStmt.execute(createCMD);
        initStmt.close();
    }

    /**
     * Used to drop a table
     *
     * @param conn  The SQL Connection
     * @param table The name of the table to drop
     * @return true if the table was dropped, false otherwise.
     * @author Alex Friedman (ahf)
     */
    public final boolean dropTable(Connection conn, String table) {
        try {
            String sql = "drop table " + table;
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();

            return true;
        } catch (SQLException e) {
            //System.out.println("MSG: " + e.getMessage());
            if (e.getMessage().contains("'DROP TABLE' cannot be performed on '" + table + "' because it does not exist."))
                return false;

            //FIXME: DO BETTER
            //e.printStackTrace();
            return false;
        }
    }


    /**
     * method to create and populate the tables needed in the database (DOES NOT POPULATE SERVICE REQUESTS OR USERS)
     *
     * @param conn     the active connection object to the DB to use
     * @param nodeData list of string arrays with node data
     * @param edgeData list of string arrays with edge data
     * @return true on success, false otherwise
     * @throws Exception if error occurs in DB operations
     */
    private boolean populateData(Connection conn, List<String[]> nodeData, List<String[]> edgeData) throws Exception {
        try {
            final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                    "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                    "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
            createTable(conn, initNodesTable);
            NodeHandler.populateNodes(nodeData);

            final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                    "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
            createTable(conn, initEdgesTable);
            EdgeHandler.populateEdges(edgeData);

            final String initServiceReqTable = "CREATE TABLE SERVICE_REQUESTS(uuid varchar(200), name varchar(200), " +
                    "assignedPerson varchar(200), completed varchar(200), primary key(uuid))";
            createTable(conn, initServiceReqTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
