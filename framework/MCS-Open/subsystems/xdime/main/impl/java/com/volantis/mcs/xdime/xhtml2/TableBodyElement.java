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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 TableBody element object.
 */
public class TableBodyElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(TableBodyElement.class);
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(TableBodyElement.class);

    public TableBodyElement(XDIMEContextInternal context) {
        super(XHTML2Elements.TBODY, context);

        protocolAttributes = new TableBodyAttributes();
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        try {
            protocol.writeOpenTableBody((TableBodyAttributes)protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;        
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context) {

        VolantisProtocol protocol = getProtocol(context);

        protocol.writeCloseTableBody((TableBodyAttributes)protocolAttributes);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05  9673/5  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05  9673/3  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 30-Sep-05  9562/1  pabbott VBM:2005092011 Add XHTML2 Object element

 21-Sep-05  9128/4  pabbott VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05  9128/2  pabbott VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
