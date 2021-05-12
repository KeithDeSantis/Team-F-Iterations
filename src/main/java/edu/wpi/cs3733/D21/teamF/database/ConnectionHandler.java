package edu.wpi.cs3733.D21.teamF.database;

import edu.wpi.cs3733.D21.teamF.utils.KeyManager;

import java.sql.*;

public class ConnectionHandler {

    private ConnectionHandler() {}

    //FIXME: at some point, refactor better, probably move into helper

    /**
     * Method to establish the connection to DB
     * @return Connection object
     * @throws SQLException on error with the DB
     */
    private static Connection establishConnection() throws SQLException {
        String protocol = "jdbc:derby:";
        String embeddedURL;
        String remoteURL = protocol + "//conntinuity.org:10142/projectC2;create=true;user=falcons;password=" + KeyManager.getKeyManager().getKey("ahfDerbyPass");

        embeddedURL = protocol + "projectC1;create=true";

        try {
            DriverManager.setLoginTimeout(5);
            return DriverManager.getConnection(remoteURL);

        } catch (SQLException e) {
            return DriverManager.getConnection(embeddedURL);
        }

    }

    private static class ConnectionSingletonHelper {
        private static Connection connection;

        static {
            try {
                connection = establishConnection();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return ConnectionSingletonHelper.connection;
    }
}
