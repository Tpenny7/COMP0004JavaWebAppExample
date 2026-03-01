package uk.ac.ucl.model.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * An iterator type class to read values from a text file.  This is a subclass of
 * <code>Input</code> that hides the exceptions that can happen when opening a file.  As with
 * <code>Input</code> all errors during use of the iterator result in a message output to the
 * standard error and default values returned.
 * <p/>
 * <p>This class is useful for people new to Java since it allows them to write programs using
 * input from a file without having to fully understand the read / parse / handle exceptions model
 * of the standard Java classes.</p>
 */
public class FileInput extends Input
{
    /**
     * Construct <code>FileInput</code> object given a file name.
     * If the file cannot be opened a message is output to standard error and the program
     * using FileInput is allowed to continue running. However, the program will be a potentially
     * an invalid state as any attempts to read data from the file will fail.
     */
    public FileInput(final String fileName) throws FileNotFoundException
    {
        scanner = new Scanner(new FileInputStream(fileName));
    }

    /**
     * Construct <code>FileInput</code> object given a file name.
     */
    public FileInput(final FileInputStream fileStream)
    {
        super(fileStream);
    }

    /**
     * @return true if the named file exists, else false.
     * The fileName can be either relative or absolute.
     */
    public static boolean exists(final String fileName)
    {
        return new File(fileName).exists();
    }
}
