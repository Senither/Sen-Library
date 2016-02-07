package com.senither.library;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SenLogger
{

    /**
     * Represents our Sen Library instance, this is
     * used to call other parts of the library.
     *
     * @var SenLibrary
     */
    private final SenLibrary library;

    /**
     * Represents our database logger instance, allowing
     * us to get more information during development
     * if debugging has been enabled.
     *
     * @var Logger
     */
    private static final Logger LOGGER = Logger.getGlobal();

    /**
     * Creates a new sen logger instance.
     *
     * @param library The sen library instance.
     */
    public SenLogger(SenLibrary library)
    {
        this.library = library;
    }

    /**
     * Sends a info log message to the console/terminal.
     *
     * @param message The message to send.
     */
    public void info(String message)
    {
        log(Level.INFO, message);
    }

    /**
     * Sends a info log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    public void info(String message, Object... objects)
    {
        log(Level.INFO, message, objects);
    }

    /**
     * Sends a warning log message to the console/terminal.
     *
     * @param message The message to send.
     */
    public void warning(String message)
    {
        log(Level.WARNING, message);
    }

    /**
     * Sends a warning log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    public void warning(String message, Object... objects)
    {
        log(Level.WARNING, message, objects);
    }

    /**
     * Sends a error log message to the console/terminal.
     *
     * @param message The message to send.
     */
    public void error(String message)
    {
        log(Level.SEVERE, message);
    }

    /**
     * Sends a error log message to the console/terminal.
     *
     * @param message The message to send.
     * @param objects The objects to parse to the logger log method.
     */
    public void error(String message, Object... objects)
    {
        log(Level.SEVERE, message, objects);
    }

    /**
     * Sends a log message to the console/terminal of the given
     * level if the debugging level is set to true.
     *
     * @param level   The level of the log message.
     * @param message The message to send.
     */
    public void log(Level level, String message)
    {
        if (library.isDebugging()) {
            LOGGER.log(level, "[{0}::SenLibrary]: {1}", new Object[]{library.getPlugin().getName(), message});
        }
    }

    /**
     * Sends a log message to the console/terminal of the given level
     * with the given objects if the debugging level is set to true.
     *
     * @param level   The level of the log message.
     * @param message The message to send.
     * @param objects The objects that should be parsed to the logger log method.
     */
    public void log(Level level, String message, Object... objects)
    {
        if (library.isDebugging()) {
            LOGGER.log(level, "[" + library.getPlugin().getName() + "::SenLibrary]: " + message, objects);
        }
    }
}
