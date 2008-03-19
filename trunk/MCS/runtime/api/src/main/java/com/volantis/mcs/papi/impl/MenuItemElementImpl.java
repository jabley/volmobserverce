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
 * $Header: /src/voyager/com/volantis/mcs/papi/MenuItemElement.java,v 1.3.4.1 2002/08/06 13:32:29 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Nov-01    Paul            VBM:2001112909 - Created.
 * 11-Mar-02    Paul            VBM:2001122105 - Initialise any general event
 *                              attributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 06-Aug-02    Paul            VBM:2002080604 - Added target attribute.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 31-Mar-03    Sumit           VBM:2003032714 - Converted references to parent
 *                              element to ProtocolAttributesContainer and
 *                              MenuAttributes to MenuItemCollector in
 *                              elementStart()
 * 17-Apr-03    Adrian          VBM:2003040903 - Updated elementStart to
 *                              populate the prompt field.
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
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.AccessibleMenuStyleProperties;
import com.volantis.mcs.papi.MenuItemAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.menu.MenuInternals;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.RolloverComponentReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuImageStyleKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The menu item element.
 */

public class MenuItemElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MenuItemElementImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(MenuItemElementImpl.class);

    /**
     * The asset reference used for a normal image for menu item icon
     */
    private ImageAssetReference normal = null;

    /**
     * The asset reference used for an over image for menu item icon
     */
    private ImageAssetReference over = null;


    /**
     * Initializes the new instance.
     */
    public MenuItemElementImpl() {
    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        MenuItemAttributes attributes = (MenuItemAttributes) papiAttributes;

        Styles styles = pageContext.getStylingEngine().getStyles();

        processMenuItemStyles(attributes, styles);

        MenuModelBuilder builder = pageContext.getMenuBuilder();

        try {
            // Start a new menu item
            builder.startMenuItem();

            // Start a new menu label
            builder.startLabel();

            // Start a new menu text
            builder.startText();

            // Set the text by constructing an artificial output buffer from
            // the text attribute
            // NB: The allocation mechanism must be compatible with the
            //     de-allocation mechanism used in
            //     {@link MenuElement#releaseLabelOutputBuffers}
            OutputBuffer text = pageContext.getProtocol().
                    getOutputBufferFactory().createOutputBuffer();
            text.writeText(attributes.getText());
            builder.setText(text);

            // End the menu text
            builder.endText();

            // OK, get the attributes from PAPI
            String onImage = attributes.getOnImage();
            String offImage = attributes.getOffImage();
            String rolloverImage = attributes.getRolloverImage();
            String image = attributes.getImage();

            // Set the normal (on) and over (off) images
            setNormalAndOverImages(pageContext, onImage, offImage,
                    rolloverImage, image);

            // Create the menu icon on the builder
            if (normal != null) {
                // Start a new menu icon
                builder.startIcon();

                // Set the values
                builder.setNormalImageURL(normal);
                if (over != null) {
                    builder.setOverImageURL(over);
                }

                // End the menu icon
                builder.endIcon();
            }

            // End the menu label;
            builder.endLabel();

            // Set the stylistic information in the model for this element.
            MenuInternals.setElementDetails(builder, attributes,
                    styles);

            // Set the segment
            builder.setSegment(attributes.getSegment());

            // Set the target
            builder.setTarget(attributes.getTarget());

            // Set the event handling
            MenuInternals.setEvents(builder, attributes, pageContext);

            // Set the pane attribute
            MenuInternals.setPane(builder, attributes.getPane(), pageContext);

            // Initialise the attributes specific to this field.
            String accessKey = attributes.getAccessKey();
            String shortcut = attributes.getShortcut();

            if (shortcut != null) {
                if (accessKey != null) {
                    throw new PAPIException(exceptionLocalizer.
                            format(
                            "menu-building-shortcut-and-accesskey-error"));
                }
            } else {
                shortcut = accessKey;
            }

            PolicyReferenceResolver resolver =
                    pageContext.getPolicyReferenceResolver();

            // Process the shortcut
            TextAssetReference shortcutObj =
                    resolver.resolveQuotedTextExpression(shortcut);
            builder.setShortcut(shortcutObj);

            //Process the href as a mariner expression
            LinkAssetReference href = resolver.resolveQuotedLinkExpression(
                    attributes.getHref(), PageURLType.MENU_ITEM);
            builder.setHref(href);

            // Process the prompt as a mariner expression
            TextAssetReference prompt = resolver.resolveQuotedTextExpression(
                    attributes.getPrompt());
            builder.setPrompt(prompt);

            // Set the title.
            builder.setTitle(attributes.getTitle());

        } catch (BuilderException be) {
            logger.error("menu-building-error", be);
            throw new PAPIException(
                    exceptionLocalizer.format("menu-building-error"), be);
        }

        return PROCESS_ELEMENT_BODY;
    }

    /**
     * using the MenuItemAttributes find if the offColor and onColor values
     * have been specified. If the offColor has been set this should be mapped
     * to the style colour value. If the onColor has been set this should be
     * mapped to the :hover style value
     *
     * @param attributes
     * @param styles
     */
    private void processMenuItemStyles(
            MenuItemAttributes attributes,
            Styles styles) {

        String offColour = attributes.getOffColor();
        String onColour = attributes.getOnColor();

        if (offColour != null) {
            StyleColorName colorStyleValue =
                    StyleColorNames.getColorByName(offColour);

            styles.getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.COLOR, colorStyleValue);
        }

        if (onColour != null) {
            StyleColorName colorStyleValue =
                    StyleColorNames.getColorByName(onColour);

            styles.getNestedStyles(StatefulPseudoClasses.HOVER).
                    getPropertyValues().setComputedAndSpecifiedValue(
                    StylePropertyDetails.COLOR, colorStyleValue);
        }
    }

    /**
     * A utility method that returns true if a given string exists.  This means
     * that the string is not null.  It does not check for empty strings.
     *
     * @param string The string to check
     * @return True if the string is null, false otherwise
     */
    private boolean exists(String string) {
        return (string != null);
    }

    /**
     * A utility method that sets the {@link #normal} and {@link #over} fields
     * to appropriate values for the current page context and the various
     * image components provided.
     *
     * @param pageContext   The current page context, needed for resolving the
     *                      component names into asset references.  May not
     *                      be null.
     * @param onImage       The name of the image component for the on image.
     * @param offImage      The name of the component for the off image.
     * @param rolloverImage The name of the component for the rollover image.
     * @param image         The name of the component for the image.
     */
    private void setNormalAndOverImages(
            MarinerPageContext pageContext,
            String onImage, String offImage,
            String rolloverImage, String image) {

        if (pageContext == null) {
            throw new IllegalArgumentException(
                    "The page context cannot be null for image resolution to succeed");
        }

        // Obtain the parent menu's properties
        AccessibleMenuStyleProperties element =
                (AccessibleMenuStyleProperties) pageContext.getCurrentElement();
        PropertyValues parentProperties = element.getPropertyValues();

        // Obtain specified image style, if any, from the parent properties
        StyleValue imageStyle = parentProperties.getComputedValue(
                StylePropertyDetails.MCS_MENU_IMAGE_STYLE);

        // Handle the various steps in the image reference resolution
        if (imageStyle == MCSMenuImageStyleKeywords.ROLLOVER) {
            // A rollover menu has been specified
            if (exists(rolloverImage)) {
                // There is a rollover image specified so this will be used
                RolloverComponentReference reference =
                        createRolloverComponentReference(pageContext,
                                rolloverImage);
                normal = reference.getNormal();
                over = reference.getOver();
            } else {
                // No rollover has been specified, so set the normal
                // and over references independently.
                if (exists(offImage)) {
                    normal = createImageAssetReference(pageContext, offImage);
                } else if (exists(image)) {
                    normal = createImageAssetReference(pageContext, image);
                }

                if (exists(onImage) && (normal != null)) {
                    over = createImageAssetReference(pageContext, onImage);
                }
            }
        } else if (imageStyle == MCSMenuImageStyleKeywords.PLAIN) {
            // A plain menu has been specified
            if (exists(image)) {
                // Use the image if it has been set
                normal = createImageAssetReference(pageContext, image);
            } else if (exists(offImage)) {
                // Otherwise use the off image if that has been set
                normal = createImageAssetReference(pageContext, offImage);
            }

        } else {
            // The imageStyle specified is MarinerMenuImageStyleEnumeration
            // .MARINER_MENU_IMAGE_STYLE__NONE or has not been set, so do
            // nothing here.
        }

    }

    /**
     * Javadoc inherited from super class.
     */
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        MenuModelBuilder builder = pageContext.getMenuBuilder();

        try {
            // End the existing menu item
            builder.endMenuItem();
        } catch (BuilderException be) {
            logger.error("menu-building-error", be);
            throw new PAPIException(
                    exceptionLocalizer.format("menu-building-error"), be);
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return false;
    }

    /**
     * Creates an instance of an image asset reference based on an expression
     * which resolves to an image component identity.
     *
     * @param context         The context resolve the expression against.
     * @param imageExpression An expression which resolves to an image
     *                        component identity.
     * @return An image asset reference based on the values provided.
     */
    private static ImageAssetReference createImageAssetReference(
            MarinerPageContext context, String imageExpression) {

        // Create the reference for the image by resolving the expression.
        // Note that this must be done up front to because we don't want
        // expressions being evaluated out of order.
        PolicyReferenceResolver resolver =
                context.getPolicyReferenceResolver();
        RuntimePolicyReference reference =
                resolver.resolveUnquotedPolicyExpression(imageExpression,
                        PolicyType.IMAGE);
        return new DefaultComponentImageAssetReference(reference,
                context.getAssetResolver());
    }

    /**
     * Creates a rollover component reference based on an expression which
     * resolves to a rollover image component identity.
     *
     * @param context            The context resolve the expression against.
     * @param rolloverExpression An expression which resolves to a rollover
     *                           image component identity.
     * @return A component reference based on the values provided.
     */
    private static RolloverComponentReference createRolloverComponentReference(
            MarinerPageContext context, String rolloverExpression) {

        PolicyReferenceResolver resolver =
                context.getPolicyReferenceResolver();

        // Create the identity for the rollover by resolving the expression.
        // Note that this must be done up front to because we don't want
        // expressions being evaluated out of order.
        RuntimePolicyReference reference =
                resolver.resolveUnquotedPolicyExpression(rolloverExpression,
                        PolicyType.ROLLOVER_IMAGE);

        return new RolloverComponentReference(
                context.getPolicyFetcher(), context.getAssetResolver(),
                reference);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 17-Oct-05	9840/3	pduffin	VBM:2005082215 Committing after fixing conflicts

 27-Sep-05	9609/1	ibush	VBM:2005082215 Move on/off color values for menu items

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 22-Apr-05	7766/1	matthew	VBM:2005041907 Allow shortcuts to be specified using 'shortcut' or 'accessKey'

 10-Mar-05	7022/5	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 09-Mar-05	7022/3	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 13-Sep-04	5371/1	byron	VBM:2004083102 Title attribute on the <menuitem> element is being ignored

 01-Sep-04	5357/1	byron	VBM:2004083102 Title attribute on the <menuitem> element is being ignored

 01-Jul-04	4778/2	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4248/1	geoff	VBM:2004050708 Enhance Menu Support: Integration Bugs: Simple problems with rollover images

 29-Apr-04	4091/4	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 27-Apr-04	4010/1	claire	VBM:2004042208 Pane handling in PAPI for enhanced menus

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 06-Apr-04	3429/8	philws	VBM:2004031502 MenuLabelElement implementation

 01-Apr-04	3641/7	claire	VBM:2004032602 Refactored stylistic menu code

 30-Mar-04	3641/5	claire	VBM:2004032602 Tidying menu types and styles in PAPI

 30-Mar-04	3641/3	claire	VBM:2004032602 Using menu types and styles in PAPI

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 19-Mar-04	3412/6	claire	VBM:2004031201 Improving PAPI and new menus

 18-Mar-04	3412/3	claire	VBM:2004031201 Early implementation of new menus in PAPI

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 13-Aug-03	958/1	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 ===========================================================================
*/
