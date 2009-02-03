package com.volantis.xml.pipeline.sax.drivers.googledocs;

import org.xml.sax.Locator;

import java.util.Map;

/**
 * Class representing Unknown GData Error thrown during GDocs authentication.
 * See {@link Authenticator}
*/
class UnknownError extends GDataException {
    private static final String GDATA_UNKNOWN_ERROR_NAME = "unknown";


    public UnknownError(String message, String sourceID) {
        super(message, sourceID, GDATA_UNKNOWN_ERROR_NAME, null, null);
    }
}
