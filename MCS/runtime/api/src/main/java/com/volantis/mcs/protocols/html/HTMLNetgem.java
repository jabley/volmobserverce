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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLNetgem.java,v 1.4 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Aug-01    Allan           VBM:2001083101 - Created. openBody () added
 *                              to allow no-repeat, repeat-x and repeat-y
 *                              background styles to work. Added doInputStyle,
 *                              override doBooleanInput (), doTextInput (),
 *                              all the openHeading methods. Override
 *                              openParagraph (), and openTableDataCell ().
 *                              Override openTextArea (), openTableHeaderCell.
 *                              All these methods contain Netgem specific
 *                              stylistic information that is extended from
 *                              HTML 3.2.
 * 05-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 05-Sep-01    Allan           VBM:2001090308 - Overide openCanvas () to
 *                              output a focus tag if specified in the
 *                              attributes.
 * 01-Oct-01    Doug            VBM:2001092501 - now use the MarinerPageContext
 *                              method getBackgroundImageURLAsString to
 *                              calculate the background-image url.
 * 04-Oct-01    Doug            VBM:2001100201 - Modified the method
 *                              openTextArea to check the
 *                              supportsAccessKeyAttribute flag before writing
 *                              out an accesskey attribute
 * 11-Oct-01    Allan           VBM:2001090401 - Replaced TableDataCell and
 *                              TableHeaderCell Attributes with
 *                              TableCellAttributes.
 * 31-Oct-01    Doug            VBM:2001092806 added constructor so that I
 *                              could set supportsBackgroundInTable to false
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
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
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 10-May-02    Adrian          VBM:2002040808 - fixed call to getWhiteSpace in
 *                              method addTextFormatAttributes.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BackgroundRepeatKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

/**
 * Protocol class for Netgem iTV device.
 */
public class HTMLNetgem
    extends HTMLVersion3_2 {

    /**
     * Name of buffer containing style attributes set for input tags that
     * actually go in the body tag.
     */
    private static final String INPUT_STYLE_BODY_ATTRIBUTES = "input";

    /**
     * Creates a new <code>HTMLNetgem</code> instance.
     *
     */
    public HTMLNetgem (ProtocolSupportFactory supportFactory,
                          ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        supportsBackgroundInTable = false;
    }

    // ========================================================================
    //   General helper methods
    // ========================================================================

    /**
     * Attributes for some text type tags are generic so do them all in one
     * method.... this method. Netgem adds a nowrap extension to the
     * heading tags.
     * @param element The Element to modify.
     */
    private void addTextFormatAttributes(Element element,
                                           MCSAttributes attributes) {

        Styles styles = attributes.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.WHITE_SPACE);

        if (value == WhiteSpaceKeywords.NOWRAP) {
            element.setAttribute("nowrap", "nowrap");
        }
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
        PropertyValues propertyValues = styles.getPropertyValues();

        // Add the super class attributes.
        super.addBodyAttributes (element, attributes);

        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.BACKGROUND_REPEAT);
        if (value == BackgroundRepeatKeywords.REPEAT_Y) {
            element.setAttribute("nohtilebg", "nohtilebg");
        } else if (value == BackgroundRepeatKeywords.REPEAT_X) {
            element.setAttribute("novtilebg", "novtilebg");
        } else if (value == BackgroundRepeatKeywords.NO_REPEAT) {
            element.setAttribute("nohtilebg", "nohtilebg");
            element.setAttribute("novtilebg", "novtilebg");
        }

        Element input = (Element)
            getPageHead().getAttribute (INPUT_STYLE_BODY_ATTRIBUTES);
        if (input != null) {
            element.mergeAttributes (input, true);
        }
    }

    /**
     * Open the canvas tag which in this case means html. This is
     * handled by the super class version though. To set the initial
     * focus in Netgem we output a focus tag here if initialFocus is
     * set in the canvas attributes.
     */
    protected void openCanvas (DOMOutputBuffer dom,
                               CanvasAttributes attributes) {

        String value;

        super.openCanvas (dom, attributes);

        if ((value = attributes.getInitialFocus ()) != null) {
            Element element = dom.addStyledElement ("focus", attributes);
            element.setAttribute ("ctl", value);
        }
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    protected void addHeadingAttributes (Element element,
                                         HeadingAttributes attributes,
                                         int level) throws ProtocolException {

        super.addHeadingAttributes (element, attributes, level);

        addTextFormatAttributes(element, attributes);
    }

    protected void addParagraphAttributes (Element element,
                                           ParagraphAttributes attributes)
            throws ProtocolException {

        super.addParagraphAttributes (element, attributes);

        addTextFormatAttributes(element, attributes);
    }

    // ========================================================================
    //   List element methods.
    // ========================================================================

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    protected void addTableCellAttributes (Element element,
                                           TableCellAttributes attributes)
            throws ProtocolException {

        super.addTableCellAttributes (element, attributes);

        addTextFormatAttributes (element, attributes);
    }

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // ========================================================================
    //   Classic form element methods.
    // ========================================================================

    /**
     * Provide Netgem specific style attributes on the input tag.
     * @param attributes VolantisAttribute to use when retrieving the style.
     */
    private void doInputStyle (MCSAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        Element element = (Element)
            getPageHead().getAttribute (INPUT_STYLE_BODY_ATTRIBUTES);
        if (element == null) {
            element = domFactory.createStyledElement(attributes.getStyles());
            getPageHead().setAttribute (INPUT_STYLE_BODY_ATTRIBUTES, element);
        }

        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("window", value);
        }
        if ((value = colorHandler.getAsString(styles)) != null) {
            element.setAttribute("windowtext", value);
        }
    }

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    /**
     * Generate extented forms action input form. Include Netgem
     * input tag style options. (Netgem input style options go
     * in the body tag.)
     * @param attributes XFActionAttributes for the text input form.
     */
    public void doActionInput (DOMOutputBuffer dom,
                               XFActionAttributes attributes)
            throws ProtocolException {

        super.doActionInput (dom, attributes);

        doInputStyle (attributes);
    }

    /**
     * Generate extented forms text input form. Include Netgem
     * input tag style options. (Netgem input style options go
     * in the body tag.)
     * @param attributes XFTextInputAttributes for the text input form.
     */
    public void doTextInput (XFTextInputAttributes attributes)
            throws ProtocolException {

        super.doTextInput (attributes);

        doInputStyle (attributes);
    }

    /**
     * Generate extented forms text input form. Include Netgem
     * input tag style options. (Netgem input style options go
     * in the body tag.)
     * @param attributes XFTextInputAttributes for the text input form.
     */
    public void doBooleanInput (XFBooleanAttributes attributes)
            throws ProtocolException {

        super.doBooleanInput (attributes);

        doInputStyle (attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
