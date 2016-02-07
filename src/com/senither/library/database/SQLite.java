package com.senither.library.database;

import com.senither.library.SenLibrary;
import com.senither.library.database.contacts.FilenameDatabase;
import com.senither.library.database.contacts.StatementContract;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite extends FilenameDatabase
{

    /**
     * Creates a new SQLite database connection,
     * this will create an in-memory database.
     *
     * @param library The sen-library instance.
     */
    public SQLite(SenLibrary library)
    {
        super(library);
    }

    /**
     * Creates a new SQLite database connection.
     *
     * @param library   The sen-library instance.
     * @param directory The folder the database is located in.
     * @param filename  The name of the database file.
     * @param extension The extension of the database file.
     */
    public SQLite(SenLibrary library, String directory, String filename, String extension)
    {
        super(library, directory, filename, extension);
    }

    @Override
    protected boolean initialize()
    {
        try {
            Class.forName("org.sqlite.JDBC");

            return true;
        } catch (ClassNotFoundException e) {
            library.getLogger().error("Database - Class not found in initialize(): {0}", e);
        }

        return false;
    }

    @Override
    public boolean open()
    {
        if (initialize()) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + (getFile() == null ? ":memory:" : getFile().getAbsolutePath()));

                return true;
            } catch (SQLException e) {
                library.getLogger().error("Database - Could not establish an SQLite connection, SQLException: {0}", e.getMessage());

                return false;
            }
        }

        return false;
    }

    @Override
    protected void queryValidation(StatementContract paramStatement) throws SQLException
    {
        // This does nothing for SQLite
    }

    @Override
    public StatementContract getStatement(String query) throws SQLException
    {
        String[] statement = query.trim().split(" ", 2);

        try {
            return SQLiteStatement.valueOf(statement[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SQLException(String.format("Unknown statement: \"%s\".", statement[0]));
        }
    }

    @Override
    public boolean isTable(String table)
    {
        try {
            DatabaseMetaData md = connection.getMetaData();

            try (ResultSet tables = md.getTables(null, null, table, null)) {
                if (tables.next()) {
                    tables.close();

                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            library.getLogger().error("Database - Could not check if table \"{0}\" exists, SQLException: {1}", table, e.getMessage());
        }

        return false;
    }

    @Override
    public boolean truncate(String table)
    {
        try {
            if (!isTable(table)) {
                library.getLogger().error("Database - Table \"{0}\" does not exist.", table);
                return false;
            }

            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(String.format("DELETE FROM `%s`;", table));
            }

            return true;
        } catch (SQLException e) {
            if ((!e.getMessage().toLowerCase().contains("locking")) && (!e.getMessage().toLowerCase().contains("locked")) && (!e.toString().contains("not return ResultSet"))) {
                library.getLogger().error("Database - Error in wipeTable() query: {0}", e);
            }
        }

        return false;
    }
}
