package com.senither.library.database.contacts;

import com.senither.library.SenLibrary;
import com.senither.library.exceptions.DatabaseException;
import java.io.File;

public abstract class FilenameDatabase extends Database
{

    private String directory;
    private String filename;
    private String extension;

    private File file;

    public FilenameDatabase(SenLibrary library)
    {
        super(library);

        file = null;
    }

    public FilenameDatabase(SenLibrary library, String directory, String filename, String extension)
    {
        super(library);

        setFile(directory, filename, extension);
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        
        this.extension = extension;
    }

    public File getFile()
    {
        return file;
    }

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
