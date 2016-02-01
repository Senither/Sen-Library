package com.senither.test;

import com.senither.library.database.eloquent.Eloquent;
import com.senither.library.database.utils.QueryBuilder;
import com.senither.library.database.utils.QueryHandler;

public class User extends Eloquent
{

    @Override
    public Eloquent instance()
    {
        return this;
    }

    @Override
    protected String table()
    {
        return "users";
    }

    @QueryHandler
    public void groups(QueryBuilder query)
    {
        query.innerJoin("group").on("users.id", "group.id");
    }
}
