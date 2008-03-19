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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;

/**
 * Define mapping between XHTML 2 elements and their factories.
 */
public class XHTML2ElementHandler
        extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(XHTML2Elements.A, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new AnchorElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.ABBR, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new AbbreviationElement(context);
            }
        });
        
        builder.addMapping(XHTML2Elements.ACCESS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new AccessElement(context);
            }
        });
        
        builder.addMapping(XHTML2Elements.ADDRESS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new AddressElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.BLOCKQUOTE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new BlockQuoteElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.BODY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new BodyElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.CAPTION, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new CaptionElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.CITE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new CiteElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.CODE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new CodeElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.DD, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DefinitionDataElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.DFN, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DefinitionElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.DIV, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DivElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.DL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DefinitionListElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.DT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new DefinitionTermElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.EM, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new EmphasisElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.H1, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new Heading1Element(context);
            }
        });
        builder.addMapping(XHTML2Elements.H2, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new Heading2Element(context);
            }
        });
        builder.addMapping(XHTML2Elements.H3, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new Heading3Element(context);
            }
        });
        builder.addMapping(XHTML2Elements.H4, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new Heading4Element(context);
            }
        });
        builder.addMapping(XHTML2Elements.H5, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new Heading5Element(context);
            }
        });
        builder.addMapping(XHTML2Elements.H6, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new Heading6Element(context);
            }
        });
        builder.addMapping(XHTML2Elements.HEAD, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new HeadElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.HTML, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new HtmlElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.KBD, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new UserInputElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.LABEL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ListLabelElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.LI, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ListItemElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.LINK, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new LinkElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.META, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new MetaInformationElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.NL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new NavigationListElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.OBJECT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ObjectElementImpl(context);
            }
        });
        builder.addMapping(XHTML2Elements.OL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new OrderedListElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.P, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ParagraphElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.PARAM, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ParametersElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.PRE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new PreserveElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.QUOTE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new QuoteElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.SAMP, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SampleElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.SPAN, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SpanElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.STRONG, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new StrongElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.STYLE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new StyleElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.SUB, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SubscriptElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.SUP, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SuperscriptElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TABLE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TBODY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableBodyElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TD, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableDataElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TFOOT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableFooterElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TH, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableHeaderCellElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.THEAD, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableHeaderElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TITLE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TitleElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.TR, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new TableRowElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.UL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new UnorderedListElement(context);
            }
        });
        builder.addMapping(XHTML2Elements.VAR, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new VariableElement(context);
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/6	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
