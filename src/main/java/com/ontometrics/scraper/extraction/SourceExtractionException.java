package com.ontometrics.scraper.extraction;

public class SourceExtractionException extends RuntimeException {
    @SuppressWarnings("unused")
    public SourceExtractionException(String message) {
        super(message);
    }

    public SourceExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
