package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.ServiceEntry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ServiceRequestHandler implements DatabaseEntry {

    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO service_requests values(?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues) {
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean editEntry(String id, String newVal, String colName) {
        boolean success = false;
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
        return success;
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
        boolean success = false;
        final String initServicesTable = "CREATE TABLE SERVICE_REQUESTS(uuid varchar(200), name varchar(200)," +
                "assignedPerson varchar(200), completed varchar(200), primary key(uuid))";
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

    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            DatabaseAPI1.getDatabaseAPI1().addServiceReq(arr);
        }

    }
}

