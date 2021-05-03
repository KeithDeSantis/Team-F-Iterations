package edu.wpi.cs3733.D21.teamF.database;

import java.sql.SQLException;
import java.util.List;
import java.sql.*;

public class SystemPreferences implements DatabaseEntry{
    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO SYSTEM_PREFERENCES VALUES(?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues){
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean editEntry(String id, String val, String colName) throws Exception {
        if (colName.equals("algorithm") || colName.equals("id")) {
            String query = String.format("UPDATE SYSTEM_PREFERENCES SET %s=(?) WHERE id=(?)", colName);
            try {
                PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
                stmt.setString(1, val);
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
        String query = "DELETE FROM SYSTEM_PREFERENCES WHERE id=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, id);
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean createTable() {
        boolean success;
        final String initNodesTable = "CREATE TABLE SYSTEM_PREFERENCES(ID varchar(200), " +
                "ALGORITHM varchar(200), primary key(ID))";
        try{
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(initNodesTable);
            stmt.close();
            success = true;
        }
        catch (SQLException e){
            success = false;
        }
        return success;
    }

    @Override
    public boolean dropTable() {
        boolean success;
        String query = "DROP TABLE SYSTEM_PREFERENCES";
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

    public String getAlgorithm() throws SQLException{
        String query = "SELECT * FROM SYSTEM_PREFERENCES WHERE ID=(?)";
        ResultSet resultSet;
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, "MASTER");
        resultSet = stmt.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(2);
        }
        return null;
    }

    @Override
    public void populateTable(List<String[]> entries) {
    }
}
