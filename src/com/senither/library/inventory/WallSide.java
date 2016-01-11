package com.senither.library.inventory;

public enum WallSide
{
    ALL("All", 0),
    TOP("Top", 1),
    RIGHT("Right", 2),
    BOTTOM("Bottom", 3),
    LEFT("Left", 4);

    private final String name;
    private final int id;

    private WallSide(String name, int id)
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
