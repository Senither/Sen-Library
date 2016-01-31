package com.senither.library.database.contacts;

import com.senither.library.database.utils.QueryBuilder;
import java.util.Arrays;
import java.util.List;

public abstract class Grammar
{

    /**
     * The query SQL string, this string will be appended to
     * and formated by the addPart and removeLast methods.
     *
     * @var String
     */
    protected String query;

    /**
     * A list a SQL operators, this is used to compare and
     * validate operators to make sure they're valid.
     *
     * @var List
     */
    protected final List<String> operators = Arrays.asList(
    "=", "<", ">", "<=", ">=", "<>", "!=",
    "like", "like binary", "not like", "between", "ilike",
    "&", "|", "^", "<<", ">>",
    "rlike", "regexp", "not regexp",
    "~", "~*", "!~*", "similar to",
    "not similar to"
    );

    /**
     * A list of SQL order operators, this is used to compare
     * and validate operators to make sure they're valid.
     *
     * @var List
     *
     */
    protected final List<String> orderOperators = Arrays.asList("ASC", "DESC");

    /**
     * The query formatter method, this is called by the query
     * builder when the query should be built.
     *
     * @param builder The query builder to format.
     * @return String
     */
    public abstract String format(QueryBuilder builder);

    /**
     * Adds the last few touches the query needs to be ready to be executed.
     *
     * @param builder The query builder to finalize.
     * @return String
     */
    protected abstract String finalize(QueryBuilder builder);

    /**
     * Checks to see if a string is numeric, this will help
     * determine how to format values into the query.
     *
     * @param string The string to check.
     * @return Boolean
     */
    protected boolean isNumeric(String string)
    {
        return string.matches("[-+]?\\d*\\.?\\d+");
    }

    /**
     * Adds the given part to the query.
     *
     * @param part The string to add.
     * @return Grammar
     */
    public Grammar addPart(String part)
    {
        query = query.trim() + part;

        return this;
    }

    /**
     * Removes the given number of characters
     * from the end of the query string.
     *
     * @param characters The amount of characters to remove.
     * @return Grammar
     */
    protected Grammar removeLast(int characters)
    {
        query = query.substring(0, query.length() - characters);

        return this;
    }

    /**
     * Formats a query field, splitting it up using dot-notation.
     *
     * @param field The field to format.
     * @return String
     */
    protected String formatField(String field)
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
