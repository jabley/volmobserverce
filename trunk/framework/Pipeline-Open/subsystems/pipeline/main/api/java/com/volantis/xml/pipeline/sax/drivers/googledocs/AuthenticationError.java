package com.volantis.xml.pipeline.sax.drivers.googledocs;

import org.xml.sax.Locator;

import java.util.Map;

/**
* Class representing Authentication GData Error thrown during GDocs authentication.
 *
 * See {@link Authenticator}
*/
class AuthenticationError extends GDataException {
    private static final String GDATA_AUTH_ERROR_NAME = "authentication-required";
    
    public AuthenticationError(String message, String sourceID, Map infos) {
        super(message, sourceID, GDATA_AUTH_ERROR_NAME, infos, null);
    }
}
