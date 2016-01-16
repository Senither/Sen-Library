package com.senither.library.placeholder.contracts;

public interface GlobalPlaceholder extends Placeholder
{

    /**
     * Global placeholder interface, this should be used
     * for placeholders that doesn't need any objects
     * from the library to return a valid value.
     *
     * @return String
     */
    public String run();
}
