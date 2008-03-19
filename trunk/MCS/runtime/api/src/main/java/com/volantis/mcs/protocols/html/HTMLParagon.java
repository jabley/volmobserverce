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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLParagon.java,v 1.5 2003/03/24 13:57:59 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Oct-02    Allan           VBM:2002100208 - Created. A protocol for the
 *                              Motorola Paragon device.
 * 15-Nov-02    Phil W-S        VBM:2002111303 - Change this into an HTML 4.01
 *                              protocol specialization.
 * 19-Nov-02    Byron           VBM:2002110517 - Added method createAction-
 *                              Element().
 * 05-Dec-02    Phil W-S        VBM:2002120503 - Inline device and layout
 *                              stylesheets. Settings in the constructor.
 * 21-Mar-03    Phil W-S        VBM:2003031910 - Modified the constructor's
 *                              initialization of the stylesheet rendering
 *                              preferences to match the changes made to
 *                              VolantisProtocol in this VBM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.styling.StyleContainer;

/**
 * A protocol for the Motorola Paragon device. This is an HTML 4.01 device.
 */
public class HTMLParagon
        extends HTMLVersion4_01 {

    /**
     * Initialize the new instance.
     *
     * @todo later this protocol has been initialized to use inline stylesheets
     *             for device and layout stylistic values. However, this
     *             is an HTML protocol and therefore currently doesn't use
     *             layout stylesheets but rather stylistic attributes in the
     *             markup.
     */
    public HTMLParagon(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        
        // Explicitly state that inline styles are supported instead of
        // relying on a superclass to set this as needed
        supportsInlineStyles = true;

        // The following defines how this protocol expects to render
        // the various types of stylesheet.
        // @todo layout styles are actually rendered as markup attributes in
        //       this protocol, so there are no layout stylesheets
        protocolThemeStylesheetPreference = StylesheetRenderMode.EXTERNAL;
    }

    // javadoc inherited
    protected Element createActionElement(DOMOutputBuffer dom,
                                          String actionType,
                                          String name,
                                          String caption,
                                          String src,
                                          StyleContainer styleContainer) {

        Element element = null;
        if ("button".equals(actionType) || "submit".equals(actionType)) {
            // Now build up the HTML tag.
            element = dom.openStyledElement("button", styleContainer);

            element.setAttribute("type", actionType);

            // If the name has been specified then make sure that we also generate
            // a value, even if it is blank.
            if (name != null) {
                element.setAttribute("name", name);
            }
            // We have to use the caption as the value because of a limitation
            // in the X/HTML forms as the buttons use the value as both the
            // caption and the value. However, we fix this up when we process
            // the form information.
            if (caption == null) {
                caption = "Submit";
            }
            element.setAttribute("value", caption);
            // Create the text that this 'button' encloses
            Text text = domFactory.createText();
            text.append(caption);
            text.addToTail(element);

            dom.closeElement("button");
        } else {
            element = super.createActionElement(dom,
                                                actionType,
                                                name,
                                                caption,
                                                src,
                                                styleContainer);
        }
        return element;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
