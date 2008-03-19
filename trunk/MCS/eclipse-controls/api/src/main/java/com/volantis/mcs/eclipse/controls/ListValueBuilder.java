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
package com.volantis.mcs.eclipse.controls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * The ListValueBuilder provides a control that allows the editing of lists of
 * values. The list may or may not be ordered. If ordered, then ListValueBuilder
 * provides Up and Down buttons. The ListValueBuilder can use an optional
 * SelectionDialog to provide new values for its list. If a SelectionDialog is
 * provided, ListValueBuilder uses a Browse button to invoke it. In this case,
 * the list of values is read-only: values can only be added and removed. If
 * there is no SelectionDialog, an Add button is provided instead of Browse.
 * When Add is pressed, a "default" item is added to the list and is selected
 * for editing. The absence of a dialog to provide values in this case means
 * that all items in the list are editable.
 *
 * The ListValueBuilder is a single-selection list. It implements the
 * ISelectionProvider interface to allow users to set and get selections, and
 * to listen for selection changes. Only single selections can be set.
 *
 * ListValueBuilder may be sublassed to work with any object. The default
 * implementation works with lists of String objects. To have ListValueBuilder
 * work with your domain objects you can override the following methods:
 * <ul>
 * <li>
 * <code>protected CellEditor[] createCellEditors()</code>
 * </li>
 * <li>
 * <code>protected ICellModifier createCellModifier()</code>
 * </li>
 * <li>
 * <code>protected ITableLabelProvider createLabelProvider()</code>
 * </li>
 * <li>
 * <code>protected Object createNewItem()</code>
 * </li>
 * <li>
 * <code>protected TableViewer createTableViewer()</code>
 * </li>
 * </ul>
 *<strong>
 * It is important that the subclass implementations of the above methods are
 * consistent with each other; that is, they work with the same type of domain
 * object.
 * </strong>
 */
public class ListValueBuilder extends Composite implements ISelectionProvider {

    /**
     * Resource prefix for the ListValueBuilder.
     */
    private final static String RESOURCE_PREFIX = "ListValueBuilder.";

    /**
     * The text for the Add button.
     */
    private static final String ADD_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.add");

