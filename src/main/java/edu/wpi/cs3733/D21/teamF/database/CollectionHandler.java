package edu.wpi.cs3733.D21.teamF.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CollectionHandler {
    /**
     * Returns a list of a user's favorite or recently used nodes
     * @param type either favorite or recent for node filtering
     * @param id the id of the user (username)
     * @return list of strings for node ids
     * @throws SQLException on error with DB operations
     */
    public ArrayList<String> getUserNodes(String type, String id) throws SQLException{
        final String query = "SELECT * FROM COLLECTIONS";
        ResultSet resultSet;
        ArrayList<String> userNodes = new ArrayList<>();

        Statement stmt = ConnectionHandler.getConnection().createStatement();
        resultSet = stmt.executeQuery(query);
        while(resultSet.next()){
            String userID = resultSet.getString(2);
            String nodeID = resultSet.getString(3);
            String entryType = resultSet.getString(4);
            if (entryType.equals(type) && userID.equals(id)){
                userNodes.add(nodeID);
            }
        }
        return userNodes;
    }

    /**
     * Deletes an entry from the user's favorite or recently used nodes
     * @param nodeID the ID of the node to remove
     * @param type the type, favorite or recent
     * @return true on success, false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean deleteNodeEntry(String nodeID, String type) throws SQLException{
        if (!(type.equals("recent")) || (!(type.equals("favorite")))){
            return false;
        }
        final String query = "DELETE FROM COLLECTIONS WHERE NODEID=(?) AND RELATION_TYPE=(?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, nodeID);
        stmt.setString(2, type);
        return stmt.executeUpdate() != 0;
    }

    /**
     * adds a node entry to the collections table
     * @param user the user associated with the node
     * @param node the nodeID to insert
     * @param type the type either favorite or recent
     * @return true on success, false otherwise
     * @throws SQLException on error with DB operations
     */
    public boolean addEntry(String user, String node, String type) throws SQLException{
        final String query = "INSERT INTO COLLECTIONS values(DEFAULT, ?, ?, ?)";
        PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query);
        stmt.setString(1, user);
        stmt.setString(2, node);
        stmt.setString(3, type);
        return stmt.executeUpdate() != 0;
    }

    /**
     * drops the collections table
     * @return true on success false otherwise
     */
    public boolean dropCollectionsTable(){
        String query = "DROP TABLE COLLECTIONS";
        try {
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(query);
            stmt.close();
        }
        catch (SQLException e){
            return false;
        }
        return true;
    }

    /**
     * create the collections table
     * @return true on success false otherwise
     */
    public boolean createCollectionTable(){
        final String initNodesTable = "CREATE TABLE COLLECTIONS(id int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "user_id varchar(30), node_id varchar(50), " +
                "relation_type varchar(30))";
        try{
            Statement stmt = ConnectionHandler.getConnection().createStatement();
            stmt.execute(initNodesTable);
            stmt.close();
        }
        catch (SQLException e){
            return false;
        }
        return true;
    }
}
