package com.senither.library.database;

import com.senither.library.SenLibrary;
import com.senither.library.database.contacts.Database;

public class DatabaseFactory
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * Represents our current database connection, this
     * is used for schemas, eloquent models and
     * query grammar evaluation.
     *
     * @var Database
     */
    private Database connection;

    /**
     * Creates a database factory instance.
     *
     * @param library The sen library instance.
     */
    public DatabaseFactory(SenLibrary library)
    {
        this.library = library;
    }

    /**
     * Returns the current database connection, if no connection
     * has been set yet, this will return null.
     *
     * @return Database
     */
    public Database getConnection()
    {
        return connection;
    }

    /**
     * Sets the database connection.
     *
     * @param connection The current database connection.
     * @return The parsed database connection.
     */
    public Database setConnection(Database connection)
    {
        this.connection = connection;

        return this.connection;
    }
}
