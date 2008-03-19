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

import com.volantis.mcs.themes.properties.FontFamilyKeywords;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.utilities.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import java.text.MessageFormat;

/**
 * A FontFamilySelectionDialog for selecting and returning a selection of
 * fonts.
 */
public class FontFamilySelectionDialog extends MessageAreaSelectionDialog {

    /**
     * The FontSelector used by the dialog.
     */
    private FontSelector fontSelector;

    /**
     * The fonts used by the FontSelector.
     */
    private String[] fontSelectorFonts;

    /**
     * The List widget used by the dialog for selections.
     */
    private List selectionList;

    /**
     * The "move left" button. The button's state is updateable.
     */
    private Button moveLeftButton;

    /**
     * The "move right" button. The button's state is updateable.
     */
    private Button moveRightButton;

    /**
     * The Up button. The button's state is updateable.
     */
    private Button upButton;

    /**
     * The Down button. The button's state is updateable.
     */
    private Button downButton;

    /**
     * The current selection for the dialog.
     */
    private String[] currentSelection;

    /**
     * Resource prefix for the FontFamilytSelectionDialog.
     */
    private final static String RESOURCE_PREFIX = "FontFamilySelectionDialog.";

    /**
     * The title of the dialog.
     */
    private static final String DIALOG_TITLE =
            ControlsMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The text for the FontSelector's label.
     */
    private static final String SELECTOR_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "label.selector");

    /**
     * The text for the selected list widget's label.
     */
    private static final String SELECTED_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "label.selected");

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
     * The text for the move left button.
     */
    private static final String MOVE_LEFT_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.left");

    /**
     * The accessible name for the move left button.
     */
    private static final String MOVE_LEFT_NAME =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.left.name");

    /**
     * The text for the move right button.
     */
    private static final String MOVE_RIGHT_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.right");

    /**
     * The accessible name for the move right button.
     */
    private static final String MOVE_RIGHT_NAME =
            ControlsMessages.getString(RESOURCE_PREFIX + "button.right.name");

    /**
     * The error message for selecting duplicate fonts.
     */
    private static final String DUPLICATE_FONT_MESSAGE =
            ControlsMessages.getString(RESOURCE_PREFIX +
            "messages.duplicateFont");

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "horizontalSpacing").intValue();

    /**
     * The margin height of the dialog.
     */
    private static final int MARGIN_HEIGHT =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "marginHeight").intValue();

    /**
     * The margin width of the dialog.
     */
    private static final int MARGIN_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "marginWidth").intValue();

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "verticalSpacing").intValue();

    /**
     * This set of generic font families has been duplicated from the
     * {@link com.volantis.mcs.css.properties.FontFamilyParser}.
     */
    private static final AllowableKeywords GENERIC_FONT_FAMILIES =
            FontFamilyKeywords.getDefaultInstance();

    /**
     * Constructor for a FontFamilySelectionDialog
     * @param parent the parent shell of the dialog
     */
    public FontFamilySelectionDialog(Shell parent) {
        super(parent);
        setTitle(DIALOG_TITLE);
        setBlockOnOpen(true);
    }

    /**
     * Creates the dialog's control.
     * @param parent the parent of the dialog's control
     * @return the created control
     */
    protected Control createDialogArea(Composite parent) {
        Composite topLevel = (Composite) super.createDialogArea(parent);
        GridLayout topLevelGrid = new GridLayout(2, true);
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.marginHeight = MARGIN_HEIGHT;
        topLevelGrid.marginWidth = MARGIN_WIDTH;
        topLevel.setLayout(topLevelGrid);

        addLabels(topLevel);

        Composite leftComposite = new Composite(topLevel, SWT.NONE);
        leftComposite.setLayout(new GridLayout(2, false));
        GridData leftData =
                new GridData(GridData.FILL_BOTH);
        leftComposite.setLayoutData(leftData);
        Composite rightComposite = new Composite(topLevel, SWT.NONE);
        rightComposite.setLayout(new GridLayout(2, false));
        GridData rightData =
                new GridData(GridData.FILL_BOTH);
        rightComposite.setLayoutData(rightData);

        addFontSelector(leftComposite);
        addArrowButtons(leftComposite);
        addSelectedList(rightComposite);
        addUpDownButtons(rightComposite);
        addErrorMessageArea(topLevel);

        updateButtons();
        validateDialog();

        initAccessible();
        return topLevel;
    }

    /**
     * Create and add the labels to the dialog.
     * @param container the parent container
     */
    private void addLabels(Composite container) {
        Label selectorLabel = new Label(container, SWT.NONE);
        selectorLabel.setText(SELECTOR_TEXT);
        Label selectedLabel = new Label(container, SWT.NONE);
        selectedLabel.setText(SELECTED_TEXT);
    }


    /**
     * Creates and adds a FontSelector control.
     * @param parent the parent Composite of the FontSelector
     */
    private void addFontSelector(Composite parent) {
        fontSelector = new FontSelector(parent, SWT.NONE);
        GridData selectorGrid =
                new GridData(GridData.FILL_BOTH);
        fontSelector.setLayoutData(selectorGrid);
        fontSelector.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                updateButtons();
            }
        });
        fontSelectorFonts = fontSelector.getFonts();
    }

    /**
     * Creates and adds the list widget that keeps track of
     * all font selections.
     * @param parent the parent Composite of the list control
     */
    private void addSelectedList(Composite parent) {
        selectionList = new org.eclipse.swt.widgets.List(parent,
                SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData listGridData = new GridData(GridData.FILL_BOTH);
        selectionList.setLayoutData(listGridData);
        selectionList.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                updateButtons();
            }
        });
        if (currentSelection != null && currentSelection.length > 0) {
            populateSelectionList(currentSelection);
        }
    }

    /**
     * Creates and adds a control of two buttons arranged vertically.
     * @param parent the parent Composite of the button control
     */
    private void addArrowButtons(Composite parent) {
        Composite arrowButtonComposite = new Composite(parent, SWT.NONE);
        GridLayout buttonGridLayout = new GridLayout();
        buttonGridLayout.verticalSpacing = VERTICAL_SPACING;
        buttonGridLayout.marginHeight = 0;
        buttonGridLayout.marginWidth = 0;
        arrowButtonComposite.setLayout(buttonGridLayout);
        GridData buttonCompositeGridData =
                new GridData(GridData.VERTICAL_ALIGN_FILL);
        buttonCompositeGridData.verticalSpan = 2;
        arrowButtonComposite.setLayoutData(buttonCompositeGridData);

        moveRightButton = new Button(arrowButtonComposite, SWT.PUSH);
        moveRightButton.setText(MOVE_RIGHT_TEXT);
        moveRightButton.setEnabled(false);
        GridData removeButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL);
        moveRightButton.setLayoutData(removeButtonData);
        moveRightButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleMoveRightButtonSelection();
            }
        });

        moveLeftButton = new Button(arrowButtonComposite, SWT.PUSH);
        moveLeftButton.setText(MOVE_LEFT_TEXT);
        moveLeftButton.setEnabled(false);
        GridData addButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL);
        moveLeftButton.setLayoutData(addButtonData);
        moveLeftButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleMoveLeftButtonSelection();
            }
        });
    }

    /**
     * Creates and adds a control of two buttons arranged vertically.
     * @param parent the parent Composite of the button control
     */
    private void addUpDownButtons(Composite parent) {
        Composite upDownButtonComposite = new Composite(parent, SWT.NONE);
        GridLayout buttonGridLayout = new GridLayout();
        buttonGridLayout.verticalSpacing = VERTICAL_SPACING;
        buttonGridLayout.marginHeight = 0;
        buttonGridLayout.marginWidth = 0;
        upDownButtonComposite.setLayout(buttonGridLayout);
        GridData buttonCompositeGridData =
                new GridData(GridData.VERTICAL_ALIGN_FILL);
        buttonCompositeGridData.verticalSpan = 2;
        upDownButtonComposite.setLayoutData(buttonCompositeGridData);

        upButton = new Button(upDownButtonComposite, SWT.PUSH);
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

        downButton = new Button(upDownButtonComposite, SWT.PUSH);
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
     * Creates and adds the message area for error messages.
     * @param parent the parent Composite of the area
     */
    private void addErrorMessageArea(Composite parent) {
        Composite errorMessageComposite =
                (Composite) createErrorMessageArea(parent);
        GridData emCompositeData =
                new GridData(GridData.FILL_HORIZONTAL);
        emCompositeData.horizontalSpan = 2;
        errorMessageComposite.setLayoutData(emCompositeData);
    }

    /**
     * Handles pressing the move right button.
     */
    private void handleMoveRightButtonSelection() {
        String fontName = fontSelector.getSelectedFont();
        //Add font name if not already there
        if (caseInsensitiveIndexOf(fontName, selectionList.getItems()) == -1) {
            selectionList.add(fontName);
            fontSelector.removeSelectedFont();
            currentSelection = selectionList.getItems();
            validateDialog();
            updateButtons();
        }
    }

    /**
     * Handles pressing the move left button.
     */
    private void handleMoveLeftButtonSelection() {
        int index = selectionList.getSelectionIndex();
        if (index >= 0) {
            String fontName = selectionList.getItem(index);
            selectionList.remove(index);
            if (caseInsensitiveIndexOf(fontName, fontSelectorFonts) >= 0 &&
                    caseInsensitiveIndexOf(fontName, selectionList.getItems())
                    == -1) {
                //Add the font to the list if it was originally there,
                //and if no duplicates remain in selection list
                fontSelector.addFont(fontName);
            } else {
                //Set the font as selected, and do not add to list
                fontSelector.setSelectedFont(fontName);
            }
            currentSelection = selectionList.getItems();
            if (index == currentSelection.length) {
                index--;
            }
            selectionList.setSelection(index);
            validateDialog();
            updateButtons();
        }
    }

    /**
     * Handles pressing the Up button.
     */
    private void handleUpButtonSelection() {
        int index = selectionList.getSelectionIndex();
        String moveUp = selectionList.getItem(index);
        String moveDown = selectionList.getItem(index - 1);
        selectionList.setItem(index - 1, moveUp);
        selectionList.setItem(index, moveDown);
        selectionList.setSelection(index - 1);
        currentSelection = selectionList.getItems();
        validateDialog();
        updateButtons();
    }

    /**
     * Handles pressing the Down button.
     */
    private void handleDownButtonSelection() {
        int index = selectionList.getSelectionIndex();
        String moveDown = selectionList.getItem(index);
        String moveUp = selectionList.getItem(index + 1);
        selectionList.setItem(index + 1, moveDown);
        selectionList.setItem(index, moveUp);
        selectionList.setSelection(index + 1);
        currentSelection = selectionList.getItems();
        validateDialog();
        updateButtons();
    }

    /**
     * Enables or disables buttons according to the current List and
     * FontSelector selections.
     */
    private void updateButtons() {
        String font = fontSelector.getSelectedFont();
        int index = selectionList.getSelectionIndex();
        int numItems = selectionList.getItemCount();
        moveLeftButton.setEnabled(index >= 0);
        moveRightButton.setEnabled(font.length() > 0 &&
                caseInsensitiveIndexOf(font, selectionList.getItems()) == -1);
        upButton.setEnabled(numItems > 1 && index > 0);
        downButton.setEnabled(numItems > 1 && index >= 0 &&
                index < numItems - 1);
    }

    /**
     * Gets the index of an item in the list in a case-insensitive manner.
     * @param name the item to look for
     * @param items the list of items to look in
     * @return -1 if item isn't in the list, its index otherwise
     */
    private int caseInsensitiveIndexOf(String name, String[] items) {
        int index = -1;
        name = name.trim();
        for (int i = 0; i < items.length && index == -1; i++) {
            if (items[i].equalsIgnoreCase(name)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Sets the font selection for the dialog and populates the
     * dialog's List with the items in the array.
     * @param items the array of items
     */
    public void setSelection(String[] items) {
        String[] unquoted = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            unquoted[i] = StringUtils.removeQuotes(items[i]);
        }
        currentSelection = unquoted;
    }

    /**
     * Empties the List and populates it with new items.
     * @param items the items for the list
     */
    private void populateSelectionList(String[] items) {
        selectionList.removeAll();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                selectionList.add(items[i]);
            }
            fontSelector.removeFonts(items);
        }
    }

    /**
     * Gets the font selection from the dialog.
     * @return the selected font names
     */
    public String[] getFonts() {
        String[] result = new String[currentSelection.length];
        for (int i = 0; i < currentSelection.length; i++) {
            int index = caseInsensitiveIndexOf(currentSelection[i],
                    fontSelectorFonts);
            result[i] = index >= 0 ? fontSelectorFonts[index] :
                    currentSelection[i];
            if (!GENERIC_FONT_FAMILIES.isValidKeyword(result[i])) {
                result[i] = StringUtils.quoteIfNeeded(result[i]);
            }
        }
        return result;
    }

    /**
     * Gets the font selection from the dialog.
     * @return the selected font names
     */
    public final Object[] getResult() {
        return getFonts();
    }

    /**
     * Validates the selected items in the list.
     */
    private void validateDialog() {
        String errorMessage = null;
        String[] items = selectionList.getItems();
        if (items != null && items.length > 0) {
            String duplicate = getFirstDuplicateInSelectionList();
            if (duplicate != null) {
                MessageFormat msgF =
                        new MessageFormat(DUPLICATE_FONT_MESSAGE);
                errorMessage = msgF.format(new String[]{duplicate});
            }
        }
        setErrorMessage(errorMessage);
    }

    /**
     * Finds and returns the first duplicate in the array of items. Used
     * in the validation. The check is case-insensitive.
     * @return the first duplicated item, or null if none was found
     */
    private String getFirstDuplicateInSelectionList() {
        String duplicate = null;
        String[] items = selectionList.getItems();
        for (int i = 0; i < items.length && duplicate == null; i++) {
            for (int j = i + 1; j < items.length && duplicate == null; j++) {
                if (items[i].equalsIgnoreCase(items[j])) {
                    duplicate = items[i];
                }
            }
        }
        return duplicate;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        initAccessibleControl(moveLeftButton, MOVE_LEFT_NAME);
        initAccessibleControl(moveRightButton, MOVE_RIGHT_NAME);
        initAccessibleControl(fontSelector, SELECTOR_TEXT);
        initAccessibleControl(selectionList, SELECTED_TEXT);
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

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 07-Jun-05	8637/4	pcameron	VBM:2005050402 Fixed quoting of font-family values

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 12-Dec-03	2201/11	pcameron	VBM:2003111803 Removed text literals from FontFamilySelectionDialog

 12-Dec-03	2201/9	pcameron	VBM:2003111803 Refactored FontFamilySelectionDialog

 ===========================================================================
*/
