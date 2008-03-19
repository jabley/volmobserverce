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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AbstractMenuRenderer;
import com.volantis.mcs.protocols.AbstractNumericShortcutMenuRenderer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MenuItemGroupAttributes;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * {@link AbstractMenuRenderer} for rendering numeric shortcut style menus on
 * Openwave devices.
 * <p/>
 * Openwave phones support automagic accesskey generation, so we use their
 * native features to generate numeric shortcut menus.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because select/option can only contain PCDATA, so even if we rendered it
 * it would be invalid and (almost always) removed by the transformer. The
 * only time it would not be removed would be if it was intercepted by the
 * "emulate emphasis" processing in the transformer and translated into just
 * text before and after (eg "[" & "]") - and it's not worth implementing just
 * for this edge condition.
 *
 * @see WMLNumericShortcutMenuRenderer
 */
public class OpenwaveNumericShortcutMenuRenderer
        extends AbstractNumericShortcutMenuRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    OpenwaveNumericShortcutMenuRenderer.class);

    /**
     * Creates a new <code>OpenwaveNumericShortcutMenuRenderer</code>
     *
     * @param menuRendererContext the <code>MenuRendererContext</code> that
     *                            is required when rendering a menu.
     * @param pageContext         the <code>MarinerPageContext</code> associated with
     *                            the page being rendered
     */
    public OpenwaveNumericShortcutMenuRenderer(
            MenuRendererContext menuRendererContext,
            MarinerPageContext pageContext) {
        super(menuRendererContext, pageContext);
    }

    // javadoc inherited
    protected void openMenu(
            DOMOutputBuffer buffer,
            MenuAttributes attributes) {

        // todo: only set whitespace if it was not specified by user
        // currently we cannot tell, fix when VBM:2005092701 is fixed
        Styles styles = attributes.getStyles();
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.WHITE_SPACE,
                WhiteSpaceKeywords.NOWRAP);
        Element p = buffer.openStyledElement(WMLConstants.BLOCH_ELEMENT, styles);

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element element = buffer.openElement("select");
        // write out the title attribute if one has been supplied
        menuRendererContext.writeTitleAttribute(element, attributes);
    }

    // javadoc inherited
    protected void closeMenu(
            DOMOutputBuffer buffer,
            MenuAttributes attriubtes) {
        buffer.closeElement("select");
        buffer.closeElement(WMLConstants.BLOCH_ELEMENT);
    }

    // javadoc inherited
    protected void writeMenuItem(
            DOMOutputBuffer buffer,
            MenuItem menuItem) {

        // open the option
        Element element = buffer.openStyledElement("option", menuItem);

        // ok get hold of the href
        LinkAssetReference reference = menuItem.getHref();
        String href = reference.getURL();

        if (logger.isDebugEnabled()) {
            logger.debug("writing numeric shortcut menu item with href " +
                         href);
        }
        // write out the attributes
        menuRendererContext.writeTitleAttribute(element, menuItem);
        element.setAttribute("onpick", href);

        // write out menus label
        buffer.appendEncoded(menuItem.getText());

        // close the option
        buffer.closeElement(element);
    }

    // javadoc inherited
    protected void openMenuGroup(
            DOMOutputBuffer outputBuffer,
            MenuItemGroupAttributes groupAtts) {
        // menu groups are ignored for numeric shortcut style menus
    }

    // javadoc inherited
    protected void closeMenuGroup(
            DOMOutputBuffer outputBuffer,
            MenuItemGroupAttributes groupAtts) {
        // menu groups are ignored for numeric shortcut style menus
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 22-Sep-03	1394/5	doug	VBM:2003090902 centralised common openwave menu rendering code

 18-Sep-03	1394/3	doug	VBM:2003090902 Added support for Openwave numeric shortcut menus

 17-Sep-03	1394/1	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 ===========================================================================
*/
