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

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.SlideAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;

import java.io.IOException;

/**
 * Provides facilities to render layout information to the protocol.
 *
 * @mock.generate
 */
public interface LayoutModule
        extends GridFormatModule {

    /**
     * Write the open segment markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenSegment(SegmentAttributes attributes)
            throws IOException;

    /**
     * Write the close segment markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseSegment(SegmentAttributes attributes)
            throws IOException;

    /**
     * Write the open segment grid markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenSegmentGrid(SegmentGridAttributes attributes)
            throws IOException;

    /**
     * Write the close segment grid markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseSegmentGrid(SegmentGridAttributes attributes)
            throws IOException;

    /**
     * Write the open column iterator pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeOpenColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the close column iterator pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeCloseColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the open column iterator pane element markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenColumnIteratorPaneElement(
            ColumnIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the close column iterator pane element markup directly to the
     * page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseColumnIteratorPaneElement(
            ColumnIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the contents of the column iterator pane element directly to the
     * page.
     *
     * @param buffer The contents of the column iterator pane element.
     */
    void writeColumnIteratorPaneElementContents(OutputBuffer buffer)
            throws IOException;

    /**
     * Write the open row iterator pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeOpenRowIteratorPane(RowIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the close row iterator pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeCloseRowIteratorPane(RowIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the open row iterator pane element markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeOpenRowIteratorPaneElement(RowIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the close row iterator pane element markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseRowIteratorPaneElement(
            RowIteratorPaneAttributes attributes)
            throws IOException;

    /**
     * Write the contents of the row iterator pane element directly to the
     * page.
     *
     * @param buffer The contents of the row iterator pane element.
     */
    void writeRowIteratorPaneElementContents(OutputBuffer buffer)
            throws IOException;

    /**
     * Write the open pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenPane(PaneAttributes attributes)
            throws IOException;

    /**
     * Write the close pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeClosePane(PaneAttributes attributes)
            throws IOException;

    /**
     * Write the pane contents buffer directly to the page.
     *
     * @param buffer The buffer to write.
     */
    void writePaneContents(OutputBuffer buffer)
            throws IOException;

    /**
     * Write the open form markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenForm(FormAttributes attributes)
            throws IOException;

    /**
     * Write the close form markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseForm(FormAttributes attributes)
            throws IOException;

    /**
     * Write the form preamble buffer directly to the page.
     *
     * @param buffer The buffer to write.
     */
    void writeFormPreamble(OutputBuffer buffer)
            throws IOException;

    /**
     * Write the form postamble buffer directly to the page.
     *
     * @param buffer The buffer to write.
     */
    void writeFormPostamble(OutputBuffer buffer)
            throws IOException;

    /**
     * Write the open dissecting pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeOpenDissectingPane(DissectingPaneAttributes attributes)
            throws IOException;

    /**
     * Write the close dissecting pane markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    void writeCloseDissectingPane(DissectingPaneAttributes attributes)
            throws IOException, ProtocolException;

    /**
     * Write the open spatial iterator markup directly to the pane.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenSpatialFormatIterator(
            SpatialFormatIteratorAttributes attributes);

    /**
     * Write the open spatial iterator row markup directly to the pane.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenSpatialFormatIteratorRow(
            SpatialFormatIteratorAttributes attributes);

    /**
     * Write the open spatial iterator child markup directly to the pane.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenSpatialFormatIteratorChild(
            SpatialFormatIteratorAttributes attributes);

    /**
     * Write the close spatial iterator markup directly to the pane.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseSpatialFormatIterator(
            SpatialFormatIteratorAttributes attributes);

    /**
     * Write the close spatial iterator row markup directly to the pane.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseSpatialFormatIteratorRow(
            SpatialFormatIteratorAttributes attributes);

    /**
     * Write the close spatial iterator child markup directly to the pane.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseSpatialFormatIteratorChild(
            SpatialFormatIteratorAttributes attributes);

    OutputBufferFactory getOutputBufferFactory();

    /**
     * Write out the open slide markup. This is used by the MMS SMIL protocol.
     * A slide consists of two panes one for some text and the other for an image.
     * A temporal format iterator would display several slides one after the other.
     *
     * @param attributes The attributes to use when generating the markup
     */
    void writeOpenSlide(SlideAttributes attributes);

    /**
     * Write out the close slide markup. This is used by the MMS SMIL protocol.
     * A slide consists of two panes one for some text and the other for an image.
     * A temporal format iterator would display several slides one after the other.
     *
     * @param attributes The attributes to use when generating the markup
     */
    void writeCloseSlide(SlideAttributes attributes);

    /**
     * Begin a nested inclusion. This is called for each inclusion within a
     * region in the including page.
     */
    void beginNestedInclusion();

    /**
     * End a nested inclusion. This is called for each inclusion within a
     * region in the including page.
     */
    void endNestedInclusion();

    /**
     * Write the open layout markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeOpenLayout(LayoutAttributes attributes)
            throws IOException;

    /**
     * Write the close layout markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    void writeCloseLayout(LayoutAttributes attributes)
            throws IOException;

    /**
     * Flag which specifies if the protocol supports targetting of fragment
     * link lists into specific panes, rather than just appending them after
     * the content of their fragment. This affects the order of rendering of
     * the fragment content versus the linklist. Initially only used by VDXML.
     */
    boolean getSupportsFragmentLinkListTargetting();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
