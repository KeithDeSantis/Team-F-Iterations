package edu.wpi.cs3733.D21.teamF.database;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseEntry {
    /**
     * Adds an entry into the table of the concrete class
     * @param colValues String array of values for the new entry
     * @return true on successful add, false otherwise
     * @throws SQLException on error performing DB operations
     */
    boolean addEntry(String[] colValues) throws SQLException;

    /**
     * Edits an entry in the table of the concrete class
     * @param id the id of the entry to edit
     * @param val the new value to insert
     * @param colName the column name to update
     * @return true on success, false otherwise
     */
    boolean editEntry(String id, String val, String colName) throws Exception;

    /**
     * Deletes an entry in the table of the corresponding concrete class
     * @param id the id of the entry to delete
     * @return true on success, false otherwise
     * @throws SQLException on error performing DB operations
     */
    boolean deleteEntry(String id) throws SQLException;

    /**
     * Creates the table associated with the concrete class
     * @return true on success, false otherwise
     */
    boolean createTable();

    /**
     * Drops the table corresponding with the concrete class
     * @return true on success, false otherwise
     */
    boolean dropTable();

    /**
     * Populates a table with data from CSV or any data in the same format
     * @param entries iterable list of string arrays containing the data to enter
     * @throws SQLException on error with DB operations
     */
    void populateTable(List<String[]> entries) throws SQLException;

}
