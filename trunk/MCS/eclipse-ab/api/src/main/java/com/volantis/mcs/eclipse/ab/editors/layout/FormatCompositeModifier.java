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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.editors.layout.states.ColumnChangeState;
import com.volantis.mcs.eclipse.ab.editors.layout.states.FormatModifyState;
import com.volantis.mcs.eclipse.ab.editors.layout.states.GridModifierState;
import com.volantis.mcs.eclipse.ab.editors.layout.states.RowChangeState;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.layouts.FormatType;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;


/**
 * FormatCompositeModifier handle all ODOMChangeEvents that result in a changes
 * to an associated FormatComposite.
 */
public class FormatCompositeModifier {

    /**
     * The default state of the FormatCompositeModifier. This is the format insertion or
     * deletion state. All state transitions invoked by FormatCompositeModifier, result
     * in an eventual return to this default state. See
     * {@link FormatModifyState}.
     */
    private GridModifierState defaultState;

    /**
     * The current state of the FormatCompositeModifier.
     */
    private GridModifierState currentState;

    /**
     * The GridFormatComposite on which FormatCompositeModifier operates.
     */
    private final FormatComposite formatComposite;

    /**
     * The FormatCompositeBuilder used by this FormatCompositeModifier to
     * build new FormatComposites.
     */
    private final FormatCompositeBuilder fcBuilder;

