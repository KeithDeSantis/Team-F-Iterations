package edu.wpi.cs3733.D21.teamF.database;

import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements DatabaseEntry {

    /**
     * verify that the admin user exists
     * @return true if admin exists, false otherwise
     * @throws SQLException on error with query
     */
    public boolean verifyAdmin() throws SQLException{
        boolean exists = false;
        String sql = "SELECT * FROM USERS WHERE USERNAME='admin'";
        ResultSet resultSet;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        resultSet = stmt.executeQuery(sql);
        while (resultSet.next()){
            if (resultSet.getString(1).equals("admin") && resultSet.getString(2).equals("administrator")){
                exists = true;
            }
        }
        resultSet.close();

        return exists;
    }

    /**
     * method to generate random salt, stored in user's DB row entry
     * @return byte array representation of the salt
     * @throws NoSuchAlgorithmException if algorithm specified is not found
     * @throws NoSuchProviderException if no provider
     */
    public static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        SecureRandom sr =  SecureRandom.getInstance("SHA1PRNG", "SUN");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * encrypts the plaintext password provided, MD5 and salt
     * @param plainText the plaintext password to encrypt
     * @param salt the salt bytearray to append to the plaintext and hash
     * @return returns a string representation of the encrypted password
     */
    private static String encryptPassword(String plainText, byte[] salt)
    {
        String cipherText = "";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(plainText.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) {
                builder.append(Integer.toString((aByte & 0xFF) + 0x100, 16).substring(1));
            }
            cipherText = builder.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Get a list of all the usernames in the DB
     * @return List of strings for usernames
     * @throws SQLException on error with DB operations
     */
    public List<String> listAllUsers() throws SQLException {
        List<String> allUsernames = new ArrayList<>();
        String query = "SELECT * FROM USERS";
        ResultSet resultSet;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            allUsernames.add(resultSet.getString(3));
        }
        resultSet.close();
        return allUsernames;
    }

    /**
     * Query the database and ensure the username and password given match the DB (hash matching)
     * @param username the string username supplied from the user
     * @param password the string password supplied from the user
     * @return true if the credentials match, false otherwise
     * @throws SQLException on error performing DB operations
     */
    public boolean authenticate(String username, String password) throws SQLException
    {
        boolean authenticated = false;
        String query = "SELECT * FROM USERS WHERE USERNAME=(?)";
        ResultSet resultSet;
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        resultSet = stmt.executeQuery();

        byte[] salt;
        if (resultSet.next()) {
            salt = resultSet.getBytes(6);
            if (encryptPassword(password, salt).equals(resultSet.getString(4)) &&
                    resultSet.getString(5).equals("true")) {
                authenticated = true;
            }
        }
        resultSet.close();
        return authenticated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO USERS values(?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);

        byte[] salt = null;
        try {
            salt = getSalt();
            String encryptedPassword = encryptPassword(colValues[3], salt);
            colValues[3] = encryptedPassword;
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException errTwo){
            errTwo.printStackTrace();
            errTwo.printStackTrace();
        }

        int colCounter = 1;
        for (String s : colValues){
            stmt.setString(colCounter, s);
            colCounter = colCounter + 1;
        }
        stmt.setBytes(6, salt);
        return stmt.executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean editEntry(String username, String val, String colName) throws Exception{
        boolean success;
        if (colName.equals("username") || colName.equals("type") || colName.equals("password") || colName.equals("cleared")) {
            String query = String.format("UPDATE USERS SET %s=(?) WHERE USERNAME=(?)", colName);
            try {
                PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
                stmt.setString(1, val);
                stmt.setString(2, username);
                stmt.executeUpdate();
                stmt.close();
                success = true;
            } catch (SQLException e) {
                success = false;
            }
        }
        else{
            throw new Exception("invalid column name");
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteEntry(String username) throws SQLException {
        String query = "DELETE FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        return stmt.executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createTable() {
        boolean success;
        final String initUserTable = "CREATE TABLE USERS(userid varchar(50), type varchar(200), " +
                "username varchar(200), password varchar(200), cleared varchar(20), salt blob(20), primary key(username))";
        try{
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(initUserTable);
            stmt.close();
            success = true;
        }
        catch (SQLException e){
          //  e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dropTable() {
        boolean success;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            DatabaseAPI.getDatabaseAPI().addUser(arr);
        }
    }

    /**
     * Generates AccountEntry objects based on the data in the database
     * @return List of generated AccountEntry objects
     * @throws SQLException on error performing DB operations
     */
    public List<AccountEntry> genAccountEntryObjects() throws SQLException {
        List<AccountEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM USERS";
        ResultSet resultSet;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        resultSet = stmt.executeQuery(query);

        while (resultSet.next())
        {
            String username = resultSet.getString(3);
            String password = resultSet.getString(4);
            String usertype = resultSet.getString(2);
            String status = resultSet.getString(5);

            AccountEntry newEntry = new AccountEntry(username, password, usertype, status);
            entries.add(newEntry);
        }
        resultSet.close();
        return entries;
    }

    /**
     * Make an AccountEntry object for a user given a username
     * @param username username of user to make entry for
     * @return AccountEntry object
     * @throws SQLException on error with DB operations
     */
    public AccountEntry getUser(String username) throws SQLException{
        String sql = "SELECT * FROM USERS WHERE USERNAME=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet resultSet;
        AccountEntry user = null;
        resultSet = stmt.executeQuery();
        while (resultSet.next()){
            String type = resultSet.getString(2);
            String password = resultSet.getString(4);
            String status = resultSet.getString(5);
            user = new AccountEntry(username, password, type, status);
        }
        stmt.close();
        resultSet.close();
        return user;
    }

}
