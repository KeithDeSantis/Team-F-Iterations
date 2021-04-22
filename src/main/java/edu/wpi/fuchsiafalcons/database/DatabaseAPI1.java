package edu.wpi.fuchsiafalcons.database;

import java.sql.*;

public class DatabaseAPI1 {
    private static DatabaseAPI1 databaseAPI1;

    private DatabaseEntry nodeHandler;
    private DatabaseEntry edgeHandler;
    private DatabaseEntry serviceRequestHandler;
    private DatabaseEntry userHandler;

    public DatabaseAPI1(){
        nodeHandler = new NodeHandler();
    }

    public boolean addNode(String[] colVals) throws SQLException{
        return nodeHandler.addEntry(colVals);
    }

    public boolean dropNodesTable() throws SQLException{
        return nodeHandler.dropTable();
    }

    public boolean createNodesTable() throws SQLException{
        return nodeHandler.createTable();
    }

    private static class DatabaseSingletonHelper{
        private static final DatabaseAPI1 databaseAPI1 = new DatabaseAPI1();
    }
    public static DatabaseAPI1 getDatabaseAPI1(){
        return DatabaseSingletonHelper.databaseAPI1;
    }
}
