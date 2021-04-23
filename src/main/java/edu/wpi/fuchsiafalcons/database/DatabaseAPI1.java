package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI1 {
    private static DatabaseAPI1 databaseAPI1;

    private DatabaseEntry nodeHandler;
    private DatabaseEntry edgeHandler;
    private DatabaseEntry serviceRequestHandler;
    private DatabaseEntry userHandler;

    public DatabaseAPI1() {
        this.nodeHandler = new NodeHandler();
        this.edgeHandler = new EdgeHandler();
        this.userHandler = new UserHandler();
        this.serviceRequestHandler = new ServiceRequestHandler();
    }

    public boolean createNodesTable() throws SQLException{
        return nodeHandler.createTable();
    }

    public boolean dropNodesTable() throws SQLException{
        return nodeHandler.dropTable();
    }

    public void populateNodes(List<String[]> entries) throws SQLException{
        nodeHandler.populateTable(entries);
    }

    public boolean addNode(String[] colVals) throws SQLException{
        return nodeHandler.addEntry(colVals);
    }

    public boolean editNode(String id, String newVal, String colName) {
        return nodeHandler.editEntry(id, newVal, colName);
    }

    public boolean deleteNode(String nodeID) throws SQLException{
        return nodeHandler.deleteEntry(nodeID);
    }

    public ArrayList<NodeEntry> genNodeEntries() throws SQLException{
        NodeHandler node = new NodeHandler();
        ArrayList<NodeEntry> ret = node.genNodeEntryObjects("AllNodes");
        return ret;
    }

    public boolean addEdge(String[] colVals) throws SQLException{
        return edgeHandler.addEntry(colVals);
    }

    public boolean editEdge(String id, String newVal, String colName){
        return edgeHandler.editEntry(id, newVal, colName);
    }

    public boolean deleteEdge(String id) throws SQLException {
        return edgeHandler.deleteEntry(id);
    }

    public boolean createEdgesTable() throws SQLException{
        return edgeHandler.createTable();
    }

    public boolean dropEdgesTable() throws SQLException{
        return edgeHandler.dropTable();
    }

    public void populateEdges(List<String[]> data) throws SQLException{
        edgeHandler.populateTable(data);
    }

    public ArrayList<EdgeEntry> genEdgeEntries(String tableName) throws SQLException
    {
        ArrayList<EdgeEntry> ret = new ArrayList<>();
        EdgeHandler edge = new EdgeHandler();
        ret = edge.genEdgeEntryObjects("AllEdges");
        return ret;
    }

    public void addServiceReq(String[] colValues) throws SQLException{
        serviceRequestHandler.addEntry(colValues);
    }

    public boolean addUser(String[] colValues) throws SQLException{
        return userHandler.addEntry(colValues);
    }

    public boolean editUser(String id, String newVal, String colName) throws SQLException{
        return userHandler.editEntry(id, newVal, colName);
    }

    public boolean deleteUser(String username) throws SQLException{
        return userHandler.deleteEntry(username);
    }

    public boolean createUserTable() throws SQLException{
        return userHandler.createTable();
    }

    public boolean dropUsersTable() throws SQLException{
        return userHandler.dropTable();
    }

    public void populateUsers(List<String[]> users) throws SQLException{
        userHandler.populateTable(users);
    }

    private static class DatabaseSingletonHelper{
        private static final DatabaseAPI1 databaseAPI1 = new DatabaseAPI1();
    }
    public static DatabaseAPI1 getDatabaseAPI1(){
        return DatabaseSingletonHelper.databaseAPI1;
    }
}
