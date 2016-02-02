package com.senither.library.database.utils;

import com.senither.library.database.contacts.Grammar;

public class DeleteGrammar extends Grammar
{

    public DeleteGrammar()
    {
        query = "DELETE FROM ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        addPart(String.format(" `%s`", builder.getTable()));

        buildWhereClause(builder);

        return finalize(builder);
    }

    @Override
    protected String finalize(QueryBuilder builder)
    {
        addPart(";");

        return query;
    }
}
