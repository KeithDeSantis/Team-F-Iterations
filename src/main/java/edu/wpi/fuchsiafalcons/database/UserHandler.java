package edu.wpi.fuchsiafalcons.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserHandler implements DatabaseEntry{

    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO USERS values(?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        int colCounter = 1;
        for (String s : colValues){
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean editEntry(String username, String val, String colName) {
        boolean success = false;
        String query = String.format("UPDATE USERS SET %s=(?) WHERE USERNAME=(?)", colName);
        try {
            PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
            stmt.setString(1, val);
            stmt.setString(2, username);
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
    public boolean deleteEntry(String username) throws SQLException {
        String query = "DELETE FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        return stmt.executeUpdate() != 0;
    }

    @Override
    public boolean createTable() {
        boolean success = false;
        final String initUserTable = "CREATE TABLE USERS(userid varchar(200), type varchar(200), " +
                "username varchar(200), password varchar(200), primary key(userid))";
        try{
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(initUserTable);
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
        boolean success = false;
        String query = "DROP TABLE USERS";
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
            DatabaseAPI1.getDatabaseAPI1().addUser(arr);
        }
    }
}
