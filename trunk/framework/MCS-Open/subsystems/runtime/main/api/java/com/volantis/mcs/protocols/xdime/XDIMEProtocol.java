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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.xdime;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom2theme.StyledDOMStyleAttributeRenderer;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractorFactory;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.DeferredInheritTransformer;
import com.volantis.mcs.protocols.css.renderer.RuntimeCSSStyleSheetRenderer;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.StyleContainer;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

import java.io.IOException;

/**
 * A protocol that simply passes the XDIME straight through.
 *
 * <p>This protocol is only intended to be used in integration testing.</p>
 */
public class XDIMEProtocol
        extends DOMProtocol {

    private static final String SPATIAL_ELEMENT_NAME = "spatial";
    private static final String SPATIAL_ROW_ELEMENT_NAME = "spatial-row";
    private static final String SPATIAL_CELL_ELEMENT_NAME = "spatial-cell";
    private static final String GRID_ELEMENT_NAME = "grid";
    private static final String GRID_ROW_ELEMENT_NAME = "grid-row";
    private static final String GRID_CELL_ELEMENT_NAME = "grid-cell";
    private static final String PANE_ELEMENT_NAME = "pane";
    private static final String INCLUSION_ELEMENT_NAME = "inclusion";

    private static final StylePercentage HUNDRED_PERCENT =
        StyleValueFactory.getDefaultInstance().getPercentage(null, 100);
    
    private String xmlNamespace = null;

    public XDIMEProtocol(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);

        styleSheetRenderer = RuntimeCSSStyleSheetRenderer.getSingleton();
        supportsInlineStyles = true;
    }

    public void writeStyleSheet() throws IOException {
        // Override to prevent style sheet place holder being written out as
        // styles are added to 'style' attributes.
    }

    /**
     * Override to generate styles as style attributes on the elements as it
     * makes it easier to see which styles apply to an element than when using
     * classes.
     */
    protected void generateCSS() throws IOException {

        DeferredInheritTransformer transformer =
                new DeferredInheritTransformer();
        document = transformer.transform(this, document);

        StyledDOMThemeExtractorFactory factory =
            StyledDOMThemeExtractorFactory.getDefaultInstance();

        final ExtractorConfiguration configuration =
            protocolConfiguration.getExtractorConfiguration();

        StyledDOMStyleAttributeRenderer rewriter =
            factory.createRenderer(configuration,getExtractorContext());

        rewriter.renderStyleAttributes(document);
    }

    public String defaultMimeType() {
        return "x-application/vnd.xdime+xml";
    }

    protected void openCanvas (
                DOMOutputBuffer dom,
                               CanvasAttributes attributes) {

        Element element = dom.openElement("html");

        if (xmlNamespace != null) {
            element.setAttribute ("xmlns", xmlNamespace);
        }

        // The title is not part of the canvas, it should be written to the
        // head buffer.
        dom = getHeadBuffer ();
        doTitle (dom, attributes);
    }

    protected void closeCanvas (DOMOutputBuffer dom,
                                CanvasAttributes attributes) {
        dom.closeElement("html");
    }

    protected void doTitle (DOMOutputBuffer dom,
                            CanvasAttributes attributes) {

        // XHTML protocols require the title element to be present.
        // If the title has not been set then output the empty string
        String value = ((attributes.getPageTitle() == null)
                        ? "" : attributes.getPageTitle());

        dom.openElement ("title");
        dom.appendEncoded (value);
        dom.closeElement ("title");
    }

    protected void openHead (DOMOutputBuffer dom,
                             boolean empty) {
        dom.openElement ("head");
    }

    protected void closeHead (DOMOutputBuffer dom,
                              boolean empty) {
        dom.closeElement ("head");
    }

    protected void openInclusion(
            DOMOutputBuffer dom, CanvasAttributes attributes)
            throws ProtocolException {
        openBlock(dom, INCLUSION_ELEMENT_NAME, attributes);
    }

    protected void closeInclusion(
            DOMOutputBuffer dom, CanvasAttributes attributes) {
        closeBlock(dom, INCLUSION_ELEMENT_NAME);
    }

    protected void openBody (DOMOutputBuffer dom,
                             BodyAttributes attributes)
            throws ProtocolException {

        dom.openStyledElement ("body", attributes);
    }

    protected void closeBody (DOMOutputBuffer dom,
                              BodyAttributes attributes) {
        dom.closeElement ("body");
    }

    protected void openSpatialFormatIterator(
            DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        Element element = dom.openStyledElement(SPATIAL_ELEMENT_NAME,
                attributes);
        element.setAttribute("cols", String.valueOf(attributes.getColumns()));
    }

    protected void closeSpatialFormatIterator(
            DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        dom.closeElement(SPATIAL_ELEMENT_NAME);
    }

    protected void openSpatialFormatIteratorRow(
            DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        Element element = dom.openStyledElement(SPATIAL_ROW_ELEMENT_NAME,
                attributes.getStyles()/*,
                DisplayKeywords.TABLE_ROW*/);
        element.setAttribute("cols", String.valueOf(attributes.getColumns()));
    }

    protected void closeSpatialFormatIteratorChild(
            DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        dom.closeElement(SPATIAL_CELL_ELEMENT_NAME);
    }

    protected void openSpatialFormatIteratorChild(
            DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        Element element = dom.openStyledElement(SPATIAL_CELL_ELEMENT_NAME,
                attributes.getStyles()/*,
                DisplayKeywords.TABLE_CELL*/);
        element.setAttribute("cols", String.valueOf(attributes.getColumns()));
    }

    protected void closeSpatialFormatIteratorRow(
            DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        dom.closeElement(SPATIAL_ROW_ELEMENT_NAME);
    }

    protected void openGrid(
            DOMOutputBuffer dom, GridAttributes attributes) {
        Element element = dom.openStyledElement(GRID_ELEMENT_NAME, attributes);
        element.setAttribute("cols", String.valueOf(attributes.getColumns()));
    }

    protected void closeGrid(
            DOMOutputBuffer dom, GridAttributes attributes) {
        dom.closeElement(GRID_ELEMENT_NAME);
    }

    protected void openGridRow(
            DOMOutputBuffer dom, GridRowAttributes attributes) {
        Element element = dom.openStyledElement(GRID_ROW_ELEMENT_NAME,
                attributes);
        element.setAttribute("cols", String.valueOf(attributes.getColumns()));
    }

    protected void closeGridRow(
            DOMOutputBuffer dom, GridRowAttributes attributes) {
        dom.closeElement(GRID_ROW_ELEMENT_NAME);
    }

    protected void openGridChild(
            DOMOutputBuffer dom, GridChildAttributes attributes) {
        Element element = dom.openStyledElement(
                GRID_CELL_ELEMENT_NAME, attributes, DisplayKeywords.TABLE_CELL);
        element.setAttribute("cols", String.valueOf(attributes.getColumns()));
    }

    protected void closeGridChild(
            DOMOutputBuffer dom, GridChildAttributes attributes) {
        dom.closeElement(GRID_CELL_ELEMENT_NAME);
    }

    protected void openPane(
            DOMOutputBuffer dom, PaneAttributes attributes) {
        openBlock(dom, PANE_ELEMENT_NAME, attributes);
    }

    private void openBlock(
            DOMOutputBuffer dom, String name, StyleContainer container) {

        // According to the CSS specification a width value of auto is treated
        // as 100% on block elements. Unfortunately, for backwards
        // compatibility reasons layouts can generate widths of 100% so convert
        // them back to auto so that they do not appear in the output.
        Styles styles = container.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getStyleValue(StylePropertyDetails.WIDTH);
        if (value.equals(HUNDRED_PERCENT)) {
            propertyValues.setComputedValue(StylePropertyDetails.WIDTH,
                    WidthKeywords.AUTO);
        }
        dom.openStyledElement(name, styles);
    }

    private void closeBlock(DOMOutputBuffer dom, String name) {
        dom.closeElement(name);
    }

    protected void closePane(
            DOMOutputBuffer dom, PaneAttributes attributes) {
        closeBlock(dom, PANE_ELEMENT_NAME);
    }

    // ========================================================================
    //     Block elements
    // ========================================================================

    protected void openAddress(
            DOMOutputBuffer dom, AddressAttributes attributes)
            throws ProtocolException {
        openBlock(dom, "address", attributes);
    }

    protected void closeAddress(
            DOMOutputBuffer dom, AddressAttributes attributes) {
        closeBlock(dom, "address");
    }

    protected void openBlockQuote(
            DOMOutputBuffer dom, BlockQuoteAttributes attributes)
            throws ProtocolException {
        openBlock(dom, "blockquote", attributes);
    }

    protected void closeBlockquote(
            DOMOutputBuffer dom, BlockQuoteAttributes attributes) {
        closeBlock(dom, "blockquote");
    }

    public void openDiv(
            DOMOutputBuffer dom, DivAttributes attributes)
            throws ProtocolException {
        openBlock(dom, "div", attributes);
    }

    public void closeDiv(
            DOMOutputBuffer dom, DivAttributes attributes) {
        closeBlock(dom, "div");
    }

    // ========================================================================
    //     Inline elements
    // ========================================================================

    protected void openBig(
            DOMOutputBuffer dom, BigAttributes attributes)
            throws ProtocolException {
        dom.openStyledElement("big", attributes);
    }

    protected void closeBig(
            DOMOutputBuffer dom, BigAttributes attributes) {
        dom.closeElement("big");
    }

    protected void openBold(
            DOMOutputBuffer dom, BoldAttributes attributes)
            throws ProtocolException {
        dom.openStyledElement("bold", attributes);
    }

    protected void closeBold(
            DOMOutputBuffer dom, BoldAttributes attributes) {
        dom.closeElement("bold");
    }

    protected void openEmphasis(
            DOMOutputBuffer dom, EmphasisAttributes attributes)
            throws ProtocolException {
        dom.openStyledElement("emphasis", attributes);
    }

    protected void closeEmphasis(
            DOMOutputBuffer dom, EmphasisAttributes attributes) {
        dom.closeElement("emphasis");
    }

    public void openSpan(
            DOMOutputBuffer dom, SpanAttributes attributes)
            throws ProtocolException {
        dom.openStyledElement("span", attributes);
    }

    public void closeSpan(
            DOMOutputBuffer dom, SpanAttributes attributes) {
        dom.closeElement("span");
    }
}
