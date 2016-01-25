package com.senither.library.database.utils;

import com.senither.library.database.utils.DeleteGrammar;
import com.senither.library.database.utils.InsertGrammar;
import com.senither.library.database.utils.SelectGrammar;
import com.senither.library.database.utils.UpdateGrammar;

public enum QueryType
{

    SELECT(SelectGrammar.class),
    INSERT(InsertGrammar.class),
    UPDATE(UpdateGrammar.class),
    DELETE(DeleteGrammar.class);

    private final Class grammar;

    private QueryType(Class grammar)
    {
        this.grammar = grammar;
    }

    public Class getGrammar()
    {
        return grammar;
    }
}
