package com.senither.library.database;

import com.senither.library.SenLibrary;
import com.senither.library.database.contacts.HostnameDatabase;
import com.senither.library.database.contacts.StatementContract;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends HostnameDatabase
{

    /**
     * Creates a new MySQL database connection.
     *
     * @param library  The sen-library instance.
     * @param hostname The hostname to use to connect to the database.
     * @param port     The port the database is listing on.
     * @param database The database name.
     * @param username The login username.
     * @param password The login password.
     */
    public MySQL(SenLibrary library, String hostname, int port, String database, String username, String password)
    {
        super(library, hostname, port, database, username, password);
    }

    @Override
    protected boolean initialize()
    {
        try {
            Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

            return true;
        } catch (ClassNotFoundException e) {
            library.getLogger().warning("Database - MySQL DataSource class missing: {0}", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean open()
    {
        try {
            String url = String.format("jdbc:mysql://%s:%d/%s", getHostname(), getPort(), getDatabase());

            if (initialize()) {
                connection = DriverManager.getConnection(url, getUsername(), getPassword());

                return true;
            }

        } catch (SQLException e) {
            library.getLogger().error("Database - Could not establish a MySQL connection, SQLException: {0}", e.getMessage());
        }

        return false;
    }

    @Override
    protected void queryValidation(StatementContract statement) throws SQLException
    {
        switch ((MySQLStatement) statement) {
            case USE:
                library.getLogger().warning("Database -Please create a new connection to use a different database.");
                throw new SQLException("Please create a new connection to use a different database.");

            case PREPARE:
            case EXECUTE:
            case DEALLOCATE:
                library.getLogger().warning("Database - Please use the prepare() method to prepare a query.");
                throw new SQLException("Please use the prepare() method to prepare a query.");
        }
    }

    @Override
    public StatementContract getStatement(String query) throws SQLException
    {
        String[] statement = query.trim().split(" ", 2);

        try {
            return MySQLStatement.valueOf(statement[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SQLException(String.format("Unknown statement: \"%s\".", statement[0]));
        }
    }

    @Override
    public boolean isTable(String table)
    {
        Statement statement;

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            library.getLogger().error("Database - Could not create a statement in checkTable(), SQLException: {0}", e.getMessage());

            return false;
        }

        try {
            statement.executeQuery(String.format("SELECT * FROM `%s` LIMIT 1;", table));

            return true;
        } catch (SQLException e) {
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
                statement.executeUpdate(String.format("DELETE FROM `%s`;", table));
            }

            return true;
        } catch (SQLException e) {
            library.getLogger().error("Database - Could not wipe table, SQLException: {0}", e.getMessage());
        }

        return false;
    }
}
