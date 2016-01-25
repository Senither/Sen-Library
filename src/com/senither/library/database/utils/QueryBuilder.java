package com.senither.library.database.utils;

import com.senither.library.database.contacts.Grammar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryBuilder
{

    private QueryType type;

    private String table = null;

    private int limit = -1;

    private final List<String> order = new ArrayList<>();

    private final List<String> wheres = new ArrayList<>();

    private final List<String> columns = new ArrayList<>();

    private final List<Map<String, Object>> items = new ArrayList<>();

    public QueryBuilder table(String table)
    {
        return select().from(table);
    }

    public QueryBuilder from(String table)
    {
        this.table = table;

        return this;
    }

    public String getTable()
    {
        return table;
    }

    public QueryBuilder select()
    {
        return select("*");
    }

    public QueryBuilder select(String... colums)
    {
        type = QueryType.SELECT;

        for (String column : colums) {
            addColumn(column);
        }

        return this;
    }

    protected void addColumn(String column)
    {
        if (!column.equals("*")) {
            columns.remove("*");

            if (!columns.contains(column)) {
                columns.add(column);
            }

            return;
        }

        columns.clear();
        columns.add("*");
    }

    public List<String> getColumns()
    {
        return columns;
    }

    public QueryBuilder limit(int limit)
    {
        this.limit = Math.max(limit, 0);

        return this;
    }

    public QueryBuilder noLimit()
    {
        return limit(0);
    }

    public int getLimit()
    {
        return limit;
    }

    public QueryBuilder where(String column, Object field)
    {
        return where(column, "=", field);
    }

    public QueryBuilder where(String column, String identifier, Object field)
    {
        wheres.addAll(Arrays.asList(column, identifier, "" + field));

        return this;
    }

    public QueryBuilder andWhere(String column, Object field)
    {
        return andWhere(column, "=", field);
    }

    public QueryBuilder andWhere(String column, String identifier, Object field)
    {
        wheres.add("---AND");

        return where(column, identifier, field);
    }

    public QueryBuilder orWhere(String column, Object field)
    {
        return orWhere(column, "=", field);
    }

    public QueryBuilder orWhere(String column, String identifier, Object field)
    {
        wheres.add("---OR");

        return where(column, identifier, field);
    }

    public List<String> getWhereClouses()
    {
        return wheres;
    }

    public QueryBuilder orderBy(String field)
    {
        return orderBy(field, "ASC");
    }

    public QueryBuilder orderBy(String field, String type)
    {
        order.addAll(Arrays.asList(field, type));

        return this;
    }

    public List<String> getOrder()
    {
        return order;
    }

    public QueryBuilder insert(Map<String, Object>... items)
    {
        type = QueryType.INSERT;

        this.items.addAll(Arrays.asList(items));

        return this;
    }

    public QueryBuilder insert(List<String>... arrays)
    {
        return insert(buildMapFromArrays(arrays));
    }

    public QueryBuilder update(Map<String, Object>... items)
    {
        type = QueryType.UPDATE;

        this.items.addAll(Arrays.asList(items));

        return this;
    }

    public QueryBuilder update(List<String>... arrays)
    {
        return update(buildMapFromArrays(arrays));
    }

    private Map<String, Object> buildMapFromArrays(List<String>... arrays)
    {
        Map<String, Object> map = new HashMap<>();

        for (List<String> array : arrays) {
            if (array.size() != 2) {
                continue;
            }

            map.put(array.get(0), array.get(1));
        }

        return map;
    }

    public List<Map<String, Object>> getItems()
    {
        return items;
    }

    public QueryBuilder delete()
    {
        type = QueryType.DELETE;

        return this;
    }

    public String toSQL()
    {
        try {
            Grammar grammar = (Grammar) type.getGrammar().newInstance();

            return grammar.format(this);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(QueryBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String toString()
    {
        return toSQL();
    }
}
