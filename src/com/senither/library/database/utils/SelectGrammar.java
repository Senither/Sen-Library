package com.senither.library.database.utils;

import com.senither.library.database.contacts.Grammar;
import java.util.List;

public class SelectGrammar extends Grammar
{

    public SelectGrammar()
    {
        query = "SELECT ";
    }

    @Override
    public String format(QueryBuilder builder)
    {
        buildColumns(builder);

        buildWhereClause(builder);

        return finalize(builder);
    }

    private void buildColumns(QueryBuilder builder)
    {
        if (builder.getColumns().size() == 1 && builder.getColumns().get(0).equals("*")) {
            query += "*";
        } else {
            builder.getColumns().stream().forEach((column) -> {
                query += formatField(column) + ", ";
            });

            removeLast(2);
        }

        query += String.format(" FROM %s ", formatField(builder.getTable()));
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
        if (!builder.getOrder().isEmpty()) {
            addPart(" ORDER BY ");

            List<String> orders = builder.getOrder();

            for (int i = 0; i < orders.size(); i++) {
                String field = orders.get(i);

                addPart(String.format(" %s ",
                orderOperators.contains(field.toUpperCase()) ? field.toUpperCase() : formatField(field))
                );

                if ((i + 1) % 2 == 0) {
                    addPart(", ");
                }
            }

            removeLast(2);
        }

        if (builder.getLimit() <= 0) {
            return query.trim() + ";";
        }

        addPart(String.format(" LIMIT %d;", builder.getLimit()));

        return query;
    }
}
