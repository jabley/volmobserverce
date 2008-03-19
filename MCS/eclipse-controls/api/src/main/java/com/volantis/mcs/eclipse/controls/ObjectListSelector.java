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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.eclipse.controls.events.StateChangeListenerCollection;
import com.volantis.mcs.themes.parsing.ObjectParser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * An Object button that allows a list of objects to be created through a
 * separate dialog, then stores a textual representation of that list in a
 * non-editable textfield and makes the elements in the list available through
 * an accessor method.
 */
public class ObjectListSelector extends ObjectButton {
    /**
     * The array of objects selected from the list.
     */
    private Object[] selectedObjects;

    /**
     * A factory for generating controls that are used to create new entries
     * in the list.
     */
    private ValidatedObjectControlFactory factory;

    /**
     * The validator to use for the list dialog.
     */
    private IndependentValidator validator;

    /**
     * Specifies whether the entries in the list dialog should be editable as
     * text. Note that this requires a suitable ObjectParser.
     */
    private boolean editable;

    /**
     * The resource prefix to use for obtaining labels in the selector.
     */
    private String resourcePrefix;

    /**
     * listener object
     */
    private StateChangeListenerCollection stateChangeListenerCollection =
            new StateChangeListenerCollection();

    /**
     * Constructor
     * @param parent The parent Composite
     * @param style The required style
     */
    public ObjectListSelector(Composite parent, int style, ObjectParser parser,
                              ValidatedObjectControlFactory factory,
                              IndependentValidator validator,
                              boolean editable, String resourcePrefix) {
        super(parent, style, parser, false);

        this.factory = factory;
        this.selectedObjects = new Object[0];
        this.validator = validator;
        this.editable = editable;
        this.resourcePrefix = resourcePrefix;

        // Add a listener to the button such that when the button is
        // pressed the appropriate dialog is displayed
        getButton().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                doSelectionDialog();
            }
        });

        // Add a listener to the text box for changes in the text
        getText().addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                notifyStateChangeListenerOfTextChange();
            }
        });
    }

    /**
     * Inherited validation method, which in this case is not supported.
     *
     * @throws UnsupportedOperationException because this method is not
     *         supported.
     */
    public ValidationStatus validate() {
        throw new UnsupportedOperationException(
                "No ObjectListSelector.validate()");
    }

    /**
     * Performs all processing associated with populating, constructing,
     * displaying and interrogating the dialog
     */
    private void doSelectionDialog() {
        // Construct a dialog based on the above tokens
        CustomisableObjectListSelectionDialog dialog =
                ListSelectionDialogFactory.
                createObjectListSelectionDialog(getButton().getShell(),
                        selectedObjects, factory, parser, validator, editable,
                        resourcePrefix);

        // Display the dialog, and only proceed if user okayed it
        if (dialog.open() == SelectionDialog.OK) {
            selectedObjects = dialog.getSelection();
            // Put the value into the TextButton's text
            setMultipleValue(selectedObjects);
        }
    }

    /**
     * Returns the objects in the list as an array.
     *
     * @return An array of objects from the object list.
     */
    public Object[] getSelectedObjects() {
        return selectedObjects;
    }

    /**
     * Sets the objects in the list.
     *
     * @param objects
     */
    public void setSelectedObjects(Object[] objects) {
        selectedObjects = objects;
        setMultipleValue(selectedObjects);
    }

    //javadoc inherited
    public void addStateChangeListener(StateChangeListener listener) {
        stateChangeListenerCollection.addStateChangeListener(listener);
    }

    //javadoc inherited
    public void removeStateChangeListener(StateChangeListener listener) {
        stateChangeListenerCollection.removeStateChangeListener(listener);
    }

    /**
     * notify the state change listener of a text change
     */
    private void notifyStateChangeListenerOfTextChange() {
        stateChangeListenerCollection.notifyListeners();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
