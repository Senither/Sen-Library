package com.senither.library.database.contacts;

import com.senither.library.SenLibrary;
import com.senither.library.database.utils.QueryBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Database implements DatabaseContract
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    protected final SenLibrary library;

    /**
     * Represents our prepared query statements and their statement
     * type, allowing us to quickly render and compile statements.
     *
     * @var Map
     */
    protected Map<PreparedStatement, StatementContract> preparedStatements;

    /**
     * Represents our current database connection, this is
     * used to send queries to the database, as well as
     * fetch, and persist data.
     *
     * @var Connection
     */
    protected Connection connection;

    /**
     * Represents a unix timestamp of the last time we
     * communicated with the database.
     *
     * @var Integer
     */
    protected int lastUpdate;

    /**
     * Creates a new database instance.
     *
     * @param library The sen-library instance.
     */
    public Database(SenLibrary library)
    {
        this.library = library;

        preparedStatements = new HashMap();
    }

    /**
     * Initialize the database abstraction, this should be
     * called by the open method if it's necessary.
     *
     * @return Boolean
     */
    protected abstract boolean initialize();

    /**
     * Checks a statement for faults, issues, overlaps,
     * deprecated calls and other issues.
     *
     * @param paramStatement The statement to check.
     * @throws SQLException
     */
    protected abstract void queryValidation(StatementContract paramStatement) throws SQLException;

    /**
     * Closes the database connection.
     *
     * @return Boolean
     */
    public final boolean close()
    {
        if (connection == null) {
            library.warning("Database - Could not close connection, it is null.");
            return false;
        }

        try {
            connection.close();

            return true;
        } catch (SQLException e) {
            library.warning("Database - Could not close connection, SQLException: {0}", e.getMessage());
        }

        return false;
    }

    /**
     * Returns the current database connection, if the
     * connection is not open/active, it will attempt
     * to open the connection for you.
     *
     * @return Connection
     */
    public final Connection getConnection()
    {
        if (!isOpen()) {
            open();
        }

        return connection;
    }

    /**
     * Checks to see if the database connection is still valid.
     *
     * @return Boolean
     */
    public final boolean isOpen()
    {
        return isOpen(1);
    }

    /**
     * Checks to see if the database connection is still valid.
     *
     * @param seconds The amount of time to wait for the connection for.
     * @return Boolean
     */
    public final boolean isOpen(int seconds)
    {
        if (connection != null) {
            try {
                if (connection.isValid(seconds)) {
                    return true;
                }
            } catch (SQLException e) {
            }
        }

        return false;
    }

    /**
     * Get the unix timestamp of the last time the class
     * communicated with the database.
     *
     * @return Integer
     */
    public final int getLastUpdateCount()
    {
        return lastUpdate;
    }

    /**
     * Queries the database with the given query.
     *
     * @param query The query to run.
     * @return ResultSet
     * @throws SQLException
     */
    public final synchronized ResultSet query(String query) throws SQLException
    {
        library.info("Database - The follow query has been added to the query queue: " + query);

        queryValidation(getStatement(query));
        Statement statement = getConnection().createStatement();

        if (statement.execute(query)) {
            return statement.getResultSet();
        }

        return getConnection().createStatement().executeQuery("SELECT " + (lastUpdate = statement.getUpdateCount()));
    }

    /**
     * Queries the database with the query built from the query builder object.
     *
     * @param builder The query to build.
     * @return ResultSet
     * @throws SQLException
     */
    public final synchronized ResultSet query(QueryBuilder builder) throws SQLException
    {
        return query(builder.toSQL());
    }

    /**
     * Queries the database with the given prepared statement.
     *
     * @param query     The prepared statement to run.
     * @param statement The query statement.
     * @return ResultSet
     * @throws SQLException
     */
    public final synchronized ResultSet query(PreparedStatement query, StatementContract statement) throws SQLException
    {
        library.info("Database - The follow prepared statement has been added to the query queue: " + query);

        queryValidation(statement);

        if (query.execute()) {
            return query.getResultSet();
        }

        return connection.createStatement().executeQuery("SELECT " + (lastUpdate = query.getUpdateCount()));
    }

    /**
     * Queries the database with the given prepared statement.
     *
     * @param query The prepared statement to run.
     * @return ResultSet
     * @throws SQLException
     */
    public final synchronized ResultSet query(PreparedStatement query) throws SQLException
    {
        ResultSet output = query(query, (StatementContract) preparedStatements.get(query));

        preparedStatements.remove(query);

        return output;
    }

    /**
     * Prepares a query as a prepared statement before executing it.
     *
     * @param query The query to prepare.
     * @return PreparedStatement
     * @throws SQLException
     */
    public final synchronized PreparedStatement prepare(String query) throws SQLException
    {
        library.info("Database - The follow query has been added to the prepared query queue: " + query);

        StatementContract statement = getStatement(query);
        PreparedStatement ps = connection.prepareStatement(query);

        preparedStatements.put(ps, statement);

        return ps;
    }

    /**
     * Stores data in the database from the given query, this
     * will return a list of ids from the inserted rows.
     *
     * @param query The query to run.
     * @return ArrayList
     * @throws SQLException
     */
    public final synchronized ArrayList<Long> insert(String query) throws SQLException
    {
        library.info("Database - The follow query has been added to the query inserter: " + query);

        ArrayList<Long> keys = new ArrayList();

        PreparedStatement ps = connection.prepareStatement(query, 1);
        lastUpdate = ps.executeUpdate();

        ResultSet key = ps.getGeneratedKeys();
        if (key.next()) {
            keys.add(key.getLong(1));
        }

        return keys;
    }

    /**
     * Stores data in the database from the given prepared statement,
     * this will return a list of ids from the inserted rows.
     *
     * @param query The prepared statement to run.
     * @return ArrayList
     * @throws SQLException
     */
    public final synchronized ArrayList<Long> insert(PreparedStatement query) throws SQLException
    {
        library.info("Database - The follow prepared statement has been added to the query inserter: " + query);

        lastUpdate = query.executeUpdate();
        preparedStatements.remove(query);

        ArrayList<Long> keys = new ArrayList();
        ResultSet key = query.getGeneratedKeys();
        if (key.next()) {
            keys.add(key.getLong(1));
        }

        return keys;
    }
}
