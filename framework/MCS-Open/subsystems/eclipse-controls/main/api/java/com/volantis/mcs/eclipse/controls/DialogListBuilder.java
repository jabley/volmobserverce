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

import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.Validated;
import com.volantis.mcs.eclipse.common.DelayedTaskExecutor;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Display;

import java.util.Arrays;

/**
 * A DialogListBuilder control that allows items to be added, removed and
 * reordered in a list widget with Add, Remove, Up and Down buttons. The
 * DialogListBuilder uses a ValidatedTextControl to provide the control that
 * generates values which can be added to the list. The control's getValue()
 * method returns the value which may be added. A value can only be added
 * if it is valid for the ValidatedTextControl.
 */
public class DialogListBuilder extends Composite implements Validated {

    /**
     * Resource prefix for the DialogListBuilder.
     */
    private final static String RESOURCE_PREFIX = "DialogListBuilder."; //$NON-NLS-1$

    /**
     * The text for the Add button.
     */
    private static final String ADD_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.add"); //$NON-NLS-1$

    /**
     * The text for the Remove button.
     */
    private static final String REMOVE_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.remove"); //$NON-NLS-1$

    /**
     * The text for the Up button.
     */
    private static final String UP_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.up"); //$NON-NLS-1$

    /**
     * The text for the Down button.
     */
    private static final String DOWN_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.down"); //$NON-NLS-1$

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "horizontalSpacing").intValue(); //$NON-NLS-1$

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "verticalSpacing").intValue(); //$NON-NLS-1$

    /**
     * The List widget used by DialogListBuilder.
     */
    private List list;

    /**
     * The validator to use for list items. Can be null if no validation
     * is required.
     */
    private IndependentValidator listValidator;

    /**
     * Default validation status for list.
     */
    private static final ValidationStatus OK_STATUS =
            new ValidationStatus(ValidationStatus.OK, ""); //$NON-NLS-1$

    /**
     * Flag to indicate whether duplicate items are allowed in the list.
     */
    private boolean duplicatesAllowed;

    /**
     * The validated text control used by the DialogListBuilder.
     */
    private ValidatedTextControl textControl;

    /**
     * The Add button.
     */
    private Button addButton;

    /**
     * The Remove button.
     */
    private Button removeButton;

    /**
     * The Up button.
     */
    private Button upButton;

    /**
     * The Down button.
     */
    private Button downButton;

    /**
     * The listeners which listen for ModifyText events from the
     * ValidatedTextControl. This allows external validators to
     * respond to text as it is input.
     */
    private ListenerList listeners;

    /**
     * The factory used by DialogListBuilder to create ValidatedTextControls.
     */
    private ValidatedTextControlFactory factory;

    /**
     * Provide a delayed task that validates after a set period of time.
     */
    private ValidatorTaskExecutor validatorTask;

    /**
     * The ValidatorTask that fires events and validates the text control.
     */
    private class ValidatorTaskExecutor extends DelayedTaskExecutor {
        /**
         * The event associated with this task.
         */
        private ModifyEvent event;

        /**
         * Default constructor.
         */
        public ValidatorTaskExecutor() {
            super("DialogListBuilder.ValidatorTaskExecutor", 500);
        }

        // javadoc inherited
        public void executeTask() {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    //Enable Add button only if there is a value in the
                    //selector.
                    if (!addButton.isDisposed()) {
                        addButton.setEnabled(textControl.validate().isOK());
                    }
                    fireModifyTextEvent(event);
                }
            });
        }

        /**
         * Setter for the event.
         * @param event the event to set.
         */
        public void setEvent(ModifyEvent event) {
            this.event = event;
            interrupt();
        }
    }


    /**
     * Constructs a DialogListBuilder control.
     * @param parent the parent composite
     * @param style the style for the list
     * @param title the title for the list, can be null
     * @param items the initial items for the list, can be null
     * @param duplicatesAllowed flag to indicate whether duplicate items
     * are allowed in the list
     * @param factory the factory that provides the ValidatedTextControl
     * which is used to generate values which may be added to the list. Cannot
     * be null.
     * @param listValidator the validator to use for validating the items
     * in the list. If null then no validation of list items is done.
     * @throws IllegalArgumentException if factory is null
     */
    public DialogListBuilder(Composite parent, int style,
                       String title,
                       String[] items,
                       boolean duplicatesAllowed,
                       ValidatedTextControlFactory factory,
                       IndependentValidator listValidator) {
        super(parent, SWT.NONE);
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory."); //$NON-NLS-1$
        }
        this.duplicatesAllowed = duplicatesAllowed;
        this.factory = factory;
        this.listValidator = listValidator;
        listeners = new ListenerList();
        createControl(style, title, items);

        validatorTask = new ValidatorTaskExecutor();
        validatorTask.start();
    }

    /**
     * Creates the DialogListBuilder control.
     * @param listStyle the style for the List widget.
     * @param title the title for the list, can be null.
     * @param items the initial list of items, can be null.
     */
    private void createControl(int listStyle, String title,
                               String[] items) {
        this.setLayout(new GridLayout());
        Composite topLevel = new Composite(this, SWT.NONE);
        GridLayout topLevelGrid = new GridLayout(2, false);
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.verticalSpacing = VERTICAL_SPACING;
        topLevelGrid.marginWidth = 0;
        topLevelGrid.marginHeight = 0;
        topLevel.setLayout(topLevelGrid);
        GridData topLevelGridData = new GridData(GridData.FILL_BOTH);
        topLevel.setLayoutData(topLevelGridData);
        addSelector(topLevel);
        addAddButton(topLevel);
        addListWithButtons(topLevel, listStyle, title, items);
    }

    /**
     * Creates and adds the ValidatedTextControl using the supplied factory.
     * @param container the selector's container
     */
    private void addSelector(Composite container) {
        textControl = factory.buildValidatedTextControl(container,
                SWT.NONE);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL |
                GridData.VERTICAL_ALIGN_END);
        ((Composite) textControl).setLayoutData(gridData);
        ModifyListener modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                validatorTask.setEvent(modifyEvent);
            }
        };
        ((Composite) textControl).addListener(
                    SWT.Modify,  new TypedListener(modifyListener));
    }

    // javadoc inherited
    public void dispose() {
        if (validatorTask != null) {
            validatorTask.dispose();
            validatorTask = null;
        }
        super.dispose();
    }

    /**
     * Creates and adds the Add button and its SelectionListener.
     * @param container the button's container
     */
    private void addAddButton(Composite container) {
        addButton = new Button(container, SWT.PUSH);
        addButton.setText(ADD_TEXT);
        addButton.setEnabled(false);
        GridData addButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL |
                GridData.VERTICAL_ALIGN_END);
        addButton.setLayoutData(addButtonData);
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleAddButtonSelection();
            }
        });
    }

    /**
     * Creates and adds the List widget with button panel.
     * @param container the list's container
     * @param style the list's style
     * @param title the list's title, can be null
     * @param items the initial list of items, can be null
     */
    private void addListWithButtons(Composite container, int style,
                                    String title,
                                    String[] items) {
        if (title != null) {
            GridData titleData = new GridData(GridData.FILL_HORIZONTAL);
            Label titleLabel = new Label(container, SWT.NONE);
            titleLabel.setText(title);
            titleLabel.setLayoutData(titleData);
            //Adds a dummy label to "pad out" title row to two cols.
            new Label(container, SWT.NONE);
        }
        list = new List(container, style);
        GridData listGridData = new GridData(GridData.FILL_BOTH);
        list.setLayoutData(listGridData);
        list.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                updateButtons();
            }
        });
        if (items != null && items.length > 0) {
            list.setItems(items);
        }
        addButtonPanel(container);
    }

    /**
     * Creates and adds a control of three buttons arranged vertically.
     * The buttons are aligned with the top of the list widget and are
     * the Remove, Up and Down buttons. The Add button is handled elsewhere
     * so that it can be placed on the same row as the DialogListBuilder's
     * ValidatedTextControl widget,
     * @param container the button panel's container
     */
    private void addButtonPanel(Composite container) {
        Composite buttonComposite = new Composite(container, SWT.NONE);
        GridLayout buttonGridLayout = new GridLayout();
        buttonGridLayout.verticalSpacing = VERTICAL_SPACING;
        buttonGridLayout.marginHeight = 0;
        buttonGridLayout.marginWidth = 0;
        buttonComposite.setLayout(buttonGridLayout);
        GridData panelGridData = new GridData(GridData.VERTICAL_ALIGN_FILL);
        buttonComposite.setLayoutData(panelGridData);

        removeButton = new Button(buttonComposite, SWT.PUSH);
        removeButton.setText(REMOVE_TEXT);
        removeButton.setEnabled(false);
        GridData removeButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL);
        removeButton.setLayoutData(removeButtonData);
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleRemoveButtonSelection();
            }
        });

        upButton = new Button(buttonComposite, SWT.PUSH);
        upButton.setText(UP_TEXT);
        upButton.setEnabled(false);
        GridData upButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL);
        upButton.setLayoutData(upButtonData);
        upButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleUpButtonSelection();
            }
        });

        downButton = new Button(buttonComposite, SWT.PUSH);
        downButton.setText(DOWN_TEXT);
        downButton.setEnabled(false);
        GridData downButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL);
        downButton.setLayoutData(downButtonData);
        downButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleDownButtonSelection();
            }
        });
    }

    /**
     * Handles pressing the Remove button.
     */
    private void handleRemoveButtonSelection() {
        int index = list.getSelectionIndex();
        int numItems = list.getItemCount();
        String item = list.getItem(index);
        list.remove(index);
        if (index == numItems - 1) {
            index--;
        }
        list.setSelection(index);
        updateButtons();
        fireModifyTextEvent(item);
    }

    /**
     * Handles pressing the Up button.
     */
    private void handleUpButtonSelection() {
        moveItem(-1);
    }

    /**
     * Handles pressing the Down button.
     */
    private void handleDownButtonSelection() {
        moveItem(1);
    }

    /**
     * Moves an item one position up or down the list.
     * @param direction the direction to move. A negative value
     * moves the item up the list; a positive value moves it down.
     */
    private void moveItem(int direction) {
        direction = direction < 0 ? -1 : 1;
        int index = list.getSelectionIndex();
        String item = list.getItem(index);
        String itemToMove = list.getItem(index);
        String neighbouringItem = list.getItem(index + direction);
        list.setItem(index + direction, itemToMove);
        list.setItem(index, neighbouringItem);
        list.setSelection(index + direction);
        updateButtons();
        fireModifyTextEvent(item);
    }

    /**
     * Handles pressing the Add button.
     */
    private void handleAddButtonSelection() {
        String item = textControl.getValue().trim();
        if (duplicatesAllowed || !isContainedIn(item)) {
            // add the item to the end of the list
            list.add(item);
            // ensure the item that has just been added is selected.
            list.setSelection(list.getItemCount() -1);
            updateButtons();
            fireModifyTextEvent(item);  
            // ask the text control to set its value to empty
            textControl.setValue("");
        }
    }

    /**
     * Checks that an item is present in the DialogListBuilder. Item comparisons
     * are case-insensitive.
     * @param item the item to check for
     * @return true if the item is in the list, false otherwise
     */
    private boolean isContainedIn(String item) {
        String[] listItems = list.getItems();
        Arrays.sort(listItems, String.CASE_INSENSITIVE_ORDER);
        int index =
                Arrays.binarySearch(listItems, item,
                        String.CASE_INSENSITIVE_ORDER);
        return index >= 0;
    }

    /**
     * Enables or disables buttons according to the
     * current list contents and selected item.
     */
    private void updateButtons() {
        boolean isSelected = list.getSelectionCount() > 0;
        int index = list.getSelectionIndex();
        //Only remove an item if something is selected
        removeButton.setEnabled(isSelected);
        //Only move something up if selection is not top
        upButton.setEnabled(index > 0);
        //Only move something down if something is selected and not at bottom
        downButton.setEnabled(isSelected && index < list.getItemCount() - 1);
    }

    /**
     * Sets the items for the DialogListBuilder.
     * @param items the items for the list
     */
    public void setItems(String[] items) {
        list.setItems(items);
        list.select(0);
        updateButtons();
        fireModifyTextEvent(items);
    }

    /**
     * Adds an item to the DialogListBuilder.
     * @param item the item to add
     */
    public void add(String item) {
        list.add(item);
        updateButtons();
        fireModifyTextEvent(item);
    }

    /**
     * Adds an item at a specified index.
     * @param item the item to add
     * @param index the index for the new item
     */
    // rest of javadoc inherited
    public void add(String item, int index) {
        list.add(item, index);
        updateButtons();
        fireModifyTextEvent(item);
    }

    /**
     * Selects the item at index in the DialogListBuilder.
     * @param index the index of the item to select
     */
    // rest of javadoc inherited
    public void setSelection(int index) {
        list.setSelection(index);
        updateButtons();
    }

    /**
     * Gets the contents of the DialogListBuilder.
     * @return the items in the DialogListBuilder
     */
    // rest of javadoc inherited
    public String[] getItems() {
        return list.getItems();
    }

    /**
     * Gets the current selection.
     * @return the current selection
     */
    // rest of javadoc inherited
    public String[] getSelection() {
        return list.getSelection();
    }

    /**
     * Fires a ModifyText event to all registered listeners when list items
     * are added or removed.
     * @param items the items which were added or removed
     */
    private void fireModifyTextEvent(Object items) {
        Object[] interested = listeners.getListeners();
        if (interested != null) {
            Event event = new Event();
            event.widget = this;
            event.data = items;
            ModifyEvent me = new ModifyEvent(event);
            for (int i = 0; i < interested.length; i++) {
                if (interested[i] != null) {
                    ((ModifyListener) interested[i]).modifyText(me);
                }
            }
        }
    }

    /**
     * Adds a listener that is called when the contents of the DialogListBuilder's
     * ValidatedTextControl is modified, or when list items are added or removed.
     * @param listener for modifications
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            //The listener is added to selectControl which allows users
            //of DialogListBuilder to respond to text modification events as they
            //happen. For example, a Dialog user may wish to provide
            //validation error messages as invalid content is entered.
            ((Composite) textControl).addListener(SWT.Modify,
                    new TypedListener(listener));
            //The listener is also added to DialogListBuilder's list of listeners.
            //ModifyText events are sent to the listeners in this list with
            //the fireModifyTextEvent(Object) method.
            listeners.add(listener);
        }
    }

    /**
     * Removes a control modification listener
     * @param listener for modifications
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            ((Composite) textControl).removeListener(SWT.Modify,
                    new TypedListener(listener));
            listeners.remove(listener);
        }
    }

    /**
     * Validates the ValidatedTextControl and list in that order. Validation
     * stops when the first error is encountered.
     * @return the validation status
     */
    public ValidationStatus validate() {
        ValidationStatus status = textControl.validate();
        if (status.isOK() && listValidator != null) {
            status = validateListItems();
        }
        return status;
    }

    /**
     * Validates the items in the list. Validation stops at the first error.
     * @return the validation status
     */
    private ValidationStatus validateListItems() {
        ValidationStatus status = OK_STATUS;
        String[] items = list.getItems();
        for (int i = 0; i < items.length && status.isOK(); i++) {
            status = listValidator.validate(items[i]);
        }
        return status;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 24-Mar-04	3566/1	byron	VBM:2004021014 No validation performed in style rule wizard on class or ID name

 18-Mar-04	3416/2	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 10-Feb-04	2857/5	doug	VBM:2003112711 Added wizard for creating theme rules

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 12-Jan-04	2215/3	pcameron	VBM:2003112405 Some tweaks to TimeSelectionDialog and TimeProvider

 09-Jan-04	2215/1	pcameron	VBM:2003112405 TimeSelectionDialog, ListBuilder and refactoring

 ===========================================================================
*/
