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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.xdime.StoreDataStrategy;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Defines how an XForms Value element should be processed.
 */
public class XFValueElementImpl extends StyledXFormsElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(XFValueElementImpl.class);

    public XFValueElementImpl(XDIMEContextInternal context) {
        super(XFormElements.VALUE, new StoreDataStrategy(), context);

        protocolAttributes = new SelectOption();
        protocolAttributes.setTagName("xfvalue");
    }

    // Javadoc inherited.
    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        // retrieve this element's parent from the stack
        Object element = context.getCurrentElement();
        // if it can have a value (i.e. it is an item) then set it
        if (element instanceof XFItemElementImpl) {
            SelectOption select = (SelectOption)
                    ((XFItemElementImpl)element).getProtocolAttributes();
            // the value appears as the body content of the value element
            final String value = getCharData();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("XForms value text: " + value);
            }
            select.setValue(value);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05	9673/5	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
