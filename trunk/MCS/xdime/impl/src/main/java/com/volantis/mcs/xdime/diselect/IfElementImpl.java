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
package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.ValidationStrategy;
import com.volantis.mcs.xdime.schema.DISelectElements;

/**
 * Concrete implementation of an &lt;if&gt; element.
 */
public class IfElementImpl extends DISelectElement {

    public IfElementImpl(XDIMEContextInternal context) {
        super(DISelectElements.IF, context, ValidationStrategy.VALIDATE,
                AttributeUsage.MANDATORY);
    }

    // Javadoc inherited.
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // We know that the if's expression evaluated to true to reach this 
        // point, so we return that the element body should be processed.
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited.
    public XDIMEResult exprElementEnd(XDIMEContextInternal context) throws XDIMEException {
        // always continue processing regardless
        return XDIMEResult.CONTINUE_PROCESSING;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 09-Sep-05	9415/5	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 27-Jul-05	9060/3	tom	VBM:2005071304 Added Sel Select

 ===========================================================================
*/
