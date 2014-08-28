package com.ontometrics.scraper.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Input / Output stream operations helper
 * IOUtils.java
 */
public class IOUtils {
    /**
     * Closes provided closeable quietly swallowing {@link IOException} if close operation fails
     * @param closeable closeable object
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ignore) {
            // ignore
        }
    }
}
