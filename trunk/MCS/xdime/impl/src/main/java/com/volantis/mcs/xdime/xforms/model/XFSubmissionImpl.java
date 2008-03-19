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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms.model;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Concrete implementation of the modelled representation of a xforms
 * submission element.
 */
public class XFSubmissionImpl implements XFSubmission {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    XFSubmissionImpl.class);

    XFormMethod method;
    URI action;
    String id;
    EventAttributes events;
    String enclosingModelID;

    public XFSubmissionImpl(String id, EventAttributes events, String action,
            String method, String enclosingModelID)
            throws XDIMEException {

        if (id == null) {
            throw new IllegalArgumentException("Submission id cannot be null");
        }

        try {
            this.method = XFormMethod.valueOf(method);
            this.action = new URI(action);
            this.id = id;
            this.events = events;
            this.enclosingModelID = enclosingModelID;
        } catch (URISyntaxException e) {
            throw new XDIMEException(exceptionLocalizer.format(
                        "xdime-attribute-value-invalid",
                        new String[]{action, "action"}));
        }
    }

    // Javadoc inherited.
    public XFormMethod getMethod() {
        return method;
    }

    // Javadoc inherited.
    public void setMethod(XFormMethod method) {
        this.method = method;
    }

    // Javadoc inherited.
    public URI getAction() {
        return action;
    }

    // Javadoc inherited.
    public void setAction(URI action) {
        this.action = action;
    }

    // Javadoc inherited.
    public String getId() {
        return id;
    }

    // Javadoc inherited.
    public void setId(String id) {
        this.id = id;
    }

    // Javadoc inherited.
    public String getEnclosingModelID() {
        return enclosingModelID;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/3	emma	VBM:2005092807 Adding tests for XForms emulation

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
