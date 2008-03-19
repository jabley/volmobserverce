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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationStatus;

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
public class CustomisableListSelectionDialog
        extends MessageAreaSelectionDialog {

    /**
     * The factory to use for creating the selector control
     * for the dialog.
     */
    protected ValidatedTextControlFactory selectionFactory;

    /**
     * Resource prefix for the SelectionDialog.
     */
    protected String resourcePrefix;

    /**
     * The text for the label.
     */
    private String labelText;

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
    protected String[] currentSelection;

    /**
     * The DialogListBuilder widget used by the dialog.
     */
    protected DialogListBuilder listBuilder;

    /**
     * The validator to use on the list.
     */
    protected IndependentValidator validator;

    /**
     * If true this signifies that duplicate entries are allowed in the list.
     */
    protected boolean allowDuplicateEntries;

    /**
     * Create a CustomisableListSelectionDialog.
     *
     * @param parent The parent shell for this dialog
     * @param resourcePrefix The resource prefix to use
     * @param selectionFactory the factory used to provide the component that
     * will be used to populate the list. (This is the component across the top
     * of the dialog).
     * @param validator the validator to use on the list.
     * @param allowDuplicateEntries true if duplicate entries are allowed in
     * the list.
     * @param initialSelection the items you wish to be selected initially.
     */
    public CustomisableListSelectionDialog(
            Shell parent,
            String resourcePrefix,
            ValidatedTextControlFactory selectionFactory,
            IndependentValidator validator,
            boolean allowDuplicateEntries,
            Object[] initialSelection) {

        super(parent);
        this.resourcePrefix = resourcePrefix;
        this.validator = validator;
        this.selectionFactory = selectionFactory;
        this.currentSelection = (String[]) initialSelection;
        this.allowDuplicateEntries = allowDuplicateEntries;
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
                ControlsMessages.getInteger(resourcePrefix +
                                            "horizontalSpacing").intValue();
        topLevelGrid.verticalSpacing =
                ControlsMessages.getInteger(resourcePrefix +
                                            "verticalSpacing").intValue();
        topLevelGrid.marginHeight =
                ControlsMessages.getInteger(resourcePrefix +
                                            "marginHeight").intValue();
        topLevelGrid.marginWidth =
                ControlsMessages.getInteger(resourcePrefix +
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
        Label label = new Label(topLevel, SWT.NONE);
        label.setText(listLabelText);
        GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(labelData);
        addListBuilder(topLevel);
        addErrorMessageArea(topLevel);
        validateDialog();
        topLevel.layout();
        return topLevel;
    }

    /**
     * Sets the selection that this dialog will use.
     *
     * @param items the array of items that you wish this dialog to diaply as
     * currently selected. This implementation assumes a String[].
     */
    public final void setSelection(String[] items) {
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
    public final String[] getSelection() {
        return currentSelection;
    }

    /**
     * Creates and adds the DialogListBuilder with a ModifyListener to listen for
     * item removals, and populates the list with an initial selection.
     *
     * @param parent the parent composite object.
     */
    protected void addListBuilder(Composite parent) {
        listBuilder = new DialogListBuilder(parent,
                SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL,
                null,
                currentSelection,
                allowDuplicateEntries,
                selectionFactory,
                validator);
        GridData listGridData = new GridData(GridData.FILL_BOTH);
        listBuilder.setLayoutData(listGridData);
        setSelection(currentSelection);
        listBuilder.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                validateDialog();
                currentSelection = listBuilder.getItems();
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
        ValidationStatus status = listBuilder.validate();
        if (status.isOK()) {
            //Clear error message
            setErrorMessage(null);
        } else if (status.getSeverity() == ValidationStatus.ERROR) {
            setErrorMessage(status.getMessage());
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 09-Mar-05	7073/7	matthew	VBM:2005022203 javadoc fixups

 09-Mar-05	7073/5	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 09-Mar-05	7073/3	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 08-Mar-05	7073/1	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 ===========================================================================
*/
