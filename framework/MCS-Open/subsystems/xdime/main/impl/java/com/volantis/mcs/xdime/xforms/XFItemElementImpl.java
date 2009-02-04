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

import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.xdime.StoreDataStrategy;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Describes how an XForm Item element should be processed.
 *
 * @mock.generate 
 */
public class XFItemElementImpl extends StyledXFormsElement {

    /**
     * Initialize a new instance.
     * @param context
     */
    public XFItemElementImpl(XDIMEContextInternal context) {
        super(XFormElements.ITEM, new StoreDataStrategy(), context);

        protocolAttributes = new SelectOption();
        protocolAttributes.setTagName("xfoption");
    }

    // Javadoc inherited.
    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        // selector item markup should be sent to the entry container instance.
        SelectOption option = ((SelectOption)protocolAttributes);
        option.setEntryContainerInstance(
                getPageContext(context).getCurrentContainerInstance());

        // retrieve this element's parent from the stack
        Object element = context.getCurrentElement();
        if (element instanceof AbstractXFSelectElementImpl) {
            ((XFSelectAttributes)((AbstractXFSelectElementImpl)element).
                    getProtocolAttributes()).addOption(option);
        } else {
            throw new XDIMEException("xdime-bad-stack");
        }
    }

    // javadoc inherited
    public boolean isElementAtomic() {
        return true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 12-Oct-05	9673/6	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/5	emma	VBM:2005092807 Adding tests for XForms emulation

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
