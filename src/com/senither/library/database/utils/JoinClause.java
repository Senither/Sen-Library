package com.senither.library.database.utils;

import java.util.ArrayList;
import java.util.List;

public class JoinClause
{

    /**
     * The type of join being performed.
     *
     * @var String
     */
    public String type;

    /**
     * The table the join clause is joining to.
     *
     * @var String
     */
    public String table;

    /**
     * The "on" clauses for the join.
     *
     * @var List
     */
    public List<Clause> clauses = new ArrayList<>();

    /**
     * Create a new join clause instance.
     *
     * @param type  The type of the join.
     * @param table The table to join.
     */
    public JoinClause(String type, String table)
    {
        this.type = type;
        this.table = table;
    }

    public JoinClause on(String one, String two)
    {
        return on(one, "=", two);
    }

    public JoinClause on(String one, String identifier, String two)
    {
        clauses.add(new Clause(one, identifier, two));

        return this;
    }
}
