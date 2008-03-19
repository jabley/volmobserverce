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

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * The FixedListSelectionDialog allows the user to select a set of
 * values from a supplied set of available values. The available values are
 * shown in a list on the left of the dialog. The selected values are shown in
 * a list on the right of the dialog. A selection is made by moving values
 * between the two lists using buttons. The user can specify whether the
 * dialog allows the selection of duplicate values.
 */
public class FixedListSelectionDialog extends SelectionDialog {

    /**
     * Resource prefix for the FixedListSelectionDialog.
     */
    private final static String RESOURCE_PREFIX = "FixedListSelectionDialog.";

    /**
     * The text for the available items list's label.
     */
    private static final String LEFT_LIST_TITLE =
            ControlsMessages.getString(RESOURCE_PREFIX + "leftLabel");

    /**
     * The text for the selected items list's label.
     */
    private static final String RIGHT_LIST_TITLE =
            ControlsMessages.getString(RESOURCE_PREFIX + "rightLabel");

    /**
     * The text for the to left button.
     */
    private static final String TO_LEFT_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "leftButton.text");

    /**
     * The accessible name for the to left button.
     */
    private static final String TO_LEFT_NAME =
            ControlsMessages.getString(RESOURCE_PREFIX + "leftButton.name");

    /**
     * The text for the to right button.
     */
    private static final String TO_RIGHT_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "rightButton.text");

    /**
     * The accessible name for the to right button.
     */
    private static final String TO_RIGHT_NAME =
            ControlsMessages.getString(RESOURCE_PREFIX + "rightButton.name");

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "horizontalSpacing").
            intValue();

    /**
     * The margin height of the dialog.
     */
    private static final int MARGIN_HEIGHT =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "marginHeight").
            intValue();

    /**
     * The margin width of the dialog.
     */
    private static final int MARGIN_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "marginWidth").
            intValue();

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "verticalSpacing").
            intValue();

    /**
     * The height hint for each list widget.
     */
    private static final int HEIGHT_HINT =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "list.heightHint").
            intValue();

    /**
     * The width hint for each list widget.
     */
    private static final int WIDTH_HINT =
            ControlsMessages.getInteger(RESOURCE_PREFIX + "list.widthHint").
            intValue();

    /**
     * The ListController is used to modify the contents of a List widget. The
     * default implementation adds and removes items in the usual manner, but
     * maintains the original item order throughout if the list had content
     * when the controller was constructed.
     */
    private static class ListController {
        /**
         * The List widget of the controller.
         */
        public final List list;

        /**
         * The contents of the List widget when the controller is constructed.
         * This is used to maintain the original order of the lists contents
         * when adding items.
         */
        private final String[] contents;

        /**
         * Creates a new ListController for modifying the contents of the
         * supplied List widget. If the List has contents, the contents are
         * used to maintain the original item order when items are added back.
         * @param list the List widget. Cannot be null.
         * @throws IllegalArgumentException if list is null
         */
        public ListController(List list) {
            if (list == null) {
                throw new IllegalArgumentException("Cannot be null: list");
            }
            this.list = list;
            this.contents = list.getItems();
        }

        /**
         * Computes the difference between the original indices of the two
         * items. If there was no content for the list at this controller's
         * construction time then 1 is returned.
         * @param item1 the first item
         * @param item2 the second item
         * @return the difference between indices <item1index> - <item2index>
         *         if the list has content; otherwise 1 to indicate that item2
         *         comes after item1 by default (in the absence of any ordering).
         */
        protected int indexDifference(String item1, String item2) {
            int val = 1;
            if (contents.length > 0) {
                val = getIndexOf(contents, item1) -
                        getIndexOf(contents, item2);
            }
            return val;
        }

        /**
         * Adds the supplied items to the list's contents. If the list had
         * content when the controller was constructed, the original item order
         * is maintained. Otherwise, the items are added in the order given.
         * @param items the items to add
         */
        public void add(String[] items) {
            if (items != null && items.length > 0) {

                // Loop through each item to be added.
                for (int j = 0; j < items.length; j++) {
                    String newItem = items[j];
                    boolean found = false;

                    // Loop through each item in the list, comparing it
                    // against the current new item. Add the new item when
                    // the first list item that comes after it is found.
                    for (int i = 0; i < list.getItemCount() && !found; i++) {
                        int diff = indexDifference(newItem, list.getItem(i));
                        if (diff < 0) {
                            // The new item comes before the current item,
                            // so insert the new item at the same position.
                            list.add(newItem, i);
                            found = true;
                        }
                    }
                    if (!found) {
                        // The new item comes after all items in the list so
                        // add it to the end.
                        list.add(newItem, list.getItemCount());
                    }
                }
            }
        }

        /**
         * Removes the supplied items from the list's contents.
         * @param items the items to remove
         */
        public void remove(String[] items) {
            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    list.remove(items[i]);
                }
            }
        }
    }

    /**
     * The List widget used by the dialog for the available values.
     */
    private List availableItemsList;

    /**
     * The List widget used by the dialog for the selected values.
     */
    private List selectedItemsList;

    /**
     * The "to left" button. The button's state is updateable.
     */
    private Button toLeftButton;

    /**
     * The "to right" button. The button's state is updateable.
     */
    private Button toRightButton;

    /**
     * The current selection for the dialog. This is returned by
     * {@link #getResult}.
     */
    private String[] currentSelection;

    /**
     * The available items for populating the available items list. These
     * values are provided at construction time.
     */
    private String[] availableItems;

    /**
     * A flag indicating whether this dialog allows selections to contain
     * duplicate values.
     */
    private final boolean duplicatesAllowed;

    /**
     * The initial selection of items, if any. These values are supplied
     * post-construction with {@link #setInitialSelections}.
     */
    private String[] initialSelection;

    /**
     * The controller which modifies the contents of the available items list.
     */
    private ListController leftListController;

    /**
     * The controller which modifies the contents of the selected items list.
     */
    private ListController rightListController;

    /**
     * Constructs a new FixedListSelectionDialog.
     * @param parent the parent Shell for the dialog. Cannot be null.
     * @param availableItems an array of available values used to populate the
     *                        dialog's left list. Cannot be null.
     * @param duplicatesAllowed a flag indicating whether the dialog allows
     *                          duplicate values to be selected.
     * @throws IllegalArgumentException if availableItems is null.
     */
    public FixedListSelectionDialog(Shell parent,
                                    String[] availableItems,
                                    boolean duplicatesAllowed) {
        super(parent);
        if (availableItems == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "availableItems");
        }
        this.availableItems = availableItems;
        this.duplicatesAllowed = duplicatesAllowed;

        setBlockOnOpen(true);
    }

    /**
     * Constructs a new FixedListSelectionDialog for situations when the
     * available items are not yet known, but can be supplied post-construction
     * by the subclass with {@link #setAvailableSelection}.
     * @param parent the parent Shell for the dialog. Cannot be null.
     * @param duplicatesAllowed a flag indicating whether the dialog allows
     *                          duplicate values to be selected.
     */
    protected FixedListSelectionDialog(Shell parent,
                                       boolean duplicatesAllowed) {
        super(parent);
        this.duplicatesAllowed = duplicatesAllowed;
    }

    /**
     * Creates the dialog's controls. This is called each time the dialog
     * is opened.
     * @param parent the parent Composite of the dialog's control
     * @return the created control
     */
    protected Control createDialogArea(Composite parent) {
        // The available items should exist at this point. If not, something
        // has gone wrong, so throw an exception. The subclass may not have
        // called {@link setAvailableSelection}.
        if (availableItems == null) {
            throw new IllegalStateException("There are no available items " +
                    "for the dialog.");
        }
        Composite topLevel = (Composite) super.createDialogArea(parent);
        GridLayout topLevelGrid = new GridLayout(2, true);
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.marginHeight = MARGIN_HEIGHT;
        topLevelGrid.marginWidth = MARGIN_WIDTH;
        topLevel.setLayout(topLevelGrid);

        // Add the list titles as labels.
        addLabels(topLevel);

        // Create container for left list, move left and move right buttons.
        Composite leftComposite = new Composite(topLevel, SWT.NONE);
        GridLayout compGridLayout = new GridLayout(2, false);
        compGridLayout.marginHeight = 0;
        compGridLayout.marginWidth = 0;
        leftComposite.setLayout(compGridLayout);
        GridData leftData = new GridData(GridData.FILL_BOTH);
        leftComposite.setLayoutData(leftData);

        availableItemsList = addList(leftComposite);

        availableItemsList.setItems(availableItems);
        createButtonPanel(leftComposite);

        selectedItemsList = addList(topLevel);

        addListControllers();
        configureDND();

        if (initialSelection != null) {
            // Add the initial selection.
            rightListController.add(initialSelection);
            leftListController.remove(initialSelection);
            currentSelection = initialSelection;
        }

        initAccessible();
        return topLevel;
    }


    /**
     * Configure drag-n-drop for the left list. This left side can never
     * contain duplicates. Sometimes it can act as a drag target but only
     * when duplicates are not allowed in the right list.
     */
    private void configureLeftDND() {
        final StringArrayTransfer transfer =
                StringArrayTransfer.getInstance();

        DragSource leftDragSource;

        // Provide data in Text format
        Transfer[] types = new Transfer[]{
            transfer,
        };

        if (duplicatesAllowed) {
            // This controller does not modify the list's contents because
            // duplicates are allowed.
            leftDragSource = new DragSource(leftListController.list,
                    DND.DROP_COPY);
        } else {
            leftDragSource = new DragSource(leftListController.list,
                    DND.DROP_MOVE);

            // Since move is allowed we need a drop target for the left list
            DropTarget leftDropTarget = new DropTarget(leftListController.list,
                    DND.DROP_MOVE);
            leftDropTarget.setTransfer(types);
            leftDropTarget.addDropListener(new DropTargetAdapter() {
                public void dragOver(DropTargetEvent event) {
                    event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
                }

                public void drop(DropTargetEvent event) {
                    if (event.data == null) {
                        event.detail = DND.DROP_NONE;
                    } else {
                        leftListController.add((String[]) event.data);
                        // Update the current selection.
                        currentSelection = selectedItemsList.getItems();
                    }
                }
            });
        }

        leftDragSource.setTransfer(types);

        leftDragSource.addDragListener(new DragSourceListener() {
            public void dragStart(DragSourceEvent event) {
                // Only start the drag if there is something selected
                event.doit = leftListController.list.getSelectionCount() > 0;
            }

            public void dragSetData(DragSourceEvent event) {
                // Provide the data of the requested type.
                if (StringArrayTransfer.getInstance().
                        isSupportedType(event.dataType)) {
                    event.data = leftListController.list.getSelection();
                }
            }

            public void dragFinished(DragSourceEvent event) {
                // If a move operation has been performed, remove the data
                // from the source
                if (event.detail == DND.DROP_MOVE) {
                    leftListController.remove(leftListController.list.
                            getSelection());
                }
            }
        });
    }

    /**
     * Configure drag-n-drop for the right. The right list is always a target
     * but only a source if duplicates are not allowed in the right list.
     */
    private void configureRightDND() {

        final StringArrayTransfer transfer = StringArrayTransfer.getInstance();
        // Provide data in String array format
        Transfer[] types = new Transfer[]{
            transfer
        };

        if (!duplicatesAllowed) {
            DragSource rightDragSource =
                    new DragSource(rightListController.list, DND.DROP_MOVE);

            // Provide data in Text format
            rightDragSource.setTransfer(types);

            rightDragSource.addDragListener(new DragSourceListener() {
                public void dragStart(DragSourceEvent event) {
                    // Only start the drag if there is a selection
                    event.doit =
                            rightListController.list.getSelectionCount() > 0;
                }

                public void dragSetData(DragSourceEvent event) {
                    // Provide the data of the requested type.
                    if (StringArrayTransfer.getInstance().
                            isSupportedType(event.dataType)) {
                        event.data = rightListController.list.getSelection();
                    }
                }

                public void dragFinished(DragSourceEvent event) {
                    rightListController.remove(rightListController.list.
                            getSelection());
                }
            });
        }

        // Creates the controller for the selected items list. This controller
        // adds and removes items from the list in the usual way.
        DropTarget rightDropTarget = new DropTarget(selectedItemsList,
                DND.DROP_COPY | DND.DROP_MOVE);

        rightDropTarget.setTransfer(types);

        rightDropTarget.addDropListener(new DropTargetAdapter() {

            public void dragOver(DropTargetEvent event) {
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
            }

            public void drop(DropTargetEvent event) {
                if (event.data == null) {
                    event.detail = DND.DROP_NONE;
                } else {
                    rightListController.add((String[]) event.data);
                    // Update the current selection.
                    currentSelection = selectedItemsList.getItems();
                }
            }

        });
    }

    /**
     * Configure drag-n-drop for the FixedListSelectionDialog.
     */
    private void configureDND() {
        configureLeftDND();
        configureRightDND();
    }

    /**
     * Creates and adds the ListControllers.
     */
    private void addListControllers() {
        // Creates the controller for the available items list.
        if (duplicatesAllowed) {
            // This controller does not modify the list's contents because
            // duplicates are allowed.
            leftListController = new ListController(availableItemsList) {
                /**
                 * Overridden to do nothing when duplicates are allowed.
                 * @param items
                 */
                public void add(String[] items) {
                }

                /**
                 * Overridden to do nothing when duplicates are allowed.
                 * @param items
                 */
                public void remove(String[] items) {
                }
            };
        } else {
            leftListController = new ListController(availableItemsList);
        }

        rightListController = new ListController(selectedItemsList);
    }

    /**
     * Create and add the lists' titles to the dialog.
     * @param container the parent Composite for the titles
     */
    private void addLabels(Composite container) {
        Label selectorLabel = new Label(container, SWT.NONE);
        selectorLabel.setText(LEFT_LIST_TITLE);
        Label selectedLabel = new Label(container, SWT.NONE);
        selectedLabel.setText(RIGHT_LIST_TITLE);
    }

    /**
     * Adds a list widget with multi-select capabilities, scroll bars and a
     * border. A SelectionListener to update the buttons when selections are
     * made.
     * @param parent the parent Composte for the list
     * @return the List widget
     */
    private List addList(Composite parent) {
        List list = new List(parent, SWT.MULTI |
                SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData listGridData = new GridData(GridData.FILL_BOTH);
        listGridData.heightHint = HEIGHT_HINT;
        listGridData.widthHint = WIDTH_HINT;
        list.setLayoutData(listGridData);
        list.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                updateButtons();
            }
        });
        return list;
    }

    /**
     * Creates and adds a control of two buttons arranged vertically. The
     * buttons move or copy items between the two lists.
     * @param parent the parent Composite of the button control
     */
    private void createButtonPanel(Composite parent) {
        Composite buttonComp = new Composite(parent, SWT.NONE);
        GridLayout compGridLayout = new GridLayout();
        compGridLayout.verticalSpacing = VERTICAL_SPACING;
        compGridLayout.marginHeight = 0;
        compGridLayout.marginWidth = 0;
        buttonComp.setLayout(compGridLayout);
        GridData compGridData = new GridData(GridData.VERTICAL_ALIGN_FILL);
        buttonComp.setLayoutData(compGridData);

        toRightButton = createButton(buttonComp, TO_RIGHT_TEXT);
        toRightButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                updateLists(leftListController, rightListController);
            }
        });

        toLeftButton = createButton(buttonComp, TO_LEFT_TEXT);
        toLeftButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                updateLists(rightListController, leftListController);
            }
        });
    }

    /**
     * Creates and returns a button.
     * @param parent the parent Composite of the button
     * @param text the text for the button
     * @return the Button widget
     */
    private Button createButton(Composite parent, String text) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(text);
        button.setEnabled(false);
        GridData buttonGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        button.setLayoutData(buttonGridData);
        return button;
    }

    /**
     * Updates both List widgets when the left or right button has been pressed.
     * @param from the ListController controlling the source of the selected
     *             items
     * @param to the ListController controlling the destination for the
     *           selected items
     */
    private void updateLists(ListController from, ListController to) {
        String[] selectedItems = from.list.getSelection();
        int highlightIndex = from.list.getSelectionIndices()[0];

        // Remove the selection from the source
        from.remove(selectedItems);
        // Add the selection to the destination
        to.add(selectedItems);

        // Update the current selection.
        currentSelection = selectedItemsList.getItems();

        // Highlight the "next" item.
        if (highlightIndex == from.list.getItemCount()) {
            highlightIndex--;
        }
        from.list.setSelection(highlightIndex);

        updateButtons();
    }

    /**
     * Enables or disables buttons according to the current selection.
     */
    private void updateButtons() {
        toLeftButton.setEnabled(selectedItemsList.getSelectionCount() > 0);
        toRightButton.setEnabled(availableItemsList.getSelectionCount() > 0);
    }

    /**
     * Gets the original index of the supplied item in the available items
     * list.
     * @param items the array of items to search
     * @param item the item to look for
     * @return the index if found, -1 if item not present.
     */
    private static int getIndexOf(String[] items, String item) {
        boolean found = false;
        int i = 0;
        if (items != null) {
            for (i = 0; i < items.length && !found; i++) {
                found = items[i].equals(item);
            }
        }
        return found ? i - 1 : -1;
    }

    /**
     * Populates the selected items list with the supplied pre-selected items.
     * The available items list is adjusted accordingly.
     * @param selectedItems the pre-selected items for the list
     * @throws IllegalStateException if selectedItems contains items that are
     *                               not present in the available items
     *                               specified at construction, or if a
     *                               duplicate item is present when the dialog
     *                               does not accept duplicates.
     * @throws IllegalArgumentException if selectedItems is null
     */
    public void setInitialSelections(Object[] selectedItems) {
        if (selectedItems == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "selectedItems.");
        }
        // The available items should exist at this point. If not, something
        // has gone wrong, so throw an exception. The subclass may not have
        // called {@link setAvailableSelection}.
        if (availableItems == null) {
            throw new IllegalStateException("There are no available items " +
                    "for the dialog.");
        }
        ArrayList initialSelectionList = new ArrayList(selectedItems.length);
        for (int i = 0; i < selectedItems.length; i++) {
            String item = (String)selectedItems[i];

            // Is the selection item in the available list, if not discard it
            if (getIndexOf(availableItems, item) != -1) {

                // If duplicates are not allowed and we already have this
                // item throw an exception
                if (!duplicatesAllowed &&
                    initialSelectionList.contains(item)) {
                    throw new IllegalStateException("Value " + item +
                        " is duplicated. " + this.getClass() +
                        " does not accept duplicate values.");
                } else {
                    initialSelectionList.add(item);
                }
            }
        }

        // Copy to String array
        initialSelection = new String[initialSelectionList.size()];
        initialSelectionList.toArray(initialSelection);

    }

    /**
     * Sets the available items for the dialog. The left list is populated
     * with these items.
     * @param availableItems the items for the list. Cannot be null or empty.
     * @throws IllegalArgumentException if availableItems is null.
     */
    protected void setAvailableSelection(Object[] availableItems) {
        if (availableItems == null) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    "availableItems.");
        }
        this.availableItems = (String[]) availableItems;
    }

    // javadoc inherited
    public Object[] getResult() {
        return currentSelection;
    }

    /**
     * Overidden to clear a selection when Cancel is pressed. Calls to
     * {@link #getResult} will subsequently return null.
     */
    protected void cancelPressed() {
        currentSelection = null;
        super.cancelPressed();
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        initAccessibleControl(toLeftButton, TO_LEFT_NAME);
        initAccessibleControl(toRightButton, TO_RIGHT_NAME);
        initAccessibleControl(availableItemsList, LEFT_LIST_TITLE);
        initAccessibleControl(selectedItemsList, RIGHT_LIST_TITLE);
    }

    /**
     * Initialise accessibility listener for a specified subcontrol.
     */
    private void initAccessibleControl(Control control, final String name) {
        StandardAccessibleListener accessibleListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = name;
                    }
                };
        accessibleListener.setControl(control);
        control.getAccessible().addAccessibleListener(accessibleListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 11-May-04	4276/1	byron	VBM:2004051007 New policy creation fails for selection type policies

 10-Mar-04	3383/4	pcameron	VBM:2004030412 Some tweaks to PolicyValueSelectionDialog

 10-Mar-04	3383/1	pcameron	VBM:2004030412 Added PolicyValueSelectionDialog

 10-Mar-04	3335/31	pcameron	VBM:2004030411 Removed redundant array in list controller's add method

 09-Mar-04	3335/28	pcameron	VBM:2004030411 Some further tweaks to FixedListSelectionDialog

 09-Mar-04	3335/21	pcameron	VBM:2004030411 Some further tweaks to FixedListSelectionDialog

 09-Mar-04	3335/18	pcameron	VBM:2004030411 Refactored FixedListSelectionDialog to use list controllers

 09-Mar-04	3335/14	pcameron	VBM:2004030411 Some tweaks to FixedListSelectionDialog

 08-Mar-04	3335/10	pcameron	VBM:2004030411 Added FixedListSelectionDialog

 ===========================================================================
*/
