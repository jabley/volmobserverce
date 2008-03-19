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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.validation;

import com.volantis.mcs.xml.xpath.XPath;

import org.jdom.Element;
import org.xml.sax.Attributes;

/**
 * Detailled information for a validation error.
 */
public class ErrorDetails {
    /**
     * The absolute XPath with which the error is associated.
     */
    private XPath xPath;

    /**
     * The error message originally provided for this error.
     */
    private String message;

    /**
     * The type of error.
     */
    private String key;

    /**
     * A value relevant to the given type of error.
     */
    private String param;

    /**
     * The attributes associated with the start element that caused this
     * error (if any)
     */
    private Attributes attributes;

    /**
     * The Element with which the error is associated.
     */
    private Element invalidElement;

    /**
     * No-argument constructor to produce an uninitialised ErrorDetails
     * instance.
     */
    public ErrorDetails() {
    }

    /**
     * Constructor to produce an initialised ErrorDetails instance.
     *
     * @param initInvalidElement the Element with whicht the error is associated
     * @param initXPath          the absolute XPath with which the error is associated
     * @param initMessage        the error message originally provided for this
     *                           error
     * @param initKey            the type of error
     * @param initParam          a value relevant to the given type of error
     * @param initAttributes     the attributes associated with the start element
     */
    public ErrorDetails(Element initInvalidElement, XPath initXPath,
                        String initMessage, String initKey,
                        String initParam, Attributes initAttributes) {
        invalidElement = initInvalidElement;
        xPath = initXPath;
        message = initMessage;
        key = initKey;
        param = initParam;
        attributes = initAttributes;
    }

    /**
     * Get the Element with which the error is associated.
     *
     * @return the invalid Element
     */
    public Element getInvalidElement() {
        return invalidElement;
    }

    /**
     * Specifies the absolute XPath with which the error is associated.
     *
     * @param newXPath The XPath with which the error is associated
     */
    public void setXPath(XPath newXPath) {
        xPath = newXPath;
    }

    /**
     * Retrieves the absolute XPath with which the error is associated.
     *
     * @return The absolute XPath with which the error is associated
     */
    public XPath getXPath() {
        return xPath;
    }

    /**
     * Specifies the error message originally provided for this error.
     *
     * @param newMessage The error message originally provided for this error
     */
    public void setMessage(String newMessage) {
        message = newMessage;
    }

    /**
     * Retrieves the error message originally provided for this error.
     *
     * @return The error message originally provided for this error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Specifies a value relevant to the given type of error.
     *
     * @param newParam A value relevant to the given type of error
     */
    public void setParam(String newParam) {
        param = newParam;
    }

    /**
     * Retrieves a value relevant to the given type of error.
     *
     * @return A value relevant to the given type of error
     */
    public String getParam() {
        return param;
    }

    /**
     * Specifies the type of error.
     *
     * @param newKey The type of error
     */
    public void setKey(String newKey) {
        key = newKey;
    }

    /**
     * Retrieves the type of error.
     *
     * @return The type of error
     */
    public String getKey() {
        return key;
    }

    /**
     * Specifies the attributes associated with the start element that caused
     * this error, if the error was caused by the start of an element with
     * attributes.
     *
     * @param newAttributes The attributes associated with the element that
     *                      caused this error.
     */
    public void setAttributes(Attributes newAttributes) {
        attributes = newAttributes;
    }

    /**
     * Retrieves the attributes associated with the start element that caused
     * this error, if the error was caused by the start of an element with
     * attributes.
     *
     * @return The attributes associated with the element that caused this
     *         error.
     */
    public Attributes getAttributes() {
        return attributes;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 ===========================================================================
*/
