package edu.wpi.fuchsiafalcons.database;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodeHandler implements DatabaseEntry {
    @Override
    public boolean addEntry(String[] colValues) throws SQLException{
        final String query = "INSERT INTO L1Nodes values(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues){
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean editEntry(String id, String[] newVals) {
        return false;
    }

    @Override
    public boolean deleteEntry(String id) {
        return false;
    }

    @Override
    public ArrayList<Object> genEntryObjects(String tableName) {
        return null;
    }

    @Override
    public boolean createTable() {
        boolean success = false;
        final String initNodesTable = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
        try{
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(initNodesTable);
            stmt.close();
            success = true;
        }
        catch (SQLException e){
            System.out.println("here");
            success = false;
        }
        return success;
    }

    @Override
    public boolean dropTable() {
        boolean success = false;
        String query = "DROP TABLE L1Nodes";
        try {
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(query);
            stmt.close();
            success = true;
        }
        catch (SQLException e){
            success = false;
        }
        return success;
    }

    @Override
    public boolean populateTable(List<String[]> entries) {
        return false;
    }
}
