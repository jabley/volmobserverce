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

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * A control that comprizes of a Text and a Button.
 */
public abstract class TextButton extends Composite
        implements ValidatedTextControl {
    /**
     * The minimum width for the text field.
     */
    private static final int MIN_TEXT_WIDTH =
            ControlsMessages.getInteger("Text.textWidth").intValue();

    /**
     * The accessible name for this control.
     */
    private String accessibleName;

    /**
     * The text.
     */
    private Text text;

    /**
     * The browse button.
     */
    private Button button;

    /**
     * The grid data for this control.
     */
    private GridData textGridData;

    /**
     * Create a new PolicySelector. A style of SWT.DEFAULT will put the
     * accelerator key for the button on the first character the same
     * as BUTTON_ACCEL_1.
     * @param parent The parent of the PolicySelector control.
     */
    protected TextButton(Composite parent, int style) {
        super(parent, style);

        text = new Text(this, SWT.BORDER | SWT.SINGLE);
        button = new Button(this, SWT.PUSH);

        button.setText(ControlsMessages.
                getString("TextButton.browse"));

        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.widthHint = MIN_TEXT_WIDTH;
        text.setLayoutData(textGridData);

        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        initAccessible();
    }

    /**
     * Get the Text child component.
     * @return The Text child component.
     */
    public Text getText() {
        return text;
    }

    /**
     * Set the Text child component.
     * @param text The text child component.
     */
    public void setText(Text text) {
        this.text = text;
        if (text != null) {
            text.setLayoutData(textGridData);
        }
    }

    /**
     * Set the text contents ie. the String in the Text control.
     * @param text The text String.
     */
    public void setValue(String text) {
        this.text.setText(text);
    }

    /**
     * Get the text contents ie. the String in the Text control.
     * @return The text String.
     */
    public String getValue() {
        return text.getText();
    }

    /**
     * Get the Button child component.
     * @return The Button child component.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Set the Button child component.
     * @param button The Button child component.
     */
    public void setButton(Button button) {
        this.button = button;
    }

    /**
     * Overridden to set the focus to the Text.
     */
    public boolean setFocus() {
        return text.setFocus();
    }

    /**
     * Override setEnabled() to enable/disable both Text and Button
     * controls.
     */
    public void setEnabled(boolean enabled) {
        text.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    /**
     * Sets the accessible name for this control.
     * @param name The accessible name for this control
     */
    public void setAccessibleName(String name) {
        accessibleName = name;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener comboListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = accessibleName;
                    }
                };
        comboListener.setControl(text);
        text.getAccessible().addAccessibleListener(comboListener);
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

 16-Nov-04	6228/1	allan	VBM:2004101910 Remove mnemonics from editors and views

 03-Mar-04	3200/1	allan	VBM:2004022410 Destination field and validation fixes.

 09-Feb-04	2913/1	philws	VBM:2004020801 Change device selection box to single selection

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 28-Nov-03	2013/3	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 18-Nov-03	1921/1	steve	VBM:2003110902 CellIterations control

 06-Nov-03	1811/1	byron	VBM:2003110518 Eclipse TextButton controls should not delegate setVisible and setEnabled

 24-Oct-03	1636/1	pcameron	VBM:2003102402 Added TextControl and ValidatedTextControl interfaces, and made TextButton conform to latter

 20-Oct-03	1502/6	allan	VBM:2003092202 Completed validation for PolicySelector.

 16-Oct-03	1502/3	allan	VBM:2003092202 Updated TextButton

 ===========================================================================
*/
