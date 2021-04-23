package edu.wpi.fuchsiafalcons.database;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DatabaseEntry {
    boolean addEntry(String[] colValues) throws SQLException;
    boolean editEntry(String id, String val, String colName);
    boolean deleteEntry(String id) throws SQLException;
    ArrayList<NodeEntry> genEntryObjects(String tableName) throws SQLException;
    boolean createTable();
    boolean dropTable();
    void populateTable(List<String[]> entries) throws SQLException;
}
