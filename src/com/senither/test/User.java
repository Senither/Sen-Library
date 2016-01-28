package com.senither.test;

import com.senither.library.database.eloquent.Eloquent;

public class User extends Eloquent
{

    @Override
    protected String table()
    {
        return "users";
    }
}
