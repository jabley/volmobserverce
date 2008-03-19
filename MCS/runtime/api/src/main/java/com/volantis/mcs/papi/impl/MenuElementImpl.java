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
 * $Header: /src/voyager/com/volantis/mcs/papi/MenuElement.java,v 1.8 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed ordering of call to
 *                              super.elementReset.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 31-Mar-03    Sumit           VBM:2003032714 - This class implements the
 *                              ProtocolAttributesContainer interface
 * 16-Apr-03    Geoff           VBM:2003041603 - Add catch clause for
 *                              ProtocolException that throws PAPIException.
 * 17-Apr-03    Adrian          VBM:2003040903 - Updated elementStartImpl to
 *                              populate the new prompt, error message and help
 *                              attributes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.AccessibleMenuStyleProperties;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.MenuAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.menu.MenuInternals;
import com.volantis.mcs.papi.menu.MenuRendererSelectorLocator;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSMenuImageStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuTextStyleKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The menu element.
 */
public class MenuElementImpl
        extends BlockElementImpl
        implements AccessibleMenuStyleProperties {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MenuElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(MenuElementImpl.class);

    /**
     * Old menu type to check against for plain text menus
     */
    private static final String OLD_TYPE_PLAINTEXT = "plaintext";

    /**
     * Old menu type to check against for rollver text menus
     */
    private static final String OLD_TYPE_ROLLOVERTEXT = "rollovertext";

    /**
     * Old menu type to check against for rollover image menus
     */
    private static final String OLD_TYPE_ROLLOVERIMAGE = "rolloverimage";

    /**
     * Stores properties once they have been calculated
     */
    private PropertyValues menuProperties;


    /**
     * Initializes the new instance.
     */
    public MenuElementImpl() {
    }

    // Javadoc inherited from super class.
    protected int elementEndImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        // Pop this element
        pageContext.popElement(this);

        MenuModelBuilder builder = pageContext.getMenuBuilder();

        try {
            Menu menu = builder.endMenu();

            // If a menu is completed (i.e. this is not nested) then need to
            // process it and output against specific renderer and markup.
            if (menu != null) {
                // Obtain a suitable renderer
                MenuRendererSelectorLocator rendererLocator =
                        pageContext.getRendererLocator();
                MenuRendererSelector rendererSelector =
                        rendererLocator.getMenuRendererSelector(pageContext);
                if (rendererSelector == null) {
                    logger.error("selector-rendering-error", "menu");
                    throw new PAPIException(exceptionLocalizer.format(
                            "missing-renderer-selector", "menu"));
                }

                MenuRenderer renderer =
                        rendererSelector.selectMenuRenderer(menu);

                final ShortcutProperties shortcutProperties =
                        menu.getShortcutProperties();
                if (null != shortcutProperties) {
                    // If the device cannot support mixed content in the body
                    // of a link, then update the shortcut properties so that
                    // span elements will not be used.
                    String supportsMixedContent = pageContext.
                            getDevicePolicyValue(DevicePolicyConstants.
                                    X_ELEMENT_A_SUPPORTS_MIXED_CONTENT);
                    if (DevicePolicyConstants.NO_SUPPORT_POLICY_VALUE.equals
                            (supportsMixedContent)) {
                        shortcutProperties.setSupportsSpan(false);
                    }

                    // If the device inserts line breaks before links, then
                    // shortcuts must appear in the active area or the layout
                    // will be incorrect (regardless of the value specified in
                    // the styles).
                    String insertsLineBreaks = pageContext.getDevicePolicyValue(
                            DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE);
                    if ("before".equals(insertsLineBreaks)) {
                        shortcutProperties.setActive(true);
                    }
                }

                // Process the menu
                renderer.render(menu);
            }
        } catch (BuilderException be) {
            logger.error("menu-building-error", be);
            throw new PAPIException(
                    exceptionLocalizer.format("menu-building-error"), be);
        } catch (RendererException re) {
            logger.error("rendering-error", "menu", re);
            throw new PAPIException(
                    exceptionLocalizer.format("rendering-error", "menu"),
                    re);
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    public void elementReset(MarinerRequestContext context) {
        super.elementReset(context);
    }

    // Javadoc inherited from super class.
    protected int elementStartImpl(
            MarinerRequestContext context,
            BlockAttributes blockAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        MenuAttributes attributes = (MenuAttributes) blockAttributes;

        // Get hold of the menu builder - this will be initialised if needed
        MenuModelBuilder builder = pageContext.getMenuBuilder();

        try {
            // Starting a new menu
            builder.startMenu();

            // Setting attributes on the menu

            // Set the pane attribute
            MenuInternals.setPane(builder, attributes.getPane(), pageContext);

            // Get the styling property values for the current element
            Styles styles = pageContext.getStylingEngine().getStyles();

            // Emulate deprecated menu styles if necessary
            useDeprecatedMenuTypes(styles, attributes.getType());

            // Store the propertyvalues for any child use
            menuProperties = styles.getPropertyValues();

            // Set the stylistic information in the model for this element.
            MenuInternals.setElementDetails(builder, attributes, styles);

            // Set the event handling
            MenuInternals.setEvents(builder, attributes, pageContext);

            PolicyReferenceResolver resolver =
                    pageContext.getPolicyReferenceResolver();

            // Set the prompt
            builder.setPrompt(resolver.resolveQuotedTextExpression(
                    attributes.getPrompt()));

            // Set the error message
            builder.setErrorMessage(resolver.resolveQuotedTextExpression(
                    attributes.getErrmsg()));

            // Set the help
            builder.setHelp(resolver.resolveQuotedTextExpression(
                    attributes.getHelp()));

            // Set the title
            builder.setTitle(attributes.getTitle());

            // Set the Shortcut properties.
            setShortcutPropertiesOnBuilder(pageContext, builder, styles);

            // Push this element
            pageContext.pushElement(this);

        } catch (BuilderException be) {
            logger.error("menu-building-error", be);
            throw new PAPIException(
                    exceptionLocalizer.format("menu-building-error"), be);
        }

        return PROCESS_ELEMENT_BODY;
    }


    /**
     * Utility method to create, partially configure and set a
     * <code>ShortcutProperties</code> object on the supplied <code>
     * MenuModelBuilder</code>. This method configures the ShortcutProperties
     * with the separator that should come between the menu item short cut
     * and the menu item.
     *
     * @param pageContext the pagecontext from which the CSSEmulator should
     *                    be obtained.
     * @param builder     the builder on which to set the created ShortcutProperties
     *                    object.
     * @param styles      the Styles from which the shortcut separator will be
     *                    retrieved.
     * @throws BuilderException if any error occurs.
     */
    private void setShortcutPropertiesOnBuilder(
            MarinerPageContext pageContext,
            MenuModelBuilder builder,
            Styles styles)
            throws BuilderException {

        Styles shortcutStyles =
                styles.findNestedStyles(PseudoElements.MCS_SHORTCUT);

        if (shortcutStyles != null) {

            Styles afterStyles =
                    shortcutStyles.findNestedStyles(PseudoElements.AFTER);

            if (afterStyles != null) {

                MutablePropertyValues propertyValues =
                        afterStyles.getPropertyValues();
                StyleList content = (StyleList) propertyValues.
                        getComputedValue(StylePropertyDetails.CONTENT);

                // if we have a specified seperator then create a ShortcutProperties
                // object and configure it.
                ShortcutProperties shortcutProps = new ShortcutProperties();
                if (content != null) {

                    shortcutProps.setSeparatorStyleValue(content);
                }
                builder.setShortcutProperties(shortcutProps);
            }
        }
    }

    // JavaDoc inherited
    public PropertyValues getPropertyValues() {
        return menuProperties;
    }

    /**
     * Utility method which modifies the supplied Styles to include css based
     * style attributes for menus based on the deprecated type attribute of a
     * menu.  If this type is not used, as it should not be with enhanced
     * menus, then the styles will be unaltered.
     *
     * @param styles The current Styles which apply to this element.
     * @param type   The menu type, which may be null
     */
    private void useDeprecatedMenuTypes(
            Styles styles, String type) throws PAPIException {
        // Get the mutable style property values object
        MutablePropertyValues properties = styles.getPropertyValues();

        // If the deprecated type tag has been used, need to mirror its
        // meaning using the menu style properties
        if (type != null & type != "") {
            // Handle the type accordingly
            if (type.equals(OLD_TYPE_PLAINTEXT) ||
                    type.equals(OLD_TYPE_ROLLOVERTEXT)) {
                properties.setComputedValue(
                        StylePropertyDetails.MCS_MENU_IMAGE_STYLE,
                        MCSMenuImageStyleKeywords.NONE);
                properties.setComputedValue(
                        StylePropertyDetails.MCS_MENU_TEXT_STYLE,
                        MCSMenuTextStyleKeywords.PLAIN);
            } else if (type.equals(OLD_TYPE_ROLLOVERIMAGE)) {
                properties.setComputedValue(
                        StylePropertyDetails.MCS_MENU_IMAGE_STYLE,
                        MCSMenuImageStyleKeywords.ROLLOVER);
                properties.setComputedValue(
                        StylePropertyDetails.MCS_MENU_TEXT_STYLE,
                        MCSMenuTextStyleKeywords.NONE);
            } else {
                // This state should not happen as the type (if it exists)
                // should only be one of the above.
                throw new PAPIException(
                        exceptionLocalizer.format("menu-building-error"));

            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8888/3	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 24-May-05	8123/9	ianw	VBM:2005050906 Fix Accurev issues

 24-May-05	8123/7	ianw	VBM:2005050906 Fix accurev merge issues

 24-May-05	8123/4	ianw	VBM:2005050906 Fix accurev merge issues

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 17-Feb-05	6957/2	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 16-Feb-05	6129/13	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/11	matthew	VBM:2004102019 yet another supermerge

 28-Jan-05	6129/9	matthew	VBM:2004102019 supermerge required

 27-Jan-05	6129/7	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/4	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4164/4	pduffin	VBM:2004050404 Fixed some problems with HTML orientation separator

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 10-May-04	4248/1	geoff	VBM:2004050708 Enhance Menu Support: Integration Bugs: Simple problems with rollover images

 29-Apr-04	4091/5	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 27-Apr-04	4010/2	claire	VBM:2004042208 Pane handling in PAPI for enhanced menus

 16-Apr-04	3272/2	philws	VBM:2004021117 Fix supermerge issues

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 06-Apr-04	3429/9	philws	VBM:2004031502 MenuLabelElement implementation

 06-Apr-04	3641/8	claire	VBM:2004032602 Enhancements and updating testcase coverage

 01-Apr-04	3641/6	claire	VBM:2004032602 Refactored stylistic menu code

 30-Mar-04	3641/4	claire	VBM:2004032602 Tidying menu types and styles in PAPI

 30-Mar-04	3641/2	claire	VBM:2004032602 Using menu types and styles in PAPI

 19-Mar-04	3478/1	claire	VBM:2004031805 Removed MenuMarkupGenerator and associated code

 19-Mar-04	3412/6	claire	VBM:2004031201 Improving PAPI and new menus

 18-Mar-04	3412/3	claire	VBM:2004031201 Early implementation of new menus in PAPI

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
