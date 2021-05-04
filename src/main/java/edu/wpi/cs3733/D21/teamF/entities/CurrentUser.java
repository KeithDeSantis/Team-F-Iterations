package edu.wpi.cs3733.D21.teamF.entities;


import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.SQLException;

public class CurrentUser {

    /**
     * Tracks if the user has been authenticated
     */
    private final BooleanProperty isAuthenticated = new SimpleBooleanProperty(false);
    private AccountEntry loggedIn = null;

    /**
     * Attempts to log in a user
     * @param user The username
     * @param pass The password
     * @return True if we successfully logged in, false otherwise.
     * @throws SQLException
     * @author Alex Friedman (ahf)
     */
    public boolean login(String user, String pass) throws SQLException {

        this.isAuthenticated.set(DatabaseAPI.getDatabaseAPI().authenticate(user, pass));
        if(isAuthenticated.get()){
            loggedIn = DatabaseAPI.getDatabaseAPI().getUser(user);
        } else {
            loggedIn = null;
        }
        return this.isAuthenticated.get();
    }

    /**
     * Attempts to log out a user
     * @return True if successful log out. False otherwise (eg. if we weren't logged in to start with!)
     */
    public boolean logout()
    {
        if(!isAuthenticated.get())
            return false;

        isAuthenticated.set(false);

        loggedIn = null;
        return true;
    }

    /**
     * Used to test if we are authenticated. THIS SHOULD NOT BE USED TO TEST AUTH. Instead, use this to
     * test if we are currently logged in. Then, use auth tokens to determine if we are authenticated.
     * @return True if the current user is logged in, false otherwise.
     */
    public boolean isAuthenticated() { return isAuthenticated.get(); }

    public BooleanProperty authenticatedProperty() { return isAuthenticated; }

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
