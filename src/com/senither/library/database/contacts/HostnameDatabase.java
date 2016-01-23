package com.senither.library.database.contacts;

import com.senither.library.SenLibrary;
import com.senither.library.exceptions.DatabaseException;

public abstract class HostnameDatabase extends Database
{

    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;

    public HostnameDatabase(SenLibrary library, String hostname, int port, String database, String username, String password)
    {
        super(library);

        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        if ((port < 0) || (65535 < port)) {
            throw new DatabaseException("Port number cannot be below 0 or greater than 65535.");
        }

        this.port = port;
    }

    public String getDatabase()
    {
        return database;
    }

    public void setDatabase(String database)
    {
        this.database = database;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
