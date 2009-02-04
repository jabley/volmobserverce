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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLLiberate.java,v 1.6 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Aug-01    Doug            VBM:2001062705 Created this file.
 *                              HTMLLiberate extends HTMLVersion3_2Transparent
 *                              to add support for accessKeys via the
 *                              Liberate handset. AccessKey support is provided
 *                              through a JavaScript event handler. Handset key
 *                              events are forwarded to the handler for
 *                              processing. The HTML between the body tags
 *                              from this protocol is identical to that of the
 *                              superclass. Therefore any methods overriden
 *                              in this class delagated the tag output to
 *                              the superclass itself.
 * 10-Aug-01    Allan           VBM:2001062705 Moved openBody, openInput and
 *                              openTextArea from HTML3_2Transparent to
 *                              here. Updated doTextInput to have a
 *                              default transparent bgColor.
 * 31-Aug-01    Allan           VBM:2001083102 - Modified openBody() to
 *                              use retrieveImageAssetURLAsString() instead of
 *                              retrieveAbsoluteURLAsString().
 * 04-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 06-Sep-01    Allan           VBM:2001083005 - Modified openCanvas() to
 *                              add the initialfocus meta tag to a pageHead
 *                              buffer if initial focus is set in the
 *                              attributes. Modified writePageHead to add
 *                              the initialfocus meta buffer to the head
 *                              if available. Added constant for
 *                              initialfocus meta buffer name. Changed
 *                              access of script key hander buffer name
 *                              contanst from public to protected.
 * 14-Sep-01   Kula             VBM:2001091011 - The overscan attribute added
 *                              to body and frameset tags. The openBody and
 *                              openSegmentGrid methods are modified for this.
 *                              The attribute value is created using theme.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 21-Sep-01    Doug            VBM:2001090302 - replaced doMenu method with
 *                              menuItemAddedOK which gets called from from
 *                              super.doMenu. This avoids replicating vast
 *                              chunks of code. Similiarly replaced doAnchor
 *                              with anchorAddedOK method.
 * 01-Oct-01    Doug            VBM:2001092501 - now use the MarinerPageContext
 *                              method getBackgroundImageURLAsString to
 *                              calculate the background-image url.
 * 04-Oct-01    Doug            VBM:2001100201 - Modified the methods
 *                              opentInput, openTextArea, doTextInput to
 *                              check the supportsAccessKeyAttribute flag
 *                              before writing out a accesskey attribute
 * 09-Oct-01    Payal           VBM:2001090605 - HTMLLiberate extends
 *                              HTMLTransparentTV as HTMLVersion3_2Transparent
 *                              is renamed to HTMLTransparentTV.
 * 10-Oct-01    Allan           VBM:2001092806 - Removed the code that adds
 *                              a background to body and replaced this with
 *                              a call to addBackground() in the parent.
 *                              Set supportsBackgroundInTD and
 *                              supportsBackgroundInTH to true.
 * 29-Oct-01    Paul            VBM:2001102901 - Removed slightly modified
 *                              copy of doTextInput and added the extra
 *                              functionality by overriding two new special
 *                              methods addTextAreaAttributes and
 *                              addTextInputAttributes.
 * 31-Oct-01    Doug            VBM:2001092806 set supportsBackgroundInTable
 *                              to false in the constructor.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import
 * 22-Jan-02    Doug            VBM:2002011003 - Moved the addXFFormAttributes
 *                              into HTMLRoot. Added the method
 *                              addTransparentBackground.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed calls to getPageHead
 *                              method in MarinerPageContext as it is now
 *                              accessible to subclasses of StringProtocol
 *                              through a protected field.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 04-Mar-02    Paul            VBM:2001101803 - Modified doActionInput due
 *                              to changes to StringProtocol.
 * 08-Mar-02    Paul            VBM:2002030607 - Stopped calling toString on
 *                              StringOutputBuffers.
 * 11-Mar-02    Adrian          VBM:2002030710 - Modified writePageHead to test
 *                              if keyHandlerBody not null AND not empty
 *                              instead of not null OR not empty
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 10-May-02    Adrian          VBM:2002040808 - Check for null margin values
 *                              in addOverscan attributes
 * 27-Aug-02    Adrian          VBM:2001090607 - Removed default bgColor of
 *                              "transparent" from methods... addBodyAttributes
 *                              and addBackgroundColorAttribute.
 * 10-Oct-02    Adrian          VBM:2002100404 - update writePageHead to call
 *                              throw IOException.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.html.menu.DeprecatedExternalShortcutRenderer;
