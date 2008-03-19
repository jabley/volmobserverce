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
package com.volantis.mcs.layouts;

/**
 * Validation messages for Layouts. These are here rather than PolicyMessages
 * as PolicyMessages is not accessible from repository/api.
 */
public class LayoutMessages {

    /**
     * @i18n.message Format name '{0}' is illegal.
     * @i18n.arg 0 Format name.
     */
    public static final String FORMAT_NAME_ILLEGAL =
            "policy.variant.content.layout.format.format-name-illegal";

    /**
     * @i18n.message Format name unspecified.
     */
    public static final String FORMAT_NAME_UNSPECIFIED =
            "policy.variant.content.layout.format.format-name-unspecified";
    /**
     * @i18n.message Background color '{0}' is illegal.
     * @i18n.arg 0 Background color.
     */
    public static final String BACKGROUND_COLOR_ILLEGAL =
            "policy.variant.content.layout.format.background-color-illegal";
    /**
     * @i18n.message Background component type '{0}' is illegal.
     * @i18n.arg 0 Background component type.
     */
    public static final String BACKGROUND_COMPONENT_TYPE_ILLEGAL =
            "policy.variant.content.layout.format.background-component-type-illegal";
    /**
     * @i18n.message Border width '{0}' is illegal.
     * @i18n.arg 0 Border width.
     */
    public static final String BORDER_WIDTH_ILLEGAL =
            "policy.variant.content.layout.format.border-width-illegal";
    /**
     * @i18n.message Cell padding '{0}' is illegal.
     * @i18n.arg 0 Cell padding.
     */
    public static final String CELL_PADDING_ILLEGAL =
            "policy.variant.content.layout.format.cell-padding-illegal";
    /**
     * @i18n.message Cell spacing '{0}' is illegal.
     * @i18n.arg 0 Cell spacing.
     */
    public static final String CELL_SPACING_ILLEGAL =
            "policy.variant.content.layout.format.cell-spacing-illegal";
    /**
     * @i18n.message Height '{0}' is illegal.
     * @i18n.arg 0 Height.
     */
    public static final String HEIGHT_ILLEGAL =
            "policy.variant.content.layout.format.height-illegal";
    /**
     * @i18n.message Horizontal alignment '{0}' is illegal.
     * @i18n.arg 0 Horizontal alignment.
     */
    public static final String HORIZONTAL_ALIGNMENT_ILLEGAL =
            "policy.variant.content.layout.format.horizontal-alignment-illegal";
    /**
     * @i18n.message Vertical alignment '{0}' is illegal.
     * @i18n.arg 0 Vertical alignment.
     */
    public static final String VERTICAL_ALIGNMENT_ILLEGAL =
            "policy.variant.content.layout.format.vertical-alignment-illegal";
    /**
     * @i18n.message Width '{0}' is illegal.
     * @i18n.arg 0 Width.
     */
    public static final String WIDTH_ILLEGAL =
            "policy.variant.content.layout.format.width-illegal";
    /**
     * @i18n.message Width '{0}' is illegal.
     * @i18n.arg 0 Width.
     */
    public static final String WIDTH_UNITS_ILLEGAL =
            "policy.variant.content.layout.format.width-units-illegal";
    /**
     * @i18n.message Style class illegal '{0}' is illegal.
     * @i18n.arg 0 Style class.
     */
    public static final String STYLE_CLASS_ILLEGAL =
            "policy.variant.content.layout.format.style-class-illegal";
    /**
     * @i18n.message Filter keyboard usability '{0}' is illegal.
     * @i18n.arg 0 Filter keyboard usability.
     */
    public static final String FILTER_KEYBOARD_USABILITY_ILLEGAL =
            "policy.variant.content.layout.pane.filter-keyboard-usability-illegal";
    /**
     * @i18n.message Directionality '{0}' is illegal.
     * @i18n.arg 0 Directionality.
     */
    public static final String DIRECTIONALITY_ILLEGAL =
            "policy.variant.content.layout.directionality-illegal";
    /**
     * @i18n.message Optimization level usability '{0}' is illegal.
     * @i18n.arg 0 Optimization level usability.
     */
    public static final String OPTIMIZATION_LEVEL_ILLEGAL =
            "policy.variant.content.layout.format.optimization-level-illegal";
    /**
     * @i18n.message Next link style class '{0}' is illegal.
     * @i18n.arg 0 Next link style class.
     */
    public static final String NEXT_LINK_STYLE_CLASS_ILLEGAL =
            "policy.variant.content.layout.format.next-link-style-class-illegal";
    /**
     * @i18n.message Previous link style class '{0}' is illegal.
     * @i18n.arg 0 Previous link style class.
     */
    public static final String PREVIOUS_LINK_STYLE_CLASS_ILLEGAL =
            "policy.variant.content.layout.format.previous-link-style-class-illegal";
    /**
     * @i18n.message Maximum content size '{0}' is illegal.
     * @i18n.arg 0 Maximum content size.
     */
    public static final String MAXIMUM_CONTENT_SIZE_ILLEGAL =
            "policy.variant.content.layout.dissectingPane.maximum-content-size-illegal";
    /**
     * @i18n.message Shard link order '{0}' is illegal.
     * @i18n.arg 0 Shard link order.
     */
    public static final String SHARD_LINK_ORDER_ILLEGAL =
            "policy.variant.content.layout.dissectingPane.shard-link-order-illegal";
    /**
     * @i18n.message Indexing direction '{0}' is illegal.
     * @i18n.arg 0 Indexing direction.
     */
    public static final String INDEXING_DIRECTION_ILLEGAL =
            "policy.variant.content.layout.spatial.indexing-direction-illegal";
    /**
     * @i18n.message Rows '{0}' is illegal.
     * @i18n.arg 0 Rows.
     */
    public static final String ROWS_ILLEGAL =
            "policy.variant.content.layout.format.rows-illegal";
    /**
     * @i18n.message Rows unspecifed.
     */
    public static final String ROWS_UNSPECIFIED =
            "policy.variant.content.layout.format.rows-unspecified";
    /**
     * @i18n.message Row count '{0}' is illegal.
     * @i18n.arg 0 Row count.
     */
    public static final String ROW_COUNT_ILLEGAL =
            "policy.variant.content.layout.spatial.row-count-illegal";
    /**
     * @i18n.message Row count unspecified.
     */
    public static final String ROW_COUNT_UNSPECIFIED =
            "policy.variant.content.layout.spatial.row-count-unspecified";
    /**
     * @i18n.message  Row style classes '{0}' is illegal.
     * @i18n.arg 0 Row style classes.
     */
    public static final String ROW_STYLE_CLASSES_ILLEGAL =
            "policy.variant.content.layout.spatial.row-style-classes-illegal";
    /**
     * @i18n.message Columns '{0}' is illegal.
     * @i18n.arg 0 Columns.
     */
    public static final String COLUMNS_ILLEGAL =
            "policy.variant.content.layout.format.columns-illegal";
    /**
     * @i18n.message Columns unspecifed.
     */
    public static final String COLUMNS_UNSPECIFIED =
            "policy.variant.content.layout.format.columns-unspecified";
    /**
     * @i18n.message Column count '{0}' is illegal.
     * @i18n.arg 0 Column count.
     */
    public static final String COLUMN_COUNT_ILLEGAL =
            "policy.variant.content.layout.spatial.column-count-illegal";
    /**
     * @i18n.message Column count unspecified.
     */
    public static final String COLUMN_COUNT_UNSPECIFIED =
            "policy.variant.content.layout.spatial.column-count-unspecified";
    /**
     * @i18n.message Column style classes '{0}' is illegal.
     * @i18n.arg 0 Column style classes.
     */
    public static final String COLUMN_STYLE_CLASSES_ILLEGAL =
            "policy.variant.content.layout.spatial.column-style-classes-illegal";
    /**
     * @i18n.message Align content '{0}' is illegal.
     * @i18n.arg 0 Align content.
     */
    public static final String ALIGN_CONTENT_ILLEGAL =
            "policy.variant.content.layout.spatial.align-content-illegal";
    /**
     * @i18n.message Clock values '{0}' is illegal.
     * @i18n.arg 0 Clock values.
     */
    public static final String CLOCK_VALUES_ILLEGAL =
            "policy.variant.content.layout.temporal.clock-values-illegal";
    /**
     * @i18n.message Clock values unspecified.
     */
    public static final String CLOCK_VALUES_UNSPECIFIED =
            "policy.variant.content.layout.temporal.clock-values-unspecified";
    /**
     * @i18n.message Cells '{0}' is illegal.
     * @i18n.arg 0 Cells.
     */
    public static final String CELLS_ILLEGAL =
            "policy.variant.content.layout.temporal.cells-illegal";
    /**
     * @i18n.message Cells unspecified.
     */
    public static final String CELLS_UNSPECIFIED =
            "policy.variant.content.layout.temporal.cells-unspecified";
    /**
     * @i18n.message Cell count '{0}' is illegal.
     * @i18n.arg 0 Cell count.
     */
    public static final String CELL_COUNT_ILLEGAL =
            "policy.variant.content.layout.temporal.cell-count-illegal";
    /**
     * @i18n.message Cell count unspecified.
     */
    public static final String CELL_COUNT_UNSPECIFIED =
            "policy.variant.content.layout.temporal.cell-count-unspecified";
    /**
     * @i18n.message Allow reset '{0}' is illegal.
     * @i18n.arg 0 Allow reset.
     */
    public static final String ALLOW_RESET_ILLEGAL =
            "policy.variant.content.layout.formFragment.allow-reset-illegal";
    /**
     * @i18n.message Next link position '{0}' is illegal.
     * @i18n.arg 0 Next link position.
     */
    public static final String NEXT_LINK_POSITION_ILLEGAL =
            "policy.variant.content.layout.formFragment.next-link-position-illegal";
    /**
     * @i18n.message Previous link position '{0}' is illegal.
     * @i18n.arg 0 Previous link position.
     */
    public static final String PREVIOUS_LINK_POSITION_ILLEGAL =
            "policy.variant.content.layout.formFragment.previous-link-position-illegal";
    /**
     * @i18n.message Link style class '{0}' is illegal.
     * @i18n.arg 0 Link style class.
     */
    public static final String LINK_STYLE_CLASS_ILLEGAL =
            "policy.variant.content.layout.fragment.link-style-class-illegal";
    /**
     * @i18n.message Show peer links '{0}' is illegal.
     * @i18n.arg 0 Show peer links.
     */
    public static final String SHOW_PEER_LINKS_ILLEGAL =
            "policy.variant.content.layout.fragment.show-peer-links-illegal";
    /**
     * @i18n.message Fragment link order '{0}' is illegal.
     * @i18n.arg 0 Fragment link order.
     */
    public static final String FRAGMENT_LINK_ORDER_ILLEGAL =
            "policy.variant.content.layout.fragment.fragment-link-order-illegal";

    public static final String SOURCE_FORMAT_NAME_ILLEGAL =
            "policy.variant.content.layout.replica.source-format-name-illegal";

    public static final String SOURCE_FORMAT_NAME_UNSPECIFIED =
            "policy.variant.content.layout.replica.source-format-name-unspecified";

    public static final String SOURCE_FORMAT_TYPE_ILLEGAL =
            "policy.variant.content.layout.replica.source-format-type-illegal";

    public static final String SOURCE_FORMAT_TYPE_UNSPECIFIED =
            "policy.variant.content.layout.replica.source-format-type-unspecified";
}
