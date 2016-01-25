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
