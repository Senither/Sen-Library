package com.senither.library.database.contacts;

import com.senither.library.SenLibrary;
import com.senither.library.exceptions.DatabaseException;
import java.io.File;

public abstract class FilenameDatabase extends Database
{

    /**
     * The directory/folder the database is stored in.
     *
     * @var String
     */
    private String directory;

    /**
     * The name of the database file.
     *
     * @var String
     */
    private String filename;

    /**
     * The extension of the database file.
     *
     * @var String
     */
    private String extension;

    /**
     * The database file.
     *
     * @var File
     */
    private File file;

    /**
     * Creates a new filename database instance.
     *
     * @param library The sen-library instance.
     */
    public FilenameDatabase(SenLibrary library)
    {
        super(library);

        file = null;
    }

    /**
     * Creates a new filename database instance.
     *
     * @param library   The sen-library instance
     * @param directory The folder the database is stored in.
     * @param filename  The database file name.
     * @param extension The database file extension.
     */
    public FilenameDatabase(SenLibrary library, String directory, String filename, String extension)
    {
        super(library);

        setFile(directory, filename, extension);
    }

    /**
     * Returns the folder name the database is stored in.
     *
     * @return String
     */
    public String getDirectory()
    {
        return directory;
    }

    /**
     * Sets the folder name the database is stored in.
     *
     * @param directory
     */
    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    /**
     * Returns the database file name.
     *
     * @return String
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Sets the database file name.
     *
     * @param filename
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    /**
     * Returns the database extension of the file name.
     *
     * @return String
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * Sets the database file name extension.
     *
     * @param extension
     */
    public void setExtension(String extension)
    {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        this.extension = extension;
    }

    /**
     * Returns the database file object.
     *
     * @return File
     */
    public File getFile()
    {
        return file;
    }

    /**
     * Sets and creates the database file object from the given values.
     *
     * @param directory The folder the database is stored in.
     * @param filename  The database file name.
     * @param extension The database file extension.
     * @throws DatabaseException
     */
    private void setFile(String directory, String filename, String extension) throws DatabaseException
    {
        setExtension(extension);
        setDirectory(directory);
        setFilename(filename);

        File folder = new File(getDirectory());
        if (!folder.exists()) {
            folder.mkdir();
        }

        file = new File(folder.getAbsolutePath() + File.separator + getFilename() + getExtension());
    }
}
