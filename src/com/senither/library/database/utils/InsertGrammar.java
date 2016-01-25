package com.senither.library.database.utils;

import com.senither.library.database.contacts.Grammar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertGrammar extends Grammar
{

    private final List<String> keyset = new ArrayList<>();

    public InsertGrammar()
    {
        query = "INSERT INTO ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        addPart(String.format(" `%s`", builder.getTable()));

        buildKeyset(builder);

        buildValues(builder);

        return finalize(builder);
    }

    private void buildKeyset(QueryBuilder builder)
    {
        List<Map<String, Object>> items = builder.getItems();

        items.stream().forEach((map) -> {
            map.keySet().stream().filter((key) -> (!keyset.contains(key))).forEach((key) -> {
                keyset.add(key);
            });
        });

        addPart(" (");

        keyset.stream().forEach((key) -> {
            addPart(String.format("`%s`, ", key));
        });

        removeLast(2).addPart(")");
    }

    private void buildValues(QueryBuilder builder)
    {
        addPart(" VALUES ");

        List<Map<String, Object>> items = builder.getItems();

        for (Map<String, Object> row : items) {
            addPart(" (");

            keyset.stream().forEach((key) -> {
                addPart((row.containsKey(key)) ? String.format("'%s', ", row.get(key)) : "NULL, ");
            });

            removeLast(2).addPart("),");
        }

        removeLast(1);
    }

    @Override
    protected String finalize(QueryBuilder builder)
    {
        addPart(";");

        return query;
    }
}
