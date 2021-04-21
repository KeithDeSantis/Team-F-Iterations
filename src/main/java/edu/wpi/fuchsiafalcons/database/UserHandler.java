package edu.wpi.fuchsiafalcons.database;

import java.sql.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class UserHandler {

    DatabaseAPI2 DatabaseAPI = new DatabaseAPI2();

    /**
     * adds a user to the database in the table USERS
     *
     * @param userType employee, visitor, admin
     * @param username username of the user
     * @param password the user's password
     * @return true on success false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean addUser(String userType, String username, String password) throws SQLException {
        boolean success = false;
        String sql = "INSERT INTO USERS values(?, ?, ?)";

        return DatabaseAPI.buildInsertQuery(userType, username, password, success, sql);
    }


    /**
     * method to edit a user's username, password or type in the DB
     *
     * @param userName the username of the user to edit
     * @param colName  the column name to edit
     * @param newVal   the new value to be inserted
     * @return true on success, false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean editUser(String userName, String colName, String newVal) throws SQLException {
        String query = "";
        boolean success = false;
        switch (colName) {
            case "username":
                query = "UPDATE USERS SET USERNAME=(?) WHERE USERNAME=(?)";
                break;
            case "type":
                query = "UPDATE USERS SET TYPE=(?) WHERE USERNAME=(?)";
                break;
            case "password":
                query = "UPDATE USERS SET PASSWORD=(?) WHERE USERNAME=(?)";
                break;
        }
        return DatabaseAPI.genEditQuery(userName, newVal, query, success);
    }

    /**
     * method for deleting a user from the USERS database
     *
     * @param username the username of the user to delete
     * @return true on success, false otherwise
     * @throws SQLException on error performing SQL operations
     */
    public boolean deleteUser(String username) throws SQLException {
        boolean deleteSuccess = false;
        String sql = "DELETE FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, username);
        int ret = stmt.executeUpdate();
        if (ret != 0) {
            deleteSuccess = true;
        }
        return deleteSuccess;
    }

    /**
     * Method to return list of all users in the DB (needed for the UI)
     *
     * @return list of strings with all the usernames
     * @throws SQLException on error performing DB operations
     */
    public ArrayList<String> listAllUsers() throws SQLException {
        ArrayList<String> allUsernames = new ArrayList<>();
        String query = "SELECT * FROM USERS";
        ResultSet rset;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);
        while (rset.next()) {
            allUsernames.add(rset.getString(2));
        }
        return allUsernames;
    }

    /**
     * method to check a given username and password and see if it is valid
     * @param username         the username for the account to login
     * @param suppliedPassword the password supplied by the user
     * @return true if the password is correct
     * @throws SQLException on error with DB operations
     */
    public boolean authenticate(String username, String suppliedPassword) throws SQLException {
        boolean authenticated = false;
        String query = "SELECT * FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rset;
        rset = stmt.executeQuery();
        if (rset.next()) {
            String validPass = rset.getString(3);

            if (validPass.equals(suppliedPassword)) {
                authenticated = true;
            }
        }
        return authenticated;
    }

    public void populateUsers(Connection conn) throws Exception {
        final String initUserTable = "CREATE TABLE USERS(type varchar(200), " +
                "username varchar(200), password varchar(200), primary key(username))";
        DatabaseAPI.createTable(conn, initUserTable);
        addUser("administrator", "admin", "admin");
    }

}
