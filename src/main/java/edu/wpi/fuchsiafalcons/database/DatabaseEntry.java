package edu.wpi.fuchsiafalcons.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DatabaseEntry {
    boolean addEntry(String[] colValues) throws SQLException;
    boolean editEntry(String id, String[] newVals);
    boolean deleteEntry(String id);
    ArrayList<Object> genEntryObjects(String tableName);
    boolean createTable();
    boolean dropTable();
    boolean populateTable(List<String[]> entries);
}
