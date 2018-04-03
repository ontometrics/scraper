package com.ontometrics.scraper.extraction;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Provider of {@link InputStream} for resource identified by {@link URL}
 *
 * UrlContentProvider.java
 */
public interface UrlContentProvider {
    /**
     * Opens {@link InputStream} for resource identified by provided {@link URL}
     * @param url url
     * @return stream
     * @throws IOException if i/o operation(s) fails
     */
    InputStreamWithEncoding getContent(URL url) throws IOException;
}
