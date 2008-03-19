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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;

import java.io.IOException;

/**
 * Simple test protocol that generates easily testable output.
 */
public class RendererTestProtocol extends VolantisProtocol {
    /**
     * Duration output buffer.
     */
    private StringBuffer durationBuffer;

    /**
     * Default output buffer factory
     */
    private OutputBufferFactory defaultOutputBufferFactory;

    /**
     * Create a new RendererTestProtocol with the specified Mariner page
     * context and a default duration buffer.
     * <p>Used for testing renderers that do not require access to the
     * duration buffer (in general, anything other than tests specific to
     * temporal iteration).
     * @param pageContext The Mariner page context
     */
    public RendererTestProtocol(MarinerPageContext pageContext) {
        this(pageContext, new StringBuffer());
    }

    /**
     * Create a new RendererTestProtocol with the specified duration buffer
     * and MarinerPageContext.
     * @param pageContext The Mariner page context
     * @param durationBuffer The duration buffer
     */
    public RendererTestProtocol(MarinerPageContext pageContext,
                                StringBuffer durationBuffer) {
        super(null);
        this.durationBuffer = durationBuffer;
        setMarinerPageContext(pageContext);
        this.defaultOutputBufferFactory =
                new DOMOutputBufferFactory(DOMFactory.getDefaultInstance());
    }

    // Javadoc inherited
    public void writeOpenGrid(GridAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "grid", attributes.getStyles()));
    }

    // Javadoc inherited
    public void writeOpenGridChild(GridChildAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "gridChild", attributes.getStyles()));
    }

    // Javadoc inherited
    public void writeOpenGridRow(GridRowAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "gridRow", attributes.getStyles()));
    }

    // Javadoc inherited
    public void writeCloseGrid(GridAttributes ga) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeCloseGridChild(GridChildAttributes ga) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeCloseGridRow(GridRowAttributes ga) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenPane(PaneAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "pane", attributes.getStyles()));
//        appendAttribute("backgroundColour", attributes.getBackgroundColour());
//        appendAttribute("backgroundImage", attributes.getBackgroundImage());
//        appendAttribute("borderWidth", attributes.getBorderWidth());
//        appendAttribute("cellPadding", attributes.getCellPadding());
//        appendAttribute("cellSpacing", attributes.getCellSpacing());
//        appendAttribute("height", attributes.getHeight());
//        appendAttribute("styleClass", attributes.getStyleClass());
//        appendAttribute("width", attributes.getWidth());
//        appendAttribute("widthUnits", attributes.getWidthUnits());
    }

    // Javadoc inherited
    public void writeClosePane(PaneAttributes attr) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenSlide(SlideAttributes attributes) {
        // Append the duration so that it can be verified.
        durationBuffer.append(attributes.getDuration()).append(',');
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "slide", attributes.getStyles()));
//        appendAttribute("styleClass", attributes.getStyleClass());
    }

    // Javadoc inherited
    public void writeCloseSlide(SlideAttributes attr) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenForm(FormAttributes attributes)
            throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "form", attributes.getStyles()));
//        appendAttribute("styleClass", attributes.getStyleClass());
        appendAttribute("title", attributes.getTitle());
    }

    // Javadoc inherited
    public void writeCloseForm(FormAttributes attributes)
            throws IOException {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeFormPreamble(OutputBuffer buf) throws IOException {
        getBuffer().transferContentsFrom(buf);
    }

    // Javadoc inherited
    public void writeFormPostamble(OutputBuffer buf) throws IOException {
        getBuffer().transferContentsFrom(buf);
    }

    // Javadoc inherited
    public void writeLayout(DeviceLayoutContext dlc) throws IOException {
    }

    // Javadoc inherited
    public OutputBufferFactory getOutputBufferFactory() {
        return defaultOutputBufferFactory;
    }

    // Javadoc inherited
    public void openCanvasPage(CanvasAttributes attributes)
            throws IOException {
    }

    // Javadoc inherited
    public void openInclusionPage(CanvasAttributes attributes)
            throws IOException {
    }

    // Javadoc inherited
    public void closeInclusionPage(CanvasAttributes attributes)
            throws IOException, ProtocolException {
    }

    // Javadoc inherited
    public void openMontagePage(MontageAttributes attributes)
            throws IOException {
    }

    // Javadoc inherited
    protected void writeCanvasContent(PackageBodyOutput output,
                                      CanvasAttributes attributes)
            throws IOException, ProtocolException {
    }

    // Javadoc inherited
    protected void writeMontageContent(PackageBodyOutput output,
                                       MontageAttributes attributes)
            throws IOException, ProtocolException {
    }

    // Javadoc inherited
    public String defaultMimeType() {
        return "";
    }

    // Javadoc inherited
    public void writeCloseSpatialFormatIterator
            (SpatialFormatIteratorAttributes attributes) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeCloseSpatialFormatIteratorChild
            (SpatialFormatIteratorAttributes attributes) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeCloseSpatialFormatIteratorRow
            (SpatialFormatIteratorAttributes attributes) {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenSpatialFormatIterator
            (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "spatial", attributes.getStyles()));
        appendAttribute("title", attributes.getTitle());
    }

    // Javadoc inherited
    public void writeOpenSpatialFormatIteratorChild
            (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "spatialChild", attributes.getStyles()));
