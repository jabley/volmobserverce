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

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactoryImpl;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererSelector;
import com.volantis.mcs.protocols.renderer.layouts.spatial.aligned.AlignedRenderer;
import com.volantis.mcs.protocols.renderer.layouts.spatial.nested.NestedRenderer;

/**
 * Default implementation of FormatRendererSelector.
 * <p>Stores an instance of each FormatRenderer type, and selects the
 * appropriate one based on format type.</p>
 */
public class DefaultFormatRendererSelector implements FormatRendererSelector {

    /**
     * Renderer for column iterator panes.
     */
    private final FormatRenderer columnIteratorPaneRenderer;

    /**
     * Renderer for dissecting panes.
     */
    private final FormatRenderer dissectingPaneRenderer;

    /**
     * Renderer for forms.
     */
    private final FormatRenderer formRenderer;

    /**
     * Renderer for form fragments.
     */
    private final FormatRenderer formFragmentRenderer;

    /**
     * Renderer for fragments.
     */
    private final FormatRenderer fragmentRenderer;

    /**
     * Renderer for grids.
     */
    private final FormatRenderer gridRenderer;

    /**
     * Renderer for panes.
     */
    private final FormatRenderer paneRenderer;

    /**
     * Renderer for regions.
     */
    private final FormatRenderer regionRenderer;

    /**
     * Renderer for replicas.
     */
    private final FormatRenderer replicaRenderer;

    /**
     * Renderer for row iterator panes.
     */
    private final FormatRenderer rowIteratorPaneRenderer;

    /**
     * Renderer for segments.
     */
    private final FormatRenderer segmentRenderer;

    /**
     * Renderer for segment grids.
     */
    private final FormatRenderer segmentGridRenderer;

    /**
     * Renderer for spatial format iterators.
     */
    private final FormatRenderer spatialFormatIteratorRenderer;

    /**
     * Renderer for aligned spatial format iterators that contain a grid.
     */
    private final FormatRenderer alignedSpatialFormatIteratorRenderer;

    /**
     * Renderer for temporal format iterators.
     */
    private final FormatRenderer temporalFormatIteratorRenderer;

    public DefaultFormatRendererSelector() {

        LayoutAttributesFactory factory = new LayoutAttributesFactoryImpl();

        columnIteratorPaneRenderer = new ColumnIteratorPaneRenderer();
        dissectingPaneRenderer = new DissectingPaneRenderer(factory);
        formRenderer = new FormRenderer(factory);
        formFragmentRenderer = new FormFragmentRenderer();
        fragmentRenderer = new FragmentRenderer();
        gridRenderer = new GridRenderer();
        paneRenderer = new PaneRenderer();
        regionRenderer = new RegionRenderer();
        replicaRenderer = new ReplicaRenderer();
        rowIteratorPaneRenderer = new RowIteratorPaneRenderer();
        segmentRenderer = new SegmentRenderer();
        segmentGridRenderer = new SegmentGridRenderer();
        spatialFormatIteratorRenderer =
                new NestedRenderer();
        alignedSpatialFormatIteratorRenderer =
                new AlignedRenderer();
        
        temporalFormatIteratorRenderer =
                new TemporalFormatIteratorRenderer(factory);
    }

    // Javadoc inherited
    public FormatRenderer selectFormatRenderer(Format format)
            throws RendererException {
        FormatRenderer renderer = null;

        FormatType type = format.getFormatType();

        if (type == FormatType.COLUMN_ITERATOR_PANE) {
            renderer = columnIteratorPaneRenderer;
        } else if (type == FormatType.DISSECTING_PANE) {
            renderer = dissectingPaneRenderer;
        } else if (type == FormatType.FORM) {
            renderer = formRenderer;
        } else if (type == FormatType.FORM_FRAGMENT) {
            renderer = formFragmentRenderer;
        } else if (type == FormatType.FRAGMENT) {
            renderer = fragmentRenderer;
        } else if (type == FormatType.GRID) {
            renderer = gridRenderer;
        } else if (type == FormatType.PANE) {
            renderer = paneRenderer;
        } else if (type == FormatType.REGION) {
            renderer = regionRenderer;
        } else if (type == FormatType.REPLICA) {
            renderer = replicaRenderer;
        } else if (type == FormatType.ROW_ITERATOR_PANE) {
            renderer = rowIteratorPaneRenderer;
        } else if (type == FormatType.SEGMENT) {
            renderer = segmentRenderer;
        } else if (type == FormatType.SEGMENT_GRID) {
            renderer = segmentGridRenderer;
        } else if (type == FormatType.SPATIAL_FORMAT_ITERATOR) {
            SpatialFormatIterator spatial = (SpatialFormatIterator) format;

            if (spatial.isContentAligned() &&
                    (spatial.getNumChildren() == 1) &&
                    (spatial.getChildAt(0) instanceof Grid)) {
                renderer = alignedSpatialFormatIteratorRenderer;
            } else {
                renderer = spatialFormatIteratorRenderer;
            }
        } else if (type == FormatType.TEMPORAL_FORMAT_ITERATOR) {
            renderer = temporalFormatIteratorRenderer;
        }

        if (renderer == null) {
            throw new RendererException("No valid renderer for " + format +
                                        " could be found");
        }

        return renderer;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 21-Dec-04	6402/3	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 20-Dec-04	6402/1	philws	Promoting rebuilt jar files

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
