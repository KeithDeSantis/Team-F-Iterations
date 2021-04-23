package edu.wpi.fuchsiafalcons.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DatabaseEntry {
    boolean addEntry(String[] colValues) throws SQLException;
    boolean editEntry(String id, String[] newVals);
    boolean deleteEntry(String id) throws SQLException;
    ArrayList<Object> genEntryObjects(String tableName);
    boolean createTable();
    boolean dropTable();
    void populateTable(List<String[]> entries) throws SQLException;
}