//        appendAttribute("columnStyleClass", attributes.getColumnStyleclass());
    }

    // Javadoc inherited
    public void writeOpenSpatialFormatIteratorRow
            (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "spatialRow", attributes.getStyles()));
//        appendAttribute("rowStyleClass", attributes.getRowStyleclass());
    }

    // Javadoc inherited
    public void writeFragmentLink(FraglinkAttributes attributes)
            throws IOException, ProtocolException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "fragmentLink", attributes.getStyles()));
//        appendAttribute("styleClass", attributes.getStyleClass());
    }

    // Javadoc inherited
    public void writeOpenDissectingPane(
            DissectingPaneAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "dissectingPane", attributes.getStyles()));
        appendAttribute("backLinkText", attributes.getBackLinkText());
        appendAttribute("inclusionPath", attributes.getInclusionPath());
        appendAttribute("linkText", attributes.getLinkText());
//        appendAttribute("styleClass", attributes.getStyleClass());
        appendAttribute("title", attributes.getTitle());
    }

    // Javadoc inherited
    public void writeCloseDissectingPane(
            DissectingPaneAttributes attributes) throws IOException {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenRowIteratorPane(RowIteratorPaneAttributes attributes)
            throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "rowIteratorPane", attributes.getStyles()));
//        appendAttribute("backgroundColour", attributes.getBackgroundColour());
//        appendAttribute("backgroundImage", attributes.getBackgroundImage());
//        appendAttribute("borderWidth", attributes.getBorderWidth());
//        appendAttribute("cellPadding", attributes.getCellPadding());
//        appendAttribute("cellSpacing", attributes.getCellSpacing());
//        appendAttribute("height", attributes.getHeight());
//        appendAttribute("styleClass", attributes.getStyleClass());
//        appendAttribute("width", attributes.getWidth());
//        appendAttribute("widthUnits", attributes.getWidthUnits());
    }

    // Javadoc inherited
    public void writeCloseRowIteratorPane(RowIteratorPaneAttributes attrs)
            throws IOException {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
            throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "columnIteratorPane", attributes.getStyles()));
//        appendAttribute("backgroundColour", attributes.getBackgroundColour());
//        appendAttribute("backgroundImage", attributes.getBackgroundImage());
//        appendAttribute("borderWidth", attributes.getBorderWidth());
//        appendAttribute("cellPadding", attributes.getCellPadding());
//        appendAttribute("cellSpacing", attributes.getCellSpacing());
//        appendAttribute("height", attributes.getHeight());
//        appendAttribute("styleClass", attributes.getStyleClass());
//        appendAttribute("width", attributes.getWidth());
//        appendAttribute("widthUnits", attributes.getWidthUnits());
    }

    // Javadoc inherited
    public void writeCloseColumnIteratorPane(
            ColumnIteratorPaneAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeOpenSegment(SegmentAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "segment", attributes.getStyles()));
        appendAttribute("borderColor", attributes.getBorderColor());
        appendAttribute("frameBorder", String.valueOf(attributes.getFrameBorder()));
        appendAttribute("marginHeight", String.valueOf(attributes.getMarginHeight()));
        appendAttribute("marginWidth", String.valueOf(attributes.getMarginWidth()));
        appendAttribute("name", attributes.getName());
        appendAttribute("scrolling", String.valueOf(attributes.getScrolling()));
//        appendAttribute("styleClass", attributes.getStyleClass());
        appendAttribute("title", attributes.getTitle());
    }

    public void writeOpenSegmentGrid(SegmentGridAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement(
                "segmentGrid", attributes.getStyles()));
        appendAttribute("borderColor", attributes.getBorderColor());
        appendAttribute("borderWidth", String.valueOf(attributes.getBorderWidth()));
        appendAttribute("frameSpacing", String.valueOf(attributes.getFrameSpacing()));
//        appendAttribute("styleClass", attributes.getStyleClass());
        appendAttribute("title", attributes.getTitle());
    }

    public void writeCloseSegmentGrid(SegmentGridAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    // Javadoc inherited
    public void writeCloseSegment(SegmentAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    /**
     * Appends an attribute to the output buffer if its value is not null.
     * @param attrName The name of the attribute
     * @param attrValue The value of the attribute
     */
    private void appendAttribute(String attrName, String attrValue) {
        if (attrValue != null) {
            DOMOutputBuffer buffer = getBuffer();
            Element ele = buffer.getCurrentElement();
            ele.setAttribute(attrName, attrValue);
        }
    }

    private DOMOutputBuffer getBuffer() {
        return (DOMOutputBuffer)
                getMarinerPageContext().getCurrentOutputBuffer();
    }

    public ValidationHelper getValidationHelper() {
        return null;
    }    
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/6	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 06-Jan-05	6391/6	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/2	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
