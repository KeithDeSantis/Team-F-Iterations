package edu.wpi.cs3733.D21.teamF.database;

import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestHandler implements DatabaseEntry {


    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO service_requests values(?, ?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues) {
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }


    @Override
    public boolean editEntry(String id, String newVal, String colName) throws Exception{
        if (colName.equals("name") || colName.equals("assignedperson") || colName.equals("completed") ||
        colName.equals("Additional instructions")) {
            String query = String.format("UPDATE service_requests SET %s=(?) WHERE uuid=(?)", colName);
            try {
                PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
                stmt.setString(1, newVal);
                stmt.setString(2, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException e) {
                return false;
            }
        }
        else{
            throw new Exception("Invalid column name");
        }
        return true;
    }

    @Override
    public boolean deleteEntry(String id) throws SQLException {
        String query = "DELETE FROM service_requests WHERE uuid=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, id);
        return stmt.executeUpdate() != 0;
    }


    @Override
    public boolean createTable() {
        boolean success;
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

    @Override
    public boolean dropTable() {
        String query = "DROP TABLE service_requests";
        try {
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(query);
            stmt.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            DatabaseAPI.getDatabaseAPI().addServiceReq(arr);
        }
    }

    /**
     * Generates a ServiceEntry object for a service entry given the uuid
     * @param uuid the uuid of the service request/ticket to fetch
     * @return the ServiceEntry object for the row
     * @throws SQLException on error with DB operations
     */
    public ServiceEntry getServiceRequest(String uuid) throws SQLException{
        final String sql = "SELECT * FROM SERVICE_REQUESTS WHERE uuid=(?)";
        final PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, uuid);

        ResultSet resultSet;
        try {
            resultSet = stmt.executeQuery();
        } catch (SQLException e) {
            if (e.getMessage().contains("Table/View 'L1NODES' does not exist."))
                return null;
            else
                e.printStackTrace();
            return null;
        }
        while (resultSet.next()) {
            String requestID = resultSet.getString(1);
            String requestName = resultSet.getString(2);
            String requestPerson = resultSet.getString(3);
            String requestCompleted = resultSet.getString(4);
            String requestInfo = resultSet.getString(5);
            resultSet.close();
            return new ServiceEntry(requestID, requestName, requestPerson, requestCompleted, requestInfo);
        }
        resultSet.close();
        return null;
    }


    /**
     * Generate a list of ServiceEntry objects based on current database entries
     * @return ArrayList of ServiceEntry objects
     * @throws SQLException on error performing DB operations
     */
    public List<ServiceEntry> genServiceRequestEntries() throws SQLException{
        List<ServiceEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM service_requests";
        ResultSet resultSet;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        resultSet = stmt.executeQuery(query);

        while (resultSet.next())
        {
            String uuid = resultSet.getString(1);
            String name = resultSet.getString(2);
            String assignedPerson = resultSet.getString(3);
            String completed = resultSet.getString(4);
            String additionalInstructions = resultSet.getString(5);

            ServiceEntry newEntry = new ServiceEntry(uuid, name, assignedPerson, completed, additionalInstructions);
            entries.add(newEntry);
        }
        resultSet.close();

        return entries;
    }
}

