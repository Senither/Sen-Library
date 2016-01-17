package com.senither.library.chat;

public enum ChatFilterType
{

    ALL("All", 0),
    WORD_FILTER("Word filter", 1),
    CAPS_FILTER("Caps filter", 2);

    private final String name;
    private final int id;

    private ChatFilterType(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}
