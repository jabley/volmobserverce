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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class overcomes problems with the way VDXML handles actions.
 * It provides a means of updating the parameters in the URL into
 * a consistent form.
 */
public final class VDXMLActionFieldHandler implements FieldHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(VDXMLActionFieldHandler.class);

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final VDXMLActionFieldHandler singleton;

    /**
     * Initialise the static fields.
     */
    static {
      // Always initialise to prevent a synchronization problem if it is
      // done lazily.
      singleton = new VDXMLActionFieldHandler();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static VDXMLActionFieldHandler getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private VDXMLActionFieldHandler() {
    }

    // JavaDoc inherited
    public void updateParameterValue(MarinerPageContext context,
                                     FieldDescriptor descriptor,
                                     MarinerURL src,
                                     MarinerURL dst) {

        // Only need to do something if the name was specified
        String name = descriptor.getName();
        if (name == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Nothing to do as field " + descriptor
                             + " does not have a name");
            }
            return;
        }

        // Get the encoded value for the paramter
        String value = src.getParameterValue(name);
        if (logger.isDebugEnabled()) {
            logger.debug("Decoding value " + value + " for field " + name);
        }

        String decodedValue = null;
        if (value != null && !value.equals("")) {
            // Strip the first character off
            decodedValue = value.substring(1);
        }

        // Update the source and destination urls.
        src.removeParameter(name);
        if (decodedValue != null) {
            if (logger.isDebugEnabled()){
                logger.debug("Decoded value for " + value + " is " +
                             decodedValue);
            }
            dst.setParameterValue(name, decodedValue);
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 07-Jun-04	4495/3	claire	VBM:2004051807 Added JavaDoc where missing, and tidied up some existing comments

 28-May-04	4495/1	claire	VBM:2004051807 Colour handling for form fields and basic action support

 ===========================================================================
*/
