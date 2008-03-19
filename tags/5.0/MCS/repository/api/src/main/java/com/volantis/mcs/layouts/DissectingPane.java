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
 * $Header: /src/voyager/com/volantis/mcs/layouts/DissectingPane.java,v 1.20 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright
 *                              and added visit method.
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit method to
 *                              return a boolean.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 01-Oct-01    Doug            VBM:2001092501 - Added Format.BACKGROUND_
 *                              COMPONENT_TYPE_ATTRIBUTE to the userAttributes
 *                              and defaultAttributes arrays.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed some unused
 *                              code which was commented out.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 03-May-02    Paul            VBM:2002042203 - Added getMaximumContentSize.
 * 04-Dec-02    Phil W-S        VBM:2002111208 - Added handling for previous
 *                              and next shard style class attributes.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 14-Mar-03    Doug            VBM:2003030409 - Added 
 *                              SHARD_LINK_ORDER_ATTRIBUTE to the 
 *                              userAttributes array. Added the new 
 *                              isNextLinkFirst() method.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.model.LayoutModel;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;

import java.util.Set;

/**
 * @mock.generate base="Pane"
 */
public class DissectingPane extends Pane {

    private static String[] userAttributes = new String[]{
        FormatConstants.NAME_ATTRIBUTE,
        FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
        FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
        FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
        FormatConstants.BORDER_WIDTH_ATTRIBUTE,
        FormatConstants.CELL_PADDING_ATTRIBUTE,
        FormatConstants.CELL_SPACING_ATTRIBUTE,
        FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE,
        FormatConstants.WIDTH_ATTRIBUTE,
        FormatConstants.WIDTH_UNITS_ATTRIBUTE,
        FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE,
        FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE,
        FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE,
        FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
        FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE,
        FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE,
        FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE,
        FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE,
        FormatConstants.DIRECTION_ATTRIBUTE
    };

    private static String[] defaultAttributes = new String[]{
        FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
        FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
        FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
        FormatConstants.BORDER_WIDTH_ATTRIBUTE,
        FormatConstants.CELL_PADDING_ATTRIBUTE,
        FormatConstants.CELL_SPACING_ATTRIBUTE,
        FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
        FormatConstants.WIDTH_ATTRIBUTE,
        FormatConstants.WIDTH_UNITS_ATTRIBUTE,
        FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE,
        FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE,
        FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
        FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE,
        FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE
    };

    private static String[] persistentAttributes = userAttributes;

    public DissectingPane(CanvasLayout canvasLayout) {
        super(canvasLayout);
    }

    public FormatType getFormatType() {
        return FormatType.DISSECTING_PANE;
    }

    public String[] getUserAttributes() {
        return userAttributes;
    }

    public String[] getDefaultAttributes() {
        return defaultAttributes;
    }

    public String[] getPersistentAttributes() {
        return persistentAttributes;
    }

    public String getNextShardLinkText() {
        return (String) getAttribute(FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE);
    }

    public void setNextShardLinkText(String linkText) {
        setAttribute(FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE,
                     linkText);
    }

    /**
     * Get the style class for the next shard navigation link
     *
     * @return the style class for the next shard navigation link
     */
    public String getNextShardLinkClass() {
        return (String)
                getAttribute(FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE);
    }

    /**
     * Set the style class for the next shard navigation link
     *
     * @param linkClass the style class for the next shard navigation link
     */
    public void setNextShardLinkClass(String linkClass) {
        setAttribute(FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE,
                     linkClass);
    }

    /**
     * Get the shortcut to use to navigate to the next shard.
     *
     * @return The shortcut to use to navigate to the next shard, or null if no
     *         shortcut has been specified.
     */
    public String getNextShardShortcut() {
        return (String) getAttribute(FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE);
    }

    /**
     * Set the shortcut to use to navigate to the next shard.
     *
     * @param shortcut the shortcut to use to navigate to the next shard.
     */
    public void setNextShardShortcut(String shortcut) {
        setAttribute(FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
                     shortcut);
    }

    public String getPreviousShardLinkText() {
        return (String)
                getAttribute(FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE);
    }

    public void setPreviousShardLinkText(String linkText) {
        setAttribute(FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE,
                     linkText);
    }

    /**
     * Get the style class for the previous shard navigation link
     *
     * @return the style class for the previous shard navigation link
     */
    public String getPreviousShardLinkClass() {
        return (String)
                getAttribute(FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE);
    }

    /**
     * Set the style class for the previous shard navigation link
     *
     * @param styleClass the style class for the previous shard navigation link
     */
    public void setPreviousShardLinkClass(String styleClass) {
        setAttribute(FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE,
                     styleClass);
    }

    /**
     * Get the shard link order.
     *
     * @return the shard link order.
     */
    public String getShardLinkOrder() {
        return (String)getAttribute(FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE);
    }

    /**
     * Set the shard link order.
     *
     * @param shardLinkOrder the shard link order.
     */
    public void setShardLinkOrder(String shardLinkOrder) {
        setAttribute(FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE,
                     shardLinkOrder);
    }

