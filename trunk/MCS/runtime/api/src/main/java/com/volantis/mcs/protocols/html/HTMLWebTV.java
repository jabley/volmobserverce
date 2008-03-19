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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLWebTV.java,v 1.4 2003/04/17 10:21:07 geoff Exp $
 * ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Oct-01    Payal           VBM: 2001090605 - Created this file.
 *                              Modified openCanvas() to add the
 *                              initialfocus  to a pageHead
 *                              buffer if initial focus is set in the
 *                              attributes. Modified openAnchor() if initial
 *                              focus is set in the canvas tag to the name
 *                              of an anchor tag that is being generated, then
 *                              that generated anchor tag should include
 *                              the selected attribute.
 * 10-Oct-01    Payal           VBM:2001090605 - javadoc for the class.
 *                              Modified method openAnchor to work if initial
 *                              focus is not set and removed the
 *                              supportsAccessKeyAttribute.
 * 10-Oct-01    Allan           VBM:2001092806 - Set supportsBackgroundInTable
 *                              to true in a newly created constructor.
 * 18-Oct-01    Payal           VBM:2001100904 - Modified method openAnchor
 *                              to check if new style attribute view is set.
 * 29-Oct-01    Paul            VBM:2001102901 - Removed unused import.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 06-Feb-02    Paul            VBM:2001122103 - Added missing call to
 *                              super.addAnchorAttributes.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed calls to getPageHead
 *                              method in MarinerPageContext as it is now
 *                              accessible to subclasses of StringProtocol
 *                              through a protected field.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Augment
 *                              addPhoneNumberAttributes in a similar way to
 *                              how addAnchorAttributes is augmented.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

/**
 * This is a sub-class of the HTMLRoot protocol class to provide the
 * precise definition of the HTMLWebTV protocol.Very little here is different
 * from the HTMLRoot class definition, so most things are referenced to the
 * superclass.
 */
public class HTMLWebTV
        extends HTMLTransparentTV {

    /**
     * Constant name for page head attribute which will hold the name of the
     * anchor which should have the initial focus.
     */
    private static final String INITIAL_FOCUS_ATTRIBUTE_NAME = "initial";

    /**
     * Creates a new <code>HTMLWebTV</code> instance.
     */
    public HTMLWebTV(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);

        supportsBackgroundInTable = true;
    }

    // ==========================================================================
    //   General helper methods
    // ==========================================================================

    // ==========================================================================
    //   Page element methods
    // ==========================================================================

    /**
     * Open the canvas tag which in this case means html. This is
     * handled by the super class version though. WebTV initialfocus
     * is handled by this method.
     */
    protected void openCanvas(
            DOMOutputBuffer dom,
            CanvasAttributes attributes) {

        String value;
        if ((value = attributes.getInitialFocus()) != null) {
            getPageHead().setAttribute(INITIAL_FOCUS_ATTRIBUTE_NAME, value);
        }

        super.openCanvas(dom, attributes);
    }

    // ==========================================================================
    //   Dissection methods
    // ==========================================================================

    // ==========================================================================
    //   Layout / format methods
    // ==========================================================================

    // ==========================================================================
    //   Navigation methods.
    // ==========================================================================

    /**
     * Override this method to add extra attributes to the body.
     */
    protected void addAnchorAttributes(
            Element element,
            AnchorAttributes attributes)
            throws ProtocolException {

        // Add the super class attributes.
        super.addAnchorAttributes(element, attributes);

        String value;

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue;

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_LINK_MEDIA);
        element.setAttribute("view", styleValue.getStandardCSS());

        if ((value = attributes.getId()) != null) {
            String initialFocusAnchor
                    = (String) getPageHead()
                    .getAttribute(INITIAL_FOCUS_ATTRIBUTE_NAME);

            if (value.equals(initialFocusAnchor)) {
                element.setAttribute("selected", "selected");
            }
        }
    }

    protected void addPhoneNumberAttributes(
            Element element,
            PhoneNumberAttributes attributes)
            throws ProtocolException {
        super.addPhoneNumberAttributes(element, attributes);

        String value;

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue;

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_LINK_MEDIA);
        element.setAttribute("view", styleValue.getStandardCSS());

        // Initial focus can be set by setting the initial focus name to
        // the full number associated with this phone number link
        if ((value = attributes.getDefaultContents()) != null) {
            String initialFocusAnchor = (String)
                    getPageHead().getAttribute(INITIAL_FOCUS_ATTRIBUTE_NAME);

            if (value.equals(initialFocusAnchor)) {
                element.setAttribute("selected", "selected");
            }
        }
    }

    // ==========================================================================
    //   Block element methods.
    // ==========================================================================

    // ==========================================================================
    //   List element methods.
    // ==========================================================================

    // ==========================================================================
    //   Table element methods.
    // ==========================================================================

    // ==========================================================================
    //   Inline element methods.
    // ==========================================================================

    // ==========================================================================
    //   Special element methods.
    // ==========================================================================

    // ==========================================================================
    //   Menu element methods.
    // ==========================================================================

    // ==========================================================================
    //   Script element methods.
    // ==========================================================================

    // ==========================================================================
    //   Classic form element methods.
    // ==========================================================================

    // ==========================================================================
    //   Extended function form element methods.
    // ==========================================================================

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 ===========================================================================
*/
