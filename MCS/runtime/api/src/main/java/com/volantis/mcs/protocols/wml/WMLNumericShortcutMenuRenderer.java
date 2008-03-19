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
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AbstractMenuRenderer;
import com.volantis.mcs.protocols.AbstractNumericShortcutMenuRenderer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MenuItemGroupAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * {@link AbstractMenuRenderer} for rendering numeric shortcut style menus on
 * generic WML devices.
 * <p/>
 * Since generic WML phones don't support automagic accesskey generation ala
 * OpenWave phones, we emulate the Openwave behaviour for generic WML.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because we only write 'a' tags which cannot contain the stylistic tags.
 *
 * @see OpenwaveNumericShortcutMenuRenderer
 */
public class WMLNumericShortcutMenuRenderer
        extends AbstractNumericShortcutMenuRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    WMLNumericShortcutMenuRenderer.class);

    /**
     * Create a new <code>WMLNumericShortcutMenuRenderer</code> instance
     *
     * @param menuRendererContext the current <code>MenuRendererContext</code>
     * @param pageContext         the current <code>MarinerPageContext</code>
     */
    public WMLNumericShortcutMenuRenderer(
            MenuRendererContext menuRendererContext,
            MarinerPageContext pageContext) {
        super(menuRendererContext, pageContext);
    }

    // javadoc inherited
    protected void openMenu(
            DOMOutputBuffer outputBuffer,
            MenuAttributes attributes)
            throws ProtocolException {
        // do nothing
    }

    // javadoc inherited
    protected void closeMenu(
            DOMOutputBuffer outputBuffer,
            MenuAttributes attributes)
            throws ProtocolException {
        // do nothing
    }

    // javadoc inherited
    protected void openMenuGroup(
            DOMOutputBuffer outputBuffer,
            MenuItemGroupAttributes groupAtts)
            throws ProtocolException {
        // do nothing
    }

    // javadoc inherited
    protected void closeMenuGroup(
            DOMOutputBuffer outputBuffer,
            MenuItemGroupAttributes groupAtts)
            throws ProtocolException {
        // do nothing
    }

    // javadoc inherited
    protected void writeMenuItem(
            DOMOutputBuffer outputBuffer,
            MenuItem menuItem)
            throws ProtocolException {

        InternalDevice device = pageContext.getDevice();
        boolean isAccesskeyPrefixKeyNeeded =
                !device.getBooleanPolicyValue(DevicePolicyConstants.
                                              SUPPORTS_WML_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY);

        // Extract the href from the menu item.
        LinkAssetReference reference = menuItem.getHref();
        String href = reference.getURL();
        
        // Extract the text from the menu item.
        String text = menuItem.getText();
        
        // Add the dummy access key prefix to the text if necessary.
        if (isAccesskeyPrefixKeyNeeded) {
            text = AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_STRING +
                    " " + text;
        }
        
        // Report what we are about to do for debugging.
        if (logger.isDebugEnabled()) {
            logger.debug("writing numeric shortcut menu item with href=" +
                         href + ", text=" + text);
        }
        
        // Open the annotation element.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element annotator = outputBuffer.openStyledElement(
                AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT, menuItem);

        // Open the anchor element.
        Element anchor = outputBuffer.openElement("a");

        // Copy attributes into the anchor element.
        menuRendererContext.writeTitleAttribute(anchor, menuItem);
        anchor.setAttribute("href", href);
        // Add the dummy accesskey attribute as well.
        anchor.setAttribute("accesskey",
                            AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_STRING);

        // Write out the menu text as the content of the link.
        outputBuffer.appendEncoded(text);

        // Close the anchor element.
        outputBuffer.closeElement(anchor);
        
        // Close the annotation element.
        outputBuffer.closeElement(annotator);
        
        // Add BR to force hardcoded vertical alignment.
        // This is compatible with actual Openwave numeric shortcut rendering 
        // which is always vertical.
        // NOTE: This means that the mariner-menu-orientation style is ignored.
        outputBuffer.addStyledElement("br", menuItem);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 02-Mar-05	7243/10	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 16-Feb-05	6129/9	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/7	matthew	VBM:2004102019 yet another supermerge

 27-Jan-05	6129/5	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/2	matthew	VBM:2004102019 Enable shortcut menu link rendering

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 27-Apr-04	4025/2	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 06-Oct-03	1469/9	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix testsuite error not picked up before)

 06-Oct-03	1469/7	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/5	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 26-Sep-03	1423/1	doug	VBM:2003091701 try and commit so geoff can have his own wspace

 ===========================================================================
*/
