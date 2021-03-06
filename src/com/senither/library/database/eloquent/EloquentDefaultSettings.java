package com.senither.library.database.eloquent;

public abstract class EloquentDefaultSettings
{

    public abstract Eloquent instance();

    protected abstract String table();

    protected String primaryKey()
    {
        return "id";
    }

    protected boolean incrementing()
    {
        return true;
    }

    protected boolean timestamps()
    {
        return true;
    }
}