    /**
     * Method to determine whether the next link is displayed before the
     * previouse link
     *
     * @return true if and only if the next link is to be displayed before the
     *         previous link
     */
    public boolean isNextLinkFirst() {
        String firstLink =
                (String) getAttribute(FormatConstants.SHARD_LINK_ORDER_ATTRIBUTE);
        // if link is null then previous link should be generated first
        return (FormatConstants.NEXT_FIRST.equals(firstLink));
    }

    /**
     * Get the shortcut to use to navigate to the previous shard.
     *
     * @return The shortcut to use to navigate to the previous shard, or null
     *         if no shortcut has been specified.
     */
    public String getPreviousShardShortcut() {
        return (String)
                getAttribute(FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE);
    }

    /**
     * Set the shortcut to use to navigate to the previous shard.
     *
     * @param shortcut the shortcut to use to navigate to the previous shard.
     */
    public void setPreviousShardShortcut(String shortcut) {
        setAttribute(FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE,
                     shortcut);
    }

    /**
     * Get the maximum content size.
     *
     * @return The maximum content size.
     */
    public String getMaximumContentSize() {
        return (String) getAttribute(FormatConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE);
    }

    /**
     * Set the maximum content size.
     *
     * @param maxContentSize The maximum content size.
     */
    public void setMaximumContentSize(String maxContentSize) {
        setAttribute(FormatConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE,
                     maxContentSize);
    }

    public void setParent(Format parent) {

        Fragment originalEnclosingFragment = getEnclosingFragment();
        Fragment newEnclosingFragment = ((parent == null)
                                         ? null
                                         : parent.getFragment());

        super.setParent(parent);

        // If the enclosing fragment has changed then deregister from the original
        // one and register with the new one.
        if (originalEnclosingFragment != newEnclosingFragment) {

            if (originalEnclosingFragment != null) {
                originalEnclosingFragment.removeDissectingPane(this);
            }

            if (newEnclosingFragment != null) {
                newEnclosingFragment.addDissectingPane(this);
            }
        }
    }

    // Javadoc inherited from super class.
    public boolean visit(FormatVisitor visitor, Object object)
            throws FormatVisitorException {
        return visitor.visit(this, object);
    }

    private static Object DUMMY_IDENTIFIER = new Object();

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        // A single Dissecting Pane may only appear within a Fragment
        DefinitionScope scope = context.getDefinitionScope(
                LayoutModel.DISSECTING_PANE_SCOPE_TYPE);
        if (scope != null) {
            // TODO: add level and message in here so we can pass the name
            // in the message. This means that DefinitionTypeHandler is defunct!
            scope.define(context, sourceLocation, DUMMY_IDENTIFIER);
        }

        if (!isContainedBy(Fragment.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("dissecting-pane-must-be-in-fragment",
                            this.getName()));
        }

        validateDissectingPaneAttributes(context);
    }

    private void validateDissectingPaneAttributes(
            final ValidationContext context) {

        String element = "dissectingPane";

        validateAllPaneAttributes(context, element);

        // nextLinkText requires no validation.

        Step step = context.pushPropertyStep("nextLinkStyleClass");
        final String nextLinkStyleClass = getNextShardLinkClass();
        if (nextLinkStyleClass != null) {
            if (!LayoutTypeValidator.isThemeClassNameType(nextLinkStyleClass)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.NEXT_LINK_STYLE_CLASS_ILLEGAL,
                        nextLinkStyleClass));
            }
        }
        context.popStep(step);

        // nextLinkShortcut requires no validation.

        // previousLinkText requires no validation.

        step = context.pushPropertyStep("previousLinkStyleClass");
        final String previousLinkStyleClass = getPreviousShardLinkClass();
        if (previousLinkStyleClass != null) {
            if (!LayoutTypeValidator.isThemeClassNameType(previousLinkStyleClass)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.PREVIOUS_LINK_STYLE_CLASS_ILLEGAL,
                        previousLinkStyleClass));
            }
        }
        context.popStep(step);

        // previousLinkShortcut requires no validation.

        step = context.pushPropertyStep("maxContentSize");
        final String maximumContentSize = getMaximumContentSize();
        if (maximumContentSize != null) {
            if (!LayoutTypeValidator.isUnsigned(maximumContentSize)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.MAXIMUM_CONTENT_SIZE_ILLEGAL,
                        maximumContentSize));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("shardLinkOrder");
        final String shardLinkOrder = getShardLinkOrder();
        if (shardLinkOrder != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords(element, "shardLinkOrder",
                    new String[] {"next-first", "previous-first"});
            if (!keywords.contains(shardLinkOrder)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.SHARD_LINK_ORDER_ILLEGAL,
                        shardLinkOrder));
            }
        }
        context.popStep(step);

    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9590/3	schaloner	VBM:2005092204 finished regular JiBX bindings

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
