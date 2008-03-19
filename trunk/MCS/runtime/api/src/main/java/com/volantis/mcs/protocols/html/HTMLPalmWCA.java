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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLPalmWCA.java,v 1.9 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Aug-01    Allan           VBM:2001083102 - Removed openBody() since it is
 *                              identical to the super class definitio. Added
 *                              this change history.
 * 21-Sep-01    Doug            VBM:2001090302 - openAnchor accepts href as
 *                              new parameter because we are not allowed to
 *                              modify values in the Attribute classes
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 05-Mar-02    Allan           VBM:2002030102 - Overide paneNeedsTableWrapper
 *                              to return true if the pane is not topLevel.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Stopped calling the deprecated
 *                              doMeta method.
 * 26-Mar-02    Allan           VBM:2002022007 - generateContentTree() now
 *                              takes an Object. generateContents() returns an
 *                              Object and uses ReusableStringBuffer (and
 *                              needs more work).
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 26-Apr-02    Paul            VBM:2002042205 - Removed paneNeedsTableWrapper
 *                              method as it is no longer used and should have
 *                              been removed already.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Paul            VBM:2002042203 - Removed the preprocess flag,
 *                              this protocol is broken as the nested tables
 *                              are not handled properly.
 * 20-Nov-02    Geoff           VBM:2002111504 - commented out unused methods
 *                              (generateContents, generateContentTree),
 *                              cleaned up imports and unused locals.
 * 10-Jan-03    Phil W-S        VBM:2002110402 - This protocol does not support
 *                              format optimization.
 * 16-Jan-03    Byron           VBM:2003011501 - Added doProtocolString and
 *                              getDOMTransformer.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Augment
 *                              addPhoneNumberAttributes in the same way that
 *                              addAnchorAttributes is augmented. NB: Could
 *                              have performed some refactoring, but as the
 *                              duplicate code is only 4 statements, didn't.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSLinkStyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

/**
 * Protocol class for Palm WCA. This supports version 1.1.
 */
public class HTMLPalmWCA extends HTMLVersion3_2 {

    public HTMLPalmWCA(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        supportsScripts = false;        
        supportsFormatOptimization = false;

        // Set the protocol default for nested table support to false.
        // This may be updated by the initialise method depending on the
        // value in the device database.
        supportsNestedTables = false;
    }

    // Javadoc inherited.
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "html", "-//POS//DTD WCA HTML 1.1//EN",
                "http://www.palm.com/dev/webclippinghtml-dtd-11.dtd", null,
                MarkupFamily.SGML);
        document.setDocType(docType);
    }

    // javadoc inherited
    protected void doTitle(DOMOutputBuffer dom,
                           CanvasAttributes attributes) {

        String value;

        if ((value = attributes.getPageTitle()) != null) {
            Element element = dom.addStyledElement("meta", attributes);
            element.setAttribute("name", "historylisttext");
            element.setAttribute("content", value);
        }

        super.doTitle(dom, attributes);
    }

    // Javadoc inherited from super class.
    protected void openHead(DOMOutputBuffer dom,
                            boolean empty) {

        // Write the meta tags to the head.
        MetaAttributes ma = new MetaAttributes();
        ma.setName("PalmComputingPlatform");
        ma.setContent("true");
        writeMeta(ma);

        ma.setName("HandheldFriendly");
        writeMeta(ma);

        super.openHead(dom, empty);
    }

    // javadoc inherited
    protected void addAnchorAttributes(Element element,
                                       AnchorAttributes attributes)
            throws ProtocolException {
        super.addAnchorAttributes(element, attributes);

        addButtonAttribute(element, attributes);
    }

    private void addButtonAttribute(Element element, MCSAttributes attributes) {
        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_LINK_STYLE);

        if (value == MCSLinkStyleKeywords.BUTTON) {
            element.setAttribute("button", "button");
        }
    }

    // javadoc inherited
    protected void addPhoneNumberAttributes(Element element,
                                            PhoneNumberAttributes attributes)
            throws ProtocolException {
        super.addPhoneNumberAttributes(element, attributes);

        addButtonAttribute(element, attributes);
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openMonospaceFont(DOMOutputBuffer dom,
                                     MonospaceFontAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeMonospaceFont(DOMOutputBuffer dom,
                                      MonospaceFontAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void openScript(DOMOutputBuffer dom,
                              ScriptAttributes attributes) {
    }

    /**
     * This is not supported in this protocol.
     */
    protected void closeScript(DOMOutputBuffer dom,
                               ScriptAttributes attributes) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/1	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/2	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 19-May-05	8335/1	philws	VBM:2005051705 Port Palm WCA style emulation from 3.3

 19-May-05	8305/1	philws	VBM:2005051705 Provide style emulation rendering for HTML Palm WCA version 1.1

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 15-Jul-04	4869/1	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 14-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 17-Sep-03	1412/4	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
