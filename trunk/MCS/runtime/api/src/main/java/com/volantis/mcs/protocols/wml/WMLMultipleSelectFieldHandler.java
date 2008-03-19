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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLMultipleSelectFieldHandler.java,v 1.4 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Mar-02    Paul            VBM:2001101803 - Created to fixup problems with
 *                              the way WML handles multiple select fields.
 * 22-Feb-02    Paul            VBM:2002021802 - Moved from protocols package.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class overcomes problems with the way wml handles multiple select
 * fields.
 */
public final class WMLMultipleSelectFieldHandler
        implements FieldHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    WMLMultipleSelectFieldHandler.class);

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final WMLMultipleSelectFieldHandler singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new WMLMultipleSelectFieldHandler();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static WMLMultipleSelectFieldHandler getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private WMLMultipleSelectFieldHandler() {
    }

    /**
     * In WML multiple values are passed as a semi-colon separated list which
     * is different to the way HTML works. This class cooperates with the
     * protocol in order to correctly encode and decode the values.
     */
    public void updateParameterValue(
            MarinerPageContext context,
            FieldDescriptor descriptor,
            MarinerURL src,
            MarinerURL dst) {

        // Only need to do something if the name was specified.
        String name = descriptor.getName();
        if (name == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Nothing to do as field " + descriptor
                        + " does not have a name");
            }
            return;
        }

        VolantisProtocol protocol = context.getProtocol();
        WMLRoot wmlProtocol = (WMLRoot) protocol;

        // Get the encoded value.
        String value = src.getParameterValue(name);

        if (logger.isDebugEnabled()) {
            logger.debug("Decoding value " + value + " for field " + name);
        }

        // Decode the string into multiple values.
        String[] values = wmlProtocol.decodeMultipleSelectValues(value);
        if (logger.isDebugEnabled()) {
            if (values == null) {
                logger.debug("No values to decode");
            } else {
                for (int i = 0; i < values.length; i += 1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "Decoded value " + i + " is " + values[i]);
                    }
                }
            }
        }

        // Update the source and destination urls.
        src.removeParameter(name);
        if (values != null) {
            dst.setParameterValues(name, values);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
