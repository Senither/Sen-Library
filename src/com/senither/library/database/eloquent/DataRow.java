package com.senither.library.database.eloquent;

import java.util.Map;

public class DataRow
{

    private final Map<String, Object> items;

    public DataRow(Map<String, Object> items)
    {
        this.items = items;
    }

    public boolean has(String name)
    {
        return items.containsKey(name);
    }

    public Object get(String name)
    {
        if (has(name)) {
            return items.get(name);
        }

        return null;
    }

    public String getString(String name)
    {
        Object obj = get(name);

        if (obj == null) {
            return null;
        }

        return (String) obj;
    }
}
