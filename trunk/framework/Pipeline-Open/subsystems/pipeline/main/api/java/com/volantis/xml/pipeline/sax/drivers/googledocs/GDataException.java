package com.volantis.xml.pipeline.sax.drivers.googledocs;

import com.volantis.xml.pipeline.sax.XMLPipelineException;

import java.util.Map;

import org.xml.sax.Locator;

/**
 * Base class for all Google Data errors. 
 * At the moment, the only derived exception are used in GoogleDocs connector, so it stays in this package.
 * Consider moving it after using elsewhere.
*/
abstract public class GDataException extends XMLPipelineException {
    private static final String GDATA_ERROR_CODE_URI = "http://www.volantis.com/xmlns/2008/08/gdata";

     public GDataException(String message, String sourceID,  String codeName, Map infos, Locator locator) {
        super(message, locator);
        initErrorInfo(sourceID, GDATA_ERROR_CODE_URI , codeName, infos);
    }
}