    /**
     * The text for the Browse button.
     */
    private static final String BROWSE_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.browse");

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
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "horizontalSpacing").
            intValue();

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "verticalSpacing").
            intValue();

    /**
     * A flag indicating whether the list's items are ordered.
     */
    private final boolean ordered;

    /**
     * The optional SelectionDialog used by the ListValueBuilder to select a
     * value to add to the list. If null, a "blanK" value is added to the list.
     */
    private final SelectionDialog selectionDialog;

    /**
     * The table viewer used by this control.
     */
    protected TableViewer tableViewer;

    /**
     * The Add or Browse button (depending on whether selectionDialog is null
     * or not).
     */
    private Button addOrBrowseButton;

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
     * The underlying model maintained by this control.
     */
    private ItemsList itemsList;

    /**
     * The accessible name of the list.
     */
    private String accessibleName;

    /**
     * The modify listener used by the model to refresh itself after a
     * model change.
     */
    private final ModifyListener itemsListModifyListener = new ModifyListener() {
        /**
         * Refresh the table viewer, inform ListValueBuilder's registered
         * listeners and update the buttons in response to the model
         * changing.
         * @param modifyEvent the ModifyEvent object describing the change.
         * Not used.
         */
        public void modifyText(ModifyEvent modifyEvent) {
            tableViewer.refresh();
            fireModifyEvent(listeners);
            updateButtons();
        }
    };

    /**
     * The listeners which listen for ModifyText events.
     */
    private final ListenerList listeners;

    /**
     * The Composite container for the buttons. This is a field so that its
     * background colour may be changed by {@link #setBackground}.
     */
    private Composite buttonComposite;

    /**
     * Constructs a new ListValueBuilder.
     *
     * @param parent the parent Composite. Cannot be null.
     * @param ordered a flag indicating whether the ListValueBuilder uses an
     *                ordered (true) or unordered (false) list.
     * @param selectionDialog an optional SelectionDialog. Can be null.
     */
    public ListValueBuilder(Composite parent, boolean ordered,
                            SelectionDialog selectionDialog) {
        super(parent, SWT.NONE);
        this.ordered = ordered;
        this.selectionDialog = selectionDialog;
        this.listeners = new ListenerList();
        createControl();
        // create the list that will contain the items
        itemsList = new ItemsList();
        itemsList.addModifyListener(itemsListModifyListener);
        tableViewer.setInput(itemsList);
        initAccessible();
    }


    /**
     * Creates the ListValueBuilder controls.
     */
    private void createControl() {
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = HORIZONTAL_SPACING;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);

        // Create the table viewer.
        tableViewer = createTableViewer(this);

        // Ensure the table fills the available space.
        tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

        // If there is no selection dialog, then addition of items requires
        // table cells to be editable.
        if (selectionDialog == null) {

            // Create and set the cell editors for the viewer.
            tableViewer.setCellEditors(createCellEditors());

            // Create and set the cell modifier for the viewer. The cell
            // modifier is invoked when cells gain focus.
            tableViewer.setCellModifier(createCellModifier());
        }

        // Create and set the content provider.
        tableViewer.setContentProvider(new ListBuilderContentProvider());
        // Create and set the label provider.
        tableViewer.setLabelProvider(createLabelProvider());

        // Add a selection listener which enables or disables buttons
        // according to the list selection.
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                updateButtons();
            }
        });
        addButtonPanel(this);
    }

    /**
     * Creates and returns an array of cell editors to use for editing the
     * cells of the table viewer. Subclasses should override this method to
     * supply the appropriate array of editors to work with the types of data
     * displayed in the table columns. The default implementation supplies an
     * array of TextCellEditors. This override is only necessary when a
     * SelectionDialog is not being used by the ListValueBuilder.
     *
     * @return an array of cell editors
     */
    protected CellEditor[] createCellEditors() {
        // Get the number of columns in the table. This is actually the
        // number of {@link TableColumn}s, if any, created and added to the
        // Table widget by {@link #createTableViewer}.
        int numColumns = tableViewer.getTable().getColumnCount();

        // If no {@link TableColumn}s were explicitly created (as in the
        // default implementation here of {@link #createTableViewer}, then
        // the column count is zero, despite there visually being one
        // column of items present. Bump up the count to one so that the
        // cell editors are added correctly.
        if (numColumns == 0) {
            numColumns = 1;
        }

        // Create the cell editors. These editors work only with Strings.
        CellEditor[] editors = new CellEditor[numColumns];
        for (int i = 0; i < numColumns; i++) {
            editors[i] = new TextCellEditor(tableViewer.getTable());
        }

        return editors;
    }

    /**
     * Creates the cell modifier to use with the table viewer.  Subclasses
     * should override this method to supply an appropriate cell modifier for
     * their domain objects. In this way the subclass controls which cells of
     * the table are modifiable and provides the knowledge for modifying
     * a domain object. This override is only necessary when a
     * SelectionDialog is not being used by the ListValueBuilder.
     * <strong>
     * It is important that the implementation of the {@link ICellModifier}
     * <code>public void modify(Object element, String property, Object value)</code>
     * method does something similar to the following to inform the
     * ListValueBuilder's model of a domain object modification:
     * </strong>
     * <pre>
     * TableItem item = (TableItem) element;
     * int index = tableViewer.getTable().indexOf(item);
     * getModel().itemChanged(index, value);
     * </pre>
     * @return a cell modifier
     */
    protected ICellModifier createCellModifier() {
        return new ICellModifier() {

            // javadoc inherited
            public boolean canModify(Object element, String property) {
                // All cells are modifiable.
                return true;
            }

            // javadoc inherited
            public Object getValue(Object element, String property) {
                return (String) element;
            }

            // javadoc inherited
            public void modify(Object element, String property,
                               Object value) {
                TableItem item = (TableItem) element;
                String domainObject = (String) item.getData();

                // Only process a change if the value has actually
                // changed.
                if (!value.equals(domainObject)) {
                    int index = tableViewer.getTable().indexOf(item);
                    itemsList.itemChanged(index, value);
                }
            }
        };
    }

    /**
     * Creates and returns the label provider used by the {@link TableViewer}
     * created with {@link #createTableViewer}. This implementation assumes
     * domain objects of type String. Subclasses should override this method to
     * supply appropriate label providers for their table cells. In this way
     * the correct text and images to display for domain objects can be
     * specified.
     *
     * @return the label provider
     */
    protected ITableLabelProvider createLabelProvider() {
        return new ITableLabelProvider() {
            // javadoc inherited
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            // javadoc inherited
            public String getColumnText(Object element, int columnIndex) {
                return (String) element;
            }

            // javadoc inherited
            public void addListener(ILabelProviderListener listener) {
            }

            // javadoc inherited
            public void dispose() {
            }

            // javadoc inherited
            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            // javadoc inherited
            public void removeListener(ILabelProviderListener listener) {
            }
        };
    }

    /**
     * Creates and returns the TableViewer used by this control. Subclasses
     * should override this method to supply the TableViewer and Table
     * widget that works with their domain objects. Note that you must set
     * column properties with {@link TableViewer#setColumnProperties} as these
     * property names are used by the {@link ITableLabelProvider} and
     * {@link ICellModifier} to determine which column is being operated upon.
     *
     * @param container the Composite container for the table
     */
    protected TableViewer createTableViewer(Composite container) {
        // Create a single-selection table with scroll bars.
        Table table = new Table(container, SWT.SINGLE | SWT.H_SCROLL |
                SWT.V_SCROLL | SWT.BORDER);

        // Create the table's viewer.
        TableViewer tableViewer = new TableViewer(table);

        // Set a dummy columnn ID. This must be non-null.
        tableViewer.setColumnProperties(new String[]{"values"});

        // create a column
        final TableColumn column = new TableColumn(table, SWT.LEFT);

        // Sub-class the TableLayout in order to pad the 1st column (there should only be
        // one column) so that its width is the table clientarea width.
        // This is a workaround for Windows which doesn't do this by default.
        TableLayout tableLayout = new TableLayout() {
            public void layout(Composite c, boolean flush) {
                super.layout(c, flush);
                // Ensure we are laying out a table.
                if (c instanceof Table) {
                    int tableWidth = c.getClientArea().width;
                    // set the column to be the width of the table
                    column.setWidth(tableWidth);
                }
            }
        };
        tableViewer.getTable().setLayout(tableLayout);
        return tableViewer;
    }

    /**
     * Creates a new "default" domain object which is added to the model when
     * the Add button is pressed. Subclasses should override this method to
     * provide a "default" domain object to add to the table when the Add
     * button is pressed. This override is only necessary when a
     * SelectionDialog is not being used by the ListValueBuilder.
     *
     * @return a new "default" domain object
     */
    protected Object createNewItem() {
        return "";
    }

    /**
     * Get the model used by this ListValueBuilder.
     * @return the model.
     */
    protected final ItemsList getModel() {
        return itemsList;
    }

    /**
     * Creates and adds buttons arranged vertically. The buttons are
     * aligned with the top of the Table widget and are Add (or Browse),
     * Remove and optionally Up and Down buttons.
     *
     * @param container the button panel's container
     */
    private Composite addButtonPanel(Composite container) {
        buttonComposite = new Composite(container, SWT.NONE);
        GridLayout buttonGridLayout = new GridLayout();
        buttonGridLayout.verticalSpacing = VERTICAL_SPACING;
        buttonGridLayout.marginHeight = 0;
        buttonGridLayout.marginWidth = 0;
        buttonComposite.setLayout(buttonGridLayout);
        GridData panelGridData = new GridData(GridData.VERTICAL_ALIGN_FILL);
        buttonComposite.setLayoutData(panelGridData);
        addAddOrBrowseButton(buttonComposite);

        removeButton = createButton(buttonComposite, REMOVE_TEXT);
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleRemoveButtonSelection();
            }
        });

        if (ordered) {
            // Items are orderable so add Up and Down buttons.
            upButton = createButton(buttonComposite, UP_TEXT);
            upButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    handleUpButtonSelection();
                }
            });

            downButton = createButton(buttonComposite, DOWN_TEXT);
            downButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    handleDownButtonSelection();
                }
            });
        }
        return buttonComposite;
    }

    /**
     * Creates a Button widget.
     *
     * @param container the button's container
     * @param text the text for the button
     * @return the button widget
     */
    private Button createButton(Composite container, String text) {
        Button button = new Button(container, SWT.PUSH);
        button.setText(text);
        button.setEnabled(false);
        GridData buttonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL);
        button.setLayoutData(buttonData);
        return button;
    }

    /**
     * Creates and adds an Add (no selection dialog specified) or Browse
     * (selection dialog was specified) button and associated SelectionListener.
     *
     * @param container the button's container
     */
    private void addAddOrBrowseButton(Composite container) {
        addOrBrowseButton = new Button(container, SWT.PUSH);
        addOrBrowseButton.setEnabled(true);

        addOrBrowseButton.setText(selectionDialog == null ?
                ADD_TEXT : BROWSE_TEXT);

        if (selectionDialog == null) {
            addOrBrowseButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    handleAddButtonSelection();
                }
            });
        } else {
            addOrBrowseButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    handleBrowseButtonSelection();
                }
            });

        }

        GridData addButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL |
                GridData.VERTICAL_ALIGN_END);
        addOrBrowseButton.setLayoutData(addButtonData);
    }

    /**
     * Handles pressing the Add button. A new object is appended to the model
     * and the table viewer subsequently starts editing it. The new object is
     * created with {@link #createNewItem}.
     */
    private void handleAddButtonSelection() {
        final Object newItem = createNewItem();
        // Add the item to the model.
        itemsList.addItem(newItem);
        // Start editing the item in the first column.
        tableViewer.reveal(newItem);
        tableViewer.editElement(newItem, 0);
    }

    /**
     * Handles pressing the Browse button. This populates the selection dialog
     * with the current selection, opens the selection dialog, and retrieves
     * the results when the dialog is closed.
     */
    private void handleBrowseButtonSelection() {
        Object[] originalSelection = itemsList.getItems().toArray();
        selectionDialog.setInitialSelections(originalSelection);
        selectionDialog.open();
        itemsList.setItems(selectionDialog.getResult());
    }

    /**
     * Handles pressing the remove button. The item is removed from the model.
     */
    private void handleRemoveButtonSelection() {
        Table table = tableViewer.getTable();
        // This is casting upwards. However, this is what is done in many
        // Eclipse examples, and in other parts of MCS.
        Object domainObject = ((IStructuredSelection)
                tableViewer.getSelection()).getFirstElement();

        int index = table.getSelectionIndex();
        itemsList.removeItem(domainObject);
        // Select the "next" item after a removal and update the Remove
        // button, after an item has been removed from the model.
        if (index == table.getItemCount()) {
            index--;
        }
        table.setSelection(index);

        updateButtons();
    }

    /**
     * Handles pressing the down button.
     */
    private void handleDownButtonSelection() {
        moveItem(1);
    }

    /**
     * Handles pressing the up button.
     */
    private void handleUpButtonSelection() {
        moveItem(-1);
    }

    /**
     * Moves an item one position up or down the list.
     *
     * @param direction the direction to move. A negative value
     * moves the item up the list; a positive value moves it down.
     */
    private void moveItem(int direction) {
        direction = direction < 0 ? -1 : 1;
        int index = tableViewer.getTable().getSelectionIndex();
        itemsList.swapItems(index, index + direction);
    }

    /**
     * Updates the ListValueBuilder's buttons by enabling or disabling them
     * depending upon the current status of the ListValueBuilder.
     */
    private void updateButtons() {
        int selectionIndex = tableViewer.getTable().getSelectionIndex();
        int numItems = tableViewer.getTable().getItemCount();
        if (upButton != null) {
            upButton.setEnabled(selectionIndex > 0);
        }
        if (downButton != null) {
            downButton.setEnabled(selectionIndex >= 0 &&
                    selectionIndex < numItems - 1);
        }
        removeButton.setEnabled(selectionIndex >= 0);
    }

    /**
     * Fires a ModifyEvent to all registered listeners when list items
     * are added, removed, modified or re-ordered. This method is used by
     * the ListValueBuilder widget to inform any external listeners, and also by
     * the ItemsList model to inform its listeners. The widget field of the
     * ModifyEvent is this ListValueBuilder.
     *
     * @param listeners the list of listeners to inform
     */
    private void fireModifyEvent(ListenerList listeners) {
        Object[] interested = listeners.getListeners();
        if (interested != null && interested.length > 0) {
            Event event = new Event();
            event.widget = this;
            event.data = null;
            ModifyEvent me = new ModifyEvent(event);
            for (int i = 0; i < interested.length; i++) {
                if (interested[i] != null) {
                    ((ModifyListener) interested[i]).modifyText(me);
                }
            }
        }
    }

    /**
     * Populates the ListValueBuilder with the specified items.
     *
     * @param items the items for the ListValueBuilder
     */
    public void setItems(Object[] items) {
        // Create the new list.
        itemsList.removeModifyListener(itemsListModifyListener);
        try {
            itemsList.setItems(items);
        } finally {
            itemsList.addModifyListener(itemsListModifyListener);
        }
        tableViewer.refresh();
    }

    /**
     * Gets the current list of items displayed by the ListValueBuilder.
     *
     * @return the items
     */
    public Object[] getItems() {
        List items = itemsList.getItems();
        Object[] values = new Object[items.size()];
        items.toArray(values);
        return values;
    }

    /**
     * Adds a ModifyListener that is called when the contents of the
     * ListValueBuilder changes.
     *
     * @param listener the listener to add
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a ModifyListener.
     *
     * @param listener the listener to remove
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    // javadoc inherited
    public void setBackground(Color bgColor) {
        super.setBackground(bgColor);
        buttonComposite.setBackground(bgColor);
        tableViewer.getTable().setBackground(bgColor);
    }

    /**
     * Enables or disables this ListValueBuilder. Buttons are only enabled if
     * they comply with the current list selection.
     *
     * @param enable the enablement status
     */
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        ControlUtils.setEnabledHierarchical(this, enable);

        if (enable) {
            // Enable or disable the buttons according to the list selection
            // status.
            updateButtons();
        }
    }


    // javadoc inherited
    public void addSelectionChangedListener(
            ISelectionChangedListener listener) {
        if (listener != null) {
            tableViewer.addSelectionChangedListener(listener);
        }
    }

    // javadoc inherited
    public ISelection getSelection() {
        return tableViewer.getSelection();
    }

    // javadoc inherited
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        if (listener != null) {
            tableViewer.removeSelectionChangedListener(listener);
        }
    }

    // javadoc inherited
    public void setSelection(ISelection selection) {
        tableViewer.setSelection(selection);
    }

    // Javadoc inherited
    public Point computeSize(int wHint, int hHint, boolean changed) {
        // Calculate the size based on the height of the corresponding
        // buttonComposite to prevent the ListValueBuilder from expanding
        // as new entries are added
        return new Point(super.computeSize(wHint, hHint, changed).x,
        buttonComposite.computeSize(wHint, hHint, changed).y);
    }

    /**
     * The content provider implementation used by this control. It returns
     * the content as an array of items in the model {@link ItemsList}.
     */
    private final class ListBuilderContentProvider
            implements IStructuredContentProvider {
        // javadoc inherited
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        // javadoc inherited
        public void dispose() {
        }

        // Return the items as an array of Objects
        public Object[] getElements(Object parent) {
            return itemsList.getItems().toArray();
        }
    }

    /**
     * The model (M) used by the content provider for the table viewer (V)
     * and its underlying Table widget (C), giving the MVC pattern.
     */
    protected final class ItemsList {
        /**
         * The list of items in the model.
         */
        private List items = new ArrayList();

        /**
         * The list of listeners interested in model changes.
         */
        private ListenerList modelListeners;

        /**
         * Constructs a new ItemsList.
         */
        ItemsList() {
            modelListeners = new ListenerList();
        }

        /**
         * Sets the items that this ItemsList contains.
         *
         * @param items the array of items. Can be null or empty.
         */
        void setItems(Object[] items) {
            this.items.clear();
            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    this.items.add(items[i]);
                }
            }
            fireModifyEvent(modelListeners);
        }
        /**
         * Return the list of items.
         */
        List getItems() {
            return items;
        }

        /**
         * Adds a new item to the model and informs any listeners of the change
         * with a ModifyEvent.
         */
        void addItem(Object item) {
            items.add(item);
            fireModifyEvent(modelListeners);
        }

        /**
         * Removes an item from the model and informs any listeners of the
         * change with a ModifyEvent.
         */
        void removeItem(Object item) {
            items.remove(item);
            fireModifyEvent(modelListeners);
        }

        /**
         * Called when an item in the model is editable and has been modified.
         * The editable nature of an item is determined by the presence of a
         * cell editor in the TableViewer for that item's cell together with
         * whether or not the cell should be editable (specified by the
         * {@link ICellModifier} implementation. Any model listeners are
         * informed of the change via a ModifyEvent. This method is public to
         * allow subclasses to comunicate a domain object modification to the
         * model.
         *
         * @param index the index of the modified item
         * @param newItem the new value
         */
        public void itemChanged(int index, Object newItem) {
            items.set(index, newItem);
            fireModifyEvent(modelListeners);
        }

        /**
         * Called when an item is moved up or down. Since two items are
         * affected here, this updates the model and fires off only one
         * ModifyEvent to registered listeners.
         *
         * @param origIndex the original index of the item
         * @param newIndex the new index for the item
         */
        void swapItems(int origIndex, int newIndex) {
            Object item1 = items.get(origIndex);
            Object item2 = items.get(newIndex);
            items.set(origIndex, item2);
            items.set(newIndex, item1);
            fireModifyEvent(modelListeners);
        }

        /**
         * Adds items to the model and informs registered listeners of the
         * change with a ModifyEvent.
         *
         * @param items the items to add
         */
        void addItems(Object[] items) {
            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    this.items.add(items[i]);
                }
                fireModifyEvent(modelListeners);
            }
        }

        /**
         * Adds a ModifyListener which listens for model modifications.
         *
         * @param listener the listener to add
         */
        void addModifyListener(ModifyListener listener) {
            if (listener != null) {
                modelListeners.add(listener);
            }
        }

        /**
         * Removes a ModifyListener.
         *
         * @param listener the listener to remove
         */
        void removeModifyListener(ModifyListener listener) {
            if (listener != null) {
                modelListeners.remove(listener);
            }
        }
    }

    /**
     * Set the accessible name for this list value builder.
     *
     * @param name The accessible name
     */
    public void setAccessibleName(String name) {
        accessibleName = name;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener al =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = accessibleName;
                    }
                };
        tableViewer.getControl().getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 04-Nov-04	6100/1	adrianj	VBM:2004102101 Fix for ListValueBuilder sizing

 10-Sep-04	5479/3	tom	VBM:2004081805 ensured column width is set to entire table area (for windows)

 13-Aug-04	5202/1	allan	VBM:2004072803 Fix enablement of ListValueBuilder.

 14-May-04	4367/5	doug	VBM:2004051108 Ensured that the MasterValueSection refreshes whenever the policies type changes

 14-May-04	4367/3	doug	VBM:2004051108 Ensured that the MasterValueSection refreshes whenever the policies type changes

 11-May-04	4250/3	pcameron	VBM:2004051005 Added Restore Defaults button and changed ODOMElement and StandardElementHandler to deal with listener removal

 10-May-04	4235/1	pcameron	VBM:2004031603 Added MasterValueSection

 07-May-04	4172/11	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 07-May-04	4172/9	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 04-May-04	4007/3	doug	VBM:2004032304 Added a PrimaryPatterns form section

 21-Apr-04	3968/4	pcameron	VBM:2004032402 setSelection delegates right to tableviewer

 21-Apr-04	3968/2	pcameron	VBM:2004032402 ListValueBuilder now implements ISelectionProvider

 21-Apr-04	3909/3	pcameron	VBM:2004031004 Some rework issues for CategoryCompositeBuilder

 20-Apr-04	3909/1	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 19-Mar-04	3416/17	pcameron	VBM:2004022309 Some tweaks to ListValueBuilder and ListPolicyValueModifier

 19-Mar-04	3416/15	pcameron	VBM:2004022309 Some tweaks to ListValueBuilder and ListPolicyValueModifier

 18-Mar-04	3416/13	pcameron	VBM:2004022309 Added ListValueBuilder and ListPolicyValueModifier with tests

 ===========================================================================
*/
