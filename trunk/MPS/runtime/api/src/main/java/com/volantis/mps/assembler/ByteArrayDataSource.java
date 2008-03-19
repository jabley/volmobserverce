/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
package com.volantis.mps.assembler;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Implementation of {@link DataSource} that uses an underlying byte array to
 * store the data.
 */
public class ByteArrayDataSource implements DataSource {
    /**
     * Default charset to use when reading in data in string form.
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * The string used to identify the specified charset in the content type.
     */
    final String CHARSET_TOKEN = "charset=";

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger( ByteArrayDataSource.class );

    /**
     * The localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer( ByteArrayDataSource.class );

    /**
     * An empty byte array.
     */
    private static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * The data to be returned by this DataSource.
     */
    private byte[] data;

    /**
     * The content type for the data.
     */
    private String contentType;

    /**
     * Create a data source providing the specified array of bytes as data of a
     * specified type.
     *
     * @param data The data to make available through this DataSource
     * @param contentType The content type for this data
     */
    public ByteArrayDataSource(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    /**
     * Create a data source providing the content of the provided input stream
     * as data of a specified type.
     *
     * @param data The data to make available through this DataSource
     * @param contentType The content type for this data
     */
    public ByteArrayDataSource(InputStream data, String contentType) {
        this.contentType = contentType;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int value = data.read();
            while (value != -1) {
                bos.write(value);
                value = data.read();
            }
        } catch (IOException ioe) {
            logger.error(ioe);
        }

        this.data = bos.toByteArray();
        if (this.data == null) {
            this.data = EMPTY_ARRAY;
        }
    }

    /**
     * Create a data source providing the content of the provided input stream
     * as data of a specified type.
     *
     * @param data The data to make available through this DataSource
     * @param contentType The content type for this data
     */
    public ByteArrayDataSource(String data, String contentType) {
        this.contentType = contentType;
        String charset = charsetFromContentType(contentType);
        try {
            this.data = data.getBytes(charset);
        } catch (UnsupportedEncodingException uce) {
            logger.warn(uce);
        }
    }

    /**
     * Retrieve the charset specified by a content type (or a default value if
     * none is specified).
     *
     * @param type Content type to parse
     * @return The encoding string, e.g. Big5 if specified or the default
     *         encoding (UTF-8)
     */
    private String charsetFromContentType(String type) {
        String charset = DEFAULT_CHARSET;
        if (type == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No type specified. Returning default charset: '" +
                        DEFAULT_CHARSET + "'");
            }
        } else {
            // Check for a text content type.
            if (type.indexOf("text/", 0) == -1) {
                // Assume type is already a specified charset.
                if (logger.isDebugEnabled()) {
                    logger.debug("getCharSet returns: " + type);
                }
                charset = type;
            } else {
                // This is a text content-type - check for a charset token.
                if (type.indexOf(CHARSET_TOKEN, 0) != -1) {
                    // Use specified charset
                    charset = type.substring(type.indexOf(CHARSET_TOKEN, 0) +
                            CHARSET_TOKEN.length()).trim();
                    // Strip trailing comma/semi-colon
                    if (charset.endsWith(",") || charset.endsWith(";")) {
                        charset = charset.substring(0, charset.length() - 1);
                    }

                    // Strip quotes
                    if (charset.endsWith("\'") || charset.endsWith("\"")) {
                        charset = charset.substring(0, charset.length() - 1);
                    }
                    if (charset.startsWith("\'") || charset.startsWith("\"")) {
                        charset = charset.substring(1, charset.length());
                    }
    
                    if (logger.isDebugEnabled()) {
                        logger.debug("getCharSet returns: " + charset);
                    }
                } else {
                    // no charset specified use default
                    if (logger.isDebugEnabled()) {
                        logger.debug("No charset specified. Returning " +
                                "default charset: '" + DEFAULT_CHARSET + "'");
                    }
                    charset = DEFAULT_CHARSET;
                }
            }
        }

        return charset;
    }

    // Javadoc inherited
    public String getContentType() {
        return contentType;
    }

    // Javadoc inherited
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    // Javadoc inherited
    public String getName() {
        return "byte-array";
    }

    // Javadoc inherited
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Not supported");
    }
}
