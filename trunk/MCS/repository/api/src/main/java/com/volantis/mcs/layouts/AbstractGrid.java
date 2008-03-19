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
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-00    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Renamed from GridFormat and
 *                              added visit method.
 * 09-Jul-01    Paul            VBM:2001062810 - Sorted out the background
 *                              image retrieval and changed the visit method
 *                              to return a boolean.
 * 23-Jul-01    Paul            VBM:2001070507 - Renamed setBorder method in
 *                              GridAttributes to setBorderWidth so that it
 *                              more closely matches its use.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 30-Jul-01    Paul            VBM:2001071609 - Removed some unnecessary code.
 * 02-Aug-01    Allan           VBM:2001072604 - Replaced openComment() -
 *                              comment - closeComment() with
 *                              doComment(comment).
 * 10-Aug-01    Paul            VBM:2001072505 - Allow subclasses to modify
 *                              the created rows and columns, this is mainly
 *                              to allow SegmentGrid to change the default
 *                              setting for the height units attribute in
 *                              row from pixels to percent.
 * 14-Aug-01    Payal           VBM:2001080803 - Height property added to Grid,
 *                              added HEIGHT_ATTRIBUTE and getHeight method
 *                              to get the Height attribute.
 * 17-Aug-01    Allan           VBM:2001081612 - Added set units for height
 *                              and width to writeChildPreamble()
 * 14-Sep-01    Allan           VBM:2001091103 - Override setParent() to
 *                              set the hasGridChildren property in the parent.
 *                              Modified the write...Preamble and
 *                              write...Postamble methods to set the parent
 *                              format on the attributes in use.
 * 18-Sep-01    Allan           VBM:2001091103 - No need for hasGridChildren
 *                              so removing the reference to it
 * 01-Oct-01    Doug            VBM:2001092501 - now use the Format.java
 *                              method getBestBackgroundImage to calculate the
 *                              background image url. Removed the methods
 *                              getBackgroundImage and setBackroundImage. Added
 *                              Format.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE
 *                              to the userAttributes and defaultAttributes
 *                              arrays
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 28-Jan-02    Steve           VBM:2002011412 - Added name property so grids
 *                              can be accessed by replicas.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Apr-02    Paul            VBM:2002042205 - Made sure that Grid
 *                              initialised width properly.
 * 21-May-02    Allan           VBM:2002052302 - Removed previous fix to
 *                              widths that have no value being set to 100%.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 03-Jan-03    Phil W-S        VBM:2002122404 - Add the OPTIMIZATION_LEVEL
 *                              attribute.
 * 20-Feb-03    Allan           VBM:2003021803 - Modified hasMoreElements() in
 *                              getSubComponentInfo() to check for null
 *                              rows/columns before trying to use them.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 27-Mar-03    Allan           VBM:2003030603 - Added clone().
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * A format that provides a grid.
 *
 * @mock.generate base="Format"
 */
