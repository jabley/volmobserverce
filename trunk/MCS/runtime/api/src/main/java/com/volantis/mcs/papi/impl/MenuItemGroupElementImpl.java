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
 * $Header: /src/voyager/com/volantis/mcs/papi/MenuItemGroupElement.java,v 1.3 2003/04/11 09:15:01 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 31-Mar-2003  Sumit       VBM:2003032714 - PAPI element for MenuItemGroup
 *                          support
 * 08-Apr-2003  Sumit       VBM:2003032713 - Added pushElement and popElement
 *                          calls to element start and end
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.MenuItemGroupAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.menu.MenuInternals;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The menu item group element.
 */

public class MenuItemGroupElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MenuItemGroupElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(MenuItemGroupElementImpl.class);
    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * Create a new <code>MenuItemGroupElement</code>.
     */
    public MenuItemGroupElementImpl() {
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes baseAttributes) throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        MenuItemGroupAttributes attributes =
                (MenuItemGroupAttributes) baseAttributes;

        MenuModelBuilder builder = pageContext.getMenuBuilder();

        try {
            // Retrive the pane name...
            String paneName = attributes.getPane();

            // Emulating behaviour for BlockAttributes and panes
            if (paneName != null) {
                // Try and find the pane with the specified name, if it could
                // not be found then return and skip the element body.
                FormatReference formatRef =
                        FormatReferenceParser.parsePane(paneName, pageContext);
                Pane pane = pageContext.getPane(formatRef.getStem());

                if (pane == null) {
                    skipped = true;
                    return SKIP_ELEMENT_BODY;
                }
            }

            // Start a new menu group
            builder.startMenuGroup();

            // Setting attributes on the menu group

            // Set the pane attribute
            MenuInternals.setPane(builder, paneName, pageContext);

            // Set the stylistic information in the model for this element.
            MenuInternals.setElementDetails(builder, attributes,
                    pageContext.getStylingEngine().getStyles());

        } catch (BuilderException be) {
            logger.error("menu-building-error", be);
            throw new PAPIException(
                    exceptionLocalizer.format("menu-building-error"), be);
        }

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes baseAttributes) throws PAPIException {

        // Emulating behaviour for BlockAttributes and panes
        if (!skipped) {
            // Only want to do this is the element start did not skip processing
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);

            MenuModelBuilder builder = pageContext.getMenuBuilder();

            try {
                // End the existing menu group
                builder.endMenuGroup();
            } catch (BuilderException be) {
                logger.error("menu-building-error", be);
                throw new PAPIException(
                        exceptionLocalizer.format("menu-building-error"), be);
            }
        }
        return CONTINUE_PROCESSING;
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.papi.PAPIElement#elementReset(com.volantis.mcs.context.MarinerRequestContext)
     */
    public void elementReset(MarinerRequestContext context) {
        // TODO Auto-generated method stub
        super.elementReset(context);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 10-May-04	4248/1	geoff	VBM:2004050708 Enhance Menu Support: Integration Bugs: Simple problems with rollover images

 29-Apr-04	4091/5	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 28-Apr-04	4010/5	claire	VBM:2004042208 Refined skipping code for menu item groups

 27-Apr-04	4010/3	claire	VBM:2004042208 Pane handling in PAPI for enhanced menus

 16-Apr-04	3272/2	philws	VBM:2004021117 Fix supermerge issues

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 19-Mar-04	3412/5	claire	VBM:2004031201 Improving PAPI and new menus

 18-Mar-04	3412/2	claire	VBM:2004031201 Early implementation of new menus in PAPI

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
