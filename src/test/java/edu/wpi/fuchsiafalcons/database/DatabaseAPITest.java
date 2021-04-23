package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.wpi.fuchsiafalcons.utils.CSVManager;

import javax.xml.soap.Node;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseAPITest {
    @BeforeEach
    public void setUp() throws Exception
    {
        DatabaseAPI1.getDatabaseAPI1().dropNodesTable();
        DatabaseAPI1.getDatabaseAPI1().dropEdgesTable();
        DatabaseAPI1.getDatabaseAPI1().dropUsersTable();

        DatabaseAPI1.getDatabaseAPI1().createNodesTable();
        DatabaseAPI1.getDatabaseAPI1().createEdgesTable();
        DatabaseAPI1.getDatabaseAPI1().createUserTable();
    }

    @Test()
    @DisplayName("test dropping nodes table")
    public void testDropNodesTable() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().dropNodesTable());
        assertThrows(SQLException.class, () -> DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
    }

    @Test()
    @DisplayName("test dropping edges table")
    public void testDropEdgesTable() throws SQLException
    {
        String[] newEdge = {"test", "start", "end"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().dropEdgesTable());
        assertThrows(SQLException.class, () -> DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge));
    }

    @Test
    @DisplayName("test adding nodes table")
    public void testAddNodesTable() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        DatabaseAPI1.getDatabaseAPI1().dropNodesTable();
        assertTrue(DatabaseAPI1.getDatabaseAPI1().createNodesTable());
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
    }

    @Test
    @DisplayName("test adding edges table")
    public void testAddEdgesTable() throws SQLException
    {
        String[] newEdge = {"test",  "start", "end"};
        DatabaseAPI1.getDatabaseAPI1().dropEdgesTable();
        assertTrue(DatabaseAPI1.getDatabaseAPI1().createEdgesTable());
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge));
    }

    @Test
    @DisplayName("test adding a node")
    public void testAddNodeBasic() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
    }

    @Test
    @DisplayName("test adding a edge")
    public void testAddEdgeBasic() throws SQLException
    {
        String[] newEdge = {"test", "start", "end"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge));
    }

    @Test
    @DisplayName("test invalid node add")
    public void testInvalidNodeAdd() throws SQLException
    {
        String[] newNode = {"test", "1234"};
        assertThrows(SQLException.class, () -> DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
    }

    @Test
    @DisplayName("test invalid edge add")
    public void testInvalidEdgeAdd() throws SQLException
    {
        String[] newEdge = {"test", "1234"};
        assertThrows(SQLException.class, () -> DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge));
    }

    @Test
    @DisplayName("test delete node")
    public void testDeleteNode() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        DatabaseAPI1.getDatabaseAPI1().addNode(newNode);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteNode("test"));
    }

    @Test
    @DisplayName("test delete invalid node")
    public void testDeleteInvalidNode() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        DatabaseAPI1.getDatabaseAPI1().addNode(newNode);
        assertFalse(DatabaseAPI1.getDatabaseAPI1().deleteNode("notTest"));
    }

    @Test
    @DisplayName("test populate nodes")
    public void testPopulateNodes() throws Exception
    {
        DatabaseAPI1.getDatabaseAPI1().populateNodes(CSVManager.load("MapfAllNodes.csv"));
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteNode("ACONF00102"));
    }

    @Test
    @DisplayName("test generating node entry list")
    public void testGenerateNodeEntries() throws SQLException
    {
        NodeEntry entry = new NodeEntry("test", "1", "1", "f", "b", "t", "l", "s");
        ArrayList<NodeEntry> expected = new ArrayList<>();
        expected.add(entry);
        String[] newNode = {"test", "1", "1", "f", "b", "t", "l", "s"};
        DatabaseAPI1.getDatabaseAPI1().addNode(newNode);
        ArrayList<NodeEntry> actual = DatabaseAPI1.getDatabaseAPI1().genNodeEntries();
        assertEquals(expected.get(0).getNodeID(), actual.get(0).getNodeID());
    }

    @Test
    @DisplayName("test editing a node")
    public void testEditNode() throws SQLException
    {
        String[] newNode = {"test", "1", "2", "f", "b", "t", "l", "s"};
        DatabaseAPI1.getDatabaseAPI1().addNode(newNode);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().editNode("test", "test1", "nodeid"));
    }

    @Test
    @DisplayName("test editing a Edge")
    public void testEditEdge() throws SQLException
    {
        String[] newEdge = {"test", "start", "end"};
        DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().editEdge("test", "test1", "startnode"));
    }

    @Test
    @DisplayName("test deleting an edge")
    public void testDeleteEdge() throws SQLException
    {
        String[] newEdge = {"test", "start", "end"};
        DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteEdge("test"));
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge));
    }

    @Test
    @DisplayName("test populating edges table")
    public void testPopulateEdges() throws SQLException, Exception
    {
        DatabaseAPI1.getDatabaseAPI1().populateEdges(CSVManager.load("MapfAllEdges.csv"));
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteEdge("AHALL00202_AHALL00302"));
    }

    @Test
    @DisplayName("test generating edge entry list")
    public void testGenerateEdgeEntries() throws SQLException
    {
        EdgeEntry entry = new EdgeEntry("test", "start", "end");
        ArrayList<EdgeEntry> expected = new ArrayList<>();
        expected.add(entry);
        String[] newEdge = {"test", "start", "end"};
        DatabaseAPI1.getDatabaseAPI1().addEdge(newEdge);
        ArrayList<EdgeEntry> actual = DatabaseAPI1.getDatabaseAPI1().genEdgeEntries("AllEdges");
        assertEquals(expected.get(0).getEdgeID(), actual.get(0).getEdgeID());
    }

    @Test
    @DisplayName("test adding a user")
    public void testAddUser() throws SQLException
    {
        String[] newUser = {"1", "employee", "declan", "password"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addUser(newUser));
    }

    @Test
    @DisplayName("test deleting a user")
    public void testDeleteUser() throws SQLException
    {
        String[] newUser = {"1", "employee", "declan", "password"};
        DatabaseAPI1.getDatabaseAPI1().addUser(newUser);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteUser("declan"));
    }

    @Test
    @DisplayName("test editing a user")
    public void testEditUser() throws SQLException
    {
        String[] newUser = {"1", "employee", "declan", "password"};
        DatabaseAPI1.getDatabaseAPI1().addUser(newUser);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().editUser("declan", "password123", "password"));
    }

    @Test
    @DisplayName("test dropping users table")
    public void testDropUsersTable() throws SQLException
    {
        assertTrue(DatabaseAPI1.getDatabaseAPI1().dropUsersTable());
    }

    @Test
    @DisplayName("test adding users table")
    public void testAddUsersTable() throws SQLException
    {
        DatabaseAPI1.getDatabaseAPI1().dropUsersTable();
        assertTrue(DatabaseAPI1.getDatabaseAPI1().createUserTable());
    }

    @Test
    @DisplayName("test populating users table")
    public void testPopulateUsers() throws SQLException
    {
        ArrayList<String[]> users = new ArrayList<>();
        String[] user1 = {"1", "admin", "username", "password"};
        String[] user2 = {"2", "employee", "testuser", "testpass"};
        users.add(user1);
        users.add(user2);

        DatabaseAPI1.getDatabaseAPI1().populateUsers(users);
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteUser("testuser"));
        assertTrue(DatabaseAPI1.getDatabaseAPI1().deleteUser("username"));
    }
}
