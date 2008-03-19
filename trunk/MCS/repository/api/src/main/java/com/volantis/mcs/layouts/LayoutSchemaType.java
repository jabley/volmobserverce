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

package com.volantis.mcs.layouts;

import java.util.HashMap;
import java.util.Map;

/**
 * Type safe enumeration of element names, attribute names and attribute
 * values that are used to create a layout xml representaion.
 */
public class LayoutSchemaType {

    /**
     * Map that will be used to store all <code>LayoutSchemaType</code>
     * instances, keyed on their name. This declaration and initialisation
     * must come before any static LayoutSchemaType instances.
     */
    private static Map layoutSchemaTypes = new HashMap();

    /**
     * background color attribute name.
     */
    public static final LayoutSchemaType BACKGROUND_COLOR_ATTRIBUTE =
            new LayoutSchemaType("backgroundColor");

    /**
     * border color attribute name.
     */
    public static final LayoutSchemaType BORDER_COLOR_ATTRIBUTE =
            new LayoutSchemaType("borderColor");

    /**
     * background color attribute name.
     */
    public static final LayoutSchemaType BORDER_WIDTH_ATTRIBUTE =
            new LayoutSchemaType("borderWidth");

    /**
     * generic name attribute name.
     */
    public static final LayoutSchemaType NAME_ATTRIBUTE =
            new LayoutSchemaType("name");

    /**
     * layout attribute name
     */
    public static final LayoutSchemaType LAYOUT =
            new LayoutSchemaType("layout");

    /**
     * canvasLayout attribute name
     */
    public static final LayoutSchemaType CANVAS_LAYOUT =
            new LayoutSchemaType("canvasLayout");

    /**
     * montageLayout attribute name
     */
    public static final LayoutSchemaType MONTAGE_LAYOUT =
            new LayoutSchemaType("montageLayout");

    /**
     * gridFormat element name.
     */
    public static final LayoutSchemaType GRID_FORMAT_ELEMENT =
            new LayoutSchemaType("gridFormat");

    /**
     * segmentGridFormat element name.
     */
    public static final LayoutSchemaType SEGMENT_GRID_FORMAT_ELEMENT =
            new LayoutSchemaType("segmentGridFormat");

    /**
     * gridFormatRows element name.
     */
    public static final LayoutSchemaType GRID_FORMAT_ROWS_ELEMENT =
            new LayoutSchemaType("gridFormatRow");

    /**
     * segmentGridFormatRow element name.
     */
    public static final LayoutSchemaType SEGMENT_GRID_FORMAT_ROW_ELEMENT =
            new LayoutSchemaType("segmentGridFormatRow");

    /**
     * gridFormatColumns element name.
     */
    public static final LayoutSchemaType GRID_FORMAT_COLUMNS_ELEMENT =
            new LayoutSchemaType("gridFormatColumns");

    /**
     * gridFormatColumn element name.
     */
    public static final LayoutSchemaType GRID_FORMAT_COLUMN_ELEMENT =
            new LayoutSchemaType("gridFormatColumn");

    /**
     * segmentGridFormatColumns element name.
     */
    public static final LayoutSchemaType SEGMENT_GRID_FORMAT_COLUMNS_ELEMENT =
            new LayoutSchemaType("segmentGridFormatColumns");

    /**
     * segmentGridFormatColumn element name.
     */
    public static final LayoutSchemaType SEGMENT_GRID_FORMAT_COLUMN_ELEMENT =
            new LayoutSchemaType("segmentGridFormatColumn");

    /**
     * columns attribute name.
     */
    public static final LayoutSchemaType GRID_COLUMNS_ATTRIBUTE =
            new LayoutSchemaType("columns");

    /**
     * rows attribute name.
     */
    public static final LayoutSchemaType GRID_ROWS_ATTRIBUTE =
            new LayoutSchemaType("rows");

    /**
     * width attribute name.
     */
    public static final LayoutSchemaType GRID_WIDTH_ATTRIBUTE =
            new LayoutSchemaType("width");

    /**
     * widthUnits attribute name.
     */
    public static final LayoutSchemaType GRID_WIDTH_UNITS_ATTRIBUTE =
            new LayoutSchemaType("widthUnits");

    /**
     * height attribute name.
     */
    public static final LayoutSchemaType GRID_HEIGHT_ATTRIBUTE =
            new LayoutSchemaType("height");

    /**
     * heightUnits attribute name.
     */
    public static final LayoutSchemaType GRID_HEIGHT_UNITS_ATTRIBUTE =
            new LayoutSchemaType("heightUnits");

    /**
     * columns attribute name
     */
    public static final LayoutSchemaType SPATIAL_ITERATOR_COLUMNS_ATTRIBUTE =
            new LayoutSchemaType("columns");

    /**
     * columns attribute name
     */
    public static final LayoutSchemaType SPATIAL_ITERATOR_ROWS_ATTRIBUTE =
            new LayoutSchemaType("rows");

    /**
     * style class attribute
     */
    public static final LayoutSchemaType STYLE_CLASS_ATTRIBUTE =
            new LayoutSchemaType("styleClass");

    /**
     * columns and row attribute value enumerated value "fixed"
     */
    public static final LayoutSchemaType SPATIAL_ITERATOR_CELLS_VALUE_FIXED =
            new LayoutSchemaType("fixed");

    /**
     * columns and row attribute value enumerated value "variable"
     */
    public static final LayoutSchemaType SPATIAL_ITERATOR_CELLS_VALUE_VARIABLE
            = new LayoutSchemaType("variable");

