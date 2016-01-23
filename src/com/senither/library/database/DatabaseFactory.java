package com.senither.library.database;

import com.senither.library.SenLibrary;
import com.senither.library.database.contacts.Database;

public class DatabaseFactory
{

    private final SenLibrary library;

    private Database connection;

    public DatabaseFactory(SenLibrary library)
    {
        this.library = library;
    }

    public Database getConnection()
    {
        return connection;
    }

    public Database setConnection(Database connection)
    {
        this.connection = connection;

        return this.connection;
    }
}