public abstract class AbstractGrid
        extends Format
        implements StyleAttributes, OptimizationLevelAttribute {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractGrid.class);

    public static final String ROW_FORMAT_TYPE = "Grid.Row";
    public static final String COLUMN_FORMAT_TYPE = "Grid.Column";

    public static final String TWO_COLUMN_FORMAT_TYPE = "TwoColumn";
    public static final String THREE_ROW_FORMAT_TYPE = "ThreeRow";
    public static final String THREE_BY_TWO_FORMAT_TYPE = "ThreeByTwo";

    public static final int threeByTwoRows = 20;
    public static final int threeByTwoColumns = 2;

    public static final String COLUMNS_ATTRIBUTE = "Columns";
    public static final String ROWS_ATTRIBUTE = "Rows";

    private Row[] rows;
    private Column[] columns;

    /**
     * Create a new AbstractGrid format.
     *
     * @param layout The Layout to which this format belongs
     */
    public AbstractGrid(Layout layout) {
        super(0, layout);
    }

    /**
     * Set the parent format of this object. Since this is a grid also set the
     * flag in the parent that says the parent has grid children
     * @param parent Format to set to the parent of this grid.
     */
    public void setParent(Format parent) {
        this.parent = parent;

        if (parent != null) {
            // Inherit the Layout from the parent if necessary.
            if (getLayout() == null) {
                setDeviceLayout(parent.getLayout());
            }
        }
    }

    public void setRows(int rows) {
        final String value = String.valueOf(rows);
        setRows(value);
    }

    public void setRows(final String value) {
        setAttribute(ROWS_ATTRIBUTE, value);
    }

    public int getRows() {
        String s = getRowsString();
        return (s == null) ? -1 : Integer.parseInt(s);
    }

    public String getRowsString() {
        return (String) getAttribute(ROWS_ATTRIBUTE);
    }

    public void setColumns(int columns) {
        final String value = String.valueOf(columns);
        setColumns(value);
    }

    public void setColumns(final String value) {
        setAttribute(COLUMNS_ATTRIBUTE, value);
    }

    // javadoc inherited
    public int getColumns() {
        String s = getColumnsString();
        return (s == null) ? -1 : Integer.parseInt(s);
    }

    public String getColumnsString() {
        return (String) getAttribute(COLUMNS_ATTRIBUTE);
    }

    public void setBorderColour(String borderColour) {
        // do nothing
    }

    public String getBorderColour() {
        return null;
    }

    public String getOptimizationLevel() {
        return (String)
                getAttribute(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE);
    }

    public void setOptimizationLevel(String optimizationLevel) {
        setAttribute(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
                     optimizationLevel);
    }

    /**
     * Return the style class associated with this <code>Grid</code>.
     * @return the style class associated with this <code>Grid</code>.
     */
    public String getStyleClass() {
        return (String) getAttribute(FormatConstants.STYLE_CLASS);
    }

    /**
     * Sets the style class associated with this <code>Grid</code>.
     * @param styleClass the style class.
     */
    public void setStyleClass(String styleClass) {
        setAttribute(FormatConstants.STYLE_CLASS,
                     styleClass);
    }

    public Row getRow(int index) {
        return rows[index];
    }

    public Column getColumn(int index) {
        return columns[index];
    }

    public Format getChildAt(int row, int column) {
        return getChildAt(row * getColumns() + column);
    }

    public void insertRow(int row) {
        Row[] newRows = new Row[rows.length + 1];
        for (int i = 0; i < row; i += 1) {
            newRows[i] = rows[i];
        }
        newRows[row] = createRow();
        for (int i = row; i < rows.length; i += 1) {
            newRows[i + 1] = rows[i];
        }
        rows = newRows;
    }

    public void deleteRow(int row) {
        Row[] newRows = new Row[rows.length - 1];

        for (int i = 0; i < row; i += 1) {
            newRows[i] = rows[i];
        }
        for (int i = row + 1; i < rows.length; i += 1) {
            newRows[i - 1] = rows[i];
        }
        rows = newRows;
    }

    public void insertColumn(int column) {
        Column[] newColumns = new Column[columns.length + 1];
        for (int i = 0; i < column; i += 1) {
            newColumns[i] = columns[i];
        }
        newColumns[column] = createColumn();
        for (int i = column; i < columns.length; i += 1) {
            newColumns[i + 1] = columns[i];
        }
        columns = newColumns;
    }

    public void deleteColumn(int column) {
        Column[] newColumns = new Column[columns.length - 1];

        for (int i = 0; i < column; i += 1) {
            newColumns[i] = columns[i];
        }
        for (int i = column + 1; i < columns.length; i += 1) {
            newColumns[i - 1] = columns[i];
        }
        columns = newColumns;
    }

    // javadoc inherited.
    public void setAttribute(String key, Object value) {
        if (COLUMNS_ATTRIBUTE.equals(key)) {
            //this.columns = Integer.parseInt ((String) value);
        } else if (ROWS_ATTRIBUTE.equals(key)) {
            //this.rows = Integer.parseInt ((String) value);
        }

        super.setAttribute(key, value);
    }

    // javadoc inherited.
    public void attributesHaveBeenSet() {

        if (children == null) {
            setNumChildren(getRows() * getColumns());

            createRowsAndColumns();
        }
    }

    private void createRowsAndColumns() {

        int columnCount = getColumns();
        int rowCount = getRows();
        if (logger.isDebugEnabled()) {
            logger.debug("Initialising rows: rowCount=" + rowCount);
        }
        if (rowCount > 0) {
            rows = new Row[rowCount];
            for (int i = 0; i < rows.length; i += 1) {
                rows[i] = createRow();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising columns: colCount=" + columnCount);
        }
        if (columnCount > 0) {
            columns = new Column[columnCount];
            for (int i = 0; i < columns.length; i += 1) {
                columns[i] = createColumn();
            }
        }
    }

    protected Row createRow() {
        return new Row(this);
    }

    protected Column createColumn() {
        return new Column(this);
    }

    // javadoc inherited.
    public SimpleAttributeContainer createSubComponent(String type,
                                                       int childIndex) {

        SimpleAttributeContainer attributes = null;

        if (ROW_FORMAT_TYPE.equals(type)) {
            Row row = createRow();
            attributes = row;
            rows[childIndex] = row;
        } else if (COLUMN_FORMAT_TYPE.equals(type)) {
            Column column = createColumn();
            attributes = column;
            columns[childIndex] = column;
        }

        return attributes;
    }

    // javadoc inherited.
    public Enumeration getSubComponentInfo() {
        return new Enumeration() {
            private int count = 0;

            /**
             * Implementation of hasMoreElements().
             * @return true if count is less than the number of rows plus the
             * number of columns and neither rows or columns is null; otherwise
             * false.
             */
            public boolean hasMoreElements() {
                boolean more = rows != null && columns != null;
                if (more) {
                    more = count < rows.length + columns.length;
                }
                return more;
            }

            public Object nextElement() {
                SubComponentInfo result;
                int childIndex;

                if (count < rows.length) {
                    childIndex = count;
                    result = new SubComponentInfo(rows[childIndex],
                                                  ROW_FORMAT_TYPE,
                                                  childIndex);
                } else if (count < rows.length + columns.length) {
                    childIndex = count - rows.length;
                    result = new SubComponentInfo(columns[childIndex],
                                                  COLUMN_FORMAT_TYPE,
                                                  childIndex);
                } else {
                    throw new NoSuchElementException("Grid SubType Enumeration");
                }

                count += 1;
                return result;
            }
        };
    }

    // javadoc inherited.
    public String getName() {
        return (String) getAttribute(FormatConstants.NAME_ATTRIBUTE);
    }

    // javadoc inherited.
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;

        final AbstractGrid grid = (AbstractGrid) other;
        return Arrays.equals(columns, grid.columns) &&
            Arrays.equals(rows, grid.rows);
    }

    // javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (rows != null ? rows.hashCode() : 0);
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9652/1	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 29-Sep-05	9590/3	schaloner	VBM:2005092204 Added width, height, and style accessor interfaces derived from CoreAttributes interface

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 18-Feb-05	7037/1	pcameron	VBM:2005021704 Width units default to percent if not present

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 08-Oct-04	5758/1	byron	VBM:2004100804 Support style classes on grids and spatial format iterators: Common

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