import com.volantis.mcs.protocols.html.menu.HTMLLiberateMenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModule;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.Styles;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class HTMLLiberate
    extends HTMLTransparentTV implements DeprecatedExternalShortcutRenderer {
    /**
     * Constant name setting for buffer that will hold access key scripts.
     */
    private static final String SCRIPT_KEYHANDLER_BODY_NAME = "keyhandler";

    private static final Set legalActions = new HashSet();
    static {
        legalActions.add("focus");
        legalActions.add("submit");
        legalActions.add("reset");
    }


    /**
     * Creates a new <code>HTMLLiberate</code> instance.
     */
    public HTMLLiberate(ProtocolSupportFactory supportFactory,
                          ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        supportsBackgroundInTD = true;
        supportsBackgroundInTH = true;
        supportsBackgroundInTable = false;
    }

    // ========================================================================
    //   General helper methods
    // ========================================================================

    private void addOverscanAttribute (Element element,
                                         Styles styles) {

        // Create overscan attribute, if a margin is empty then the name
        // or the margin bottom, top, left, right needs to be added to
        // a comma separated list. If all the margins are empty then this
        // needs to be set to all, otherwise if all margins are not empty
        // then no overscan attribute is needed.

        // Count how many of the margins are 0.
        String top = getMargin(styles, StylePropertyDetails.MARGIN_TOP);
        String bottom = getMargin(styles, StylePropertyDetails.MARGIN_BOTTOM);
        String left = getMargin(styles, StylePropertyDetails.MARGIN_LEFT);
        String right = getMargin(styles, StylePropertyDetails.MARGIN_RIGHT);

        int emptyMargins = (("0".equals (top) ? 0 : 1)
                            + ("0".equals (bottom) ? 0 : 1)
                            + ("0".equals (left) ? 0 : 1)
                            + ("0".equals (right) ? 0 : 1));

        if (top == null && bottom == null && left == null && right == null) {
          emptyMargins = 4;
        }

        if (emptyMargins == 4) {
            element.setAttribute ("overscan", "all");
        } else if (emptyMargins != 0) {
            StringBuffer buffer = new StringBuffer (32);

            String separator = "";
            if ("0".equals (bottom)) {
                buffer.append (separator).append ("bottom");
                separator = ", ";
            }
            if ("0".equals (top)) {
                buffer.append (separator).append ("top");
                separator = ", ";
            }
            if ("0".equals (left)) {
                buffer.append (separator).append ("left");
                separator = ", ";
            }
            if ("0".equals (right)) {
                buffer.append (separator).append ("right");
            }

            element.setAttribute ("overscan", buffer.toString ());
        }
    }

    private ReusableStringBuffer getScriptKeyHandlerBody (boolean create) {
        ReusableStringBuffer buffer = (ReusableStringBuffer)
            getPageHead().getAttribute (SCRIPT_KEYHANDLER_BODY_NAME);
        if (buffer == null && create) {
            buffer = new ReusableStringBuffer (32);
            getPageHead().setAttribute (SCRIPT_KEYHANDLER_BODY_NAME, buffer);
        }

        return buffer;
    }

    // ========================================================================
    //   Page element methods
    // ========================================================================

    /**
     * Override this method to add extra attributes to the body.
     */
    protected void addBodyAttributes (Element element,
            BodyAttributes attributes) throws ProtocolException {

        Styles styles = attributes.getStyles();

        // Add the super class attributes.
        super.addBodyAttributes (element, attributes);

        addOverscanAttribute (element, styles);
    }

    /**
     * Open the canvas tag which in this case means html. This is
     * handled by the super class version though. Liberate initialfocus
     * is handled by this method.
     */
    protected void openCanvas (DOMOutputBuffer dom,
                               CanvasAttributes attributes) {

        String value;
        if ((value = attributes.getInitialFocus ()) != null) {
            DOMOutputBuffer buffer = getHeadBuffer ();
            Element element = buffer.addStyledElement ("meta", attributes);
            element.setAttribute ("initialfocus", value);
        }

        super.openCanvas (dom, attributes);
    }

    /**
     * Write out the head of the generated page. Most of the work for
     * this is done in the super class. Liberate may also write out
     * an access key script and an initial focus meta tag is these have
     * been specified.
     */
    protected void writePageHead() throws IOException {

        ReusableStringBuffer keyHandlerBody = getScriptKeyHandlerBody (false);
        if (keyHandlerBody != null && keyHandlerBody.length () != 0) {
            DOMOutputBuffer script = getScriptBuffer ();
            script.appendEncoded ("function onKeyOutHandler (){ ")
                .appendEncoded (keyHandlerBody.getChars (),
                                0, keyHandlerBody.length ())
                .appendEncoded ("return true; }" +
                        "window.onkeyout = onKeyOutHandler;");
        }

        super.writePageHead ();
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    /**
     * Override this method to add extra attributes to the segment grid.
     */
    protected
        void addSegmentGridAttributes (Element element,
                                       SegmentGridAttributes attributes) {

        super.addSegmentGridAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        addOverscanAttribute (element, styles);
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    /**
     * anchorAddedOK is a method that is called in super.doAnchor
     * It allows subclassed protocols do perform additional processing
     * after the succesfull writing of an anchor tag. This class
     *
     * @param attributes the AnchorAttributes
     * @param resolvedHref the href for the anchor
     */
    public void anchorAddedOK (AnchorAttributes attributes,
                               String resolvedHref) {
        doLinkAccessKey (attributes.getShortcut (), resolvedHref);
    }

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    // ========================================================================
    //   List element methods.
    // ========================================================================

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    /**
     * menuItemAddedOK is a method that is called in super.do
     * It allows subclassed protocols do perform additional processing
     * after the succesfull writing of an anchor tag. This class
     *
     * @param item the MenuItem
     * @param resolvedHref the href for the anchor
     */
    protected void menuItemAddedOK (MenuItem item, String resolvedHref) {
        doLinkAccessKey (item.getShortcut (), resolvedHref);
    }

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    private void doActionAccessKey (TextAssetReference object, String element,
                                      String action) {

        String keyCode = getPlainText(object);
        if (keyCode == null || element == null || action == null) {
            return;
        }
        if (!legalActions.contains (action)){
            throw new IllegalArgumentException ();
        }

        ReusableStringBuffer keyHandlerBody = getScriptKeyHandlerBody (true);

        keyHandlerBody.append ("if (navigator.input.curKey == ")
            .append (keyCode)
            .append (") { document.")
            .append (element)
            .append ("." + action + "(); return false; }");

        /*
        StringOutputBuffer keyHandlerBody
            = getExtraBuffer (SCRIPT_KEYHANDLER_BODY_NAME, true);

        keyHandlerBody.append ("if (navigator.input.curKey == ")
            .append (keyCode)
            .append (") { document.")
            .append (element)
            .append ("." + action + "(); return false; }");
        */
    }

    /**
     * Override this method to add support for accessKeys.
     * @param attributes The attributes to use when generating the markup.
     */
    public void doActionInput (DOMOutputBuffer dom,
                              XFActionAttributes attributes)
            throws ProtocolException {

        String formName = attributes.getFormData().getName();

        doActionAccessKey (attributes.getShortcut (), formName,
                attributes.getType ());

        super.doActionInput (dom, attributes);
    }

    /**
     * Override this method to add support for accessKeys.
     * @param attributes The attributes to use when generating the markup.
     */
    public void doBooleanInput (XFBooleanAttributes attributes)
            throws ProtocolException {

        final String name = attributes.getFormData().getName();

        doActionAccessKey (attributes.getShortcut (), name, "focus");

        super.doBooleanInput (attributes);
    }

    /**
     * Override this method to add support for accessKeys.
     * @param attributes The attributes to use when generating the markup.
     */
    public void doSelectInput (XFSelectAttributes attributes)
            throws ProtocolException {

        final String name = attributes.getFormData().getName();

        doActionAccessKey (attributes.getShortcut (), name, "focus");

        super.doSelectInput (attributes);
    }

    // Helper methods to write the Liberate onkeyout javascript handler
    private void doLinkAccessKey (TextAssetReference object, String link) {

        String keyCode = getPlainText (object);
        if (keyCode == null || link == null) {
            return;
        }

        ReusableStringBuffer keyHandlerBody = getScriptKeyHandlerBody (true);

        keyHandlerBody.append ("if (navigator.input.curKey == ")
            .append (keyCode)
            .append (") { window.location.href = \"")
            .append (link)
            .append ("\"; return false; }");

        /*
        // Write to the key handler buffer, if it did not exist already then
        // make sure that it is created.
        StringOutputBuffer keyHandlerBody
            = getExtraBuffer (SCRIPT_KEYHANDLER_BODY_NAME, true);

        keyHandlerBody.append ("if (navigator.input.curKey == ")
            .append (keyCode)
            .append (") { window.location.href = \"")
            .append (link)
            .append ("\"; return false; }");
        */
    }

    /**
     * A method that allows subclasses to specify additional attributes
     * for the XFTextInput element
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addTextInputAttributes (Element element,
                                           XFTextInputAttributes attributes)
            throws ProtocolException {

        // allow super class to added extra attributers first
        super.addTextInputAttributes (element, attributes);

        addBackgroundColorAttribute (element, attributes);
    }

    /**
     * A method that allows subclasses to specify additional attributes
     * for the XFTextInput (textarea)  element
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addTextAreaAttributes (Element element,
                                         XFTextInputAttributes attributes)
            throws ProtocolException {

        // allow super class to added extra attributers first
        super.addTextAreaAttributes (element, attributes);

        addBackgroundColorAttribute (element, attributes);
    }

    /**
     * Helper method to write out a attributes that specify a
     * transparent background.
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    private void addBackgroundColorAttribute (Element element,
                                          XFTextInputAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgColor", value);
        }
    }

    public void doTextInput (XFTextInputAttributes attributes)
            throws ProtocolException {

        final String name = attributes.getFormData().getName();

        doActionAccessKey (attributes.getShortcut (), name, "focus");

        super.doTextInput (attributes);
    }

    // Javadoc inherited.
    public void renderShortcut(
            com.volantis.mcs.protocols.menu.model.MenuItem item) {

        String href = item.getHref().getURL();
        doLinkAccessKey(item.getShortcut(), href);
    }

    //========================================================================
    // MenuModule related implementation.
    //========================================================================

    /**
     * Creates an HTMLLiberate specific default menu module for this protocol.
     */
    // Other javadoc inherited.
    protected MenuModule createMenuModule(
            MenuModuleRendererFactoryFilter metaFactory) {

        MenuModuleRendererFactory rendererFactory =
                new HTMLLiberateMenuModuleRendererFactory(getRendererContext(),
                        getDeprecatedOutputLocator(),
                        getMenuModuleCustomisation());

        if (metaFactory != null) {
            rendererFactory = metaFactory.decorate(rendererFactory);
        }

        return new DefaultMenuModule(getRendererContext(),
                rendererFactory);
    }

    //========================================================================
    // DeprecatedOutputLocator interface implementation.
    //========================================================================

    public DeprecatedExternalShortcutRenderer getExternalShortcutRenderer() {
        return this;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 14-May-04	4315/5	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4174/2	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
