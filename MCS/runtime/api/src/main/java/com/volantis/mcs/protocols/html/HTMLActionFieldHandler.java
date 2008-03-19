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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLActionFieldHandler.java,v 1.2 2003/03/20 15:15:32 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to fixup problems with
 *                              the way HTML handles action fields.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 05-Apr-02    Ian             VBM:2002030606 - Changed updateParameterValue
 *                              to remap the caption to the value on an action.
 *                              Also for images to pass a parameter=value as
 *                              well as the usual parameter.[xy]= value.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class overcomes problems with the way html handles actions.
 */
public final class HTMLActionFieldHandler
        implements FieldHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HTMLActionFieldHandler.class);

    /**
     * The reference to the single allowable instance of this class.
     */
    private static final HTMLActionFieldHandler singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new HTMLActionFieldHandler();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static HTMLActionFieldHandler getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    private HTMLActionFieldHandler() {
    }

    /**
     * In HTML actions which are named and which are displayed as images
     * do not send the value, instead they send the coordinates that the mouse
     * was clicked within the image. This method adds a parameter for the action.
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
                logger.debug("Nothing to do as action does not have a name");
            }
            return;
        }

        String parameter;
        String value;
        boolean imageSelected = false;

        // Copy the x parameter over.
        parameter = name + ".x";
        value = src.getParameterValue(parameter);
        if (value != null) {
            // This is an image so set it to selected.
            imageSelected = true;
            // Move the parameter.
            src.removeParameter(parameter);
            dst.setParameterValue(parameter, value);
        }

        // Copy the y parameter over.
        parameter = name + ".y";
        value = src.getParameterValue(parameter);
        if (value != null) {
            // This is an image so set it to selected.
            imageSelected = true;
            src.removeParameter(parameter);
            dst.setParameterValue(parameter, value);
        }

        // Fixup initial value.

        parameter = name;
        if (imageSelected) {
            dst.setParameterValue(name, descriptor.getInitialValue());
        } else {
            value = src.getParameterValue(parameter);

            if (value != null) {
                //
                // We only want to attempt a remap if we tagged the field (i.e. it had
                // an initial value).
                //
                if (descriptor.getFieldTag() != null) {

                    String caption = (String) descriptor.getFieldTag();
                    //
                    // HTML returns the caption and has no concept of value as in other
                    // protocols. We will map the caption back to the initial value.
                    //
                    if (value.equals(caption)) {
                        src.removeParameter(parameter);
                        dst.setParameterValue(parameter,
                                descriptor.getInitialValue());
                    }
                }
            }
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

 ===========================================================================
*/
