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

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jdom.Element;

/**
 * Provides a lazily populated source of prototypical ODOM structures
 * equivalent to requested {@link FormatType}s.
 *
 * <p><strong>NOTE</strong>: the prototypical document fragments must use
 * element names and structure that match the schema definition. This is only
 * impacted in this code where grid-based prototypes are generated.</p>
 */
public class FormatPrototype {
    /**
     * The set of existing prototypes. Keyed on the {@link FormatType}, mapping
     * to the associated prototype.
     *
     * @associates <{ODOMElement}>
     * @supplierRole prototypes
     * @supplierCardinality 1
     * @link aggregationByValue
     */
    private static final HashMap prototypes = new HashMap();

    /**
     * This factory instance is used to construct all the ODOM elements for
     * the prototypes. This is package accessible.
     */
    static final ODOMFactory factory = new LPDMODOMFactory();

    /**
     * Returns a clone of the prototype associated with the given {@link
     * FormatType}.
     *
     * @param formatType the format type for which the prototype is required
     * @return a clone of the given formatType's prototype
     */
    public static synchronized ODOMElement get(
        FormatType formatType) {
        if (formatType == null) {
            throw new IllegalArgumentException(
                "A non-null format type is required"); //$NON-NLS-1$
        }

        ODOMElement prototype = (ODOMElement)prototypes.get(formatType);

        if (prototype == null) {
            FormatType.Structure structure = formatType.getStructure();
            String name = formatType.getElementName();

            // Double check that things won't go horribly wrong (simple
            // and grid containers both need to reference the empty format,
            // so the latter must be a leaf format)
            if ((formatType == FormatType.EMPTY) &&
                structure != FormatType.Structure.LEAF) {
                throw new IllegalStateException(
                            "The empty format must be a leaf " + //$NON-NLS-1$
                            "structured format"); //$NON-NLS-1$
            }

            // Create the main element for the prototype
            prototype = (ODOMElement)factory.element(name);

            if (structure == FormatType.Structure.LEAF) {
                // Leaf formats have no internal structure
            } else if (structure == FormatType.Structure.SIMPLE_CONTAINER) {
                // Simple containers have an empty format placeholder for child
                // creation
                prototype.addContent(get(FormatType.EMPTY));

                // The following code block inserts default values for new
                // Formats.  This will eventually be required for all formats.
                // However at the moment we only set the following required
                // fields of the Spatial Iterators....
                // - 2D Indexing Direction
                // - Row Iterations
                // - Column Iterations
                //
                // When this is extended to other formats the code should
                // almost certainly be refactored out of this method.
                //
                // Setting defaults allows the user to build a layout without
                // having to explicitly set all required fields, reducing the
                // level of error feedback and facilitating faster layout
                // construction.
                if (formatType == FormatType.SPATIAL_FORMAT_ITERATOR) {
                    prototype.setAttribute(
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_COLUMNS_ATTRIBUTE.getName(),
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_CELLS_VALUE_VARIABLE.getName());

                    prototype.setAttribute(
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_COLUMN_COUNT_ATTRIBUTE.getName(),
                            "0");

                    prototype.setAttribute(
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_ROWS_ATTRIBUTE.getName(),
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_CELLS_VALUE_VARIABLE.getName());

                    prototype.setAttribute(
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_ROW_COUNT_ATTRIBUTE.getName(),
                            "0");

                    prototype.setAttribute(
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_INDEXING_DIRECTION_ATTRIBUTE.
                            getName(),
                            LayoutSchemaType.
                            SPATIAL_ITERATOR_INDEXING_DIRECTION_VALUE_ACROSS_DOWN.
                            getName());
                }
            } else if (structure == FormatType.Structure.GRID) {
                // Create the complex prototype structure based on the grid
                // format's name
                String gridName = prototype.getName();
                String colName = LayoutSchemaType.getGridColumnName(gridName);
                String colsName = LayoutSchemaType.getGridColumnsName(gridName);
                String rowName = LayoutSchemaType.getGridRowName(gridName);

                ODOMElement col = (ODOMElement)factory.element(colName);
                ODOMElement cols = (ODOMElement)factory.element(colsName);
                ODOMElement row = (ODOMElement)factory.element(rowName);

                prototype.addContent(cols);
                cols.addContent(col);
                prototype.addContent(row);
                row.addContent(get(FormatType.EMPTY));
            } else {
                throw new IllegalArgumentException(
                    "The given format type (" + //$NON-NLS-1$
                    formatType.getTypeName() +
                    ") has an unknown structure (" + //$NON-NLS-1$
                    structure.toString() + ")"); //$NON-NLS-1$
            }

            prototypes.put(formatType, prototype);
        }

        return (ODOMElement)prototype.clone();
    }

