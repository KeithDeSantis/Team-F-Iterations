package edu.wpi.cs3733.D21.teamF.database;

import java.sql.*;

public class ConnectionHandler {

    private ConnectionHandler() {}

    //FIXME: at some point, refactor better, probably move into helper
    private static Connection establishConnection(boolean createDB)
    {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String protocol = "jdbc:derby:";
        String URL;

        if (createDB)
        {
            URL = protocol + "projectC1;create=true";
        }
        else {URL = protocol + "projectC1";}
        
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class ConnectionSingletonHelper {
        private static final Connection connection = establishConnection(true);
    }

    public static Connection getConnection() {
        return ConnectionSingletonHelper.connection;
    }
}
