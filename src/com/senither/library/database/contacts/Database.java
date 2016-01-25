package com.senither.library.database.contacts;

import com.senither.library.SenLibrary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * Represents our database logger instance, allowing
     * us to get more information during development
     * if debugging has been enabled.
     *
     * @var Logger
     */
    protected final Logger logger;

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
     * Represents our debugging value, if this is set to true, the
     * console will tell you about everything that is going on
     * within the database factory and the database.
     *
     * @var Boolean
     */
    protected boolean debug;

    /**
     * Creates a new database instance.
     *
     * @param library The sen-library instance.
     */
    public Database(SenLibrary library)
    {
        this.library = library;

        debug = false;
        logger = library.getPlugin().getLogger();
        preparedStatements = new HashMap();
    }

    /**
     * Creates a new database instance.
     *
     * @param library The sen-library instance.
     * @param debug   The database debugging value.
     */
    public Database(SenLibrary library, boolean debug)
    {
        this.library = library;

        logger = library.getPlugin().getLogger();
        preparedStatements = new HashMap();

        this.debug = debug;
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
            warning("Could not close connection, it is null.");
            return false;
        }

        try {
            connection.close();

            return true;
        } catch (SQLException e) {
            warning("Could not close connection, SQLException: {0}", e.getMessage());
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
     * Sets the database debugging value, if set to true,
     * the database class will send messages to the
     * terminal about what is going on within the
     * class and with the database.
     *
     * @param debug The debugging value.
     */
    public final void enableDebug(boolean debug)
    {
        this.debug = debug;
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
        queryValidation(getStatement(query));
        Statement statement = getConnection().createStatement();

        if (statement.execute(query)) {
            return statement.getResultSet();
        }

        return getConnection().createStatement().executeQuery("SELECT " + (lastUpdate = statement.getUpdateCount()));
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
     * @param ps The prepared statement to run.
     * @return ArrayList
     * @throws SQLException
     */
    public final synchronized ArrayList<Long> insert(PreparedStatement ps) throws SQLException
    {
        lastUpdate = ps.executeUpdate();
        preparedStatements.remove(ps);

        ArrayList<Long> keys = new ArrayList();
        ResultSet key = ps.getGeneratedKeys();
        if (key.next()) {
            keys.add(key.getLong(1));
        }

        return keys;
    }

    /**
     * Sends a info log message to the console/terminal.
     *
     * @param message The message to send.
     */
    protected final void info(String message)
    {
        log(Level.INFO, message);
    }

    /**
     * Sends a info log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    protected final void info(String message, Object... objects)
    {
        log(Level.INFO, message, objects);
    }

    /**
     * Sends a warning log message to the console/terminal.
     *
     * @param message The message to send.
     */
    protected final void warning(String message)
    {
        log(Level.WARNING, message);
    }

    /**
     * Sends a warning log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    protected final void warning(String message, Object... objects)
    {
        log(Level.WARNING, message, objects);
    }

    /**
     * Sends a error log message to the console/terminal.
     *
     * @param message The message to send.
     */
    protected final void error(String message)
    {
        log(Level.SEVERE, message);
    }

    /**
     * Sends a error log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    protected final void error(String message, Object... objects)
    {
        log(Level.SEVERE, message, objects);
    }

    /**
     * Sends a log message to the console/terminal of the given
     * level if the debugging level is set to true.
     *
     * @param level   The level of the log message.
     * @param message The message to send.
     */
    protected final void log(Level level, String message)
    {
        if (debug) {
            logger.log(level, message);
        }
    }

    /**
     * Sends a log message to the console/terminal of the given level
     * with the given objects if the debugging level is set to true.
     *
     * @param level   The level of the log message.
     * @param message The message to send.
     * @param objects The objects that should be parsed to the logger log method.
     */
    protected final void log(Level level, String message, Object... objects)
    {
        if (debug) {
            logger.log(level, message, objects);
        }
    }
}
