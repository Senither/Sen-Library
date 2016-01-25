package com.senither.library.database.contacts;

import com.senither.library.database.utils.QueryBuilder;
import java.util.Arrays;
import java.util.List;

public abstract class Grammar
{

    protected String query;

    protected List<String> operators = Arrays.asList(
    "=", "<", ">", "<=", ">=", "<>", "!=",
    "like", "like binary", "not like", "between", "ilike",
    "&", "|", "^", "<<", ">>",
    "rlike", "regexp", "not regexp",
    "~", "~*", "!~*", "similar to",
    "not similar to"
    );

    protected List<String> orderOperators = Arrays.asList("ASC", "DESC");

    public abstract String format(QueryBuilder builder);

    protected abstract String finalize(QueryBuilder builder);

    protected boolean isNumeric(String string)
    {
        return string.matches("[-+]?\\d*\\.?\\d+");
    }

    public Grammar addPart(String part)
    {
        query = query.trim() + part;

        return this;
    }

    protected Grammar removeLast(int characters)
    {
        query = query.substring(0, query.length() - characters);

        return this;
    }

    public String formatField(String field)
    {
        if (field.contains(".")) {
            String[] both = field.split("\\.");

            if (both.length == 2) {
                field = String.format("%s`.`%s", both[0], both[1]);
            }
        }

        return String.format("`%s`", field);
    }
}