    /**
     * Creates a FormatCompositeModifier instance for the supplied
     * FormatComposite.
     * @param formatComposite the FormatComposite modified by this instance.
     * @param fcBuilder the FormatCompositeBuilder used to build new
     * FormatComposites as children of formatComposites where necessary.
     */
    public FormatCompositeModifier(final FormatComposite formatComposite,
                                   FormatCompositeBuilder fcBuilder) {
        this.formatComposite = formatComposite;
        this.fcBuilder = fcBuilder;

        // Create the default state (format insertion and deletion), and
        // assign itself as its next state.
        defaultState = new FormatModifyState(this);
        defaultState.setNextState(defaultState);
        // The current state is initially the default state.
        currentState = defaultState;


        final ODOMChangeListener hierarchyChangeListener =
                new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                if (event.getSource() instanceof Element) {
                    ODOMElement source = (ODOMElement) event.getSource();
                    if (source != getElement()) {
                        processEvent(node, event);
                    }

                }
            }
        };

        final ODOMChangeListener attributeChangeListener =
                new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                if (event.getSource() instanceof Attribute) {
                    processEvent(node, event);
                }
            }
        };
        formatComposite.getElement().addChangeListener(hierarchyChangeListener,
                ChangeQualifier.HIERARCHY);
        formatComposite.getElement().addChangeListener(attributeChangeListener,
                ChangeQualifier.ATTRIBUTE_VALUE);
        formatComposite.getElement().addChangeListener(attributeChangeListener,
                ChangeQualifier.HIERARCHY);

        // Add a dispose listener to the FormatComposite so that this
        // FormatCompositeModifier will stop trying to modify the
        // FormatComposite subsequent to its departure.
        formatComposite.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                ODOMElement element = formatComposite.getElement();
                element.removeChangeListener(hierarchyChangeListener,
                                ChangeQualifier.HIERARCHY);
                element.removeChangeListener(attributeChangeListener,
                        ChangeQualifier.ATTRIBUTE_VALUE);
                element.removeChangeListener(attributeChangeListener,
                        ChangeQualifier.HIERARCHY);
            }
        });
    }

    /**
     * Processes the ODOMChangeEvent forwarded by this FormatCompositeModifier's
     * GridFormatComposite. All forwarded events originate from
     * @param node  the ODOMElement reporting the change
     * @param event the change event
     */
    public void processEvent(ODOMObservable node, ODOMChangeEvent event) {
        ODOMObservable source = event.getSource();
        if (source instanceof Attribute) {
            handleAttributeEvent(event);
        } else if (source instanceof Element) {
            handleElementEvent(event);
        }
    }

    /**
     * Handles an Element ODOMChangeEvent by processing the current state
     * of the FormatCompositeModifier, and changing to the resultant state. The event
     * originates from a descendant of the grid.
     * @param event the change event
     */
    private void handleElementEvent(ODOMChangeEvent event) {
        currentState = currentState.processEvent(event);
    }

    /**
     * Handles an Attribute ODOMChangeEvent. The event originates from the
     * grid's element.
     * @param event the change event
     */
    private void handleAttributeEvent(ODOMChangeEvent event) {
        /**
         * Change events are received from the grid's element or from any of
         * its decendents. We only process attribute insertions, deletions and
         * updates where the attribute in question belongs directly to the
         * grid's element, hence the checks against the grid's element.
         */
        ODOMObservable source = event.getSource();
        Attribute attr = (Attribute) source;
        ChangeQualifier qualifier = event.getChangeQualifier();
        String attrName = attr.getName();
        ODOMElement element = formatComposite.getElement();

        final String attributeName = source.getName();
        final Object newValue = event.getNewValue();

        boolean attributeDeleted =
                qualifier == ChangeQualifier.HIERARCHY &&
                newValue == null;

        boolean attributeChanged =
                (qualifier == ChangeQualifier.
                ATTRIBUTE_VALUE) &&
                (source.getParent() == element);

        if (attributeDeleted || attributeChanged) {
            if (attributeName.equals(FormatComposite.
                    BACKGROUND_COLOUR_ATTR_NAME)) {
                formatComposite.setBackgroundColor((String) newValue);
            } else if (attributeName.equals(FormatComposite.
                    BORDER_WIDTH_ATTR_NAME)) {
                int borderLineWidth = 0;
                try {
                    borderLineWidth =
                            ((ODOMAttribute) source).
                            getIntValue();
                } catch (DataConversionException e) {
                    // In this case we simply default to 0.
                }
                formatComposite.setBorderLineWidth(borderLineWidth);
            } else if (attributeName.equals(FormatComposite.
                    BORDER_COLOUR_ATTR_NAME)) {
                formatComposite.setBorderColor((String) newValue);
            } else if (attributeName.equals(FormatComposite.
                    NAME_ATTR_NAME)) {
            	formatComposite.updateName();
            	formatComposite.updateText();
            	formatComposite.getRoot().layout(); 
            } else if (attrName.equals(FormatComposite.COLUMNS_ATTR_NAME)) {
                // The column count for the grid has changed meaning that
                // columns are being inserted or deleted. Change the state of
                // the FormatCompositeModifier to ColumnChangeState, and set the new
                // state's terminal state to the previous current state.
                // Immediately process the new state with the current event.
                // A State pattern is used here because column insertion and
                // deletion (as well as row changes), is not an atomic ODOM
                // change. A sequence of events is generated, some of which
                // must be processed, with the rest ignored. The
                // ColumnChangeState handles all this for us.
                GridModifierState columnChange =
                        new ColumnChangeState(this);
                // Return to the current state after processing.
                columnChange.setNextState(currentState);
                // Process the column change and return to its terminal state,
                // which was set above.
                currentState = columnChange.processEvent(event);

            } else if (attrName.equals(FormatComposite.ROWS_ATTR_NAME)) {
                // See the comment above for the "columns" attribute.
                GridModifierState rowChange =
                        new RowChangeState(this);
                rowChange.setNextState(currentState);
                currentState = rowChange.processEvent(event);
            }
        }
    }


    /**
     * Finds which child FormatComposite corresponds to the given (deleted)
     * element. This is used when a format, row or column is being deleted. In
     * such a case, the deleted element has a null parent, so a lookup has to
     * be done on the grid's existing cells (prior to them being removed), so
     * that the deleted element's row and column position can be determined.
     * @param element the deleted element to look for
     * @return the FormatComposite for the element
     */
    public FormatComposite getChildFCForElement(ODOMElement element) {
        return formatComposite.getChildFC(element);
    }

    /**
     * Get the FormatCompositeModifier grid's element. This is only used by the
     * {@link FormatModifyState}.
     * @return the grid's element
     */
    public ODOMElement getElement() {
        return formatComposite.element;
    }

    /**
     * Get the FormatCompositeModifier's GridFormatComposite.
     * @return the GridFormatComposite
     */
    public FormatComposite getFormatComposite() {
        return formatComposite;
    }

    /**
     * Remove a FormatComposite that is a child of the FormatComposite
     * associated with this FormatCompositeModifier.
     * @param childFC the child FormatComposite
     */
    public void removeChildFC(FormatComposite childFC) {
        Control children [] = formatComposite.getChildren();
        boolean isChild = false;
        for (int i = 0; i < children.length && !isChild; i++) {
            isChild = children[i] == childFC;
        }

        if (!isChild) {
            throw new IllegalArgumentException(childFC + " is not a child of" +
                    formatComposite);
        }

        childFC.dispose();
    }

    /**
     * Insert a FormatComposite for the given element at the specified
     * row/column location.
     *
     * @param element the ODOMElement to associate with new FormatComposite
     * @param row     the row within the FormatComposite associated with this
     *                FormatCompositeModifier into which to place the new
     *                FormatComposite.
     * @param column  the column within the FormatComposite associated with
     *                this FormatCompositeModifier into which to place the new
     *                FormatComposite.
     */
    public void insertFC(ODOMElement element, int row, int column) {
        fcBuilder.build(formatComposite, element, row, column);

        // Laying out the parent is slightly faster but does not look quite
        // as good (it will not take resizing across the entire layout into
        // consideration).
        formatComposite.getRoot().layout();
    }

    /**
     * Refreshes the FormatCompositeModifier's grid by performing a layout on
     * its top level Composite.
     */
    private void refreshGrid() {
        formatComposite.updateText();

        // Laying out the parent is slightly faster but does not look quite
        // as good (it will not take resizing across the entire layout into
        // consideration).
        formatComposite.getRoot().layout();
    }

    /**
     * Get the number of rows for the FormatCompositeModifier's grid.
     * @return the row count
     */
    public int getNumRows() {
        return formatComposite.getRowCount();
    }

    /**
     * Get the number of columns for the FormatCompositeModifier's grid.
     * @return the column count
     */
    public int getNumColumns() {
        return formatComposite.getColumnCount();
    }

    /**
     * Add columns to the FormatComposite associated with this
     * FormatCompositeModifier
     *
     * @param start the index of the first column to insert
     * @param elements the Elements that were inserted in the order they
     * were inserted. The expected array is in column order.
     */
    public void insertColumns(int start, ODOMElement[] elements) {
        GridLayout layout = (GridLayout) formatComposite.getLayout();
        int nextElement = 0;
        int noOfNewColumns = elements.length / formatComposite.getRowCount();
        for(int column=start; column<start+noOfNewColumns; column++) {
            layout.numColumns++;
            for (int row = 0; row < formatComposite.getRowCount(); row++) {
                fcBuilder.build(formatComposite, elements[nextElement],
                        row, column);
                nextElement++;
            }
        }

        // When a column is added FormatComposite children that are
        // empty need to have their names updated.
        updateEmptyChildFCNames();

        refreshGrid();
    }

    /**
     * Update the names of the empty format children of the FormatComposite
     * associated with FormatCompositeModifier.
     */
    private void updateEmptyChildFCNames() {
        Control children [] = formatComposite.getChildren();
        for (int i = 0; i < children.length; i++) {
            FormatComposite childFC = (FormatComposite) children[i];
            if (childFC.getFormatType() == FormatType.EMPTY) {
                childFC.updateName();
            }
        }
    }

    /**
     * Add columns to the FormatComposite associated with this
     * FormatCompositeModifier
     *
     * @param start the index of the first column to insert
     * @param elements the Elements that were inserted in the order they
     * were inserted
     */
    public void insertRows(int start, ODOMElement[] elements) {
        int colCount = formatComposite.getColumnCount();
        int nextFC = 0;
        for(int row=start; row<start+elements.length/colCount; row++) {
            for(int col=0; col<colCount; col++) {
                fcBuilder.build(formatComposite, elements[nextFC], row, col);
                nextFC++;
            }
        }

        // When a row is added FormatComposite children that are
        // empty need to have their names updated.
        updateEmptyChildFCNames();

        refreshGrid();
    }

    /**
     * Delete columns from the FormatComposite associated with this
     * FormatCompositeModifier.
     * @param columnsToDelete an array specifying by index the columns to delete
     */
    public void deleteColumns(int[] columnsToDelete) {

        int rows = formatComposite.getRowCount();

        GridLayout layout = (GridLayout) formatComposite.getLayout();
        layout.numColumns -= columnsToDelete.length;

        // Determine the cells to delete and delete them.
        for (int i = 0; i < columnsToDelete.length; i++) {
            for (int row = 0; row < rows; row++) {
                FormatComposite fcToDelete =
                        formatComposite.getChildFC(row, columnsToDelete[i]);
                fcToDelete.dispose();
            }
        }
        // When a column is removed FormatComposite children that are
        // empty need to have their names updated.
        updateEmptyChildFCNames();

        refreshGrid();

    }

    /**
     * Delete rows from the FormatComposite associated with this
     * FormatCompositeModifier.
     * @param rowsToDelete an array specifying by index the rows to delete
     */
    public void deleteRows(int[] rowsToDelete) {
        int columns = formatComposite.getColumnCount();

        // Determine the cells to delete and delete them.
        FormatComposite fcsToDelete [] =
                new FormatComposite[rowsToDelete.length * columns];
        int nextFC = 0;
        for (int i = 0; i < rowsToDelete.length; i++) {
            for (int column = 0; column < columns; column++) {
                FormatComposite fcToDelete =
                        formatComposite.getChildFC(rowsToDelete[i], column);
                fcsToDelete[nextFC] = fcToDelete;
                nextFC++;
            }
        }

        for(int i=0; i<fcsToDelete.length; i++) {
            fcsToDelete[i].dispose();
        }

        // When a row is removed FormatComposite children that are
        // empty need to have their names updated.
        updateEmptyChildFCNames();

        refreshGrid();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jan-05	6576/1	philws	VBM:2004122102 Fix sizing issue on insertion of format into grid

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Aug-04	4902/16	allan	VBM:2004071504 Rework issues

 03-Aug-04	4902/14	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 22-Jul-04	4905/2	adrian	VBM:2004071507 Refactored Color support in layout formats

 25-Feb-04	3213/1	pcameron	VBM:2004022414 A few tweaks to background colours

 24-Feb-04	3021/9	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 24-Feb-04	3021/6	pcameron	VBM:2004020211 Added StyledGroup and background colours

 19-Feb-04	3021/3	pcameron	VBM:2004020211 Committed for integration

 13-Feb-04	2915/21	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 12-Feb-04	2915/19	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
