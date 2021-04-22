package edu.wpi.fuchsiafalcons.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseAPITest {
    @BeforeEach
    public void setUp() throws Exception
    {
        DatabaseAPI1.getDatabaseAPI1().dropNodesTable();
        DatabaseAPI1.getDatabaseAPI1().createNodesTable();
    }

    @Test()
    @DisplayName("test dropping nodes table")
    public void testDropNodesTable() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().dropNodesTable());
        assertThrows(SQLException.class, () -> DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
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
    @DisplayName("test adding a node")
    public void testAddNodeBasic() throws SQLException
    {
        String[] newNode = {"test", "10", "10", "floor", "building", "type", "long", "short"};
        assertTrue(DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
    }

    @Test()
    @DisplayName("test invalid node add")
    public void testInvalidNodeAdd() throws SQLException
    {
        String[] newNode = {"test", "1234"};
        assertThrows(SQLException.class, () -> DatabaseAPI1.getDatabaseAPI1().addNode(newNode));
    }
}
