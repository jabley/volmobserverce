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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransTable.java,v 1.10 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002.
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Ensure that trans cells have
 *                              their parentage defined.
 * 24-Dec-02    Phil W-S        VBM:2002122402 - Update promoteCell and
 *                              preserveStyle to handle the original cell.
 * 02-Jan-03    Phil W-S        VBM:2002122401 - Update to handle table
 *                              sections. Adds the header, footer and body
 *                              properties (including fields and get/set
 *                              methods) and affects the constructor and
 *                              refactor(DOMProtocol, TransTable, Element,
 *                              Element, int, int) methods
 * 07-Jan-03    Phil W-S        VBM:2003010712 - Add the optimizationLevel
 *                              member, the canOptimize method and the
 *                              canOptimizeStyle method. Updated the
 *                              constructor to initialize the optimizationLevel
 *                              member as required and added the
 *                              OptimizationLevel nested class.
 * 08-Jan-03    Phil W-S        VBM:2003010712 - Added alternative constructor
 *                              and determineOptimizationLevel methods. Added
 *                              getPreserveStyleAttributes and processing of
 *                              them in the preserveStyle method.
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Remove defunct constructor and
 *                              update remaining constructor to take the new
 *                              protocol parameter. Remove the protocol
 *                              parameter from the process method. Update
 *                              addCell and preserveStyle. Add
 *                              getPreserveStyleAttributes and
 *                              preserveStyleAttributes, now invoked by
 *                              preserveStyle.
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Fixed previously unused
 *                              preserveStyleAttributes method needed by this
 *                              task.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 * Represents a table found when traversing a DOM. A reference to the original
 * table DOM element is maintained to allow specializations to more easily
 * transform the DOM. DOM pre-processing is actioned by the preprocess method
 * in conjunction with the TransCell class.
 * <p>The TransTable will look for a special (internal) attribute
 * on the associated DOM table element and if found it sets the associated
 * optimization level based on the attribute's value. This value is then
 * used by the {@link #canOptimize} method to determine if the table should
 * be retained or may be optimized away. This can involve invoking
 * {@link #canOptimizeStyle} to determine if the table's stylistic value is
 * insignificant and therefore will not be missed if the table is optimized
 * away. Specializations can override the latter to change the default
 * behaviour (which is determined by the constructor invoked). If no attribute
 * is found, the table is treated as a candidate for removal.</p>
 * <p>DOM transformation is actioned by the process and refactor methods in
 * conjunction with the TransCell class.</p>
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public abstract class TransTable extends TransElement {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();
    
    /**
     * Tracks the actual number of columns contained within the original DOM,
     * then tracks the effective number of columns contained in the table in
     * the transformed DOM during the transformation process.
     */
    private int cols = 0;

    /**
     * Tracks the actual number of rows contained within the original DOM,
     * then tracks the effective number of rows contained in the table in the
     * transformed DOM during the transformation process.
     */
    private int rows = 0;

    /**
     * Indicates how canOptimize should behave. Initial value controlled by
     * the constructor variants.
     * @supplierRole optimizationLevel
     */
    private OptimizationLevel optimizationLevel;

    /**
     * The original table DOM element for which the trans table was created.
     * @supplierRole table
     */
    protected Element table;

    /**
     * The DOM table header element found within the table, or null.
     *
     * @supplierRole header
     */
    private Element header;

    /**
     * The DOM table footer element found within the table, or null.
     *
     * @supplierRole footer
     */
    private Element footer;

    /**
     * The DOM table body element found within the table, or null.
     *
     * @supplierRole footer
     */
    private Element body;

    /**
     * This holds the grid of cells found within the table while analysing the
     * DOM. The grid may be empty. Each instance in the cell is a TransCell or
     * specialization thereof, as dictated by the factory in use.
     * @supplierRole cells
     * @supplierCardinality 0..*
     * @associationAsClass <{TwoDVector}>
     * @associates <{Element}>*/
    private final TwoDVector cells = new TwoDVector();

    /**
     * This holds, in reverse depth order, the direct cell children of the
     * table.
     *
     * This is used to allow a true depth-first traversal of the table
     * hierarchy during the pre-processing phase of DOM transformation.
     * @associates TransCell
     */
    private final LinkedList<TransCell> depthFirst =
            new LinkedList<TransCell>();
    
    /**
     * Type-safe enumeration of possible optimization levels. Used by
     * {@link TransTable#canOptimize} and the constructor(s). This should match the
     * optimization options available in
     * {@link com.volantis.mcs.layouts.FormatConstants}.
     */
    private static final class OptimizationLevel {
        private final String name;

        private OptimizationLevel(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static final OptimizationLevel NEVER =
            new OptimizationLevel("never");

        public static final OptimizationLevel LITTLE_IMPACT =
            new OptimizationLevel("littleimpact");

        public static final OptimizationLevel ALWAYS =
            new OptimizationLevel("always");
    }

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TransTable.class);

    /**
     * Constant values used for validating input data.
     */
    protected static final StylePercentage hundredPercent =
            STYLE_VALUE_FACTORY.getPercentage(null, 100);
    protected static final StylePercentage zeroPercent =
            STYLE_VALUE_FACTORY.getPercentage(null, 0);
    protected static final StyleLength zeroPixels =
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);
    
    /**
     * Initializes the instance from the given parameters.
     *
     * @param table             the DOM table element to be represented by this
     *                          instance
     * @param protocol          the protocol which generated the DOM to be
     *                          processed
     */
    protected TransTable(Element table, DOMProtocol protocol) {
        super(protocol);

        this.table = table;
        this.header = null;
        this.footer = null;

        // Handle the default situation where the body is implicit or where
        // the table sections are not actually supported within the protocol
        this.body = table;

        // Determine the basic optimization behaviour
        determineOptimizationLevel();

        if (logger.isDebugEnabled()) {
            logger.debug("TransTable created with " +
                         this.optimizationLevel.toString() + " optimization");
        }
    }

    /**
     * Returns the current notion of the number of columns contained within
     * the table (directly during initial DOM traversal and after DOM
     * transformation and indirectly during DOM pre-processing and during
     * transformation).
     *
     * @return number of columns in the table
     */
    public final int getCols() {
        return cols;
    }

    /**
     * Returns the current notion of the number of rows contained within the
     * table (directly during initial DOM traversal and after DOM
     * transformation and indirectly during DOM pre-processing and during
     * transformation).
     *
     * @return number of rows in the table
     */
    public final int getRows() {
        return rows;
    }

    /**
     * Returns the DOM table element for which the instance was created.
     *
     * @return the DOM table element represented by the trans table
     */
    @Override
    public final Element getElement() {
        return table;
    }

    /**
     * Allows the DOM table header element to be set or reset.
     *
     * @param header the DOM table header element, or null
     */
    public final void setHeader(Element header) {
        this.header = header;

        if (cells.getWidth() != 0) {
            logger.warn("header-set-after-row",
                    new Object[]{cells.getWidth()});
        }
    }

    /**
     * Returns the DOM table footer element for this instance. Can be null.
     *
     * @return the DOM table footer element for this instance
     */
    public final Element getFooter() {
        return footer;
    }

    /**
     * Allows the DOM table footer element to be set or reset.
     *
     * @param footer the DOM table footer element, or null
     */
    public final void setFooter(Element footer) {
        this.footer = footer;
    }

    /**
     * Allows the DOM table body element to be set or reset. Passing in null
     * will result in the body being reset to its initial state.
     *
     * @param body the DOM table body element, or null
     */
    public final void setBody(Element body) {
        if (body == null) {
            this.body = this.table;
        } else {
            this.body = body;
        }
    }

    /**
     * Updates the <code>cells</code> grid using the given element
     * encapsulated within a TransCell. If the given element has row and col
     * span attributes, these are used to "replicate" the reference to the
     * TransCell from all relevant entries in the grid.
     *
     * @param tr the DOM table row element containing the cell to add
     * @param td the DOM table cell element for which the cell is to be added
     * @param row the row index for the cell
     * @param col the column index for the cell
     *
     * @return the newly added trans cell
     */
    public final TransCell addCell(Element tr, Element td, int row, int col) {
        TransCell cell = getFactory().getCell(tr, td, row, col, protocol);
        final int rowspan = cell.getRowspan();
        final int colspan = cell.getColspan();

        // Make sure the cell knows who its parent is
        cell.setParent(this);

        // Replicate the new cell across the row and col span defined by
        // its attributes
        for (int i = 0; i < rowspan; i++) {
            for (int j = 0; j < colspan; j++) {
                cells.add(row + i, col + j, cell);
            }
        }

        if (row + rowspan > rows) {
            rows = row + rowspan;
        }

        if (col + colspan > cols) {
            cols = col + colspan;
        }

        return cell;
    }

    /**
     * Allows a nested trans cell to be queried and returned. An exception
     * may be thrown if the indicies are out of range.
     *
     * @param row the row index, starting from 0
     * @param col the column index, starting from 0
     * @return the cell at the given position in the table
     * @throws ArrayIndexOutOfBoundsException if the given position is outside
     *         the table's index bounds
     */
    public final TransCell getCell(int row, int col) {
        return (TransCell)cells.get(row, col);
    }

    /**
     * DOM pre-processing involves traversal of the trans element hierarchy
     * via the depthFirst list. On the way back up the hierarchy, any cell
     * row and column spanning requirements are calculated and the row and
     * column counts updated accordingly.
     */
    @Override
    public final void preprocess() {
        // Depth first traversal of the trans element hierarchy. Note that
        // depthFirst list must be traversed from end to start to perform
        // depth first traversal in the correct order (the list is ordered
        // shallowest to deepest)
        for (ListIterator<TransCell> i = depthFirst.listIterator(
                depthFirst.size()); i.hasPrevious(); ) {
            TransCell cell = i.previous();
            cell.preprocess();
        }

        // Prefactor rows and columns to account for row and col spanning
        // where only one nested table exists in the row or column, or
        // where multiple tables exist such that LCMs may need to be
        // calculated and applied.
        prefactorRows();
        prefactorCols();
    }

    /**
     * DOM processing involves a "left-to-right, top-to-bottom, depth first"
     * traversal of the table's content, causing nested tables to be removed
     * and their cells to be "merged" into the outer table's rows and columns.
     *
     * The traversal is "depth first" to allow nesting to be eliminated a
     * single level at a time, from the bottom up. However, unlike the
     * pre-processing, the traversal is depth first in the sense that nested
     * tables are processed before outer ones, but is keyed in a
     * "left-to-right, top-to-bottom" traversal of the table's grid to ensure
     * that new rows and columns are inserted into the table in the correct
     * (visual) order.
     *
     * Note that this method invokes refactor to do the work of transformation
     * at the current hierarchy level once deeper nested tables have been
     * processed (and therefore removed from the DOM).
     */
    @Override
    public final void process() {
        final int numRows = cells.getWidth();
        final int numCols = cells.getHeight();

        // Perform a left-to-right, top-to-bottom, depth traversal of
        // the trans element hierarchy
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                TransCell cell = (TransCell)cells.get(i, j);

                // Only visit and process any given cell once
                if ((cell != null) &&
                    (cell.getStartRow() == i) &&
                    (cell.getStartCol() == j)) {
                    cell.process();
                }
            }
        }

        // Now transform the DOM appropriately at this level
        refactor(protocol);
    }

    /**
     * Allows the table's DOM to be re-factored to remove nested table DOM
     * elements, re-working them into new immediate child table cell (and row)
     * elements. Iterates across all cells left-to-right, top-to-bottom,
     * invoking the cell's refactor method passing in this trans table.
     *
     * @param protocol the current protocol
     */
    private void refactor(DOMProtocol protocol) {
        final int numRows = cells.getWidth();
        final int numCols = cells.getHeight();

        // Now refactor the cells themselves
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {

                TransCell cell = (TransCell)cells.get(i, j);

                // Only visit a given cell once (ignore spanned repetitions)
                if ((cell != null) &&
                    (cell.getStartRow() == i) &&
                    (cell.getStartCol() == j)) {
                    cell.refactor(protocol, this);
                }
            }
        }
    }

    /**
     * The trans table's notion of depth is updated to match the greatest
     * depth indicated by contained trans cells. In addition, the contained
     * trans cells are added to the depthFirst via a call to
     * addCellToDepthFirst.
     *
     * @todo improve algorithm for getting depth first cell list
     */
    @Override
    public final void trackDepth() {
        int deepest = getDepth();
        final int cellRows = getCellRows();
        final int cellCols = getCellCols();

        for (int i = 0; i < cellRows; i++) {
            for (int j = 0; j < cellCols; j++) {
                TransCell transCell = getCell(i, j);

                if ((transCell != null) &&
                    (transCell.getStartRow() == i) &&
                    (transCell.getStartCol() == j)) {
                    final int cellDepth = transCell.getDepth();

                    if (cellDepth > deepest) {
                        deepest = cellDepth;
                    }

                    addCellToDepthFirst(transCell);
                }
            }
        }

        setDepth(deepest);
    }

    /**
     * This method is invoked by TransCell to allow a nested table to
     * "merge" its cells' content with the given outer table, starting at
     * the outer row and column referenced by the given DOM elements. The
     * number of rows and columns that should be spanned by the table's
     * content are indicated. This allows the table to add in rowspan and
     * colspan values if required to align with other nested tables in the
     * same row and/or column as this one.
     */
    final void refactor(DOMProtocol protocol,
                        TransTable outerTable,
                        Element outerRow,
                        Element outerCell,
                        int requiredRows,
                        int requiredCols) {

        String id = outerRow.getAttributeValue("id");

        if (id != null) {
            // The ID attribute is removed to simplify the duplication of the
            // row when additional rows are required
            outerRow.removeAttribute("id");
        }

        // The order in which sections should be visited is such
        // that the visual top-to-bottom ordering is as required.
        // Note that the body section will automatically be set to the
        // table itself if an implicit body exists, or the protocol
        // doesn't support table sections.

        TableTransformer transformer = new TableTransformer(protocol,
                outerRow, outerCell, requiredRows, requiredCols);

        if (header != null) {
            transformer.refactor(header);
        }

        if (body != null) {
            transformer.refactor(body);
        }

        if (footer != null) {
            transformer.refactor(footer);
        }

        if (id != null) {
            // Add the ID attribute back onto the original row
            outerRow.setAttribute("id", id);
        }

        // The original table DOM element and any remnant section/row content
        // is no longer required
        table.remove();
        table = null;
    }

    /**
     * Performs table transformation, allowing a nested table to "merge" its
     * cells' content with the given outer table, starting at the outer row
     * and column referenced by the given DOM elements. The number of rows and
     * columns that should be spanned by the table's content are indicated.
     * This allows the table to add in rowspan and colspan values if required
     * to align with other nested tables in the same row and/or column as this
     * one.
     */
    private class TableTransformer {

        private final DOMProtocol protocol;
        private final Element outerRow;
        private final Element outerCell;
        private final int requiredRows;
        private final int requiredCols;
        private final ElementHelper helper;

        private Element currentRow;
        private Element currentCell;
        private int newRowNum = 0;

        TableTransformer(DOMProtocol protocol,
                                 Element outerRow,
                                 Element outerCell,
                                 int requiredRows,
                                 int requiredCols) {
            this.protocol = protocol;
            this.outerRow = outerRow;
            this.outerCell = outerCell;
            this.requiredRows = requiredRows;
            this.requiredCols = requiredCols;
            this.helper = getFactory().getElementHelper();
            this.currentRow = outerRow;
            this.currentCell = outerCell;
        }

        /**
         * The table's cells need to be "promoted" into the outerTable's given
         * row. If this table has more than one row of its own, new rows
         * must be added to the outerTable, cloned (minus ID) from the
         * original row. These are held against the original row in an
         * array list so that other tables in the same outer row can correctly
         * intersperse their new rows.
         * If the requiredRows (or requiredCols) is greater than the number
         * of rows (or columns) in this table, these values should be used to
         * determine the spanning of the nested cells.
         *
         * @param rootElement the root element to be refactored
         */
        void refactor(Element rootElement) {

            int rowPos = 0;
            Element nextRow = null;

            // Iterate through the actual row elements within the table. This does
            // not use the nested trans cells because the trans hierarchy is not
            // updated during the DOM transformation process. This loop is formed
            // this way in order to:
            // 1. avoid an extra method call
            // 2. avoid the need for a temporary array of the section elements
            for (Element nestedRow = (Element) rootElement.getHead();
                 nestedRow != null && helper.isRow(nestedRow);
                 nestedRow = nextRow, rowPos++) {

                // we need to memorize the next row, as current row is moved
                // to different place
                nextRow = (Element) nestedRow.getNext();

                if (newRowNum != 0) {
                    // Find the best row to add to the end of or
                    // allocate a new row to start
                    Object object = outerRow.getObject();
                    ArrayList<Element> newRows;
                    int index = newRowNum;

                    if ((object == null) ||
                        !(object instanceof ArrayList)) {
                        newRows = new ArrayList<Element>(requiredRows);

                        // Add this row to the array
                        newRows.add(outerRow);

                        outerRow.setObject(newRows);
                    } else {
                        newRows = (ArrayList)object;
                    }

                    if (newRows.size() < requiredRows) {
                        // Pre-populate the array with empty rows so
                        // that the array is correctly sized for the
                        // number of rows actually needed and that
                        // rows that need spanning will be available
                        // (and empty) in the final DOM
                        DOMFactory factory = protocol.getDOMFactory();

                        newRows.ensureCapacity(requiredRows);

                        for (int i = newRows.size();
                             i < requiredRows;
                             i++) {
                            // Allocate a new row element
                            currentRow = factory.createElement();

                            // Duplicate the original row and insert
                            // the new row after the previous row (and
                            // into the newRows array).
                            currentRow.copy(outerRow);
                            currentRow.insertAfter((Element)newRows.
                                                            get(i - 1));
                            newRows.add(currentRow);
                        }
                    }

                    // See if the table's cells are actually required
                    // to span rows. If so, calculate the "final" row
                    // index required taking the row spanning into
                    // account
                    if (requiredRows > getRows()) {
                        index *= requiredRows / getRows();
                    }

                    // Find the new row into which this cell should be
                    // promoted
                    currentRow = (Element)newRows.get(index);

                    // Reset the current cell so that the nested cell
                    // will be added to the end of the current row
                    currentCell = null;
                }


                int nestedCellsNum = countChildren(nestedRow);
                int cellPos = 0;
                Element nextCell = null;

                // Now iterate left-to-right across the row's cells.
                // Again, the actual DOM content is used rather than
                // the trans hierarchy.
                // The same reasoning as above applies.
                for (Element nestedCell = (Element)nestedRow.getHead();
                     nestedCell != null;
                     nestedCell = nextCell, cellPos++) {

                    // we need to memorize the next row, as current row is moved
                    // to different place
                    nextCell = (Element) nestedCell.getNext();

                    // Re-factor this cell into the current row after
                    // the given cell
                    promoteCell(nestedCell,
                                currentRow,
                                currentCell,
                                outerCell,
                                nestedCellsNum,
                                rowPos == 0,
                                cellPos == 0);
                    currentCell = nestedCell;

                    // Handle any row and col spanning required for the
                    // newly promoted cell. This is multiplicative on
                    // whatever the spanning currently is (with spans
                    // defaulting to 1).
                    if (requiredRows > getRows()) {
                        int rowspan = TransCell.getRowspan(nestedCell);
                        rowspan *= (requiredRows / getRows());
                        TransCell.setRowspan(nestedCell, rowspan);
                    }

                    if (requiredCols > getCols()) {
                        int colspan = TransCell.getColspan(nestedCell);
                        colspan *= (requiredCols / getCols());
                        TransCell.setColspan(nestedCell, colspan);
                    }
                }

                newRowNum++;
            }
        }
    }

    /**
     * Counts the number of children for specified element.
     *
     * @param element
     * @return
     */
    private static int countChildren(Element element) {
        int childrenNum = 0;

        Node currentChild = element.getHead();

        while (currentChild != null) {
            childrenNum++;
            currentChild = currentChild.getNext();
        }

        return childrenNum;
    }

    /**
     * This method is used to ensure that stylistic information associated
     * with the current table and row and the original cell containing the
     * current table is preserved (when applicable) on the given cell DOM
     * element. The row given is the prospective new parent for the cell.
     *
     * @param cell              the cell to be promoted
     * @param outerRow          prospective new parent for the cell to be promoted
     * @param originalCell      the current table's parent cell
     * @param numberOfCells     the total number of cells to promote
     * @param firstRow          indicates if the cell being promoted is located in the first row
     * @param firstColumn       indicates if the cell being promoted is located in the first column
     * @param stylingFactory    the styling factory to use when merging Styles
     */
    final void preserveStyle(Element cell,
            Element outerRow,
            Element originalCell,
            int numberOfCells,
            boolean firstRow,
            boolean firstColumn,
            StylingFactory stylingFactory) {
        // This basic version saves the nominated attributes (if any) from the
        // 1. containing row
        // 2. optional containing table section (header, footer or body)
        // 3. (great) containing table
        // 4. original cell

        StylesMerger merger = stylingFactory.getStylesMerger();
        Styles cellStyles = cell.getStyles();
        Styles mergedStyles;

        String[] attributes = getPreserveStyleAttributes();

        Element row = cell.getParent();
        Element tableSection = row.getParent();

        // 1. containing row
        preserveStyleAttributes(cell, row, attributes);
        mergedStyles = merger.merge(cellStyles, row.getStyles());

        if (tableSection != table) {
            // 2. optional containing table section
            preserveStyleAttributes(cell, tableSection, attributes);
            mergedStyles = merger.merge(mergedStyles, tableSection.getStyles());
        }

        // 3. containing table
        preserveStyleAttributes(cell, table, attributes);


        mergedStyles = merger.merge(mergedStyles, table.getStyles());

        // 4. original cell
        preserveStyleAttributes(cell, originalCell, attributes);
        if (firstRow) {
            mergeSizeAttribute(cell, originalCell, "width",
                    StylePropertyDetails.WIDTH, numberOfCells);
        }

        if (firstColumn) {
            mergeSizeAttribute(cell, originalCell, "height",
                    StylePropertyDetails.HEIGHT, numberOfCells);
        }

        StyleValue calculatedWidth = cell.getStyles().getPropertyValues()
                .getStyleValue(StylePropertyDetails.WIDTH);
        StyleValue calculatedHeight = cell.getStyles().getPropertyValues()
                .getStyleValue(StylePropertyDetails.HEIGHT);

        mergedStyles = merger.merge(mergedStyles, originalCell.getStyles());

        // update the cell with the merged styles
        MutablePropertyValues mergedValues =
            mergedStyles.getPropertyValues();

        mergedValues.setComputedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.TABLE_CELL);

        mergedValues.setComputedValue(
                StylePropertyDetails.WIDTH, calculatedWidth);
        mergedValues.setComputedValue(
                StylePropertyDetails.HEIGHT, calculatedHeight);

        cell.setStyles(mergedStyles);
    }

    /**
     * The TransTable implementations define various attributes whose values
     * should be preserved when promoting cells. If both the cell being
     * promoted and the ancestor cell specify values, the cell value 'wins'.
     *
     * @param cell          the cell being promoted
     * @param ancestor      the ancestor of the cell being promoted
     * @param attributes    the attributes whose value should be preserved
     */
    private static void preserveStyleAttributes(Element cell,
            Element ancestor,
            String[] attributes) {
        for (String attribute : attributes) {
            // remember to ignore width and height attributes
            if (attribute.equals("width") || attribute.equals("height")) {
                continue;
            }

            String cellAttr = cell.getAttributeValue(attribute);

            if (cellAttr == null) {
                String ancestorAttr = ancestor.getAttributeValue(attribute);

                if (ancestorAttr != null) {
                    cell.setAttribute(attribute, ancestorAttr);
                }
            }
        }
    }

    /**
     * Merges "width" and "height" attributes from the outer cell (ancestor) and the cell
     * being promoted. If ancestor width is specified, and the cell width
     * is specified as percent, the width of the cell should be recalculated to be
     * a fraction of the ancestor width. If ancestor width is specified, but the cell width
     * is not specified, the ancestor width should be divided evenly between cells
     * being promoted. In all other cases the width attribute of the cell should be
     * preserved.
     *
     * @param cell
     * @param ancestor
     * @param attrName name of the attribute
     * @param numberOfCells the total number of cells to promote
     */
    private static void mergeSizeAttribute(Element cell,
            Element ancestor,
            String attrName,
            StyleProperty property,
            int numberOfCells) {

        HTMLSize ancestorSize = HTMLSize.getHTMLSize(ancestor, attrName, property);

        // If ancestor size is not set then just leave the width/height of the
        // current cell alone as there is nothing to resolve it against if it
        // is relative. However, a missing size on the ancestor is probably
        // indicative of a problem elsewhere.
        if (ancestorSize != null) {

            // Get the size on the cell.
            HTMLSize cellSize = HTMLSize.getHTMLSize(cell, attrName, property);
            if (cellSize != null) {
                if (cellSize.isPercentage()) {
                    double newSize = ancestorSize.getNumber() *
                            cellSize.getNumber() / 100.0;
                    cellSize.updateElementSize(cell, newSize);
                }
            } else {
                double newSize = ancestorSize.getNumber() / numberOfCells;
                ancestorSize.updateElementSize(cell, newSize);
            }
        }
    }

    /**
     * Returns not-null array of attributes which should be preserved.
     *
     * @return
     */
    protected abstract String[] getPreserveStyleAttributes();

    /**
     * The given table cell DOM element is "promoted" out of its current
     * parent table (this TransTable instance's table) and into the given
     * outer row (after the given outer cell, if outerCell is non-null). In
     * doing this, the cell is given the opportunity to retain stylistic
     * properties from its current parent table, its current containing
     * row and the original cell containing its current parent table
     *
     * @param cell the cell to be promoted
     * @param outerRow the new row into which the cell should be promoted
     * @param outerCell optionally, the cell in the outerRow after which
     *                  the cell should be added during promotion
     * @param originalCell the cell containing the cell's current parent table
     * @param numberOfCells total number of cells (from a given row) to promote
     * @param firstRow      indicates if the cell being promoted is located in the first row
     * @param firstColumn   indicates if the cell being promoted is located in the first column
     */
    private void promoteCell(Element cell,
            Element outerRow,
            Element outerCell,
            Element originalCell,
            int numberOfCells,
            boolean firstRow,
            boolean firstColumn) {
        // Firstly ensure that stylistic attributes are carried
        // into the cell before it is promoted up
        // @todo in consequence VBM - determine where StylingFactory should be passed around and where it should be called like this
        preserveStyle(cell, outerRow, originalCell, numberOfCells,
                firstRow, firstColumn, StylingFactory.getDefaultInstance());

        // Promote the cell up into the current row
        cell.remove();

        if (outerCell != null) {
            cell.insertAfter(outerCell);
        } else {
            cell.addToTail(outerRow);
        }
    }

    /**
     * Allows the notional number of columns contained within the table to be
     * set or updated.
     * @param cols
     */
    private void setCols(int cols) {
        this.cols = cols;
    }

    /**
     * Allows the notional number of rows contained within the table to be
     * set or updated.
     * @param rows
     */
    private void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * This method pre-factors rows, identifying those that need to have LCM
     * calculated and/or rowspans applied. A pre-factored row will have had
     * required rows set for the contained cells. In addition the extra row
     * count will be added to the current trans table's row count.
     */
    private void prefactorRows() {
        TransTable[] cellTables = new TransTable[cells.getHeight()];
        final int cellsWidth = cells.getWidth();
        final int cellsHeight = cells.getHeight();

        // @todo cells are stored in the wrong row-column order
        for (int i = 0; i < cellsWidth; i++) {
            // Firstly, get the count (and table objects) for all the tables
            // in this row
            int count = 0;

            for (int j = 0; j < cellsHeight; j++) {
                TransCell cell = (TransCell)cells.get(i, j);

                if (cell != null) {
                    TransTable table = cell.getTable();

                    // Add the table to the array if it isn't already there.
                    // The table may appear in a row more than once if it has
                    // a colspan > 1, but will only ever appear in consecutive
                    // columns
                    if ((table != null) &&
                        ((count == 0) ||
                         (cellTables[count - 1] != table))) {

                        if (table.getRows() == 0) {
                            throw new IllegalStateException(
                                "Empty tables are invalid.");
                        }

                        cellTables[count++] = table;
                    }
                }
            }

            // Using the collected information determine if LCM needs to be
            // calculated or if just rowspan should be applied. LCM will need
            // calculating if:
            // 1. there are two or more tables in the row
            // 2. two or more *dissimilar* row counts are greater than one
            if (count > 1) {
                HashMap map = new HashMap();
                int lcm = 1;

                // Let's see if LCM is required
                for (int j = 0; j < count; j++) {
                    final int rows = cellTables[j].getRows();

                    if (rows > 1) {
                        // The map is set to contain counts of tables with
                        // given numbers of rows. The latter forms the key
                        // to the map, while the former forms the value.
                        Integer key = new Integer(rows);
                        Integer value = (Integer)map.get(key);

                        if (value == null) {
                            value = new Integer(1);
                        } else {
                            value = new Integer(value.intValue() + 1);
                        }

                        map.put(key, value);

                        // In case a simple situation is found, record the
                        // maximum number of rows in any table. This is
                        // applied if the LCM calculation is unnecessary.
                        if (rows > lcm) {
                            lcm = rows;
                        }
                    }
                }

                if (map.size() > 1) {
                    // LCM needs to be calculated
                    int[] quantities = new int[map.size()];
                    int index = 0;
                    Iterator iterator = map.entrySet().iterator();

                    // Collect the set of row counts needed for the LCM
                    // calculation
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry)iterator.next();

                        quantities[index++] =
                            ((Integer)entry.getKey()).intValue();
                    }

                    // Calculate the LCM from these row counts
                    lcm = getFactory().getLCM().getLCM(quantities);
                }

                // Now apply the calculated (or derived) LCM
                for (int j = 0; j < cellsHeight; j++) {
                    TransCell cell = (TransCell)cells.get(i, j);

                    if (cell != null) {
                        cell.setRequiredRows(lcm);
                    }
                }

                // Add this row increase into the table's current size
                setRows(getRows() + lcm - 1);
            } else if (count == 1) {
                // Make sure all cells in the row
                // know to span the required number of rows
                final int rows = cellTables[0].getRows();

                for (int j = 0; j < cellsHeight; j++) {
                    TransCell cell = (TransCell)cells.get(i, j);

                    if ((cell != null)) {
                        cell.setRequiredRows(rows);
                    }
                }

                // Add this row increase into the table's current size
                setRows(getRows() + rows - 1);
            }
        }
    }

   /**
     * This method pre-factors columns, identifying those that need to have
     * LCM calculated and/or colspans applied. A pre-factored column will
     * have had required columns set for the contained cells. In addition
     * the extra column count will be added to the current trans table's
     * column count.
     */
    protected void prefactorCols() {
        TransTable[] cellTables = new TransTable[cells.getWidth()];
        final int cellsHeight = cells.getHeight();
        final int cellsWidth = cells.getWidth();

       // @todo cells are stored in the wrong row-column order
        for (int i = 0; i < cellsHeight; i++) {
            // Firstly, get the count (and table objects) for all the tables
            // in this column
            int count = 0;

            for (int j = 0; j < cellsWidth; j++) {
                TransCell cell = (TransCell)cells.get(j, i);

                if (cell != null) {
                    TransTable table = cell.getTable();

                    // Add the table to the array if it isn't already there.
                    // The table may appear in a column more than once if it
                    // has a colspan > 1, but will only ever appear in
                    // consecutive rows
                    if ((table != null) &&
                        ((count == 0) ||
                         (cellTables[count - 1] != table))) {

                        if (table.getCols() == 0) {
                            throw new IllegalStateException(
                                "Empty tables are invalid.");
                        }

                        cellTables[count++] = table;
                    }
                }
            }

            // Using the collected information determine if LCM needs to be
            // calculated or if just colspan should be applied. LCM will need
            // calculating if:
            // 1. there are two or more tables in the column
            // 2. two or more *dissimilar* column counts are greater than one
            if (count > 1) {
                HashMap map = new HashMap();
                int lcm = 1;

                // Let's see if LCM is required
                for (int j = 0; j < count; j++) {
                    final int cols = cellTables[j].getCols();

                    if (cols > 1) {
                        // The map is set to contain counts of tables with
                        // given numbers of rows. The latter forms the key
                        // to the map, while the former forms the value.
                        Integer key = new Integer(cols);
                        Integer value = (Integer)map.get(key);

                        if (value == null) {
                            value = new Integer(1);
                        } else {
                            value = new Integer(value.intValue() + 1);
                        }

                        map.put(key, value);

                        // In case a simple situation is found, record the
                        // maximum number of columns in any table. This is
                        // applied if the LCM calculation is unnecessary.
                        if (cols > lcm) {
                            lcm = cols;
                        }
                    }
                }

                if (map.size() > 1) {
                    // LCM needs to be calculated
                    int[] quantities = new int[map.size()];
                    int index = 0;
                    Iterator iterator = map.entrySet().iterator();

                    // Collect the set of column counts needed for the LCM
                    // calculation
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry)iterator.next();

                        quantities[index++] =
                            ((Integer)entry.getKey()).intValue();
                    }

                    // Calculate the LCM from these row counts
                    lcm = getFactory().getLCM().getLCM(quantities);
                }

                // Now apply the calculated (or derived) LCM
                for (int j = 0; j < cellsWidth; j++) {
                    TransCell cell = (TransCell)cells.get(j, i);

                    if (cell != null) {
                        cell.setRequiredCols(lcm);
                    }
                }

                // Add this column increase into the table's current size
                setCols(getCols() + lcm - 1);
            } else if (count == 1) {
                // Make sure all cells in the column
                // know to span the required number of columns
                final int cols = cellTables[0].getCols();

                for (int j = 0; j < cellsWidth; j++) {
                    TransCell cell = (TransCell)cells.get(j, i);

                    if ((cell != null)) {
                        cell.setRequiredCols(cols);
                    }
                }

                // Add this column increase into the table's current size
                setCols(getCols() + cols - 1);
            }
        }
    }

    /**
     * The given cell, assumed to be a child of this TransTable instance, is
     * added, in reverse depth order to the depthFirst list.
     *
     * @todo improve algorithm for handling depth first ordering
     */
    private void addCellToDepthFirst(TransCell cell) {
        int index = -1;
        final int size = depthFirst.size();

        for (int i = 0; (index == -1) && (i < size); i++) {
            TransCell other = (TransCell)depthFirst.get(i);

            if (other.getDepth() >= cell.getDepth()) {
                index = i;
            }
        }

        if (index == -1) {
            depthFirst.add(cell);
        } else {
            depthFirst.add(index, cell);
        }
    }

    /**
     * Returns the number of rows contained in the cells 2D vector.
     */
    public int getCellRows() {
        return cells.getWidth();
    }

    /**
     * Returns the number of columns contained in the cells 2D vector.
     */
    public int getCellCols() {
        return cells.getHeight();
    }

    /**
     * Determine the optimisation level which should be used for this table.
     * <p>
     * If the underlying protocol does not support nested tables, this will be
     * fixed to ALWAYS. If it does support nested tables, then it defaults to
     * NEVER and will be overridden by any "hint" specified by the synthetic
     * optimisation attribute in the table markup.
     */
    private void determineOptimizationLevel() {

        if (!protocol.supportsNestedTables()) {
            // If we do not support nested tables then we must always optimise
            // tables, regardless of any "hint" from the markup.
            optimizationLevel = OptimizationLevel.ALWAYS;
        } else {
            // If we support nested tables then by default we should not try
            // and optimise them.
            optimizationLevel = OptimizationLevel.NEVER;

            // Also if we support nested tables then the layout or protocol
            // may specify an optimisation "hint" via a special attribute. So,
            // check for the attribute and set the optimisation level
            // appropriately if found.
            if (table != null) {
                String optimization = table.getAttributeValue(
                        OptimizationConstants.OPTIMIZATION_ATTRIBUTE);
                if (optimization != null) {
                    if (OptimizationConstants.OPTIMIZE_NEVER
                            .equals(optimization)) {
                        optimizationLevel = OptimizationLevel.NEVER;
                    } else if (OptimizationConstants.OPTIMIZE_ALWAYS
                            .equals(optimization)) {
                        optimizationLevel = OptimizationLevel.ALWAYS;
                    } else if (OptimizationConstants.OPTIMIZE_LITTLE_IMPACT
                            .equals(optimization)) {
                        optimizationLevel = OptimizationLevel.LITTLE_IMPACT;
                    } else {
                        logger.warn("unknown-optimization-level",
                                new Object[]{optimization});
                    }
                }
            }
        }

        // Remove the any optimisation attribute present regardless of whether
        // we used it or not; it must not propagate through to the output DOM
        if (table != null) {
            table.removeAttribute(
                    OptimizationConstants.OPTIMIZATION_ATTRIBUTE);
        }

    }

    /**
     * Indicates whether the table represented by this TransTable can be
     * optimized away or not.
     *
     * @param containingCell the TransCell representing the DOM table cell
     *                       element that contains the DOM table element
     *                       represented by this TransTable. May be null.
     * @return true if the table can be optimized away
     */
    public final boolean canOptimize(TransCell containingCell) {
        boolean result = true;

        if (optimizationLevel == OptimizationLevel.NEVER) {
            result = false;
        } else if (optimizationLevel == OptimizationLevel.ALWAYS) {
            result = true;
        } else if (optimizationLevel == OptimizationLevel.LITTLE_IMPACT) {
            result = canOptimizeStyle(containingCell);
        }

        return result;
    }

    /**
     * Indicates whether the table represented by this TransTable can be
     * optimized away when stylistic values must be taken into account.
     *
     * @param containingCell the TransCell representing the DOM table cell
     *                       element that contains the DOM table element
     *                       represented by this TransTable. May be null.
     * @return true if the table can be optimized away when stylistic values
     *         are taken into account
     * @see #canOptimize
     */
    protected boolean canOptimizeStyle(TransCell containingCell) {
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10743/1	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 09-Dec-05	10719/1	ianw	VBM:2005113021 Fixed up promotion of table's width 100% to set value to auto

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 22-Jul-05	8859/4	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
