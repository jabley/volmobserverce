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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasic_Netfront3.java,v 1.13 2003/03/24 13:57:59 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Aug-02    Adrian          VBM:2002080203 - created this protocol for
 *                              Netfront3 browser.
 * 25-Sep-02    Phil W-S        VBM:2002091901 - updated to use the new
 *                              XHTMLBasicUnabridgedTransformer to handle
 *                              table flattening in a more sophisticated
 *                              manner than the standard XHTMLBasic
 *                              protocol.
 * 09-Oct-02    Phil W-S        VBM:2002081322 - provide alternative versions
 *                              of openPane and closePane to make stylistic
 *                              attributes appear on the "right" type of
 *                              element in order to ensure that the background
 *                              colour correctly fills the element's area
 *                              rather than just behind contained text.
 *                              This is achieved by using a nested table/tr/td
 *                              instead of a div.
 *                              Note that the transformer optimizes the table
 *                              away (if it really is nested).
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Use the new Netfront3
 *                              specific nested table removal transformer.
 *                              This transformer will remove single column
 *                              tables that contain nested tables, so may have
 *                              an impact on the final output of 2002081322.
 * 14-Oct-02    Adrian          VBM:2002100404 - modified openPane to call new
 *                              protocol getPaneStyleClass method instead of
 *                              MarinerPageContext.getPaneStyleClass.
 * 08-Nov-02    Byron           VBM:2002110516 - Now uses a custom style sheet
 *                              renderer (for border-spacing). Added
 *                              getLayoutDeviceThemeFactory() and getProtocol-
 *                              StyleSheet() methods.
 * 18-Nov-02    Phil W-S        VBM:2002110509 - Update openPane to ensure that
 *                              a cell generated within the pane retains the
 *                              attributes of a cell containing the pane.
 * 01-Dec-02    Phil W-S        VBM:2002112702 - Update constructor to indicate
 *                              that some of the types of stylesheet should be
 *                              rendered as internal ("inline"). Also
 *                              duplicated the openStyle and closeStyle methods
 *                              from XHTMLFull as these are needed for internal
 *                              stylesheets.
 * 12-Dec-02    Phil W-S        VBM:2002110516 - Change the call to
 *                              getPaneStyleClass to getFormatStyleClass in
 *                              openPane.
 * 16-Jan-03    Phil W-S        VBM:2002110402 - Rework: Can now use the
 *                              XHTMLBasic UnabridgedTransformer.
 * 29-Jan-03    Byron           VBM:2003012803 - Modified constructor to set
 *                              protocolConfiguration value and any static
 *                              variables dependent on it.
 * 21-Mar-03    Phil W-S        VBM:2003031910 - Tidied imports and modified
 *                              the constructor's initialization of the
 *                              stylesheet rendering preferences to match the
 *                              changes made to VolantisProtocol in this VBM.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.dom.Document;

/**
 * This is a sub-class of the XHTMLBasic class to provide the precise
 * definition of the Netfront3 version of the XHTMLBasic protocol.
 *
 * XHTML supports style sheets, so no themes processing is performed here.
 */

public class XHTMLBasic_Netfront3
        extends XHTMLBasic {

    /**
     * XHTMLBasic_Netfront3 constructor
     */
    public XHTMLBasic_Netfront3(
            ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {

        super(protocolSupportFactory, protocolConfiguration);

        // Even though this protocol is notionally XHTML Basic, it does have
        // some XHTML strict extensions. Of these extensions we make use of
        // the ability to have style elements for inline (internal) stylesheets
        supportsInlineStyles = true;

        // 3 have requested this to support targetting of anchors, even 
        // though the Name Identification Module is not part of XHTMLBasic.
        enableNameIdentification = true;

        // The following defines how this protocol expects to render
        // the various types of stylesheet
        protocolThemeStylesheetPreference = StylesheetRenderMode.EXTERNAL;

        // hr elements are allowed
    }

    //  Javadoc inherited from super class
    public String defaultMimeType() {
        return "text/html";
    }

    // ========================================================================
    //   General helper methods.
    // ========================================================================

    // ========================================================================
    //   Page element methods
    // ========================================================================

    /**
     * Return the XML DOCTYPE declaration for this protocol
     *
     * <h1>NOTE:</h1>
     * <b>
     * The exact detail of the XML DOCTYPE for this protocol are unknown at the
     * time of creation of this class.  For the moment it simply uses the same
     * definition as XHTMLBasic.
     * </b>
     */
    protected void doProtocolString(Document document) {
        addXHTMLBasicDocType(document);
    }

    // Javadoc inherited from super class.
    protected void openStyle(
            DOMOutputBuffer dom,
            StyleAttributes attributes) {

        performDefaultOpenStyle(dom, attributes, true);
    }

    // Javadoc inherited from super class.
    protected void closeStyle(
            DOMOutputBuffer dom,
            StyleAttributes attributes) {
        
        performDefaultCloseStyle(dom, true);
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

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/2	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 02-Sep-05	9408/4	pabbott	VBM:2005083007 Move over to using JiBX accessor

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 14-Nov-03	1861/1	mat	VBM:2003110602 Add correct mimetype to descendants of XHTMLBasic

 08-Aug-03	1015/1	geoff	VBM:2003072208 merge from Mimas

 08-Aug-03	1011/1	geoff	VBM:2003072208 port from metis

 08-Aug-03	1004/1	geoff	VBM:2003072208 fix pane rendering in xhtml from netfront3

 ===========================================================================
*/
