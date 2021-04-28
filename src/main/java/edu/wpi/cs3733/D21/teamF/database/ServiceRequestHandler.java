package edu.wpi.cs3733.D21.teamF.database;

import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestHandler implements DatabaseEntry {

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        System.out.println(colValues[0]);
        System.out.println(colValues[1]);
        System.out.println(colValues[2]);
        System.out.println(colValues[3]);
        System.out.println(colValues[4]);
        final String query = "INSERT INTO service_requests values(?, ?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues) {
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean editEntry(String id, String newVal, String colName) throws Exception{
        boolean success = false;
        if (colName.equals("name") || colName.equals("assignedperson") || colName.equals("completed") ||
        colName.equals("Additional instructions")) {
            String query = String.format("UPDATE service_requests SET %s=(?) WHERE uuid=(?)", colName);
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
     * @{inheritDoc}
     */
    @Override
    public boolean deleteEntry(String id) throws SQLException {
        String query = "DELETE FROM service_requests WHERE uuid=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, id);
        return stmt.executeUpdate() != 0;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean createTable() {
        boolean success = false;
        final String initServicesTable = "CREATE TABLE SERVICE_REQUESTS(uuid varchar(200), name varchar(200)," +
                "assignedPerson varchar(200), completed varchar(200), additionalInstructions varchar(700), primary key(uuid))";
        try {
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(initServicesTable);
            stmt.close();
            success = true;
        } catch (SQLException e) {
            success = false;
        }
        return success;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean dropTable() {
        boolean success = false;
        String query = "DROP TABLE service_requests";
        try {
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(query);
            stmt.close();
            success = true;
        } catch (SQLException e) {
            success = false;
        }
        return success;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            DatabaseAPI.getDatabaseAPI().addServiceReq(arr);
        }
    }

    /**
     * Generate a list of ServiceEntry objects based on current database entries
     * @return ArrayList of ServiceEntry objects
     * @throws SQLException on error performing DB operations
     */
    public List<ServiceEntry> genServiceRequestEntries() throws SQLException{
        List<ServiceEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM service_requests";
        ResultSet rset;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);

        while (rset.next())
        {
            String uuid = rset.getString(1);
            String name = rset.getString(2);
            String assignedPerson = rset.getString(3);
            String completed = rset.getString(4);
            String additionalInstructions = rset.getString(5);

            ServiceEntry newEntry = new ServiceEntry(uuid, name, assignedPerson, completed, additionalInstructions);
            entries.add(newEntry);
        }
        rset.close();

        return entries;
    }
}

