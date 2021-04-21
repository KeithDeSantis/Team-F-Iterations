package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.ServiceEntry;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReqHandler {

    DatabaseAPI2 DatabaseAPI = new DatabaseAPI2();

    public ArrayList<ServiceEntry> genServiceEntries() throws SQLException
    {
        ArrayList<ServiceEntry> data = new ArrayList<>();
        String sql = "SELECT * FROM SERVICE_REQUESTS";
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        ResultSet rset;
        rset = stmt.executeQuery(sql);

        while (rset.next())
        {
            ServiceEntry entry = new ServiceEntry(rset.getString(1), rset.getString(2),
                    rset.getString(3), rset.getString(4));
            data.add(entry);
        }
        return data;
    }

    /**
     * method to edit a service request column in the database
     *
     * @param uuid    uuid of the service request to be edited
     * @param colName name of the column to be edited
     * @param newVal  new value for the column to have
     * @return true on success, false otherwise
     * @throws SQLException on error performing DB operations
     */
    public boolean editServiceReq(String uuid, String colName, String newVal) throws SQLException {
        String query = "";
        boolean success = false;
        switch (colName) {
            case "name":
                query = "UPDATE SERVICE_REQUESTS SET NAME=(?) WHERE UUID=(?)";
                break;
            case "person":
                query = "UPDATE SERVICE_REQUESTS SET PERSON=(?) WHERE UUID=(?)";
                break;
            case "completed":
                query = "UPDATE SERVICE_REQUESTS SET COMPLETED=(?) WHERE UUID=(?)";
                break;
        }
        return DatabaseAPI.genEditQuery(uuid, newVal, query, success);
    }

    private boolean buildInsertServiceQuery(String uuid, String name, String person, String completed, boolean success, String sql) throws SQLException {
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, uuid);
        stmt.setString(2, name);
        stmt.setString(3, person);
        stmt.setString(4, completed);

        int result = stmt.executeUpdate();
        if (result != 0) {
            success = true;
        }
        return success;
    }


    /**
     * Method to add a single service request to the DB
     *
     *
     * @param uuid
     * @param name      name of the service request to be added
     * @param person    person assigned to the new service request
     * @param completed status of the service request either "true" or "false"
     * @return true on success, false otherwise
     * @throws SQLException on error performing DB operations
     */
    public boolean addServiceReq(String uuid, String name, String person, String completed) throws SQLException {
        boolean success = false;
        final String sql = "INSERT INTO SERVICE_REQUESTS values(?, ?, ?, ?)";


        //FIXME: do better... eventually. Oh hi there Professor Wong!
        try {
            return buildInsertServiceQuery(uuid, name, person, completed, success, sql);
        }
        catch (SQLException e)
        {
            DatabaseAPI.createTable(ConnectionHandler.getConnection(), "CREATE TABLE SERVICE_REQUESTS(uuid varchar(200), name varchar(200), " +
                    "assignedPerson varchar(200), completed varchar(200), primary key(uuid))");

            return buildInsertServiceQuery(uuid, name, person, completed, success, sql);
        }
    }




}
