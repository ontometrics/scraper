package com.ontometrics.scraper.extraction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link UrlContentProvider} implementation which provides {@link InputStream} based on opening of
 * {@link java.net.URLConnection}
 *
 * UrlConnectionContentProvider.java
 */
public class UrlConnectionContentProvider implements UrlContentProvider {
    private static final Logger log = LoggerFactory.getLogger(UrlConnectionContentProvider.class);

    private Map<String, String> httpRequestProperties;

    private UrlConnectionContentProvider(Builder builder) {
        this.httpRequestProperties = builder.httpRequestProperties;
    }

    @Override
    /**
     * Opens {@link InputStream} for {@link URL} using {@link java.net.URLConnection}
     * adding {@link #httpRequestProperties} to it
     *
     * @param url url
     * @return stream
     * @throws IOException if i/o operation(s) fails
     */
    public InputStream getContent(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        if (!httpRequestProperties.isEmpty()) {
            log.debug("Passing a cookie for constructing HttpURLConnection.");
            for (Map.Entry<String, String> entryKey : httpRequestProperties.entrySet()) {
                urlConnection.setRequestProperty(entryKey.getKey(), httpRequestProperties.get(entryKey.getValue()));
            }
        }

        return urlConnection.getInputStream();
    }

    /**
     * Static Builder for {@link UrlConnectionContentProvider}
     */
    public static class Builder {
        private Map<String, String> httpRequestProperties = new HashMap<String, String>();

        /**
         * Puts a request property (http header) which will be used for constructing of {@link java.net.URLConnection}
         *
         * @param key property key for {@link java.net.URLConnection}
         * @param value property value
         * @return this, for chaining
         */
        public Builder setRequestProperty(String key, String value) {
            httpRequestProperties.put(key, value);
            return this;
        }

        /**
         * @return new instance of {@link UrlConnectionContentProvider}
         */
        public UrlConnectionContentProvider build() {
            return new UrlConnectionContentProvider(this);
        }
    }
}
