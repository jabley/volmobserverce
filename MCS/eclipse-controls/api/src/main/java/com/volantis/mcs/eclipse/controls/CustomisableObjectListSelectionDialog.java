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
package com.volantis.mcs.eclipse.controls;

/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Class that is used for all ListSelectionDialogs. ListSelectionDialogs contain
 * a custom component across the top that allows entries to be entered. A list
 * of entries in a box in the bottom left and add/remove/up/down buttons in the
 * bottom right.
 *
 * <p>By instantiating this class with appropriate parameters you can produce a
 * wide variety of dialogs that follow the form described above. This class is
 * normally instantiated by the ListSelectionDialogFactory.</p>
 */
public class CustomisableObjectListSelectionDialog
        extends MessageAreaSelectionDialog {
    /**
     * The resource prefix for values shared by all instances of this dialog.
     */
    private static final String DIALOG_RESOURCE_PREFIX =
            "CustomisableObjectListSelectionDialog.";

    /**
     * The factory to use for creating the selector control
     * for the dialog.
     */
    protected ValidatedObjectControlFactory selectionFactory;

    /**
     * Resource prefix for the SelectionDialog.
     */
    protected String resourcePrefix;

    /**
     * The title of the dialog.
     */
    protected String dialogTitle;

    /**
     * The text for the list's label.
     */
    protected String listLabelText;

    /**
     * The top level GridLayout.
     */
    protected GridLayout topLevelGrid;

    /**
     * The current selection of the dialog.
     */
    protected Object[] currentSelection;

    /**
     * The DialogListBuilder widget used by the dialog.
     */
    protected ObjectDialogListBuilder listBuilder;

    /**
     * The validator to use on the list.
     */
    protected IndependentValidator validator;

    /**
     * If true this signifies that duplicate entries are allowed in the list.
     */
    protected boolean allowDuplicateEntries;

    /**
     * If true, then the list can be edited as text, using the supplied
     * object parser.
     */
    protected boolean editable;

    private ObjectParser parser;

    /**
     * Create a CustomisableListSelectionDialog.
     *
     * @param parent
     * @param resourcePrefix
     * @param selectionFactory the factory used to provide the component that
     * will be used to populate the list. (This is the component across the top
     * of the dialog).
     * @param validator the validator to use on the list.
     * @param allowDuplicateEntries true if duplicate entries are allowed in
     * the list.
     * @param initialSelection the items you wish to be selected initially.
     */
    public CustomisableObjectListSelectionDialog(
            Shell parent,
            String resourcePrefix,
            ValidatedObjectControlFactory selectionFactory,
            IndependentValidator validator,
            boolean allowDuplicateEntries,
            Object[] initialSelection,
            ObjectParser parser, boolean editable) {

        super(parent);
        this.resourcePrefix = resourcePrefix;
        this.validator = validator;
        this.selectionFactory = selectionFactory;
        this.currentSelection = initialSelection;
        this.allowDuplicateEntries = allowDuplicateEntries;
        this.parser = parser;
        this.editable = editable;
        initialiseConstantMembers();

        setTitle(dialogTitle);
        setBlockOnOpen(true);
    }

    /**
     * This method is used to initialise the member information related to the
     * look of the dialog (width/height/labels etc). These ideally would be
     * marked final but cannot be initialised until the constructor is called
     * with the resourcePrefix necessary to extract the correct information
     * for the dialog.
     */
    private void initialiseConstantMembers() {

        // All derivatives of this class must ensure that the following keys
        // are available in the resource bundle.

        //    labelText =
        //             ControlsMessages.getString(resourcePrefix + "labelText");
        dialogTitle =
                ControlsMessages.getString(resourcePrefix + "title");

        listLabelText =
                ControlsMessages.getString(resourcePrefix + "listLabel");

        // store the layout specific attributes in the GridLayout
        topLevelGrid = new GridLayout();
        topLevelGrid.horizontalSpacing =
                ControlsMessages.getInteger(DIALOG_RESOURCE_PREFIX +
                                            "horizontalSpacing").intValue();
        topLevelGrid.verticalSpacing =
                ControlsMessages.getInteger(DIALOG_RESOURCE_PREFIX +
                                            "verticalSpacing").intValue();
        topLevelGrid.marginHeight =
                ControlsMessages.getInteger(DIALOG_RESOURCE_PREFIX +
                                            "marginHeight").intValue();
        topLevelGrid.marginWidth =
                ControlsMessages.getInteger(DIALOG_RESOURCE_PREFIX +
                                            "marginWidth").intValue();
    }

    /**
     * Creates the dialog's control. Care should be taken to ensure this method
     * is only called once.
     *
     * @param parent the parent of the dialog's control.
     * @return the created control.
     */
    protected Control createDialogArea(Composite parent) {
        Composite topLevel = (Composite) super.createDialogArea(parent);
        topLevel.setLayout(topLevelGrid);
        addListBuilder(topLevel);
        addErrorMessageArea(topLevel);
        validateDialog();
        topLevel.layout();
        return topLevel;
    }

    /**
     * Sets the selection that this dialog will use.
     *
     * @param items the array of items that you wish this dialog to display as
     * currently selected. This implementation assumes an Object[].
     */
    public final void setSelection(Object[] items) {
        currentSelection = items;
        //setSelection can be called on a dialog that has been
        //disposed, so need to check listBuilder.
        if (listBuilder != null && !listBuilder.isDisposed()) {
            listBuilder.setItems(currentSelection);
        }
    }

    /**
     * Gets the result from the dialog.
     * @return the selected items in the listBuilder.
     */
    public final Object[] getSelection() {
        return currentSelection;
    }

    /**
     * Creates and adds the DialogListBuilder with a ModifyListener to listen for
     * item removals, and populates the list with an initial selection.
     *
     * @param parent the parent composite object.
     */
    protected void addListBuilder(Composite parent) {
        listBuilder = new ObjectDialogListBuilder(parent,
                SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL,
                listLabelText,
                currentSelection,
                allowDuplicateEntries,
                selectionFactory,
                parser,
                editable);
        GridData listGridData = new GridData(GridData.FILL_BOTH);
        listBuilder.setLayoutData(listGridData);
        setSelection(currentSelection);
        listBuilder.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                validateDialog();
                currentSelection = listBuilder.getItems().toArray();
            }
        });
    }

    /**
     * Creates and adds the message area for error messages.
     * @param parent the parent Composite of the area.
     */
    private void addErrorMessageArea(Composite parent) {
        Composite errorMessageComposite =
                (Composite) createErrorMessageArea(parent);
        GridData emCompositeData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        emCompositeData.horizontalSpan = 2;
        errorMessageComposite.setLayoutData(emCompositeData);
    }

    /**
     * Validates the selector and list items. Error messages from
     * the selector validation take precedence.
     */
    protected void validateDialog() {
        if (validator != null) {
            ValidationStatus status = validator.validate(listBuilder.getItems());
            if (status.isOK()) {
                //Clear error message
                setErrorMessage(null);
            } else if (status.getSeverity() == ValidationStatus.ERROR) {
                setErrorMessage(status.getMessage());
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 09-Mar-05	7073/7	matthew	VBM:2005022203 javadoc fixups

 09-Mar-05	7073/5	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 09-Mar-05	7073/3	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 08-Mar-05	7073/1	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 ===========================================================================
*/
