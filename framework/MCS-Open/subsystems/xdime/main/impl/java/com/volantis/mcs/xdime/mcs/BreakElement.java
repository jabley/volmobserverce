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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.mcs;

import com.volantis.mcs.xdime.*;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The XDIME 2 'br' element, used to force line break
 *
 * XHTML 2 does not support an equivalent of the HTML line break element,
 * but legacy and HTML sources make heavy use of it and they are difficult
 * to integrate without it, so we add this element to mcs namespace
 */

public class BreakElement extends StylableXDIMEElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(BreakElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(BreakElement.class);


    public BreakElement(XDIMEContextInternal context) {
        super(MCSElements.BR, UnstyledStrategy.STRATEGY, context);

        protocolAttributes = new LineBreakAttributes() ;
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        // Call the protocol to generate appropriate line break representation
        protocol.writeLineBreak((LineBreakAttributes)protocolAttributes);

        // Never process the body
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
