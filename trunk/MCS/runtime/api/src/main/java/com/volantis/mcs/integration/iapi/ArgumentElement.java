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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.integration.iapi;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.integration.iapi.ArgumentAttributes;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The Arguments IAPIElement
 */
public class ArgumentElement implements IAPIElement {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ArgumentElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(ArgumentElement.class);


    // Javadoc inherited from IAPIElement interface
    public int elementStart(MarinerRequestContext context,
                            IAPIAttributes iapiAttributes)
            throws IAPIException {
        
        ArgumentAttributes attrs = (ArgumentAttributes) iapiAttributes;
        
        MarinerPageContext pageContext = 
                ContextInternals.getMarinerPageContext(context);        
        
        ArgumentsElement parent = null;
        try {
            parent = (ArgumentsElement) pageContext.peekIAPIElement();
        } catch (Exception e) {
            logger.error("iapi-element-no-parent-arguments", e);
             throw new IAPIException(
                         exceptionLocalizer.format(
                                     "iapi-element-no-parent-arguments"),
                         e);
        }

        if (parent == null) {
            logger.error("iapi-element-no-parent-arguments");
            throw new IAPIException(exceptionLocalizer.format(
                        "iapi-element-no-parent-arguments"));
        } else {
            parent.addArgument(attrs.getName(), attrs.getValue());
        }
        
        // There should not be any body content for this element.
        return SKIP_ELEMENT_BODY;
    }

    // Javadoc inherited from IAPIElement interface
    public int elementEnd(MarinerRequestContext context,
                          IAPIAttributes iapiAttributes)
            throws IAPIException {        
        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from IAPIElement interface
    public void elementReset(MarinerRequestContext context) {
        // nothing to do.
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
