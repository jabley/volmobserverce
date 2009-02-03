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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.bms.impl;

import com.volantis.mps.bms.Message;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.net.URL;

/**
 * Default implemenation of Message.
 */
public class DefaultMessage implements Message {

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    DefaultMessage.class);

    /**
     * The URL of an XDIME page that will be rendered by MCS.
     */
    private URL url;

    /**
     * Default to empty subject
     */
    private String subject = "";

    /**
     * The XDIME (wrapped in a message element) that is to be sent.
     */
    private String content;

    /**
     * Default to UTF-8?
     */
    private String characterEncoding = "UTF-8";

    public DefaultMessage() {
        // constructor for JiBX
    }

    public DefaultMessage(URL url) {
        setURL(url);
    }

    public DefaultMessage(String message) {
        this.content = message;
    }

    // javadoc inherited
    public void setURL(URL url) {
        if (null == url) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format("value-null-invalid"));
        }
        this.url = url;
    }

    // javadoc inherited
    public URL getURL() {
        return url;
    }

    // javadoc inherited
    public void setSubject(String subject) {
        if (null == subject) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format("value-null-invalid"));
        }
        this.subject = subject;
    }

    // javadoc inherited
    public String getSubject() {
        return subject;
    }

    // javadoc inherited
    public void setCharacterEncoding(String characterEncoding) {
        if (null == characterEncoding) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format("value-null-invalid"));
        }
        this.characterEncoding = characterEncoding;
    }

    // javadoc inherited
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    // javadoc inherited
    public void setContent(String content) {
        if (null == content) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format("value-null-invalid"));
        }
        this.content = content;
    }

    // javadoc inherited
    public String getContent() {
        return content;
    }
}
