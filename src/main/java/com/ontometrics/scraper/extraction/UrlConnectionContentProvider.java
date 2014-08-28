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

    private Map<String, String> requestProperties;

    private UrlConnectionContentProvider(Builder builder) {
        this.requestProperties = builder.requestProperties;
    }

    @Override
    /**
     * Opens {@link InputStream} for {@link URL} using {@link java.net.URLConnection}
     * adding {@link #requestProperties} to it
     *
     * @param url url
     * @return stream
     * @throws IOException if i/o operation(s) fails
     */
    public InputStream getContent(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        if (!requestProperties.isEmpty()) {
            log.debug("Passing a cookie for constructing HttpURLConnection.");
            for (Map.Entry<String, String> entryKey : requestProperties.entrySet()) {
                urlConnection.setRequestProperty(entryKey.getKey(), entryKey.getValue());
            }
        }

        return urlConnection.getInputStream();
    }

    public void setRequestProperty(String key, String value) {
        requestProperties.put(key, value);
    }

    public Map<String, String> getRequestProperties() {
        return requestProperties;
    }

    /**
     * Static Builder for {@link UrlConnectionContentProvider}
     */
    public static class Builder {
        private Map<String, String> requestProperties = new HashMap<String, String>();

        /**
         * Puts a request property (http header) which will be used for constructing of {@link java.net.URLConnection}
         *
         * @param key property key for {@link java.net.URLConnection}
         * @param value property value
         * @return this, for chaining
         */
        public Builder setRequestProperty(String key, String value) {
            requestProperties.put(key, value);
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