    /**
     * Returns a copy of the format type's associated prototype with the
     * specified number of rows and columns. The first cell content may be
     * explicitly specified using the <dfn>firstCell</dfn> parameter. If
     * non-null, this element will be automatically detached from any existing
     * parent and will be inserted into the returned grid at the first row/
     * column location.
     *
     * The prototype must contain the following basic structure:
     *
     * <pre>
     *     &lt;<i>prefix</i>Format&gt;
     *         &lt;<i>prefix</i>FormatColumns&gt;
     *             &lt;<i>prefix</i>FormatColumn/&gt;
     *         &lt;/<i>prefix</i>FormatColumns&gt;
     *         &lt;<i>prefix</i>FormatRow&gt;
     *             &lt;emptyFormat/&gt;
     *         &lt;/<i>prefix</i>FormatRow&gt;
     *     &lt;/<i>prefix</i>Format&gt;
     * </pre>
     *
     * The <i>prefix</i>FormatColumn and emptyFormat nodes will be duplicated
     * for the number of columns required and the <i>prefix</i>FormatRow node
     * (with duplicated number of emptyFormats) will be duplicated for the
     * number of rows required.
     *
     * <p>NB: the content will not be checked to have the correct name suffixes
     * here - the duplication will be performed on specifically positioned
     * elements within the prototypical element hierarchy. Thus, the prototype
     * created by {@link #get} must follow the above structure.
     *
     * @param formatType the (grid) format type for which a sized prototype is
     *                   to be created
     * @param firstCell  an optional element which will replace the empty
     *                   format in the first cell of the new grid. If specified
     *                   the number of rows and columns will be forced to be
     *                   at least one.
     * @param rowCount   indicates the number of rows that are required. May be
     *                   zero or more
     * @param colCount   indicates the number of columns that are required. May
     *                   be zero or more
     * @param monitor    progress monitor for measuring grid creation progress
     *                   null parameter is checked so it can be null                   
     * @return a new element structure based on the prototype and the given
     *         dimensions
     */
    public static ODOMElement createSizedGrid(FormatType formatType,
                                              Element firstCell,
                                              int rowCount,
                                              int colCount,
                                              IProgressMonitor monitor) {
        ODOMElement grid = FormatPrototype.get(formatType);
        ODOMElement cols = (ODOMElement)grid.getContent().get(0);
        ODOMElement col = (ODOMElement)cols.getContent().get(0);
        ODOMElement row = (ODOMElement)grid.getContent().get(1);
        ODOMElement placeholder = (ODOMElement)row.getContent().get(0);

        if (firstCell != null) {
            colCount = Math.max(1, colCount);
            rowCount = Math.max(1, rowCount);
        }

        if (colCount < 0) {
            throw new IllegalArgumentException(
                "A zero or positive number of columns is " + //$NON-NLS-1$
                "required (" + //$NON-NLS-1$
                colCount +
                " specified)"); //$NON-NLS-1$
        } else if (colCount == 0) {
            // remove the elements that represent columns or column content
            col.detach();
            placeholder.detach();
        } else {
            ODOMElement newCol;
            ODOMElement newPlaceholder;

            for (int i = 1; i < colCount; i++) {
                newCol = (ODOMElement)col.clone();
                newPlaceholder = (ODOMElement)placeholder.clone();

                cols.addContent(newCol);
                row.addContent(newPlaceholder);
                
                if (monitor != null) {
                    monitor.worked(1);
                }
            }
        }

        if (rowCount < 0) {
            throw new IllegalArgumentException(
                "A zero or positive number of rows is" +  //$NON-NLS-1$
                " required (" + //$NON-NLS-1$
                rowCount + " specified)"); //$NON-NLS-1$
        } else if (rowCount == 0) {
            row.detach();
        } else {
            ODOMElement newRow;

            for (int i = 1; i < rowCount; i++) {
                newRow = (ODOMElement)row.clone();

                grid.addContent(newRow);
                
                if (monitor != null) {
                    monitor.worked(1);
                }
            }
        }

        // Now the structure is complete, set the row and column dimension
        // attributes
        grid.setAttribute("rows", String.valueOf(rowCount)); //$NON-NLS-1$
        grid.setAttribute("columns", String.valueOf(colCount)); //$NON-NLS-1$

        // Finally, if needed, replace the placeholder (this is the first
        // cell) with the required firstCell content
        if (firstCell != null) {
            List content = placeholder.getParent().getContent();

            // Ensure that the firstCell is removed from wherever it used to be
            // first if necessary
            if (firstCell.getParent() != null) {
                firstCell.detach();
            }

            // Replace the placeholder with the specified firstCell
            content.set(content.indexOf(placeholder), firstCell);
        }
        
        if (monitor != null) {
            monitor.worked(5);
        }

        return grid;
    }
    
    /**
     * Calls createSizedGrid(FormatType formatType,Element firstCell,int rowCount,int colCount,IProgressMonitor monitor)
     * with null monitor.
     * @param formatType
     * @param firstCell
     * @param rowCount
     * @param colCount
     * @return
     */
    public static ODOMElement createSizedGrid(FormatType formatType,
                                    Element firstCell,
                                    int rowCount,
                                    int colCount) {
        return createSizedGrid(formatType, firstCell, rowCount, colCount, 
                                    null);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 14-May-04	4390/1	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 14-May-04	4344/3	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 14-May-04	4344/1	adrian	VBM:2004051003 Added default value support to Spatial Iterator Format

 04-Feb-04	2848/6	pcameron	VBM:2004020203 Fixed merge problems

 04-Feb-04	2848/3	pcameron	VBM:2004020203 Added ColumnInsertActionCommand

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
