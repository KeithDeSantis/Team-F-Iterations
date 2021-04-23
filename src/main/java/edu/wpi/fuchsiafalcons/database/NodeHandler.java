package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodeHandler implements DatabaseEntry {
    @Override
    public boolean addEntry(String[] colValues) throws SQLException{
        final String query = "INSERT INTO AllNodes values(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues){
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean editEntry(String id, String newVal, String colName) {
        boolean success = false;
        String query = String.format("UPDATE AllNodes SET %s=(?) WHERE NODEID=(?)", colName);
        try {
            PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
            stmt.setString(1, newVal);
            stmt.setString(2, id);
            stmt.executeUpdate();
            stmt.close();
            success = true;
        }
        catch (SQLException e) {
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteEntry(String id) throws SQLException {
        String query = "DELETE FROM AllNodes WHERE NODEID=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, id);
        return stmt.executeUpdate() != 0;
    }

    public ArrayList<NodeEntry> genNodeEntryObjects(String tableName) throws SQLException{
        ArrayList<NodeEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM AllNodes";
        ResultSet rset;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);

        while (rset.next())
        {
            String nodeID = rset.getString(1);
            int xCoord = rset.getInt(2);
            int yCoord = rset.getInt(3);
            String floor = rset.getString(4);
            String building = rset.getString(5);
            String type = rset.getString(6);
            String longName = rset.getString(7);
            String shortName = rset.getString(8);

            NodeEntry newEntry = new NodeEntry(nodeID, Integer.toString(xCoord), Integer.toString(yCoord), floor, building, type, longName, shortName);
            entries.add(newEntry);
        }

        return entries;
    }

    @Override
    public boolean createTable() {
        boolean success = false;
        final String initNodesTable = "CREATE TABLE AllNodes(NodeID varchar(200), " +
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
        String query = "DROP TABLE AllNodes";
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
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            final int x = Integer.parseInt(arr[1].trim());
            final int y = Integer.parseInt(arr[2].trim());
            DatabaseAPI1.getDatabaseAPI1().addNode(arr);
        }
    }
}
