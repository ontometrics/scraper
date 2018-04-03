package com.ontometrics.scraper.extraction;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class InputStreamWithEncoding extends FilterInputStream {
    private String encoding;

    public InputStreamWithEncoding(InputStream target, String encoding) {
        super(target);
        this.encoding = encoding;
    }
    public String getEncoding() {
        return encoding;
    }

    public Charset getCharset() {
        try {
            return encoding == null ? null : Charset.forName(encoding);
        } catch (Exception ignore) {
            return null;
        }
    }

}
