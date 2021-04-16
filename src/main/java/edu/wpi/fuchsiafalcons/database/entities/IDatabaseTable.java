package edu.wpi.fuchsiafalcons.database.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class IDatabaseTable {

    private final String createStatement;

    public IDatabaseTable(String createStatement)
    {
        this.createStatement = createStatement;
    }

    //FIXME: Check strict inheritance.
    public abstract PreparedStatement getInsertStatement(String[] data) throws SQLException;
}
