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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

/**
 * Implementation of the XHTML Mobile Profile protocol for Openwave 6.
 */
public class XHTMLMobile1_0Openwave6 extends XHTMLMobile1_0 {

    /**
     * Initializes the new instance with the given parameters.
     */
    public XHTMLMobile1_0Openwave6(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        supportsLocalSourceImg = true;
    }

    // Javadoc inherited from super class.
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "html", "-//OPENWAVE//DTD XHTML Mobile 1.0//EN",
                "http://www.openwave.com/dtd/xhtml-mobile10.dtd", null,
                MarkupFamily.XML);
        document.setDocType(docType);
    }

    // javadoc inherited
    protected void addAddressAttributes(Element element,
                                        AddressAttributes attributes)
            throws ProtocolException {
        super.addAddressAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addBlockQuoteAttributes(Element element,
                                           BlockQuoteAttributes attributes)
            throws ProtocolException {
        super.addBlockQuoteAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addDefinitionListAttributes(
            Element element,
            DefinitionListAttributes attributes)
            throws ProtocolException {
        super.addDefinitionListAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addDivAttributes(Element element,
                                    DivAttributes attributes)
            throws ProtocolException {
        super.addDivAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addHeadingAttributes(Element element,
                                        HeadingAttributes attributes,
                                        int level) throws ProtocolException {
        super.addHeadingAttributes(element, attributes, level);
        addModeAttribute(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addLineBreakAttributes(Element element,
                                          LineBreakAttributes attributes) {
        // do nothing.

    }

    // javadoc inherited
    protected void addOrderedListAttributes(Element element,
                                            OrderedListAttributes attributes)
            throws ProtocolException {
        super.addOrderedListAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addParagraphAttributes(Element element,
                                          ParagraphAttributes attributes)
            throws ProtocolException {
        super.addParagraphAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addPreAttributes(Element element,
                                    PreAttributes attributes)
            throws ProtocolException {
        super.addPreAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    // javadoc inherited
    protected void addTableAttributes(Element element,
                                      TableAttributes attributes)
            throws ProtocolException {
        super.addTableAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }
    
    // javadoc inherited
    protected void addUnorderedListAttributes(Element element,
                                              UnorderedListAttributes attributes)
            throws ProtocolException {
        super.addUnorderedListAttributes(element, attributes);
        addModeAttribute(element, attributes);
    }

    /**
     * <p>Adds a mode attribute with value "nowrap". The default value assumed by
     * an Openwave browser is "wrap". This attribute is an Openwave extension
     * and was originally introduced for the &lt;p&gt; element. However, it is
     * applicable to all block elements, although it is not recommended in the
     * Openwave style guide that you do this.</p>
     *
     * <p>This attribute is added if a whitespace nowrap style has been specified
     * in a theme or a style class.</p>
     *
     * @param element the block element to which the mode attribute is to be
     * added
     * @param attributes the current attributes of the element
     */
    private void addModeAttribute(Element element,
                                    MCSAttributes attributes) {

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();

        // Check whether a no-wrapping whitespace has been specified in a
        // theme or style class.
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.WHITE_SPACE);
        if (value == WhiteSpaceKeywords.NOWRAP) {
            element.setAttribute("mode", "nowrap");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 12-Jul-05	9015/1	amoore	VBM:2005052307 Inserted single whitespace between public Id and DTD location in Doctype to ensure correct format is sent to device

 12-Jul-05	9019/1	amoore	VBM:2005052307 Inserted single whitespace between public Id and DTD location in Doctype to ensure correct format is sent to device

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 03-Sep-04	4998/14	pcameron	VBM:2004072805 Check that style isn't null when adding mode attribute

 02-Aug-04	5054/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 29-Jul-04	4980/7	pcameron	VBM:2004072805 Fixed javadoc

 29-Jul-04	4980/3	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 03-Sep-04	5054/8	pcameron	VBM:2004072805 Check that style isn't null when adding mode attribute

 02-Aug-04	5054/1	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 03-Sep-04	4995/4	pcameron	VBM:2004072805 Check that style isn't null when adding mode attribute

 29-Jul-04	4995/2	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 29-Jul-04	4980/7	pcameron	VBM:2004072805 Fixed javadoc

 29-Jul-04	4980/3	pcameron	VBM:2004072805 Openwave protocol supports mode=nowrap attribute for block elements

 02-Sep-03	1305/1	adrian	VBM:2003082108 added new openwave6 xhtml protocol

 ===========================================================================
*/
