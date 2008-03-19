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

import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Creates a FontSelector for use in the FontSelectionDialog.
 */
public class FontSelector extends Composite {

    /**
     * Resource prefix for the FontSelector.
     */
    private final static String RESOURCE_PREFIX = "FontSelector.";

    /**
     * The vertical gap between widgets in the FontSelector.
     */
    private static final int VERTICAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "verticalSpacing").intValue();

    /**
     * The minimum height of the list widget.
     */
    private static final int MIN_LIST_HEIGHT =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "list.minHeight").intValue();

    /**
     * The minimum width of the FontSelector.
     */
    private static final int MIN_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "minWidth").intValue();

    /**
     * The five default font families.
     */
    private static final String[] DEFAULT_FONT_NAMES = {
        "cursive", "fantasy", "monospace",
        "sans-serif", "serif"
    };

    /**
     * The accessible name for the font name text field
     */
    private static final String FONT_NAME_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "fontNameText");

    /**
     * The text field widget for font selection.
     */
    private Text textField;

    /**
     * The list widget for fonts.
     */
    private List fontList;

    /**
     * The array of font names known by this selector.
     */
    private String[] fontNames;

    /**
     * The ModifyListener for the text field.
     */
    private ModifyListener textFieldModifyListener;

    /**
     * The SelectionListener for the list widget.
     */
    private SelectionAdapter listSelectionListener;

    /**
     * The listeners which listen for ModifyText
     * events from the FontSelector, fired when
     * the font selection changes.
     */
    private ListenerList listeners;


    /**
     * The ordered set of the indices of available fonts
     */
    private TreeSet visibleFontIndices;

    /**
     * Creates a FontSelector widget.
     * @param parent the parent Composite
     * @param style the style for the parent
     */
    public FontSelector(Composite parent, int style) {
        super(parent, style);
        listeners = new ListenerList();
        createSelector();
        initAccessible();
    }

    /**
     * Creates and adds the widgets for the FontSelector.
     */
    private void createSelector() {
        GridLayout containerGridLayout = new GridLayout();
        containerGridLayout.marginHeight = 0;
        containerGridLayout.marginWidth = 0;
        containerGridLayout.verticalSpacing = VERTICAL_SPACING;
        this.setLayout(containerGridLayout);
        addTextField();
        addList();
    }

    /**
     * Creates and adds the text field widget.
     */
    private void addTextField() {
        textField = new Text(this, SWT.BORDER);
        GridData textFieldData = new GridData(GridData.FILL_HORIZONTAL);
        textFieldData.widthHint = MIN_WIDTH;
        textField.setLayoutData(textFieldData);
        textFieldModifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent modifyEvent) {
                String selectedFont = textField.getText();
                int index = fontList.getSelectionIndex();
                if (index >= 0) {
                    String listName = fontList.getItem(index);
                    if (!selectedFont.equals(listName)) {
                        //The text in the text field is being edited,
                        //so deselect the list item.

                        fontList.removeSelectionListener(listSelectionListener);
                        fontList.deselect(index);

                        fontList.addSelectionListener(listSelectionListener);
                    }
                }
                fireModifyEvent(selectedFont);
            }
        };
        textField.addModifyListener(textFieldModifyListener);
    }

    /**
     * Creates and adds the list widget.
     */
    private void addList() {
        fontList = new List(this, SWT.SINGLE |
                SWT.BORDER | SWT.V_SCROLL);
        GridData fontListWidgetData = new GridData(GridData.FILL_BOTH);
        fontListWidgetData.heightHint = MIN_LIST_HEIGHT;
        fontList.setLayoutData(fontListWidgetData);
        populateList();
        listSelectionListener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                int index = fontList.getSelectionIndex();
                if (index >= 0) {
                    String selectedFont = fontList.getItem(index);
                    if (!selectedFont.equals(textField.getText())) {

                        textField.removeModifyListener(textFieldModifyListener);
                        textField.setText(selectedFont);

                        textField.addModifyListener(textFieldModifyListener);
                        fireModifyEvent(selectedFont);
                    }
                }
            }
        };
        fontList.addSelectionListener(listSelectionListener);
    }

    /**
     * Populates the list with font names. The list consists of the
     * following font names: the first five names are the default
     * font names defined in this class. The following names are
     * an alphabetically sorted list of all the system fonts
     * available at runtime. The set of items has any duplicate
     * names removed, where duplication is independent of case.
     */
    private void populateList() {
        //Add the five default font names first
        fontList.setItems(DEFAULT_FONT_NAMES);
        //Get all system fonts and remove any duplicate defaults
        TreeSet fontNamesSet = getAllSystemFonts();
        for (int i = 0; i < DEFAULT_FONT_NAMES.length; i++) {
            fontNamesSet.remove(DEFAULT_FONT_NAMES[i]);
        }
        //Add the remaining system fonts to the list
        Iterator it = fontNamesSet.iterator();
        while (it.hasNext()) {
            fontList.add((String) it.next());
        }
        fontNames = fontList.getItems();
        visibleFontIndices = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                int v1 = ((Integer) o1).intValue();
                int v2 = ((Integer) o2).intValue();
                return v1 - v2;
            }
        });
        for (int i = 0; i < fontNames.length; i++) {
            visibleFontIndices.add(new Integer(i));
        }
    }

    /**
     * Returns a sorted set of system font names.
     * @return the set of font names
     */
    private TreeSet getAllSystemFonts() {
        //null means all fonts are returned, and true means scalable
        //fonts get returned.
        FontData[] fontData = this.getDisplay().getFontList(null, true);
        //The set uses a case-insensitive comparator.
        TreeSet fontNamesSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < fontData.length; i++) {
            fontNamesSet.add(fontData[i].getName());
        }
        return fontNamesSet;
    }

    /**
     * Adds a font to the list if it's not there already and used to
     * be in the list. The comparison is case-sensitive. Otherwise,
     * the font is made the selected font.
     * @param name the name of the font to add
     */
    public void addFont(String name) {
        name = name.trim();
        if (name.length() > 0) {
            int index = caseInsensitiveIndexOf(name, fontNames);
            if (index >= 0) {
                //One of the original fonts
                name = fontNames[index];
                if (fontList.indexOf(name) < 0) {
                    //The name isn't already there.
                    refreshList(index);
                }
            }
            textField.setText(name);
            //You have to redraw the list for the selection
            //to be highlighted!
            fontList.redraw();
        }
    }

    /**
     * Refreshes the list with an additional font.
     * @param origIndex the original index of the
     * font to "reinstate"
     */
    private void refreshList(int origIndex) {
        fontList.removeAll();
        this.visibleFontIndices.add(new Integer(origIndex));
        Iterator it = visibleFontIndices.iterator();
        while (it.hasNext()) {
            fontList.add(fontNames[((Integer) it.next()).intValue()]);
        }
    }

    /**
     * Hides a selection of fonts in the selector's list.
     */
    public void removeFonts(String[] selection) {
        if (selection != null) {
            for (int i = 0; i < selection.length; i++) {
                int fontIndex = fontList.indexOf(selection[i]);
                if (fontIndex >= 0) {
                    fontList.remove(selection[i]);
                    int origIndex =
                            caseInsensitiveIndexOf(selection[i], fontNames);
                    if (origIndex >= 0) {
                        visibleFontIndices.remove(new Integer(origIndex));
                    }
                }
            }
        }
    }

    /**
     * Removes the currently selected font from the list. The
     * removal is case-insensitive.
     */
    public void removeSelectedFont() {
        String name = textField.getText();
        int index = caseInsensitiveIndexOf(name, fontList.getItems());
        if (index >= 0) {
            int origIndex = caseInsensitiveIndexOf(name, fontNames);
            if (origIndex >= 0) {
                visibleFontIndices.remove(new Integer(origIndex));
            }
            fontList.remove(index);
            if (index == fontList.getItemCount()) {
                index--;
            }
            textField.setText(index >= 0 ? fontList.getItem(index) : "");
            fontList.setSelection(index);
        }
    }

    /**
     * Gets the index of an item in the list in a case-insensitive manner.
     * @param text the item to look for
     * @param items the list of items to look in
     * @return -1 if item isn't in the list, its index otherwise
     */
    private int caseInsensitiveIndexOf(String text, String[] items) {
        int index = -1;
        if (text != null && text.length() > 0) {
            for (int i = 0; i < items.length && index == -1; i++) {
                if (items[i].equalsIgnoreCase(text)) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * Gets the currently selected font name.
     * @return the current selected font name. If the name matches
     * a known font in a case-insensitive manner, the known font
     * name is returned instead.
     */
    public String getSelectedFont() {
        String name = textField.getText();
        int index = caseInsensitiveIndexOf(name, fontNames);
        if (index >= 0) {
            name = fontNames[index];
        }
        return name;
    }

    /**
     * Sets the font selection.
     * @param fontName the name of the font to select
     */
    public void setSelectedFont(String fontName) {
        textField.setText(fontName);
    }

    /**
     * Gets all the font names known by this selector.
     * @return an array of font names
     */
    public String[] getFonts() {
        return fontNames;
    }

    /**
     * Adds a ModifyText listener which listens for font selections.
     * @param listener the listener to add. The font selection is available
     * using the getSelectedFont() method.
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a ModifyText listener which listens for font selections.
     * @param listener the listener to remove.
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Fires a ModifyText event to all registered listeners.
     * @param selectedFont the selected font. The modifyEvent.data
     * contains the selected font. This name is also available via
     * the public getSelectedFont() method.
     */
    private void fireModifyEvent(String selectedFont) {
        Event e = new Event();
        e.data = selectedFont == null ? "" : selectedFont;
        e.widget = FontSelector.this;
        ModifyEvent me = new ModifyEvent(e);
        Object[] interested = listeners.getListeners();
        for (int i = 0; i < interested.length; i++) {
            if (interested[i] != null) {
                ((ModifyListener) interested[i]).modifyText(me);
            }
        }
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener fontNameListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = FONT_NAME_TEXT;
                    }
                };
        fontNameListener.setControl(textField);
        textField.getAccessible().addAccessibleListener(fontNameListener);
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

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Dec-03	2201/12	pcameron	VBM:2003111803 FontSelector list keeps original order

 12-Dec-03 2201/9 pcameron VBM:2003111803 Refactored FontFamilySelectionDialog

 12-Dec-03 2201/7 pcameron VBM:2003111803 Added FontFamilySelectionDialog

 11-Dec-03 2187/1 pcameron VBM:2003111403 Added FontSelector

 ===========================================================================
*/

