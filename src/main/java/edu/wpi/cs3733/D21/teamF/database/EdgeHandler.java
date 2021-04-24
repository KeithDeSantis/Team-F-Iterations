package edu.wpi.cs3733.D21.teamF.database;

import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EdgeHandler implements DatabaseEntry {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO AllEdges values(?, ?, ?)";
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
    public boolean editEntry(String id, String newVal, String colName)
    {
        boolean success = false;
        String query = String.format("UPDATE AllEdges SET %s=(?) WHERE EDGEID=(?)", colName);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteEntry(String id) {
        boolean success = false;
        String query = "DELETE FROM AllEdges WHERE EDGEID=(?)";
        try {
            PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
            stmt.setString(1, id);
            stmt.executeUpdate();
            stmt.close();
            success = true;
        }
        catch (SQLException e){
            success = false;
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createTable() {
        boolean success = false;
        final String initNodesTable = "CREATE TABLE AllEdges(EdgeID varchar(200), " +
                "startNode varchar(200), endNode varchar(200), primary key(EdgeID))";
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
        String query = "DROP TABLE AllEdges";
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            DatabaseAPI.getDatabaseAPI().addEdge(arr);
        }
    }

    /**
     * Genereates EdgeEntry objects from all rows currently in the database
     * @return ArrayList of EdgeEntry objects
     * @throws SQLException on error performing DB operations
     */
    public List<EdgeEntry> genEdgeEntryObjects() throws SQLException {
        List<EdgeEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM AllEdges";
        ResultSet rset;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);

        while (rset.next())
        {
            String edgeID = rset.getString(1);
            String startNode = rset.getString(2);
            String endNode = rset.getString(3);

            EdgeEntry newEntry = new EdgeEntry(edgeID, startNode, endNode);
            entries.add(newEntry);
        }
        return entries;
    }

    /**
     * Get a specific edge and EdgeEntry object from the database given the ID
     * @param id the ID of the edge to get
     * @return an EdgeEntry object
     * @throws SQLException on error performing DB operations
     */
    public EdgeEntry getEdge(String id) throws SQLException
    {
        final String sql = "SELECT * FROM AllEdges WHERE edgeID=(?)";
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
            final String edgeID = rset.getString(1);
            final String start = rset.getString(2);
            final String end = rset.getString(3);

            return new EdgeEntry(edgeID, start, end);
        }
        return null;
    }
}
