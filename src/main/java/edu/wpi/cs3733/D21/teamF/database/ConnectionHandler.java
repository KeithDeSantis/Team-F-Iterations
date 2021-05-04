package edu.wpi.cs3733.D21.teamF.database;

import java.sql.*;

public class ConnectionHandler {

    private ConnectionHandler() {}

    //FIXME: at some point, refactor better, probably move into helper
    private static Connection establishConnection()
    {
        String protocol = "jdbc:derby:";
        String URL;

        URL = protocol + "projectC1;create=true";

        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class ConnectionSingletonHelper {
        private static final Connection connection = establishConnection();
    }

    public static Connection getConnection() {
        return ConnectionSingletonHelper.connection;
    }
}
