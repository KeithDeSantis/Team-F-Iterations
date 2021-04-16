package edu.wpi.fuchsiafalcons.database;

import java.sql.*;

public class ConnectionHandler {

    private static Connection connection;



    private static Connection establishConnection(String URL)
    {
        if(connection != null)
            return connection;

        //Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(URL);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return connection;
    }

    private static Connection main(boolean createDB)
    {
        if(connection != null)
            return connection;
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String protocol = "jdbc:derby:";
        String URL;

        if (createDB)
        {
            URL = protocol + "projectC1;create=true";
        }
        else {URL = protocol + "projectC1";}
        Connection connection = establishConnection(URL);
        return connection;
    }

    public static Connection getConnection() {
        if(connection == null)
            return connection = main(true);
        return connection;
        //return connection;
    }
}
