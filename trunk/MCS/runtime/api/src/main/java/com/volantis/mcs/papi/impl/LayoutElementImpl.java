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
 * $Header: /src/voyager/com/volantis/mcs/papi/LayoutElement.java,v 1.3 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-02    Steve           VBM:2002021105 - Created.
 *                              Element to house elements that alter the layout
 *                              at runtime.
 * 22-Mar-02    Steve           VBM:2002031801  An attempt is made to check
 *                              that the layout element is the first element
 *                              within a canvas. This is not foolproof however
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 23-Apr-03    Steve           VBM:2003041606 - Override hasMixedContent() to
 *                              return false
 * 19-May-03    Chris W         VBM:2003051902 - hasMixedContent() made package
 *                              protected.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The canvas element.
 */
public class LayoutElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(LayoutElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(LayoutElementImpl.class);

    /**
     * Create a new <code>LayoutElement</code>.
     */
    public LayoutElementImpl() {
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        if (!pageContext.isCanvasFirstChild()) {
            logger.error("layout-must-be-first");
            throw new PAPIException(
                    exceptionLocalizer.format("canvas-layout-not-first"));
        }
        logger.info("device-name-info", pageContext.getDeviceName());
        logger.info("device-info", pageContext.getDevice());
        logger.info("device-layout-info", pageContext.getDeviceLayout());
        logger.info("device-layout-context-info",
                pageContext.getDeviceLayoutContext());

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 02-Mar-05	7240/1	emma	VBM:2005022812 mergevbm from MCS 3.3

 02-Mar-05	7214/1	emma	VBM:2005022812 Fixing leftover localization logging problems

 21-Dec-04	6531/1	doug	VBM:2004122005 Enhancements to the MessageLocalizer interface

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 29-Jan-04	2756/3	ianw	VBM:2004012601 More Localisation changes

 28-Jan-04	2756/1	ianw	VBM:2004012601 Start of Localisation of Message Files

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
