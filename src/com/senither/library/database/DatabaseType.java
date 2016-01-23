package com.senither.library.database;

public enum DatabaseType
{
    MYSQL("MySQL"),
    SQLITE("SQLite");

    private final String name;

    private DatabaseType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
