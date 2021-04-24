package edu.wpi.cs3733.D21.teamF.database;

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
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            byte[] bytes = md.digest(plainText.getBytes());
            StringBuilder builder = new StringBuilder();
            for (int i=0; i<bytes.length; i++)
            {
                builder.append(Integer.toString((bytes[i] & 0xFF) + 0x100, 16).substring(1));
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
        ResultSet rset;
        Statement stmt = ConnectionHandler.getConnection().createStatement();
        rset = stmt.executeQuery(query);
        while (rset.next()) {
            allUsernames.add(rset.getString(3));
        }
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
        ResultSet rset;
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, username);
        rset = stmt.executeQuery();

        byte[] salt;
        if (rset.next()) {
            salt = rset.getBytes(5);
            if (encryptPassword(password, salt).equals(rset.getString(4))) {
                authenticated = true;
            }
        }
        return authenticated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEntry(String[] colValues) throws SQLException {
        final String query = "INSERT INTO USERS values(?, ?, ?, ?, ?)";
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
        stmt.setBytes(5, salt);
        return stmt.executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean editEntry(String username, String val, String colName) throws Exception{
        boolean success = false;
        if (colName.equals("username") || colName.equals("type") || colName.equals("password")) {
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
        boolean success = false;
        final String initUserTable = "CREATE TABLE USERS(userid varchar(200), type varchar(200), " +
                "username varchar(200), password varchar(200), salt blob(20), primary key(username))";
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateTable(List<String[]> entries) throws SQLException {
        for (String[] arr : entries) {
            DatabaseAPI.getDatabaseAPI().addUser(arr);
        }
    }
}
