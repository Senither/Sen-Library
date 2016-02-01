package com.senither.library.database.eloquent;

import com.senither.library.database.contacts.Database;
import com.senither.library.database.utils.QueryBuilder;
import com.senither.library.database.utils.QueryHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Eloquent extends EloquentDefaultSettings
{

    protected static Database database;

    protected QueryBuilder builder;

    public Eloquent()
    {
        builder = new QueryBuilder(table());
    }

    public static void setConnection(Database connection)
    {
        Eloquent.database = connection;
    }

    public static Database getConnection()
    {
        return Eloquent.database;
    }

    public Collection find(int id)
    {
        return builder().table(table()).where(primaryKey(), id).get();
    }

    public Collection all()
    {
        return builder().table(table()).get();
    }

    public Eloquent select(String... columns)
    {
        builder.select(columns);

        return this;
    }

    public Eloquent where(String column, Object field)
    {
        return where(column, "=", field);
    }

    public Eloquent where(String column, String identifier, Object field)
    {
        builder().where(column, identifier, field);

        return this;
    }

    public Eloquent andWhere(String column, Object field)
    {
        return andWhere(column, "=", field);
    }

    public Eloquent andWhere(String column, String identifier, Object field)
    {
        builder().andWhere(column, identifier, field);

        return this;
    }

    public Eloquent orWhere(String column, Object field)
    {
        return orWhere(column, "=", field);
    }

    public Eloquent orWhere(String column, String identifier, Object field)
    {
        builder().orWhere(column, identifier, field);

        return this;
    }

    public Eloquent leftJoin(String table, String one, String two)
    {
        builder().leftJoin(table, one, two);

        return this;
    }

    public Eloquent leftJoin(String table, String one, String identifier, String two)
    {
        builder().leftJoin(table, one, identifier, two);

        return this;
    }

    public Eloquent rightJoin(String table, String one, String two)
    {
        builder().rightJoin(table, one, two);

        return this;
    }

    public Eloquent rightJoin(String table, String one, String identifier, String two)
    {
        builder().rightJoin(table, one, identifier, two);

        return this;
    }

    public Eloquent innerJoin(String table, String one, String two)
    {
        builder().innerJoin(table, one, two);

        return this;
    }

    public Eloquent innerJoin(String table, String one, String identifier, String two)
    {
        builder().innerJoin(table, one, identifier, two);

        return this;
    }

    public Eloquent outerJoin(String table, String one, String two)
    {
        builder().outerJoin(table, one, two);

        return this;
    }

    public Eloquent outerJoin(String table, String one, String identifier, String two)
    {
        builder().outerJoin(table, one, identifier, two);

        return this;
    }

    public Eloquent with(String... fields)
    {
        List<String> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(fields));

        Class<? extends Eloquent> iClass = instance().getClass();

        for (Method method : iClass.getMethods()) {
            if (!methods.contains(method.getName())) {
                continue;
            }

            if (method.getAnnotations().length == 0) {
                continue;
            }

            boolean isQueryHandler = false;
            for (Annotation an : method.getAnnotations()) {
                if (an.annotationType().getName().equals(QueryHandler.class.getName())) {
                    isQueryHandler = true;
                    break;
                }
            }

            if (isQueryHandler) {
                try {
                    method.setAccessible(true);
                    method.invoke(instance(), builder);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(Eloquent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return this;
    }

    public QueryBuilder builder()
    {
        return builder;
    }

    public Collection get()
    {
        return builder.get();
    }

    @Override
    public String toString()
    {
        return builder.toSQL();
    }
}
