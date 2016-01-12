package com.senither.library.placeholder;

import com.senither.library.placeholder.contracts.GlobalPlaceholder;
import com.senither.library.placeholder.contracts.Placeholder;
import com.senither.library.placeholder.contracts.PlayerPlaceholder;
import java.util.Arrays;

public enum PlaceholderType
{

    GLOBAL("Global", 0, GlobalPlaceholder.class),
    PLAYER("Player", 1, PlayerPlaceholder.class);

    private final String name;
    private final int id;
    private final Class instance;

    private PlaceholderType(String name, int id, Class instance)
    {
        this.name = name;
        this.id = id;
        this.instance = instance;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public Class getInstance()
    {
        return instance;
    }

    public static PlaceholderType fromName(String name)
    {
        for (PlaceholderType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }

    public static PlaceholderType frominstance(Placeholder instance)
    {
        if (instance.getClass().getInterfaces().length == 0) {
            return null;
        }

        Class placeholderInterface = instance.getClass().getInterfaces()[0];

        for (PlaceholderType type : values()) {
            if (placeholderInterface.getName().equalsIgnoreCase(type.getInstance().getName())) {
                return type;
            }
        }

        return null;
    }
}
