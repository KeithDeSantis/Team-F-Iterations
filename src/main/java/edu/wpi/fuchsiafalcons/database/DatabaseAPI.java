package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import javax.xml.transform.Result;
import java.util.Base64;
import java.util.Base64.*;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI {

    private static DatabaseAPI databaseAPI;

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

        return stmt.executeUpdate() != 0;//isnt that the same?
    }


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

        return buildInsertQuery(id, startNode, endNode, addSuccess, sql);
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
     * method to edit a user's username, password or type in the DB
     *
     * @param userName the username of the user to edit
     * @param colName  the column name to edit
     * @param newVal   the new value to be inserted
     * @return true on success, false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean editUser(String userName, String colName, String newVal) throws SQLException {
        String query = "";
        boolean success = false;
        switch (colName) {
            case "username":
                query = "UPDATE USERS SET USERNAME=(?) WHERE USERNAME=(?)";
                break;
            case "type":
                query = "UPDATE USERS SET TYPE=(?) WHERE USERNAME=(?)";
                break;
            case "password":
                query = "UPDATE USERS SET PASSWORD=(?) WHERE USERNAME=(?)";
                break;
        }
        return genEditQuery(userName, newVal, query, success);
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
    private boolean genEditQuery(String first, String second, String third, boolean success) throws SQLException {
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
     * method for deleting a user from the USERS database
     *
     * @param username the username of the user to delete
     * @return true on success, false otherwise
     * @throws SQLException on error performing SQL operations
     */
    public boolean deleteUser(String username) throws SQLException {
        boolean deleteSuccess = false;
        String sql = "DELETE FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, username);
        int ret = stmt.executeUpdate();
        if (ret != 0) {
            deleteSuccess = true;
        }
        return deleteSuccess;
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
     * method to check a given username and password and see if it is valid
     * @param username         the username for the account to login
     * @param suppliedPassword the password supplied by the user
     * @return true if the password is correct
     * @throws SQLException on error with DB operations
     */
    public boolean authenticate(String username, String suppliedPassword) throws SQLException {
        boolean authenticated = false;
        String query = "SELECT * FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rset;
        rset = stmt.executeQuery();
        if (rset.next()) {
            String validPass = rset.getString(3);

            if (validPass.equals(suppliedPassword)) {
                authenticated = true;
            }
        }
        return authenticated;
    }

    /**
     * adds a user to the database in the table USERS
     *
     * @param userType employee, visitor, admin
     * @param username username of the user
     * @param password the user's password
     * @return true on success false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean addUser(String userType, String username, String password) throws SQLException {
        boolean success = false;
        String sql = "INSERT INTO USERS values(?, ?, ?)";

        return buildInsertQuery(userType, username, password, success, sql);
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
    private boolean buildInsertQuery(String first, String second, String third, boolean success, String sql) throws SQLException {
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
     * method to edit a service request column in the database
     *
     * @param name    name of the service request to be edited
     * @param colName name of the column to be edited
     * @param newVal  new value for the column to have
     * @return true on success, false otherwise
     * @throws SQLException on error performing DB operations
     */
    public boolean editServiceReq(String name, String colName, String newVal) throws SQLException {
        String query = "";
        boolean success = false;
        switch (colName) {
            case "name":
                query = "UPDATE SERVICE_REQUESTS SET NAME=(?) WHERE NAME=(?)";
                break;
            case "person":
                query = "UPDATE SERVICE_REQUESTS SET PERSON=(?) WHERE NAME=(?)";
                break;
            case "completed":
                query = "UPDATE SERVICE_REQUESTS SET COMPLETED=(?) WHERE NAME=(?)";
                break;
        }
        return genEditQuery(name, newVal, query, success);
    }

    /**
     * Method to add a single service request to the DB
     *
     * @param name      name of the service request to be added
     * @param person    person assigned to the new service request
     * @param completed status of the service request either "true" or "false"
     * @return true on success, false otherwise
     * @throws SQLException on error performing DB operations
     */
    public boolean addServiceReq(String name, String person, String completed) throws SQLException {
        boolean success = false;
        String sql = "INSERT INTO SERVICE_REQUESTS values(?, ?, ?)";

        return buildInsertQuery(name, person, completed, success, sql);
    }

    /**
     * Method for adding multiple service requests to the DB at once if needed
     *
     * @param reqData list of strings containing the service request data
     * @throws SQLException on error performing DB operations
     */
    public void populateReqs(List<String[]> reqData) throws SQLException {
        for (String[] arr : reqData) {
            addServiceReq(arr[0], arr[1], arr[2]);
        }
    }

    public void populateUsers(Connection conn) throws Exception {
        final String initUserTable = "CREATE TABLE USERS(type varchar(200), " +
                "username varchar(200), password varchar(200), primary key(username))";
        createTable(conn, initUserTable);
        addUser("administrator", "admin", "admin");
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
            populateNodes(nodeData);

            final String initEdgesTable = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                    "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
            createTable(conn, initEdgesTable);
            populateEdges(edgeData);

            final String initServiceReqTable = "CREATE TABLE SERVICE_REQUESTS(name varchar(200), " +
                    "assignedPerson varchar(200), completed varchar(200))";
            createTable(conn, initServiceReqTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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

    /**
     * Method to populate the DB with initial values of edges and nodes
     *
     * @param conn     the active connection object to use
     * @param nodeData list of string arrays containing node column values
     * @param edgeData list of string arrays containing edge column values
     * @return true on succes, false otherwise
     * @throws Exception for errors during execution of DB operations
     */
    public boolean populateDB(Connection conn, List<String[]> nodeData, List<String[]> edgeData) throws Exception {
        boolean success = false;

        dropTable(conn, "L1NODES");
        dropTable(conn, "L1EDGES");
        dropTable(conn, "SERVICE_REQUESTS");
        dropTable(conn, "USERS");

        success = populateData(conn, nodeData, edgeData);
        return success;
    }

    private DatabaseAPI() {
    }

    private static class DatabaseSingletonHelper {
        private static final DatabaseAPI databaseAPI = new DatabaseAPI();
    }

    public static DatabaseAPI getDatabaseAPI() {
        return DatabaseSingletonHelper.databaseAPI;
    }
}
