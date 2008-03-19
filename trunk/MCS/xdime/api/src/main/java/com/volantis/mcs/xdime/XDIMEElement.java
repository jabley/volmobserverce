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

package com.volantis.mcs.xdime;

import com.volantis.mcs.xdime.ElementOutputState;


/**
 * Interface for an element.
 *
 * <p>This must not be exposed as a public API without first removing the
 * XDIMEContent parameter from the methods. That should be being passed into
 * the element when it is first constructed.</p>
 *
 * @todo See above comment.
 */
public interface XDIMEElement {

    /**
     * Called at the start of an XDIME element.
     *
     * @param context       The XDIMEContext within which this element is
     *                      being processed.
     * @param attributes    The implementation of XDIMEAttributes which
     *                      contains the attributes specific to the
     *                      implementation of PAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws XDIMEException If there was a problem processing the element.
     */
    XDIMEResult elementStart (XDIMEContext context, XDIMEAttributes attributes)
            throws XDIMEException;

    /**
     * Called at the end of a XDIME element.
     * <p>
     * If the elementStart method was called then this method will also be
     * called unless an Exception occurred during the processing of the
     * body.
     * </p>
     * @param context       The XDIMEContext within which this element is
     *                      being processed.
     * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
     * @throws XDIMEException If there was a problem processing the element.
     */
    XDIMEResult elementEnd (XDIMEContext context) throws XDIMEException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 09-Sep-05	9415/4	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 25-Jul-05	9060/2	tom	VBM:2005071304 Interim Commit so Emma can see the changes we have made

 18-Jul-05	9021/1	ianw	VBM:2005071114 interim commit of XDIME API for DISelect integration

 ===========================================================================
*/
