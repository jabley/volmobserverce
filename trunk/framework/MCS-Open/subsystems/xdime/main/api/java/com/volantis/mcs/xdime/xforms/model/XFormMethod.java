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

import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

public class XFormMethod {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(XFormMethod.class);

    private String method;

    public static final XFormMethod GET = new XFormMethod("get");

    public static final XFormMethod POST = new XFormMethod("post");

    private XFormMethod(String action) {
        this.method = action;
    }

    public String toString() {
        return this.method;
    }

    /**
     * Returns an XFormMethod object holding the value of the specified
     * String. If the methodString is null it defaults to GET.
     *
     * @param methodString the String to be parsed.
     * @return the XFormMethod which maps to the specified String
     * @throws XDIMEException if the specified String did not map to a valid
     *                        XFormMethod
     */
    public static XFormMethod valueOf(String methodString)
            throws XDIMEException {

        XFormMethod method = GET;
        if (methodString != null) {
            if (GET.toString().equalsIgnoreCase(methodString)) {
                method = GET;
            } else if (POST.toString().equalsIgnoreCase(methodString)) {
                method = POST;
            } else {
                throw new XDIMEException(exceptionLocalizer.format(
                        "xdime-attribute-value-invalid",
                        new String[]{methodString, "method"}));
            }
        }
        return method;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9637/3	emma	VBM:2005092807 Adding tests for XForms emulation

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
