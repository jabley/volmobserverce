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

import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.SingleSelectFieldType;
import com.volantis.mcs.xdime.XDIMEContextInternal;

/**
 * Concrete implementation of {@link AbstractXFSelectElementImpl} which
 * allows only a single value to be selected.
 */
public class XFSelect1ElementImpl extends AbstractXFSelectElementImpl {

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFSelect1ElementImpl(XDIMEContextInternal context) {
        super(XFormElements.SELECT1, context);

        protocolAttributes = new XFSelectAttributes();
        protocolAttributes.setTagName("xfsiselect");    
        ((XFSelectAttributes)protocolAttributes).setMultiple(false);
    }

    // Javadoc inherited.
    protected FieldType getFieldType() {
        return SingleSelectFieldType.getSingleton();
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
