package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.utils.*;

import javax.xml.soap.Node;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PopulateDB {
    public List<NodeEntry> genNodeEntries(Connection conn) throws SQLException {
        List<NodeEntry> nodeEntries = new ArrayList<>();
        String sql = "SELECT * FROM L1Nodes";
        Statement stmt =  conn.createStatement();;

        ResultSet rset = null;
        try {

            rset = stmt.executeQuery(sql);
        } catch (SQLException e) {
            if(e.getMessage().contains("Table/View 'L1NODES' does not exist."))
                return nodeEntries;
            else
                e.printStackTrace();
            return null;
        }
        while (rset.next()) {
            String nodeID = rset.getString(1);
            int xCoord = rset.getInt(2);
            int yCoord = rset.getInt(3);
            String floor = rset.getString(4);
            String building = rset.getString(5);
            String type = rset.getString(6);
            String longName = rset.getString(7);
            String shortName = rset.getString(8);

            NodeEntry newEntry = new NodeEntry(nodeID, Integer.toString(xCoord), Integer.toString(yCoord), floor, building, type, longName, shortName);
            nodeEntries.add(newEntry);
        }
        return nodeEntries;
    }

    private boolean populateData(Connection conn, List<String[]> data, List <String[]> data1) throws Exception {
        boolean success = false;
        try {
            Statement initStmt = conn.createStatement();
            Statement init2Stmt = conn.createStatement();

            String init = "CREATE TABLE L1Nodes(NodeID varchar(200), " +
                    "xCoord int, yCoord int, floor varchar(200), building varchar(200), " +
                    "nodeType varchar(200), longName varchar(200), shortName varchar(200), primary key(NodeID))";
            initStmt.execute(init);


            String init2 = "CREATE TABLE L1Edges(edgeID varchar(200), " +
                    "startNode varchar(200), endNode varchar(200), primary key(edgeID))";
            init2Stmt.execute(init2);

            initStmt.close();
            init2Stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String[] arr : data) {
            try {
                Statement stmt = conn.createStatement();
                StringBuilder str = new StringBuilder();

                str.append("INSERT INTO L1Nodes VALUES(");
                str.append("'" + arr[0] + "',");
                str.append(arr[1] + ",");
                str.append(arr[2] + ",");
                str.append("'" + arr[3] + "',");
                str.append("'" + arr[4] + "',");
                str.append("'" + arr[5] + "',");
                str.append("'" + arr[6] + "',");
                str.append("'" + arr[7] + "')");

                //System.out.println(str.toString());
                stmt.execute(str.toString());
                stmt.close();

            } catch (SQLException e) {
                System.out.println("Connection failed. Check output console.");
                e.printStackTrace();
            }
        }

        for (String[] arr : data1) {
            try {
                Statement initStmt = conn.createStatement();
                Statement stmt = conn.createStatement();

                StringBuilder str = new StringBuilder();
                str.append("INSERT INTO L1Edges VALUES(");
                str.append("'" + arr[0] + "',");
                str.append("'" + arr[1] + "',");
                str.append("'" + arr[2] + "')");

               // System.out.println(str.toString());
                stmt.execute(str.toString());
                stmt.close();
                success = true;

            } catch (SQLException e) {
                System.out.println("Connection failed. Check output console.");
                e.printStackTrace();
            }
        }
        return success;
    }



    /**
     * Used to drop a table
     * @param conn The SQL Connection
     * @param table The name of the table to drop
     * @return true if the table was dropped, false otherwise.
     * @author Alex Friedman (ahf)
     */
    private final boolean dropTable(Connection conn, String table)
    {
        try
        {
            String sql = "drop table " + table;
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();

            return true;
        }
        catch (SQLException e)
        {
            //System.out.println("MSG: " + e.getMessage());
            if(e.getMessage().contains("'DROP TABLE' cannot be performed on '" + table + "' because it does not exist."))
                return false;

            e.printStackTrace();
            return false;
        }
    }

    public boolean main(Connection conn, List<String[]> nodeData, List<String[]> edgeData) throws Exception {
        boolean success = false;
        //ConnectionHandler connectionHandler = new ConnectionHandler();
        //Connection conn = connectionHandler.main(false);
        //List<String[]> data = new ArrayList<String[]>();
        /*
        List<String[]> data1 = new ArrayList<String[]>();
        try {
          //  data = CSVManager.load(nodeFile);
            data1 = CSVManager.load(edgeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        dropTable(conn, "L1NODES");
        dropTable(conn, "L1EDGES");

        success = populateData(conn, nodeData, edgeData);
        //conn.close();
        return success;
    }
}
