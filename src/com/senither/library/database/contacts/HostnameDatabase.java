package com.senither.library.database.contacts;

import com.senither.library.SenLibrary;
import com.senither.library.exceptions.DatabaseException;

public abstract class HostnameDatabase extends Database
{

    /**
     * The database hostname that should be
     * used to connect to the database.
     *
     * @var String
     */
    private String hostname;

    /**
     * The port that the database is listing on.
     *
     * @var Integer
     */
    private int port;

    /**
     * The name of the database that should be used.
     *
     * @var String
     */
    private String database;

    /**
     * The login username used to connect to the database.
     *
     * @var String
     */
    private String username;

    /**
     * The login password used to connect to the database.
     *
     * @var String
     */
    private String password;

    /**
     * Creates a new hostname database instance.
     *
     * @param library  The sen-library instance.
     * @param hostname The hostname to connect to.
     * @param port     The port the database is listing on.
     * @param database The database name to use.
     * @param username The login username.
     * @param password The login password.
     */
    public HostnameDatabase(SenLibrary library, String hostname, int port, String database, String username, String password)
    {
        super(library);

        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the hostname the database is listing on.
     *
     * @return String
     */
    public String getHostname()
    {
        return hostname;
    }

    /**
     * Sets the hostname the database is listing on.
     *
     * @param hostname
     */
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    /**
     * Returns the port the database is listing on.
     *
     * @return Integer
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets the port the database should be listing on,
     * the port has to be between 0 and 65535.
     *
     * @param port
     */
    public void setPort(int port)
    {
        if ((port < 0) || (65535 < port)) {
            throw new DatabaseException("Port number cannot be below 0 or greater than 65535.");
        }

        this.port = port;
    }

    /**
     * Gets the name of the database that should be used.
     *
     * @return String
     */
    public String getDatabase()
    {
        return database;
    }

    /**
     * Sets the database name that should be used.
     *
     * @param database
     */
    public void setDatabase(String database)
    {
        this.database = database;
    }

    /**
     * Returns the database username login.
     *
     * @return String
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the database username login.
     *
     * @param username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Returns the database password login.
     *
     * @return String
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the database password login.
     *
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}