    /**
     * columnCount attribute name
     */
    public static final LayoutSchemaType
            SPATIAL_ITERATOR_COLUMN_COUNT_ATTRIBUTE =
            new LayoutSchemaType("columnCount");

    /**
     * rowCount attribute name
     */
    public static final LayoutSchemaType
            SPATIAL_ITERATOR_ROW_COUNT_ATTRIBUTE =
            new LayoutSchemaType("rowCount");

    /**
     * indexingDirection attribute name
     */
    public static final LayoutSchemaType
            SPATIAL_ITERATOR_INDEXING_DIRECTION_ATTRIBUTE =
            new LayoutSchemaType("indexingDirection");

    /**
     * indexingDirection attribute value enumerated value "across-down"
     */
    public static final LayoutSchemaType
            SPATIAL_ITERATOR_INDEXING_DIRECTION_VALUE_ACROSS_DOWN =
            new LayoutSchemaType("across-down");

    /**
     * indexingDirection attribute value enumerated value "across-down"
     */
    public static final LayoutSchemaType
            SPATIAL_ITERATOR_INDEXING_DIRECTION_VALUE_DOWN_ACROSS =
            new LayoutSchemaType("down-across");

    /**
     * A map of maps to provide access to row, column and columns element
     * names for grids and segment grids.
     */
    private final static HashMap gridNamesMap = new HashMap(2);

    /**
     * Initialise the hasmap with the element names.
     */
    static {
        final HashMap grid = new HashMap(3);
        final HashMap segmentGrid = new HashMap(3);
        grid.put("row", GRID_FORMAT_ROWS_ELEMENT.name);
        grid.put("columns", GRID_FORMAT_COLUMNS_ELEMENT.name);
        grid.put("column", GRID_FORMAT_COLUMN_ELEMENT.name);

        segmentGrid.put("row", SEGMENT_GRID_FORMAT_ROW_ELEMENT.name);
        segmentGrid.put("columns", SEGMENT_GRID_FORMAT_COLUMNS_ELEMENT.name);
        segmentGrid.put("column", SEGMENT_GRID_FORMAT_COLUMN_ELEMENT.name);

        gridNamesMap.put(GRID_FORMAT_ELEMENT, grid);
        gridNamesMap.put(SEGMENT_GRID_FORMAT_ELEMENT, segmentGrid);
    }


    /**
     * The name of the layout schema type.
     */
    private String name;

    /**
     * Creates a new <code>LayoutSchemaType</code> instance.
     * @param name the name of the layout schema type
     */
    private LayoutSchemaType(String name) {
        this.name = name;
        layoutSchemaTypes.put(name, this);
    }

    /**
     * Returns the name of the schema type.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the <code>LayoutSchemaType</code> of the given name
     *
     * @param name the name of the LayoutSchemaType instance to retrieve
     * @return the <code>LayoutSchemaType</code> of the given name or null if
     *         one does not exist.
     */
    public static LayoutSchemaType get(String name) {
        return (LayoutSchemaType) layoutSchemaTypes.get(name);
    }

    /**
     * Gets the name of the row element for the grid with the specified
     * element name.
     * @param gridElementName the name of the grid element
     * @return the name of the row element for the grid
     */
    public static String getGridRowName(String gridElementName) {
        LayoutSchemaType grid = get(gridElementName);
        Map map = (Map) gridNamesMap.get(grid);
        if (map == null) {
            throw new IllegalArgumentException("Illegal gridElementName type: " +
                    gridElementName);
        }
        return (String) map.get("row");
    }

    /**
     * Gets the name of the column element for the grid with the specified
     * element name.
     * @param gridElementName the name of the grid element
     * @return the name of the column element for the grid
     */
    public static String getGridColumnName(String gridElementName) {
        LayoutSchemaType grid = get(gridElementName);
        Map map = (Map) gridNamesMap.get(grid);
        if (map == null) {
            throw new IllegalArgumentException("Illegal gridElementName type: " +
                    gridElementName);
        }
        return (String) map.get("column");
    }

    /**
     * Gets the name of the columns element for the grid with the specified
     * element name.
     * @param gridElementName the name of the grid element
     * @return the name of the columns element for the grid
     */
    public static String getGridColumnsName(String gridElementName) {
        LayoutSchemaType grid = get(gridElementName);
        Map map = (Map) gridNamesMap.get(grid);
        if (map == null) {
            throw new IllegalArgumentException("Illegal gridElementName type: " +
                    gridElementName);
        }
        return (String) map.get("columns");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 14-May-04	4390/1	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 14-May-04	4344/1	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 22-Mar-04	3498/1	byron	VBM:2004022501 Layout: Segment colour cannot be changed

 19-Feb-04	3021/2	pcameron	VBM:2004020211 Committed for integration

 13-Feb-04	2915/2	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 04-Feb-04	2848/3	pcameron	VBM:2004020203 Added ColumnInsertActionCommand

 03-Feb-04	2815/3	byron	VBM:2003121507 Eclipse PM Layout Editor: Addressed rework issues

 03-Feb-04	2815/1	byron	VBM:2003121507 Eclipse PM Layout Editor: Format Attributes View: Column Page

 02-Feb-04	2707/3	byron	VBM:2003121506 Eclipse PM Layout Editor: Rework issues

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: Format Attributes View: Row Page

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 ===========================================================================
*/
