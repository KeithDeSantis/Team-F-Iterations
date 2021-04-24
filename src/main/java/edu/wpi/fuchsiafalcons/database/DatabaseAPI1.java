package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.entities.ServiceEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI1 {
    private static DatabaseAPI1 databaseAPI1;

    private final DatabaseEntry nodeHandler;
    private final DatabaseEntry edgeHandler;
    private final DatabaseEntry serviceRequestHandler;
    private final DatabaseEntry userHandler;

    public DatabaseAPI1() {
        this.nodeHandler = new NodeHandler();
        this.edgeHandler = new EdgeHandler();
        this.userHandler = new UserHandler();
        this.serviceRequestHandler = new ServiceRequestHandler();
    }

    public boolean createNodesTable() {
        return nodeHandler.createTable();
    }

    public boolean dropNodesTable() {
        return nodeHandler.dropTable();
    }

    public void populateNodes(List<String[]> entries) throws SQLException{
        nodeHandler.populateTable(entries);
    }

    public boolean addNode(String[] colVals) throws SQLException {
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
        ArrayList<NodeEntry> ret = node.genNodeEntryObjects();
        return ret;
    }

    public NodeEntry getNode(String id) throws SQLException{
        NodeHandler node = new NodeHandler();
        return node.getNode(id);
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

    public boolean createEdgesTable() {
        return edgeHandler.createTable();
    }

    public boolean dropEdgesTable() {
        return edgeHandler.dropTable();
    }

    public void populateEdges(List<String[]> data) throws SQLException{
        edgeHandler.populateTable(data);
    }

    public ArrayList<EdgeEntry> genEdgeEntries() throws SQLException
    {
        ArrayList<EdgeEntry> ret;
        EdgeHandler edge = new EdgeHandler();
        ret = edge.genEdgeEntryObjects();
        return ret;
    }

    public EdgeEntry getEdge(String id) throws SQLException{
        EdgeHandler edge = new EdgeHandler();
        return edge.getEdge(id);
    }

    public boolean addServiceReq(String[] colValues) throws SQLException{
        return serviceRequestHandler.addEntry(colValues);
    }

    public boolean editServiceRequest(String id, String newVal, String colName){
        return serviceRequestHandler.editEntry(id, newVal, colName);
    }

    public boolean deleteServiceRequest(String id) throws SQLException {
        return serviceRequestHandler.deleteEntry(id);
    }

    public boolean createServiceRequestTable(){
        return serviceRequestHandler.createTable();
    }

    public boolean dropServiceRequestTable(){
        return serviceRequestHandler.dropTable();
    }

    public void populateServiceRequestTable(List<String[]> entries) throws SQLException {
        serviceRequestHandler.populateTable(entries);
    }

    public ArrayList<ServiceEntry> genServiceRequestEntries() throws SQLException{
        ServiceRequestHandler s = new ServiceRequestHandler();
        return s.genServiceRequestEntries();
    }

    public boolean addUser(String[] colValues) throws SQLException{
        return userHandler.addEntry(colValues);
    }

    public boolean editUser(String id, String newVal, String colName) {
        return userHandler.editEntry(id, newVal, colName);
    }

    public boolean deleteUser(String username) throws SQLException{
        return userHandler.deleteEntry(username);
    }

    public boolean createUserTable() {
        return userHandler.createTable();
    }

    public boolean dropUsersTable() {
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
