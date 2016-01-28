package com.senither.library.database.eloquent;

import com.senither.library.database.contacts.Database;
import com.senither.library.database.utils.QueryBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Eloquent extends EloquentDefaultSettings
{

    protected static Database database;

    protected QueryBuilder builder = new QueryBuilder();

    public static void setConnection(Database connection)
    {
        Eloquent.database = connection;
    }

    public Eloquent instance()
    {
        return this;
    }

    public Collection find(int id)
    {
        builder().table(table()).where(primaryKey(), id);

        return get();
    }

    public Collection all()
    {
        builder().table(table());

        return get();
    }

    public Eloquent where(String column, Object field)
    {
        return where(column, "=", field);
    }

    public Eloquent where(String column, String identifier, Object field)
    {
        Eloquent obj = buildObject();

        obj.builder().table(table()).where(column, identifier, field);

        return obj;
    }

    public Eloquent andWhere(String column, Object field)
    {
        return andWhere(column, "=", field);
    }

    public Eloquent andWhere(String column, String identifier, Object field)
    {
        Eloquent obj = buildObject();

        obj.builder().table(table()).andWhere(column, identifier, field);

        return obj;
    }

    public Eloquent orWhere(String column, Object field)
    {
        return orWhere(column, "=", field);
    }

    public Eloquent orWhere(String column, String identifier, Object field)
    {
        Eloquent obj = buildObject();

        obj.builder().table(table()).orWhere(column, identifier, field);

        return obj;
    }

    public QueryBuilder builder()
    {
        return builder;
    }

    private Eloquent buildObject()
    {
        try {
            return instance().getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Eloquent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public Collection get()
    {
        try {
            ResultSet result = Eloquent.database.query(builder);

            return new Collection(result);
        } catch (SQLException ex) {
            Logger.getLogger(Eloquent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String toString()
    {
        return builder.toSQL();
    }
}
