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
/* ----------------------------------------------------------------------------
 * $Header: /src/mps/com/volantis/mps/attachment/MessageAttachment.java,v 1.4 2003/01/30 12:02:33 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Nov-02    Sumit           VBM:2002112602 - Created
 * 28-Jan-03    Steve           VBM:2003012710 - Added javadoc.
 *                              Fixed equals method to take an Object parameter
 * 30-Jan-03    Steve           VBM:200312710 - Removed all code changes
 * ----------------------------------------------------------------------------
 */

    package com.volantis.mps.attachment;

import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;

/**
 * The <code>MessageAttachment</code> class is the super class for device
 * message attachments.  In this release, this class is never instantiated.
 *
 * @volantis-api-include-in PublicAPI
 */
public abstract class MessageAttachment {

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    MessageAttachment.class);

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageAttachment.class);

    /**
     * Indicates that the message attachment is undefined with no real values.
     */
    public static final int UNDEFINED = 0;

    /**
     * Indicates that the message attachment processing should use a file.
     */
    public static final int FILE = 1;

    /**
     * Indicates that the message attachment processing should use a URL.
     */
    public static final int URL = 2;

    /**
     * The URL of file that is the resource to use for the attachment.
     */
    private String value;

    /**
     * The mime type of the resource.
     */
    private String mimeType;

    /**
     * The value type of the resource.  This needs to be one of {@link #FILE},
     * {@link #URL}, or internally it can be set to {@link #UNDEFINED}.
     */
    private int valueType;

    /**
     * Initialize the new instance with an undefined value type.
     */
    protected MessageAttachment() {
        valueType = UNDEFINED;
    }

    /**
     * Initialize the new instance with the given value, MIME type and value
     * type.
     *
     * @param value     The URL or filename of the resource to use for
     *                  retrieving the attachment. Cannot not be null.
     * @param mimeType  The mimetype of the resource. Cannot be null.
     * @param valueType Must be either {@link #FILE} or
     *                  {@link #URL} depending on resource type.
     * @throws MessageException if there is a problem creating the message
     *                          attachment
     */
    public MessageAttachment(String value, String mimeType, int valueType)
            throws MessageException {
            setValue(value);
            setMimeType(mimeType);
            setValueType(valueType);
    }

    /**
     * Sets the value for the attachment. It is either a string representing
     * the URL from which the attachment can be retrieved, or a string
     * representing the name of the local file that contains it. Local in this
     * sense means local to the machine on which the MCS instance is running.
     * The value type associated with this attachment indicates which of these
     * alternatives is represented by the value.
     *
     * @param value the value to set. Cannot be null.
     * @throws MessageException if <code>value</code> is null.
     */
    public void setValue(String value) throws MessageException{
        if (value == null) {
            throw new MessageException(
                    localizer.format("value-null-invalid"));
        }

        InputStream is = null;

        try {
            boolean isURL = false;

            try {
                // check for URL
                URL u = new URL(value);
                is = u.openStream();
                isURL = true;

                // log URL type found
                if (logger.isDebugEnabled()) {
                    logger.debug("URL attachment: " + value);
                }

            } catch (MalformedURLException e) {
                // URL check failed
            }


            if (!isURL) {

                //default to file type if URL failed
                File f = new File(value);
                if (!f.exists() || !f.canRead()) {
                    throw new MessageException(localizer.format("invalid-attachment-file",
                            f.getName()));
                }
                is = new FileInputStream(f);

                // log File type found
                if (logger.isDebugEnabled()) {
                    logger.debug("File attachment: " + value);
                }
            }
        } catch (IOException e) {
            // end up here if we couldn't read attachment
            throw new MessageException(localizer.format("attachment-read-failure"), e);
        } finally {
            closeStream(is);
        }


        // all checks pass
        this.value = value;
    }

    /**
     * Utility method to close an <code>InputStream</code>
     *
     * @param is    <code>InputStream</code> to close
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch(IOException ioe) {
                // ignore
            }
        }
    }

    /**
     * Gets the value (the URL or local filename) of this attachment.
     *
     * @return the value
     * @throws MessageException if there is a problem retrieving the value
     */
    public String getValue() throws MessageException {
        return value;
    }

    /**
     * Sets the mime type for this attachment. For example "image/gif,
     * text/plain, image/jpeg" etc.
     *
     * @param mimeType The mime type of the attached content.
     * @throws MessageException if <code>mimeType</code> is null.
     */
    public void setMimeType(String mimeType) throws MessageException {
        if (mimeType == null) {
            throw new MessageException(
                    localizer.format("mime-type-null-invalid"));
        }
        this.mimeType = mimeType;
    }

    /**
     * Gets the mime type of the content of this attachment.
     *
     * @return The attachment's mime type.
     * @throws MessageException if there is a problem retrieving the mime type
     */
    public String getMimeType() throws MessageException {
        return mimeType;
    }

    /**
     * Sets the type of the attachment. This defines how to interpret the value
     * returned by the {@link #getValue()} method.
     *
     * @param valueType the value type of the attachment. This can take the
     * following values:
     * <dl>
     * <dt><code>FILE</code></dt>
     * <dd>The name of a file on the server on which the MCS instance is running
     * from which the attachment can be retrieved.</dd>
     * <dt><code>URL</code></dt>
     * <dd>The URL from which the attachment can be retrieved.</dd>
     * </dl>
     * @throws MessageException if <code>valueType</code> is invalid
     */
    public void setValueType(int valueType) throws MessageException {
        if (valueType == FILE || valueType == URL) {
            this.valueType = valueType;
        } else {
            throw new MessageException(
                    localizer.format("value-type-invalid"));
        }
    }

    /**
     * Returns the type of the attachment.
     *
     * @return The type of the attachment.
     * @throws MessageException if there were problems retrieving the type
     */
    public int getValueType() throws MessageException {
        return valueType;
    }

    // javadoc inherited
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (this == object) {
            isEqual = true;
        } else {
            if (object != null) {
                // Ensure the classes are part of the same hierarchy
                if (object.getClass().equals(this.getClass())) {
                    MessageAttachment messageAttachment =
                            (MessageAttachment) object;

                    // A non null parameter so check valueType
                    if (valueType == messageAttachment.valueType) {
                        // These are equal so check mimeType
                        if (mimeType != null ?
                                mimeType.equals(messageAttachment.mimeType) :
                                messageAttachment.mimeType == null) {
                            // These are also equal so check value
                            if (value != null ?
                                    value.equals(messageAttachment.value) :
                                    messageAttachment.value == null) {
                                // Everything is equal!
                                isEqual = true;
                            }
                        }
                    }
                }
            }
        }
        return isEqual;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	693/2	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 05-May-05	671/9	amoore	VBM:2005050315 Updated Unit tests to use local URL rather than remote

 05-May-05	671/7	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/4	amoore	VBM:2005050315 Removed javax.swing.* imports

 05-May-05	671/2	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 22-Apr-05	610/1	philws	VBM:2005040503 Port Public API changes from 3.3

 22-Apr-05	608/1	philws	VBM:2005040503 Update MPS Public API

 18-Mar-05	430/1	emma	VBM:2005031707 Merge from MPS V3.3.0 - Ensuring mps' LocalizationFactory used; moved localization source code to localization subsystem

 18-Mar-05	428/1	emma	VBM:2005031707 Making all classes use mps' LocalizationFactory, and moving localization source code to localization subsystem

 29-Nov-04	243/4	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 17-Nov-04	238/2	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 24-Sep-04	142/4	claire	VBM:2004070806 Correct invalid attachment type on PublicAPI DeviceMessageAttachment

 ===========================================================================
*/
