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
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.menu;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.AttrsAttributes;
import com.volantis.mcs.papi.BaseAttributes;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;


/**
 * Class that is used to provide some helper methods for menus.
 */
public class MenuInternals {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MenuInternals.class);

    /**
     * Private constructor to prevent any instantiations of this class.
     */
    private MenuInternals() {
        // Nothing!
    }

    /**
     * Set the provided element details in the model.
     * <p>
     * Just a convenience method to avoid code duplication.
     * 
     * @param builder       The builder to update
     * @param attributes    The attributes to set.
     * @param styles        The style property values to set.
     * @throws BuilderException
     */ 
    public static void setElementDetails(MenuModelBuilder builder,
            BaseAttributes attributes, Styles styles)
            throws BuilderException {
        
        // Set the style on the builder based on the properties.
        builder.setElementDetails(attributes.getElementName(),
                attributes.getId(),
                styles);
    }

    /**
     * Sets the pane for various menu components using the provided menu
     * builder and pane name.
     *
     * @param builder           The builder to update
     * @param paneName          The pane name to use.  If null the pane will
     *                          not be set.
     * @param context           The page context in which this takes place.
     * @throws BuilderException If there is a problem with the builder, or if
     *                          setting the pane is illegal in the current
     *                          builder state.
     */
    public static void setPane(MenuModelBuilder builder,
                               String paneName,
                               MarinerPageContext context)
            throws BuilderException {
        if (paneName != null) {
            builder.setPane(FormatReferenceParser.parsePane(paneName, context));
        }
    }

    /**
     * Sets the appropriate attributes for a menu using the provided menu
     * builder.
     *
     * @param builder           The builder to update
     * @param attributes        The attributes to process
     * @param context           The page context in which this takes place
     *
     * @throws BuilderException If there is a problem with the builder
     */
    public static void setEvents(MenuModelBuilder builder,
                                 AttrsAttributes attributes,
                                 MarinerPageContext context)
                                                    throws BuilderException {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialising events for menu builder");
        }

        String value;

        if ((value = attributes.getOnClick()) != null) {
            doEvent(builder, context, value, EventType.ON_CLICK);
        }
        if ((value = attributes.getOnDoubleClick()) != null) {
            doEvent(builder, context, value, EventType.ON_DOUBLE_CLICK);
        }
        if ((value = attributes.getOnKeyDown()) != null) {
            doEvent(builder, context, value, EventType.ON_KEY_DOWN);
        }
        if ((value = attributes.getOnKeyPress()) != null) {
            doEvent(builder, context, value, EventType.ON_KEY_PRESS);
        }
        if ((value = attributes.getOnKeyUp()) != null) {
            doEvent(builder, context, value, EventType.ON_KEY_UP);
        }
        if ((value = attributes.getOnMouseDown()) != null) {
            doEvent(builder, context, value, EventType.ON_MOUSE_DOWN);
        }
        if ((value = attributes.getOnMouseMove()) != null) {
            doEvent(builder, context, value, EventType.ON_MOUSE_MOVE);
        }
        if ((value = attributes.getOnMouseOut()) != null) {
            doEvent(builder, context, value, EventType.ON_MOUSE_OUT);
        }
        if ((value = attributes.getOnMouseOver()) != null) {
            doEvent(builder, context, value, EventType.ON_MOUSE_OVER);
        }
        if ((value = attributes.getOnMouseUp()) != null) {
            doEvent(builder, context, value, EventType.ON_MOUSE_UP);
        }
    }

    /**
     * Creates the appropriate handler object for a given type and updates the
     * builder with it.
     *
     * @param builder           The builder to set the event on
     * @param pageContext       The context in which this takes place
     * @param value             The value to use in the event handling
     * @param type              The event type to create a handler for
     *
     * @throws BuilderException If there is a problem with the builder
     */
    private static void doEvent(MenuModelBuilder builder,
                                MarinerPageContext pageContext,
                                String value, EventType type)
                                                    throws BuilderException {

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();
        ScriptAssetReference handler =
                resolver.resolveQuotedScriptExpression(value);
        builder.setEventHandler(type, handler);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 04-Mar-05	7270/1	pcameron	VBM:2004070817 Fixed menu and menuitem styling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/4	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 10-May-04	4248/1	geoff	VBM:2004050708 Enhance Menu Support: Integration Bugs: Simple problems with rollover images

 29-Apr-04	4091/5	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 27-Apr-04	4010/1	claire	VBM:2004042208 Pane handling in PAPI for enhanced menus

 19-Mar-04	3412/3	claire	VBM:2004031201 Improving PAPI and new menus

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 ===========================================================================
*/
