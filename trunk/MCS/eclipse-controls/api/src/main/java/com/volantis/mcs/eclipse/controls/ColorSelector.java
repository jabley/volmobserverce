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

import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.mcs.eclipse.validation.ColorValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TypedListener;

import java.util.HashMap;


/**
 * A color selector control for selecting colors. By default, nothing
 * is initially selected.
 */
public class ColorSelector extends Composite implements ValidatedTextControl {

    /**
     * The color button of the selector.
     */
    private ColorButton colorButton;

    /**
     * The combo list of the selector.
     */
    private Combo colorCombo;

    /**
     * The list of named colors with which to populate
     * the combo list.
     */
    private NamedColor[] colors;

    /**
     * The color validator for this control.
     */
    private ColorValidator validator;

    /**
     * Resource prefix for the color selector.
     */
    private final static String COLOR_SELECTOR_PREFIX = "ColorSelector.";

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(COLOR_SELECTOR_PREFIX +
            "horizontalSpacing").intValue();

    /**
     * The accessible name for the color button.
     */
    private static final String TEXT_COLOR_BUTTON =
            ControlsMessages.getString(COLOR_SELECTOR_PREFIX + "selector");

    /**
     * The accessible name for the color button.
     */
    private static final String TEXT_COLOR_COMBO =
            ControlsMessages.getString(COLOR_SELECTOR_PREFIX + "color");

    /**
     * Mapping between fault types understood by the ColorSelector and message keys.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    static {
        MESSAGE_KEY_MAPPINGS = new HashMap(1);
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_COLOR,
                "ColorSelector.invalidColor");
    }

    /**
     * The validation message builder used by the ColorSelector
     */
    private final ValidationMessageBuilder validationMessageBuilder =
            new ValidationMessageBuilder(
                    ControlsMessages.getResourceBundle(),
                    MESSAGE_KEY_MAPPINGS,
                    null);

    /**
     * Construct a ColorSelector with the specified parent and style,
     * and set of colors.
     * @param parent the parent composite
     * @param style the style
     * @param colors a NamedColors[] of colors for populating the
     * Combo control.
     */
    public ColorSelector(Composite parent, int style, NamedColor[] colors) {
        super(parent, style);
        if (colors == null || colors.length == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty:" +
                    "colors.");
        }
        this.colors = colors;
        this.validator = new ColorValidator(colors);
        createControls();
        //Layout the control, that is, set the size and location
        //of all of the ColorSelector's child widgets. This action
        //allows me to get hold of the height of the Combo widget
        //which I can then use to set the size of the ColorButton
        this.pack();
        int comboHeight = colorCombo.getBounds().height;
        colorButton.setSize(comboHeight, comboHeight);
        //Do the layout again, since the button has now
        //been given its size.
        this.pack();
        //The ColorSelector never has a default selection.
        //That is up to the user of the control.
        colorCombo.setText("");

