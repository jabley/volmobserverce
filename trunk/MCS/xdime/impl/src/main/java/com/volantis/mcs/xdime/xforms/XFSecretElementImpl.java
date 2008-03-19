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

import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.TextInputFieldType;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.events.XFormsFocusEvent;
import com.volantis.mcs.xdime.events.XFormsValueChangedEvent;

public class XFSecretElementImpl extends XFormsControlElement {

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFSecretElementImpl(XDIMEContextInternal context) {
        super(XFormElements.SECRET, context);

        protocolAttributes = new XFTextInputAttributes();

        // Add xfsecret specific events.
        new XFormsValueChangedEvent().registerEvents(eventMapper);
        new XFormsFocusEvent().registerEvents(eventMapper);
    }

    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // set the type of the XFTextInput to passwordType
        final String passwordType = "password";
        ((XFTextInputAttributes)protocolAttributes).setType(passwordType);
    }

    // Javadoc inherited.
    protected FieldType getFieldType() {
        return TextInputFieldType.getSingleton();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
