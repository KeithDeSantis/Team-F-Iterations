package edu.wpi.cs3733.D21.teamF.entities;


import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;

import java.sql.SQLException;

public class CurrentUser {

    /**
     * Tracks if the user has been authenticated
     */
    private boolean isAuthenticated = false;
    private AccountEntry loggedIn = null;

    private String uuid;

    public boolean tempLogin(String uuid)
    {
        this.loggedIn = null;

        try {
            this.uuid = uuid;
            this.isAuthenticated = DatabaseAPI.getDatabaseAPI().getServiceEntry(uuid) != null;
        } catch (SQLException exception) {
            this.isAuthenticated = false;
            this.uuid = null;
            return false;
        }

        return true;
    }

    /**
     * Attempts to log in a user
     * @param user The username
     * @param pass The password
     * @return True if we successfully logged in, false otherwise.
     * @throws SQLException
     * @author Alex Friedman (ahf)
     */
    public boolean login(String user, String pass) throws SQLException {

        this.uuid = null;
        this.isAuthenticated = DatabaseAPI.getDatabaseAPI().authenticate(user, pass);
        if(isAuthenticated){
            loggedIn = DatabaseAPI.getDatabaseAPI().getUser(user);
        } else {
            loggedIn = null;
        }
        return this.isAuthenticated;
    }

    /**
     * Attempts to log out a user
     * @return True if successful log out. False otherwise (eg. if we weren't logged in to start with!)
     */
    public boolean logout()
    {
        if(!isAuthenticated)
            return false;

        isAuthenticated = false;

        loggedIn = null;
        return true;
    }

    public String getUuid() {
        return uuid;
    }

    /**
     * Used to test if we are authenticated. THIS SHOULD NOT BE USED TO TEST AUTH. Instead, use this to
     * test if we are currently logged in. Then, use auth tokens to determine if we are authenticated.
     * @return True if the current user is logged in, false otherwise.
     */
    public boolean isAuthenticated() { return isAuthenticated; }

    /**
     * Method to get the current AccountEntry of the logged in user
     * @return AccountEntry
     * @author Leo Morris
     */
    public AccountEntry getLoggedIn(){ return loggedIn; }


    /*
     * Code below used for singleton.
     *
     */

    /**
     * Hide the constructor from the user
     * @author Alex Friedman (ahf)
     */
    private CurrentUser() {}

    /**
     * Static inner class to hold singleton
     * @author Alex Friedman (ahf)
     */
    private static class CurrentUserSingletonHelper{
        private static final CurrentUser currentUser = new CurrentUser();
    }


    /**
     * Used to get the current user.
     * @return The current user singleton.
     * @author Alex Friedman (ahf)
     */
    public static CurrentUser getCurrentUser(){
        return CurrentUserSingletonHelper.currentUser;
    }
}
