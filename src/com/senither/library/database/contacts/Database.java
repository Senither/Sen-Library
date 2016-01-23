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

public abstract class Database
{

    protected final SenLibrary library;
    protected final Logger logger;

    protected Map<PreparedStatement, StatementContract> preparedStatements;
    protected Connection connection;
    protected int lastUpdate;

    protected boolean debug;

    public Database(SenLibrary library)
    {
        this.library = library;

        debug = false;
        logger = library.getPlugin().getLogger();
        preparedStatements = new HashMap();
    }

    public Database(SenLibrary library, boolean debug)
    {
        this.library = library;

        logger = library.getPlugin().getLogger();
        preparedStatements = new HashMap();

        this.debug = debug;
    }

    protected abstract boolean initialize();

    public abstract boolean open();

    protected abstract void queryValidation(StatementContract paramStatement) throws SQLException;

    public abstract StatementContract getStatement(String paramString) throws SQLException;

    public abstract boolean isTable(String paramString);

    public abstract boolean truncate(String paramString);

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

    public final Connection getConnection()
    {
        if (!isOpen()) {
            open();
        }

        return connection;
    }

    public final boolean isOpen()
    {
        return isOpen(1);
    }

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

    public final int getLastUpdateCount()
    {
        return lastUpdate;
    }

    public final void enableDebug(boolean debug)
    {
        this.debug = debug;
    }

    public final synchronized ResultSet query(String query) throws SQLException
    {
        queryValidation(getStatement(query));
        Statement statement = getConnection().createStatement();

        if (statement.execute(query)) {
            return statement.getResultSet();
        }

        return getConnection().createStatement().executeQuery("SELECT " + (lastUpdate = statement.getUpdateCount()));
    }

    protected final synchronized ResultSet query(PreparedStatement ps, StatementContract statement) throws SQLException
    {
        queryValidation(statement);

        if (ps.execute()) {
            return ps.getResultSet();
        }

        return connection.createStatement().executeQuery("SELECT " + (lastUpdate = ps.getUpdateCount()));
    }

    public final synchronized ResultSet query(PreparedStatement ps) throws SQLException
    {
        ResultSet output = query(ps, (StatementContract) preparedStatements.get(ps));

        preparedStatements.remove(ps);

        return output;
    }

    public final synchronized PreparedStatement prepare(String query) throws SQLException
    {
        StatementContract s = getStatement(query);
        PreparedStatement ps = connection.prepareStatement(query);

        preparedStatements.put(ps, s);

        return ps;
    }

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

    protected final void info(String message)
    {
        log(Level.INFO, message);
    }

    protected final void info(String message, Object... objects)
    {
        log(Level.INFO, message, objects);
    }

    protected final void warning(String message)
    {
        log(Level.WARNING, message);
    }

    protected final void warning(String message, Object... objects)
    {
        log(Level.WARNING, message, objects);
    }

    protected final void error(String message)
    {
        log(Level.SEVERE, message);
    }

    protected final void error(String message, Object... objects)
    {
        log(Level.SEVERE, message, objects);
    }

    protected final void log(Level level, String message)
    {
        if (debug) {
            logger.log(level, message);
        }
    }

    protected final void log(Level level, String message, Object... objects)
    {
        if (debug) {
            logger.log(level, message, objects);
        }
    }
}
