package edu.wpi.cs3733.D21.teamF.database;

import java.sql.*;

public class ConnectionHandler {

    private ConnectionHandler() {}

    //FIXME: at some point, refactor better, probably move into helper
    private static Connection establishConnection() throws SQLException {
        String protocol = "jdbc:derby:";
        String embeddedURL;
        String remoteURL = "";

        embeddedURL = protocol + "projectC1;create=true";

        try {
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
