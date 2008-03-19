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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasic_MIB2_1.java,v 1.13 2003/03/24 13:57:59 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Aug-02    Phil W-S        VBM:2002080202 - Created. Note that the
 *                              doProtocolString method is a duplicate of the
 *                              same method in XHTMLBasic and is provided here
 *                              as a template to be updated after this
 *                              initial version.
 * 25-Sep-02    Byron           VBM:2002091904 - Added getDOMTransformer() and
 *                              getProtocolStyleSheet()
 * 16-Oct-02    Byron           VBM:2002101605 - Modified initialise method to
 *                              set the useNobreakStyleForTag to TRUE
 * 17-Oct-02    Phil W-S        VBM:2002081322 - Overrode openPane to ensure
 *                              that, if possible, the pane's style class is
 *                              added to the parent table cell (if the parent
 *                              is a table cell).
 * 23-Oct-02    Byron           VBM:2002101404 - Modified initialise() not to
 *                              set the flag useNobreakStyleForTag to true.
 * 08-Nov-02    Byron           VBM:2002110515 - Now uses a custom style sheet
 *                              renderer (for border-spacing). Added
 *                              getLayoutDeviceThemeFactory() method.
 * 26-Nov-02    Phil W-S        VBM:2002112003 - Implement a more complete
 *                              optimization of openPane and closePane that
 *                              can utilize the new determinePaneRendering
 *                              method. This processing is based losely on
 *                              that found in XHTMLFull.
 * 05-Dec-02    Phil W-S        VBM:2002120503 - Inline device and layout
 *                              stylesheets. Settings in the constructor and
 *                              new openStyle and closeStyle methods.
 * 12-Dec-02    Phil W-S        VBM:2002110516 - Change the call to
 *                              getPaneStyleClass to getFormatStyleClass in
 *                              openPane.
 * 27-Jan-03    Byron           VBM:2003012404 - Calls to
 *                              addCoreAttributeNobreak chanaged to
 *                              addCoreAttributes.
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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;