        initAccessible();
    }

    /**
     * Sets the layout for the selector, creates the controls
     * for it, and registers the selector as a listener for
     * the color button.
     */
    private void createControls() {
        GridLayout layout = new GridLayout(2, false);
        layout.horizontalSpacing = HORIZONTAL_SPACING;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addColorButton();
        addCombo();
    }

    /**
     * Creates and adds the color button to the selector.
     */
    private void addColorButton() {
        colorButton = new ColorButton(this, SWT.PUSH);
        colorButton.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                handlecolorButtonChange();
            }
        });
    }

    /**
     * Creates and adds the combo list to the selector, populating it
     * with the NamedColors[] given to the constructor, or the
     * standard set of colors if none was given.
     */
    private void addCombo() {
        colorCombo = new Combo(this, SWT.NONE);
        GridData comboGridData = new GridData(GridData.FILL_HORIZONTAL);
        colorCombo.setLayoutData(comboGridData);
        for (int i = 0; i < colors.length; i++) {
            colorCombo.add(colors[i].getName());
        }
        colorCombo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                changeColorSelection(getValue());
            }
        });
        colorCombo.setData("name", "color"); //$NON-NLS-2$
    }

    /**
     * Process a change in the text of the Combo by finding out what
     * the text is, and creating an appropriate ModifyEvent object
     * that reflects a valid or invalid color selection. Fire the
     * event change to registered listeners. This method is only
     * called when there is a valid color change.
     * @param value the text of the Combo
     */
    private void changeColorSelection(String value) {
        Event e = new Event();
        e.data = value == null ? "" : value;
        e.widget = this;
        ModifyEvent me = new ModifyEvent(e);
        // Set the new color for the color button.
        RGB rgb = null;
        if (this.validate().isOK()) {
            // Try a named color first
            rgb = Convertors.hexToRGB(NamedColor.getHex(value));
            if (rgb == null) {
                rgb = Convertors.hexToRGB(value);
            }
        }
        colorButton.setColor(rgb);
        fireModifyEvent(me);
    }

    /**
     * Handles changes from the color button.
     */
    private void handlecolorButtonChange() {
        // If you cancel out of the ColorButton's dialog, you
        // get null back, so the RGB conversion will give null.
        String value = Convertors.RGBToHex(colorButton.getColor());
        if (value != null) {
            setValue(value);
        }
    }

    /**
     * Checks that the current value is present in the selector's
     * color list. Color comparisons are case-insensitive. If the value
     * is present, the list item is returned.
     * @param item the item to check for
     * @return the matching list item if present, or the item if not present
     */
    private String getListValueFor(String item) {
        String value = null;
        for (int i = 0; i < colors.length && value == null; i++) {
            if (colors[i].getName().equalsIgnoreCase(item)) {
                value = colors[i].getName();
            }
        }
        return value == null ? item : value;
    }

    /**
     * Fires a ModifyText event to all registered listeners.
     * @param modifyEvent the ModifyEvent. modifyEvent.data
     * contains the String representation of the selected
     * color: either a color name, or a 3- or 6-digit
     * hex value prefixed with a #.
     */
    private void fireModifyEvent(ModifyEvent modifyEvent) {
        Event event = new Event();
        event.data = modifyEvent.data;
        event.widget = modifyEvent.widget;
        if (isListening(SWT.Modify)) {
            notifyListeners(SWT.Modify, event);
        }
    }

    /**
     * Adds a listener that is called when the contents of the text control
     * is modified.
     * @param listener for modifications
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            addListener(SWT.Modify, new TypedListener(listener));
        }
    }

    /**
     * Removes a control modification listener
     * @param listener for modifications
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            removeListener(SWT.Modify, new TypedListener(listener));
        }
    }

    /**
     * Overridden to set the focus to the Combo.
     */
    public boolean setFocus() {
        return colorCombo.setFocus();
    }

    // javadoc inherited
    public String getValue() {
        String value = colorCombo.getText().trim();
        return getListValueFor(value);
    }

    // javadoc inherited
    public void setValue(String value) {
        colorCombo.setText(value);
        changeColorSelection(value);
    }

    /**
     * Validates the contents of the ColorSelector.
     * @return the validation status
     */
    public ValidationStatus validate() {
        return validator.validate(getValue(),
                validationMessageBuilder);
    }

    /**
     * Getter for the color combo.
     * @return the color combo.
     */
    public Combo getCombo() {
        return colorCombo;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener comboListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = TEXT_COLOR_COMBO;
                    }
                };
        comboListener.setControl(colorCombo);
        colorCombo.getAccessible().addAccessibleListener(comboListener);

        StandardAccessibleListener buttonListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = TEXT_COLOR_BUTTON;
                    }
                };
        buttonListener.setControl(colorButton);
        colorButton.getAccessible().addAccessibleListener(buttonListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 23-Feb-04	3057/2	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 09-Jan-04	2215/4	pcameron	VBM:2003112405 TimeSelectionDialog, ListBuilder and refactoring

 28-Nov-03	2013/1	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 27-Nov-03	2024/3	pcameron	VBM:2003111704 Added ColorListSelectionDialog

 25-Nov-03	1634/7	pcameron	VBM:2003102205 Refactored to use the ColorValidator in the ColorSelector

 25-Nov-03	1634/5	pcameron	VBM:2003102205 A few changes to ColorSelector

 25-Nov-03	1634/3	pcameron	VBM:2003102205 A few changes to ColorSelector

 21-Nov-03	1634/1	pcameron	VBM:2003102205 Added ColorSelector, NamedColor and supporting classes

 ===========================================================================
*/
