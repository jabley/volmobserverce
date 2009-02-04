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

import java.util.Map;
import java.util.HashMap;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The Arguments IAPIElement
 */
public class ArgumentsElement implements IAPIElement {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(ArgumentsElement.class);

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ArgumentsElement.class);

    /**
     * The arguments for the parent markup plugin element.
     */ 
    private Map arguments;
    
    /**
     * The parent InvokeElement.
     */ 
    private InvokeElement parent;
    

    // Javadoc inherited from IAPIElement interface
    public int elementStart(MarinerRequestContext context,
                            IAPIAttributes iapiAttributes)
            throws IAPIException {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);        
        
        arguments = new HashMap();
        
        parent = null;
        try {
            parent = (InvokeElement) pageContext.peekIAPIElement();
        } catch (Exception e) {            
            logger.error("iapi-element-no-parent-markup-plugin", e);
             throw new IAPIException(
                         exceptionLocalizer.format(
                                     "iapi-element-no-parent-markup-plugin"),
                         e);
        }
        
        if (parent == null) {
            logger.error("iapi-element-no-parent-markup-plugin");
            throw new IAPIException(
                         exceptionLocalizer.format(
                                     "iapi-element-no-parent-markup-plugin"));
        }
        
        pageContext.pushIAPIElement(this);
                
        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from IAPIElement interface
    public int elementEnd(MarinerRequestContext context,
                          IAPIAttributes iapiAttributes)
            throws IAPIException {        
        if (parent != null) {
            parent.setArguments(arguments);
            // only pop ourselves of the stack if there was a parent.  if there
            // wasn't we would not have pushed ourselves onto the stack.
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);
            pageContext.popIAPIElement();
        }                
        
        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from IAPIElement interface
    public void elementReset(MarinerRequestContext context) {
        parent = null;
        arguments = null;
    }

    /**
     * Add a child name-value pair argument to the Map of arguments to pass to
     * the parent markup plugin.
     * @param name The name of the argument.
     * @param value The value of the argument.
     */ 
    void addArgument(String name, String value) {
        arguments.put(name, value);
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