/**
 * This is the XHTML Basic protocol for version 2.1 of the Mobile Internet
 * Browser. It has been customized for support of the H3G Motorola Talon
 * device.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class XHTMLBasic_MIB2_1 extends XHTMLBasic
{

    /**
     * Default constructor
     */
    public XHTMLBasic_MIB2_1(ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration) {

        super(protocolSupportFactory, protocolConfiguration);

        // Even though this protocol is notionally XHTML Basic, it does have
        // some XHTML strict extensions. Of these extensions we make use of
        // the ability to have style elements for inline (internal) stylesheets
        supportsInlineStyles = true;

        // 3 have requested this to support targetting of anchors, even
        // though the Name Identification Module is not part of XHTMLBasic.
        // This is because their XHTMLBasic devices do not support targetting
        // of ids.
        enableNameIdentification = true;

        // The following defines how this protocol expects to render
        // the various types of stylesheet
        protocolThemeStylesheetPreference = StylesheetRenderMode.EXTERNAL;

        // Can we have hr elements please
    }

    //  Javadoc inherited from super class
    public String defaultMimeType() {
        return "text/html";
    }

    // ========================================================================
    //   General helper methods.
    // ========================================================================

    // javadoc inherited
    protected DOMTransformer getDOMTransformer () {
        return new XHTMLBasic_MIB2_1Transformer();
    }

    // ========================================================================
    //   Page element methods
    // ========================================================================

    // Javadoc inherited.
    protected void doProtocolString(Document document) {
        addXHTMLBasicDocType(document);
    }

    /**
     * @todo depend Motorola Talon browser update: insert use of CDATA block
     */
    protected void openStyle(DOMOutputBuffer dom,
                             StyleAttributes attributes) {

        performDefaultOpenStyle(dom, attributes, false);
        // Output a CDATA block around the CSS content to prevent the parser
        // from incorrectly interpreting unencoded significant parser
        // characters ("<", ">" and "&") that could be found in the content.
        // It is more common to use a comment around the content, but the MIB
        // browser doesn't handle this correctly.
        // @todo commented out until the browser supports the use of CDATA
        // dom.appendLiteral("<![CDATA[\n");
    }

    /**
     * @todo depend Motorola Talon browser update: insert use of CDATA block
     */
    protected void closeStyle(DOMOutputBuffer dom,
                              StyleAttributes attributes) {
        // @todo commented out until the browser supports the use of CDATA
        // dom.appendLiteral("\n]]>");
        performDefaultCloseStyle(dom, false);
    }

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    /**
     * Returns the rendering style that should be used when generating a
     * pane.
     *
     * @param dom the DOM output buffer
     * @param attributes the pane attributes
     * @return the rendering style
     */
    private PaneRendering determinePaneRendering(DOMOutputBuffer dom,
                                                   PaneAttributes attributes) {
        PaneRendering rendering = PaneRendering.DO_NOTHING;

        // Check to see whether the enclosing element is a "td" because if
        // it is we could add the pane attributes to it (assuming that they
        // do not clash with existing attributes) instead of creating a div
        // to surround the pane's content
        Element element = dom.getCurrentElement();

        if ("td".equals(element.getName())) {
            String cellId = element.getAttributeValue("id");
            String attrId = attributes.getId();

            // If IDs clash then a div must be used
            if (((attrId != null) &&
                 (cellId != null) &&
                 !attrId.equals(cellId))) {
                rendering = PaneRendering.CREATE_ENCLOSING_ELEMENT;
            } else {
                rendering = PaneRendering.USE_ENCLOSING_TABLE_CELL;
            }
        }
        // todo Check significant styles before deciding how to render the pane.

        return rendering;
    }

    // javadoc inherited
    protected void openPane(DOMOutputBuffer dom,
                            PaneAttributes attributes) {
        // If there is a style attribute for the pane and the device supports
        // style sheets then surround the output with a div element unless
        // an enclosing table cell can be used instead
        if (supportsInlineStyles || supportsExternalStyleSheets) {
            PaneInstance paneInstance = (PaneInstance)
                    context.getDeviceLayoutContext().
                    getCurrentFormatInstance(attributes.getPane());
            PaneRendering rendering;

            rendering = determinePaneRendering(dom, attributes);

            // Record the rendering used
            // in the PaneInstance for use in closePane.
            paneInstance.setRendering(rendering);

            if (rendering == PaneRendering.USE_ENCLOSING_TABLE_CELL) {
                Element parent = dom.getCurrentElement();

                addCoreAttributes(parent, attributes);

                // preserve any Styles information on the pane element
                StylesMerger merger = StylingFactory.getDefaultInstance().
                        getStylesMerger();
                // save the display style property value as the parent.setStyles
                // may change it and we don't want that
                final StyleValue oldValue =
                    parent.getStyles().getPropertyValues().getStyleValue(
                        StylePropertyDetails.DISPLAY);
                parent.setStyles(merger.merge(attributes.getStyles(),
                        parent.getStyles()));
                parent.getStyles().getPropertyValues().
                    setComputedAndSpecifiedValue(StylePropertyDetails.DISPLAY, oldValue);
            } else if (rendering == PaneRendering.CREATE_ENCLOSING_ELEMENT) {
                // write out a div element so that the wrapped elements
                // pick up any styles.
                Element element = dom.openStyledElement("div", attributes);
                addCoreAttributes(element, attributes);
            }
        }
    }

    // javadoc inherited
    protected void closePane(DOMOutputBuffer dom,
                             PaneAttributes attributes) {
        // If a div tag was generated around the pane, then close it
        if (supportsInlineStyles || supportsExternalStyleSheets) {
            // Record the fact that the table cell was used
            // in the PaneInstance for use in closePane.
            PaneInstance paneInstance = (PaneInstance)
                    context.getDeviceLayoutContext().
                    getCurrentFormatInstance(attributes.getPane());

            PaneRendering rendering = paneInstance.getRendering();
            if (rendering == PaneRendering.CREATE_ENCLOSING_ELEMENT) {
                dom.closeElement("div");
            }
        }
    }

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

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 02-Sep-05	9408/4	pabbott	VBM:2005083007 Move over to using JiBX accessor

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/2	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	8859/2	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 05-Nov-04	6112/6	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 03-Nov-04	5871/1	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 15-Mar-04	3403/3	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 17-Nov-03	1749/4	philws	VBM:2003081102 Fix merge issues

 14-Nov-03	1861/1	mat	VBM:2003110602 Add correct mimetype to descendants of XHTMLBasic

 01-Nov-03	1749/1	philws	VBM:2003081102 Port of Spatial and Temporal Iterator layout stylesheet handling from PROTEUS

 01-Nov-03	1745/1	philws	VBM:2003081102 Provide layout style for Spatial and Temporal Iterators

 ===========================================================================
*/
