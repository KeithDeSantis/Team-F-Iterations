package edu.wpi.cs3733.D21.teamF.database;

import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NodeHandler implements DatabaseEntry {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO AllNodes values(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues){
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean editEntry(String id, String newVal, String colName) throws Exception{
        boolean success = false;
        if (colName.equals("nodeid") || colName.equals("xcoord") || colName.equals("ycoord") || colName.equals("floor")
        || colName.equals("building") || colName.equals("nodetype") || colName.equals("longname") || colName.equals("shortname")) {
            String query = String.format("UPDATE AllNodes SET %s=(?) WHERE NODEID=(?)", colName);
            try {
                PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
                stmt.setString(1, newVal);
                stmt.setString(2, id);
                stmt.executeUpdate();
                stmt.close();
                success = true;
            } catch (SQLException e) {
                success = false;
            }
        }
        else{
            throw new Exception("Invalid column name");
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteEntry(String id) throws SQLException {
        String query = "DELETE FROM AllNodes WHERE NODEID=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, id);
        return stmt.executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            final int x = Integer.parseInt(arr[1].trim());
            final int y = Integer.parseInt(arr[2].trim());
            DatabaseAPI.getDatabaseAPI().addNode(arr);
        }
    }

    /**
     * Generates a list of NodeEntry objects based on current entries in the database
     * @return ArrayList of NodeEntry objects
     * @throws SQLException on error performing DB operations
     */
    public List<NodeEntry> genNodeEntryObjects() throws SQLException{
        List<NodeEntry> entries = new ArrayList<>();
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

    /**
     * get a specific node from the database given the id
     * @param id the id of the node to fetch
     * @return a NodeEntry object for the corresponding database row
     * @throws SQLException on error performing DB operations
     */
    public NodeEntry getNode(String id) throws SQLException
    {
        final String sql = "SELECT * FROM AllNodes WHERE nodeID=(?)";
        final PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, id);

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
        }
        return null;
    }
}
