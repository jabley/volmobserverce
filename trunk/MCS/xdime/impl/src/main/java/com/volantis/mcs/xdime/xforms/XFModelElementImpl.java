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
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;

/**
 * Describes how an xforms model element should be processed.
 */
public class XFModelElementImpl extends XFormsElement {

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFModelElementImpl(XDIMEContextInternal context) {
        super(UnstyledStrategy.STRATEGY, XFormElements.MODEL, context);
    }

    // Javadoc inherited.
    protected void initialiseAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        String id = getAttribute(XDIMEAttribute.ID, attributes);

        if (id == null) {
            // if no id is specified, this is the default model.
            id = XFormBuilder.DEFAULT_MODEL_ID;
        }

        // Attempt to retrieve the form descriptor from the session
        EmulatedXFormDescriptor fd = getPageContext(context).
                getEmulatedXFormDescriptor(id);

        if (fd == null) {
            // Create a new fd if none exists in the session.
            fd = new EmulatedXFormDescriptor();
            fd.setContainingFormName(id);
            fd.setName(id);
        }

        context.getXFormBuilder().addModel(id, fd);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
