package com.senither.library.database.utils;

import com.senither.library.database.contacts.Grammar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateGrammar extends Grammar
{

    private final List<String> keyset = new ArrayList<>();

    public UpdateGrammar()
    {
        query = "UPDATE ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        addPart(String.format(" `%s` SET", builder.getTable()));

        buildKeyset(builder);

        buildValues(builder);

        buildWhereClause(builder);

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

    }

    private void buildValues(QueryBuilder builder)
    {
        List<Map<String, Object>> items = builder.getItems();

        for (Map<String, Object> row : items) {

            keyset.stream().forEach((key) -> {
                addPart((row.containsKey(key)) ? String.format(" %s = '%s', ", formatField(key), row.get(key)) : " NULL, ");
            });

            removeLast(2).addPart(" ");
        }

        removeLast(1);
    }

    private void buildWhereClause(QueryBuilder builder)
    {
        if (builder.getWhereClouses().isEmpty()) {
            return;
        }

        String whereClouse = " WHERE";
        boolean shouldResetColumn = false;

        for (String field : builder.getWhereClouses()) {
            if (field.startsWith("---")) {
                whereClouse += " " + field.substring(3, field.length());

                continue;
            }

            if (operators.contains(field.toLowerCase())) {
                whereClouse += " " + field;

                shouldResetColumn = true;

                continue;
            }

            if (!shouldResetColumn) {
                whereClouse += String.format(" %s", formatField(field));

                continue;
            }

            if (isNumeric(field)) {
                whereClouse += String.format(" %s", field);
            } else {
                whereClouse += String.format(" '%s'", field);
            }

            shouldResetColumn = false;
        }

        addPart(whereClouse);
    }

    @Override
    protected String finalize(QueryBuilder builder)
    {
        addPart(";");

        return query;
    }
}
