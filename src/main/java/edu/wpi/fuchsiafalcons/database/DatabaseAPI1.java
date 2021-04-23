package edu.wpi.fuchsiafalcons.database;

import java.sql.*;
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
       // this.serviceRequestHandler = new serviceRequestHandler();
        //this.userHandler = new UserHandler();
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
    public boolean deleteNode(String nodeID) throws SQLException{
        return nodeHandler.deleteEntry(nodeID);
    }




    public boolean addEdge(String[] colVals) throws SQLException{
        return edgeHandler.addEntry(colVals);
    }


    public boolean createEdgesTable() throws SQLException{
        return edgeHandler.createTable();
    }

    public boolean dropEdgesTable() throws SQLException{
        return edgeHandler.dropTable();
    }


    private static class DatabaseSingletonHelper{
        private static final DatabaseAPI1 databaseAPI1 = new DatabaseAPI1();
    }
    public static DatabaseAPI1 getDatabaseAPI1(){
        return DatabaseSingletonHelper.databaseAPI1;
    }
}
