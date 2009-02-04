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

import com.volantis.mcs.eclipse.common.DelayedTaskExecutor;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TypedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A general-purpose control for building up a list of objects. Objects are
 * created using a {@link ValidatedObjectControl}, and then placed into a list
 * which can be reordered.
 *
 * <p>If a suitable object parser is specified for conversion between object
 * and text, then the entries in the list can optionally be edited in place as
 * text.</p>
 */
public class ObjectDialogListBuilder extends Composite {
    /**
     * Resource prefix for the DialogListBuilder.
     */
    private final static String RESOURCE_PREFIX = "DialogListBuilder.";

    /**
     * The text for the Add button.
     */
    private static final String ADD_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.add");

    /**
     * The text for the Remove button.
     */
    private static final String REMOVE_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.remove");

    /**
     * The text for the Up button.
     */
    private static final String UP_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.up");

    /**
     * The text for the Down button.
     */
    private static final String DOWN_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.down");

    /**
     * The default object parser, which will be used if none is explicitly
     * specified.
     */
    private static final ObjectParser DEFAULT_PARSER =
            ObjectParserFactory.getDefaultInstance().createDefaultParser();

    /**
     * The horizontal spacing between widgets.
     */
    protected static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "horizontalSpacing").intValue();

    /**
     * The vertical spacing between widgets.
     */
    protected static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "verticalSpacing").intValue();

    /**
     * The TableViewer widget replacing the list.
     */
    private TableViewer tableViewer;

    /**
     * The Table widget underlying the TableViewer.
     */
    private Table table;

    /**
     * The content of the table.
     */
    private List content;

    /**
     * The listeners which listen for ModifyText events from the
     * ValidatedTextControl. This allows external validators to
     * respond to text as it is input.
     */
    private ListenerList listeners;

    /**
     * The validated object control used by the DialogListBuilder.
     */
    private ValidatedObjectControl objectControl;

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
     * Flag to indicate whether duplicate items are allowed in the list.
     */
    private boolean duplicatesAllowed;

    /**
     * The object parser for converting the objects to be stored in the list
     * to text for display purposes (and back in case of editing).
     */
    private ObjectParser parser;

    /**
     * The state change listener to be registered with the object control
     */
    private StateChangeListener stateChangeListener;

    /**
     * Constructs an ObjectDialogListBuilder
     *
     * @param parent The parent composite in which the builder is created
     * @param style The style for the control
     * @param title The title for the list
     * @param items An array of objects that will be used to initially populate
     *              the list
     * @param duplicatesAllowed True if duplicates are allowed in the list,
     *                          false to disallow duplicates
     * @param factory The validated object control factory that will generate
     *                new items to be added into the list
     * @param parser The ObjectParser for converting between the objects
     *               stored in the list and their textual representations
     * @param editable True if the objects should be editable as text - this
     *                 requires a suitable ObjectParser capable of converting
     *                 from text to object form
     */
    public ObjectDialogListBuilder(Composite parent, int style,
                                   String title, Object[] items,
                                   boolean duplicatesAllowed,
                                   ValidatedObjectControlFactory factory,
                                   ObjectParser parser, boolean editable) {
        super(parent, SWT.NONE);
        this.duplicatesAllowed = duplicatesAllowed;
        this.parser = (parser == null) ? DEFAULT_PARSER : parser;
        createControl(style, title, items, editable, factory);

        listeners = new ListenerList();

        validatorTask = new ValidatorTaskExecutor();
        validatorTask.start();

        updateButtons();
    }

    /**
     * Create the GUI control.
     *
     * @param style The style for the control
     * @param title The title for the list
     * @param items An array of objects that will be used to initially populate
     *              the list
     * @param editable True if the objects should be editable as text - this
     *                 requires a suitable ObjectParser capable of converting
     *                 from text to object form
     * @param controlFactory The validated object control factory that will
     *                       generate new items to be added into the list
     */
    private void createControl(int style, String title, Object[] items,
                               boolean editable,
                               ValidatedObjectControlFactory controlFactory) {
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
        addSelector(topLevel, controlFactory);
        addListWithButtons(topLevel, style, title, items, editable);

        layout();
    }

    /**
     * Creates and adds the ValidatedTextControl using the supplied factory.
     * @param container the selector's container
     */
    private void addSelector(Composite container,
                             ValidatedObjectControlFactory controlFactory) {
        objectControl = controlFactory.buildValidatedObjectControl(container,
                SWT.NONE);

        objectControl.addStateChangeListener(getStateChangeListener());


        // Only add the object control if it has a visual representation. If
        // it does, the add button goes alongside it. If not, the add button
        // will be positioned as part of the button panel.
        if (!(objectControl instanceof NonVisualValidatedObjectControl)) {
            GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL |
                    GridData.VERTICAL_ALIGN_END);
            ((Composite) objectControl).setLayoutData(gridData);
            ModifyListener modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent modifyEvent) {
                    validatorTask.setEvent(modifyEvent);
                }
            };
            ((Composite) objectControl).addListener(
                        SWT.Modify,  new TypedListener(modifyListener));
            addButtons(container);
        }
    }

    /**
     * Get the state change listener to be registered with the object control
     * @return
     */
    private StateChangeListener getStateChangeListener() {
        if (stateChangeListener == null) {
            stateChangeListener = new StateChangeListener() {
                public void stateChanged() {
                    updateAddButton();
                }
            };
        }
        return stateChangeListener;
    }

    // javadoc inherited
    public void dispose() {
        if (objectControl != null && stateChangeListener != null) {
            objectControl.removeStateChangeListener(stateChangeListener);
        }
        if (validatorTask != null) {
            validatorTask.dispose();
            validatorTask = null;
        }
        super.dispose();
    }

    /**
     * Add the buttons for inserting entries into the list. By default this
     * is a single simple 'Add' button, but more complex dialogs may extend
     * this class and provide additional buttons if necessary.
     *
     * <p>If this method is overridden, then it is likely that
     * {@link #updateButtons} should also be overridden.</p>
     *
     * @see #updateButtons
     * @param container The parent container for the buttons
     */
    protected void addButtons(Composite container) {
        addAddButton(container);
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
     * Handles the add button being pressed.
     *
     * <p>This retrieves the current object from the object control, and adds
     * it to the list (checking for duplicates if required).
     */
    private void handleAddButtonSelection() {
        Object item = objectControl.getValue();
        if (duplicatesAllowed || !listContains(item)) {
            // add the item to the end of the list
            addToList(item);
            // ensure the item that has just been added is selected.
            table.setSelection(table.getItemCount() -1);
            updateListOrderingButtons();
            fireModifyTextEvent(item);
            // ask the text control to set its value to empty
            objectControl.setValue(null);
        }
    }

    /**
     * Handles the remove button being pressed.
     *
     * <p>This removes the currently selected item from the list.</p>
     */
    private void handleRemoveButtonSelection() {
        int index = table.getSelectionIndex();
        Object item = tableViewer.getElementAt(index);
        removeFromList(index);

        // Select the appropriate item in the list
        int itemCount = table.getItemCount();
        if (itemCount > 0) {
            if (itemCount > index) {
                table.setSelection(index);
            } else {
                table.setSelection(table.getItemCount() - 1);
            }
        }

        updateListOrderingButtons();
        fireModifyTextEvent(item);
    }

    /**
     * Handles the up button being pressed.
     *
     * <p>This removes the currently selected item from the list and then
     * re-adds it one space higher.</p>
     */
    private void handleUpButtonSelection() {
        int index = table.getSelectionIndex();
        Object selection = tableViewer.getElementAt(index);
        removeFromList(index);
        addToList(selection, index - 1);
        table.setSelection(index - 1);
        updateListOrderingButtons();
        fireModifyTextEvent(selection);
    }


    /**
     * Handle the down button being pressed.
     *
     * <p>This removes the currently selected item from the list and then
     * re-adds it one space lower.</p>
     */
    private void handleDownButtonSelection() {
        int index = table.getSelectionIndex();
        Object selection = tableViewer.getElementAt(index);
        removeFromList(index);
        addToList(selection, index + 1);
        table.setSelection(index + 1);
        updateListOrderingButtons();
        fireModifyTextEvent(selection);
    }

    /**
     * Creates and adds the List widget with button panel.
     *
     * @param container the list's container
     * @param style the list's style
     * @param title the list's title, can be null
     * @param items the initial list of items, can be null
     */
    private void addListWithButtons(Composite container, int style,
                                    String title,
                                    Object[] items, boolean editable) {
        if (title != null) {
            GridData titleData = new GridData(GridData.FILL_HORIZONTAL);
            Label titleLabel = new Label(container, SWT.NONE);
            titleLabel.setText(title);
            titleLabel.setLayoutData(titleData);
            //Adds a dummy label to "pad out" title row to two cols.
            new Label(container, SWT.NONE);
        }

        table = new Table(container, style);
        final TableColumn column = new TableColumn(table, SWT.LEFT);
        GridData listGridData = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(listGridData);

        tableViewer = new TableViewer(table);
        tableViewer.addSelectionChangedListener(
                new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateListOrderingButtons();
            }
        });

        // Use a TableLayout to enforce the width of columns - note that this
        // is required under Windows because the columns are not expanded to
        // fill the available space by default, resulting in an apparently
        // empty table.
        final TableLayout tableLayout = new TableLayout() {
            public void layout(Composite comp, boolean flush) {
                super.layout(comp, flush);

                // Ensure we are laying out a table.
                if (comp instanceof Table) {
                    final int tableWidth = comp.getClientArea().width;
                    column.setWidth(tableWidth);
                }
            }
        };
        tableLayout.addColumnData(new ColumnWeightData(1, false));
        table.setLayout(tableLayout);

        // Set column properties - note that this is not viewable text, and
        // so does not need internationalising. It is simply used as a column
        // name for internal calls
        tableViewer.setColumnProperties(new String[]{"selectors"});

        content = new ArrayList();
        tableViewer.setContentProvider(new ArrayContentProvider());

        // Set a label provider for the table, which will ensure the correct
        // text is displayed in each cell (using the appropriate parser).
        tableViewer.setLabelProvider(
                new ILabelProvider() {
                    public Image getImage(Object o) {
                        return null;
                    }

                    public String getText(Object o) {
                        return parser.objectToText(o);
                    }

                    /**
                     * Since there are no state changes that could affect the
                     * rendering carried out by this label provider, we can
                     * simply ignore the listeners.
                     *
                     * @param iLabelProviderListener The listener to ignore
                     */
                    public void addListener(
                            ILabelProviderListener iLabelProviderListener) {
                    }

                    // Javadoc inherited
                    public void dispose() {
                    }

                    public boolean isLabelProperty(Object o, String s) {
                        return false;
                    }

                    /**
                     * Since there are no state changes that could affect the
                     * rendering carried out by this label provider, we can
                     * simply ignore the listeners.
                     *
                     * @param iLabelProviderListener The listener to ignore
                     */
                    public void removeListener(
                            ILabelProviderListener iLabelProviderListener) {
                    }
                }
        );

        // If the list is editable, set a cell modifier for it that uses the
        // parser to recreate the object from the edited text and store that
        // in the table.
        if (editable) {
            ICellModifier modifier = new ICellModifier() {
                public boolean canModify(Object o, String s) {
                    return true;
                }

                public Object getValue(Object o, String s) {
                    return parser.objectToText(o);
                }

                public void modify(Object o, String s, Object o1) {
                    TableItem ti = (TableItem) o;
                    Object parsed = parser.textToObject(String.valueOf(o1));
                    int index = tableViewer.getTable().indexOf(ti);
                    content.set(index, parsed);
                    tableViewer.refresh();
                    fireModifyTextEvent(parsed);
                }
            };
            tableViewer.setCellModifier(modifier);

            CellEditor[] editors = new CellEditor[] {
                new TextCellEditor(table)
            };
            tableViewer.setCellEditors(editors);
        }

        tableViewer.setInput(content);

        if (items != null && items.length > 0) {
            addToList(items);
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

        // If there is no displayed object selector, the add button is placed
        // with the other buttons.
        if (objectControl instanceof NonVisualValidatedObjectControl) {
            addButtons(buttonComposite);
        }

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
     * Retrieves the items in the list as a {@link java.util.List}.
     *
     * @return A {@link java.util.List} containing the items currently in the
     *         list.
     */
    public java.util.List getItems() {
        java.util.List itemList = new ArrayList();
        itemList.addAll(content);
        return itemList;
    }

    /**
     * Sets the contents of the list to the specified array of items, clearing
     * the list first.
     *
     * @param items The new values to display in the list
     */
    public void setItems(Object[] items) {
        clearList();
        addToList(items);
    }

    /**
     * Clears the object list.
     */
    private void clearList() {
        content.clear();
        tableViewer.refresh();
    }

    /**
     * Adds an array of objects to the list, preserving its current contents.
     *
     * @param items The items to add to the list
     */
    private void addToList(Object[] items) {
        for (int i = 0; i < items.length; i++) {
            addToList(items[i]);
        }
    }

    /**
     * Adds a single object to the list, preserving the existing contents.
     *
     * @param item The item to add
     */
    protected void addToList(Object item) {
        content.add(item);
        tableViewer.refresh();
    }

    /**
     * Adds a single object to the list at a specified index.
     *
     * @param item The item to add
     * @param index The index at which it should be added
     */
    protected void addToList(Object item, int index) {
        content.add(index, item);
        tableViewer.refresh();
    }

    /**
     * Removes an item from the list.
     *
     * @param index The index of the item to be removed
     */
    protected void removeFromList(int index) {
        content.remove(index);
        tableViewer.refresh();
    }

    /**
     * Checks whether the list contains a specified object.
     *
     * @param o The object to check for
     * @return True if the list contains the specified object
     */
    private boolean listContains(Object o) {
        return content.contains(o);
    }

    /**
     * Updates the enabled status of the buttons for reordering the list.
     *
     * <ul>
     *   <li>Remove can only be enabled if an item is selected</li>
     *   <li>Up can only be enabled if an item is selected that is not already
     *       the top item</li>
     *   <li>Down can only be enabled if an item is selected that is not
     *       already the bottom item</li>
     * </ul>
     */
    private void updateListOrderingButtons() {
        boolean isSelected = table.getSelectionCount() > 0;
        int index = table.getSelectionIndex();
        //Only remove an item if something is selected
        removeButton.setEnabled(isSelected);
        //Only move something up if selection is not top
        upButton.setEnabled(index > 0);
        //Only move something down if something is selected and not at bottom
        downButton.setEnabled(isSelected && index < table.getItemCount() - 1);
    }

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
            super("ObjectDialogListBuilder.ValidatorTaskExecutor", 500);
        }

        // javadoc inherited
        public void executeTask() {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    updateButtons();
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
     * Updates the buttons for adding items to the list. Should be overridden
     * if {@link #addButtons} is.
     *
     * @see #addButtons
     */
    protected void updateButtons() {
        updateAddButton();
    }

    /**
     * Updates the enabled status of the add button.
     *
     * <p>The add button will only be enabled if there is a valid value in the
     * object control.</p>
     */
    private void updateAddButton() {
        //Enable Add button only if there is a value in the
        //selector.
        if (addButton != null && !addButton.isDisposed()) {
            if (!objectControl.canProvideObject()) {
                addButton.setEnabled(false);
            } else {
                boolean validated = objectControl == null ? false:
                        objectControl.validate().isOK();
                addButton.setEnabled(validated);
            }
        }
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
     * Adds a listener that is called when the contents of the
     * DialogListBuilder's ValidatedTextControl is modified, or when list items
     * are added or removed.
     *
     * @param listener for modifications
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            // The listener is added to selectControl which allows users
            // of DialogListBuilder to respond to text modification events as
            // they happen. For example, a Dialog user may wish to provide
            // validation error messages as invalid content is entered.
            if (!(objectControl instanceof NonVisualValidatedObjectControl)) {
                ((Composite) objectControl).addListener(SWT.Modify,
                        new TypedListener(listener));
            }
            // The listener is also added to DialogListBuilder's list of
            // listeners. ModifyText events are sent to the listeners in this
            // list with the fireModifyTextEvent(Object) method.
            listeners.add(listener);
        }
    }

    /**
     * Removes a control modification listener
     *
     * @param listener for modifications
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            ((Composite) objectControl).removeListener(SWT.Modify,
                    new TypedListener(listener));
            listeners.remove(listener);
        }
    }

    /**
     * Returns the object control used for creating objects to add to the list.
     *
     * @return The object control used for creating objects to add to the list
     */
    public ValidatedObjectControl getObjectControl() {
        return objectControl;
    }

    /**
     * Returns the currently selected index, or -1 if no item is selected.
     *
     * @return The currently selected index, or -1 if no item is selected
     */
    protected int getSelectedIndex() {
        int index = -1;
        if (table != null) {
            index = table.getSelectionIndex();
        }
        return index;
    }

    /**
     * Returns the currently selected object, or null if no object is selected.
     *
     * @return The currently selected object, or null if no object is selected
     */
    protected Object getSelectedObject() {
        Object selected = null;
        int index = getSelectedIndex();
        if (index != -1) {
            selected = tableViewer.getElementAt(index);
        }
        return selected;
    }

    /**
     * Sets the selection on the list.
     *
     * @param index The new index to set as selected
     */
    protected void setSelectedIndex(int index) {
        table.setSelection(index);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/3	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 07-Dec-05	10429/11	adrianj	VBM:2005111715 Fix for editing class selectors

 24-Nov-05	10429/1	adrianj	VBM:2005111715 Fixes for editing class and attribute selectors

 24-Nov-05	10425/1	adrianj	VBM:2005111715 Fixes for editing class and attribute selectors

 11-Nov-05	10290/1	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 11-Nov-05	10266/3	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
