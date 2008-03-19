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

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.ab.editors.layout.GridDimensionsDialog;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;

/**
 * This is the action command used for each New format action within the New
 * menu that creates a grid-based format, based on a given format type. It is
 * appropriate to the Layout Outline page and the Layout Graphical Editor page.
 * It is enabled when the creation of the given format type is appropriate to
 * the currently selected element.
 */
public class NewGridFormatActionCommand
    extends FormatTypeBasedActionCommand {
    
    /**
     * Progress monitor used to count progress of grid creation.
     */
    protected IProgressMonitor progressMonitor;
    
    /**
     * Defines if grid creation progress dialog is displayed or not.
     * When set to true it will be displayed.
     */
    protected boolean displayProgress;
    
    /**
     * Initializes the new instance using the given parameters.
     * Progress dialog will be displayed by default when grid is created.
     *
     * @param formatType       identifies the type of grid that this action
     *                         command will create. Must not be null and must
     *                         have a grid structure
     * @param selectionManager selection manager that allows the
     *                         action to set the new format as the current
     *                         selection. Can not be null.
     */
    public NewGridFormatActionCommand(FormatType formatType,
                                      ODOMSelectionManager selectionManager) {
        super(formatType, selectionManager);
        this.displayProgress = true;
    }
    
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param formatType       identifies the type of grid that this action
     *                         command will create. Must not be null and must
     *                         have a grid structure
     * @param selectionManager selection manager that allows the
     *                         action to set the new format as the current
     *                         selection. Can not be null.
     * @param displayProgress  when set to true grid creation progress dialog
     *                         will be displayed                          
     */
    public NewGridFormatActionCommand(FormatType formatType,
                                    ODOMSelectionManager selectionManager,
                                    boolean displayProgress) {
        this(formatType, selectionManager);
        this.displayProgress = displayProgress;
    }

    // javadoc inherited
    protected void checkFormatType(FormatType formatType) {
        if (formatType.getStructure() != FormatType.Structure.GRID) {
            throw new IllegalArgumentException(
                "The format type must be a grid structure (" + //$NON-NLS-1$
                "was given " + //$NON-NLS-1$
                formatType.getTypeName() + ")"); //$NON-NLS-1$
        }
    }

    /**
     * This action command can only be performed when a single empty format is
     * selected and the format type can reasonably be created at the selected
     * point in the document.
     */
    public boolean enable(ODOMActionDetails details) {
        return ((details.getNumberOfElements() == 1) &&
            selectionIsAppropriate(details.getElement(0)) &&
            canReplace(details.getElement(0)));
    }

    /**
     * Supporting method that is used to make sure that the selection is
     * appropriate for this action command.
     *
     * @param selection the single selected element
     * @return true if the element is of an appropriate type for this command
     */
    protected boolean selectionIsAppropriate(Element selection) {
        return selection.getName().equals(FormatType.EMPTY.getElementName());
    }

    /**
     * Determines whether the given element can be replaced by the required
     * type of format without causing layout constraints to be violated.
     *
     * @param element the empty format element that the action wants to replace
     * @return true if the given element can be replaced without causing
     *         constraints to be violated
     */
    protected boolean canReplace(ODOMElement element) {
        Element e = ActionSupport.cloneContainingDeviceLayout(element);

        e = replace(e, 1, 1);

        return !LayoutConstraintsProvider.constraints.violated(e, null);
    }

    /**
     * The selected empty format is replaced by a grid structure obtained from
     * {@link FormatPrototype#createSizedGrid} being populated with
     * <dfn>n</dfn> rows and <dfn>m</dfn> columns as identified by the results
     * of a dialog with the user.
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException(
                "Only a single selection should be available " + //$NON-NLS-1$
                "when the action is run (" + //$NON-NLS-1$
                details.getNumberOfElements() +
                " elements selected)"); //$NON-NLS-1$
        } else {
            Dimension dimensions = getGridDimensions();

            if (dimensions != null) {
                try {
                    if (displayProgress) {
                        //progress dialog will be displayed
                        new ProgressMonitorDialog
                        (Display.getCurrent()
                         .getActiveShell())
                         .run(false,
                              false,
                              new GridActionRunner(details,
                                                   dimensions,
                                                   this));                        
                    } else {
                        replaceAndSelect(details, dimensions, null);                        
                    }                    
                } catch (InvocationTargetException e) {
                    EclipseCommonPlugin.handleError(e);                    
                } catch (InterruptedException e) {
                    EclipseCommonPlugin.handleError(e);                    
                }              
            }
        }
    }

    /**
     * This method can called by GridActionRunner - then IProgressMonitor is present
     * or can be called in other way when progress dialog is not required.
     *   
     * @param details - ODOMActionDetails containing grid element
     * @param dimensions - grid dimensions
     * @param monitor - grid creation progress monitor. Can be null-then progress is not count
     */
    protected void replaceAndSelect(ODOMActionDetails details,
                                    Dimension dimensions,
                                    IProgressMonitor monitor) {
        progressMonitor = monitor;
        // height = rows, width = columns        
        Element e = replace(details.getElement(0),
                            dimensions.height,
                            dimensions.width);

        if (progressMonitor != null) {
            progressMonitor.worked((dimensions.height + dimensions.width)
                                    *(GridActionRunner.PROGRESS_SCALE-1));
            progressMonitor = null;
        }
                                        
        setSelection(e);        
    }


    /**
     * The specified format is replaced by a prototypical instance of the
     * required type of grid format, dimensioned as specified.
     *
     * @param element   the format to be replaced
     * @param rows      the number of rows to go in the grid
     * @param cols      the number of columns to go in the grid
     * @return the new (top-level) element representing the required format
     *         type
     */
    protected Element replace(Element element,
                              int rows,
                              int cols) {
        Element replacement = null;

        if (!selectionIsAppropriate(element)) {
            throw new IllegalArgumentException(
                    "The action can not replace a " + //$NON-NLS-1$
                    element.getName());
        } else {
            try {
                // selectionManager is optional
                if (selectionManager != null) {
                    // disable the selection events
                    this.selectionManager.setEnabled(false);
                }
                replacement = FormatPrototype.createSizedGrid(formatType,
                        null,
                        rows,
                        cols,
                        progressMonitor);

            } finally {
                if (selectionManager != null) {
                    // ensure selection events are enabled
                    this.selectionManager.setEnabled(true);
                }
            }
            // call common utility method
            replaceElement(element, replacement);
        }

        return replacement;
    }
    

    /**
     * Interactively determines the grid dimensions using a
     * {@link GridDimensionsDialog}.
     *
     * @return the required grid dimensions or null if the grid dimensions
     *         were not defined
     */
    protected Dimension getGridDimensions() {
        Dimension dimensions = null;

        GridDimensionsDialog dialog =
                new GridDimensionsDialog(
                        Display.getCurrent().getActiveShell());

        int code = dialog.open();

        if (code == IDialogConstants.OK_ID) {
            Object[] result = dialog.getResult();
            dimensions = (Dimension)result[0];
        }

        return dimensions;
    }
    
    /** 
     * @return - Translated task name displayed on progress dialog
     */
    public String getTaskName() {
        return LayoutMessages.getString("NewGridFormatActionCommand.task.actionName");
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
