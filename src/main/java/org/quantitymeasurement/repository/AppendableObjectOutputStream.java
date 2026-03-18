package org.quantitymeasurement.repository;

import java.io.*;

/**
 * AppendableObjectOutputStream is a custom ObjectOutputStream that supports
 * appending
 * objects to an existing file without overwriting the serialization stream
 * header.
 *
 * This is crucial for maintaining the integrity of the data file when multiple
 * entities
 * are saved over time in the repository.
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {

    private boolean isFirstWrite;

    /**
     * Constructor for AppendableObjectOutputStream.
     *
     * @param out            the underlying OutputStream
     * @param fileHasContent true if the file already has content (for appending)
     * @throws IOException if an I/O error occurs
     */
    public AppendableObjectOutputStream(OutputStream out, boolean fileHasContent)
            throws IOException {
        super(out);
        this.isFirstWrite = !fileHasContent;
    }

    /**
     * Override writeStreamHeader to prevent writing a new header when appending.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void writeStreamHeader() throws IOException {
        if (isFirstWrite) {
            super.writeStreamHeader();
        }
    }
}
