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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.actions.layout;

/**
 * A typesafe enumeration.
 */
public class ActionID {
    /**
     * A prefix to put on the front of the resource prefixes.
     */
    private static final String PREFIX_PREFIX = "Action."; //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole CANVAS_NEW_MENU
     */
    public static final ActionID CANVAS_NEW_MENU =
                new ActionID("newMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #CANVAS_NEW_MENU}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole MONTAGE_NEW_MENU
     */
    public static final ActionID MONTAGE_NEW_MENU =
                new ActionID("newMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #CANVAS_NEW_MENU}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole UNDEFINED_NEW_MENU
     */
    public static final ActionID UNDEFINED_NEW_MENU =
                new ActionID("newMenu"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_PANE_MENU
     */
    public static final ActionID NEW_PANE_MENU =
                new ActionID("newPaneMenu"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_PANE
     */
    public static final ActionID NEW_PANE =
        new ActionID("newPane"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_ROW_ITERATOR_PANE
     */
    public static final ActionID NEW_ROW_ITERATOR_PANE =
        new ActionID("newRowIteratorPane"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_COLUMN_ITERATOR_PANE
     */
    public static final ActionID NEW_COLUMN_ITERATOR_PANE =
        new ActionID("newColumnIteratorPane"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_DISSECTING_PANE
     */
    public static final ActionID NEW_DISSECTING_PANE =
        new ActionID("newDissectingPane"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_ITERATOR_MENU
     */
    public static final ActionID NEW_ITERATOR_MENU =
        new ActionID("newIteratorMenu"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_SPATIAL_ITERATOR
     */
    public static final ActionID NEW_SPATIAL_ITERATOR =
        new ActionID("newSpatialIterator"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_TEMPORAL_ITERATOR
     */
    public static final ActionID NEW_TEMPORAL_ITERATOR =
        new ActionID("newTemporalIterator"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_GRID_MENU
     */
    public static final ActionID NEW_GRID_MENU =
        new ActionID("newGridMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_GRID_MENU}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_SEGMENT_GRID_MENU
     */
    public static final ActionID NEW_SEGMENT_GRID_MENU =
        new ActionID("newGridMenu"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_2_COLUMN_GRID
     */
    public static final ActionID NEW_2_COLUMN_GRID =
        new ActionID("new2ColumnGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_3_ROW_GRID
     */
    public static final ActionID NEW_3_ROW_GRID =
        new ActionID("new3RowGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_N_BY_M_GRID
     */
    public static final ActionID NEW_N_BY_M_GRID =
        new ActionID("newNByMGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_2_COLUMN_SEGMENT_GRID
     */
    public static final ActionID NEW_2_COLUMN_SEGMENT_GRID =
        new ActionID("new2ColumnSegmentGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_3_ROW_SEGMENT_GRID
     */
    public static final ActionID NEW_3_ROW_SEGMENT_GRID =
        new ActionID("new3RowSegmentGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_N_BY_M_SEGMENT_GRID
     */
    public static final ActionID NEW_N_BY_M_SEGMENT_GRID =
        new ActionID("newNByMSegmentGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_FRAGMENT
     */
    public static final ActionID NEW_FRAGMENT =
        new ActionID("newFragment"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_FORM
     */
    public static final ActionID NEW_FORM =
        new ActionID("newForm"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_REGION
     */
    public static final ActionID NEW_REGION =
        new ActionID("newRegion"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_REPLICA
     */
    public static final ActionID NEW_REPLICA =
        new ActionID("newReplica"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_FORM_FRAGMENT
     */
    public static final ActionID NEW_FORM_FRAGMENT =
        new ActionID("newFormFragment"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole NEW_SEGMENT
     */
    public static final ActionID NEW_SEGMENT =
        new ActionID("newSegment"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole CANVAS_WRAP_MENU
     */
    public static final ActionID CANVAS_WRAP_MENU =
                new ActionID("wrapMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #CANVAS_WRAP_MENU}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole MONTAGE_WRAP_MENU
     */
    public static final ActionID MONTAGE_WRAP_MENU =
                new ActionID("wrapMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #CANVAS_WRAP_MENU}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole UNDEFINED_WRAP_MENU
     */
    public static final ActionID UNDEFINED_WRAP_MENU =
                new ActionID("wrapMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_ITERATOR_MENU}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_ITERATOR_MENU
     */
    public static final ActionID WRAP_ITERATOR_MENU =
        new ActionID("newIteratorMenu"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_SPATIAL_ITERATOR}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_SPATIAL_ITERATOR
     */
    public static final ActionID WRAP_SPATIAL_ITERATOR =
        new ActionID("newSpatialIterator"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_TEMPORAL_ITERATOR}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_TEMPORAL_ITERATOR
     */
    public static final ActionID WRAP_TEMPORAL_ITERATOR =
        new ActionID("newTemporalIterator"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_N_BY_M_GRID
     */
    public static final ActionID WRAP_N_BY_M_GRID =
        new ActionID("wrapNByMGrid"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_N_BY_M_SEGMENT_GRID
     */
    public static final ActionID WRAP_N_BY_M_SEGMENT_GRID =
        new ActionID("wrapNByMSegmentGrid"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_FRAGMENT}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_FRAGMENT
     */
    public static final ActionID WRAP_FRAGMENT =
        new ActionID("newFragment"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_FORM}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_FORM
     */
    public static final ActionID WRAP_FORM =
        new ActionID("newForm"); //$NON-NLS-1$

    /**
     * Note that the resource prefix is intentionally the same as {@link
     * #NEW_FORM_FRAGMENT}'s because both have the visualization.
     *
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole WRAP_FORM_FRAGMENT
     */
    public static final ActionID WRAP_FORM_FRAGMENT =
        new ActionID("newFormFragment"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole CUT
     */
    public static final ActionID CUT =
        new ActionID("cut"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole COPY
     */
    public static final ActionID COPY =
        new ActionID("copy"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole PASTE
     */
    public static final ActionID PASTE =
        new ActionID("paste"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole DELETE
     */
    public static final ActionID DELETE =
        new ActionID("delete"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole REPLACE
     */
    public static final ActionID REPLACE =
        new ActionID("replace"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole SWAP
     */
    public static final ActionID SWAP =
        new ActionID("swap"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole GRID_MODIFY_MENU
     */
    public static final ActionID GRID_MODIFY_MENU =
        new ActionID("gridModifyMenu"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole INSERT_COLUMNS
     */
    public static final ActionID INSERT_COLUMNS =
        new ActionID("insertColumns"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole DELETE_COLUMN
     */
    public static final ActionID DELETE_COLUMN =
        new ActionID("deleteColumn"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole INSERT_ROWS
     */
    public static final ActionID INSERT_ROWS =
        new ActionID("insertRows"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole DELETE_ROW
     */
    public static final ActionID DELETE_ROW =
        new ActionID("deleteRow"); //$NON-NLS-1$

    /**
     * @link aggregationByValue
     * @supplierCardinality 1
     * @supplierRole SHOW_ATTRIBUTE_VIEW
     */
    public static final ActionID SHOW_ATTRIBUTE_VIEW =
        new ActionID("showAttributeView"); //$NON-NLS-1$

    /**
     * The internal name for the enumeration literal.
     */
    private final String name;

    /**
     * The resource prefix associated with the enumeration literal.
     */
    private final String prefix;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the internal name for the new literal
     */
    private ActionID(String name) {
        this.name = name;
        this.prefix = new StringBuffer(PREFIX_PREFIX).append(name).append('.').
            toString();
    }

    /**
     * Returns the resource prefix for the enumeration literal.
     *
     * @return resource prefix for the enumeration literal
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns the internal name for the enumeration literal. This must not
     * be used for presentation purposes.
     *
     * @return internal name for the enumeration literal
     */
    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2797/1	philws	VBM:2004012903 Make the layout editor context menu device layout type sensitive

 28-Jan-04	2783/1	philws	VBM:2003121514 Implement Wrap Actions for the Layout Editor context menu

 23-Jan-04	2727/1	philws	VBM:2004012301 First draft of new, delete and clipboard actions in context menu

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 ===========================================================================
*/
