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

import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import com.volantis.mcs.themes.parsing.ObjectParser;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * A control that comprizes of a Text and a Button, mapping to an object.
 */
public abstract class ObjectButton extends Composite
        implements ValidatedObjectControl {
    /**
     * The minimum width for the text field.
     */
    private static final int MIN_TEXT_WIDTH =
            ControlsMessages.getInteger("Text.textWidth").intValue();

    /**
     * The default object parser, which will be used when no explicit parser
     * is provided.
     */
    private static final ObjectParser DEFAULT_PARSER =
            ObjectParserFactory.getDefaultInstance().createDefaultParser();

    /**
     * The accessible name for this control.
     */
    private String accessibleName;

    /**
     * The text.
     */
    private Text text;

    /**
     * The value.
     */
    private Object value;

    /**
     * The parser to use for this control.
     */
    protected ObjectParser parser;

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
     *
     * @param parent The parent of the PolicySelector control.
     * @param style The style to use
     * @param parser The object parser to use when converting the text field
     * @param editable True if the text field can be edited (in this case the
     *                 object parser <EM>must</EM> provide a valid mapping to
     *                 and from text).
     */
    protected ObjectButton(Composite parent, int style, ObjectParser parser,
                           boolean editable) {
        super(parent, style);
        if (parser == null) {
            parser = DEFAULT_PARSER;
        }
        this.parser = parser;

        // Construct the GUI
        createControl(editable);
    }

    /**
     * Builds the GUI components for the ObjectButton.
     *
     * @param editable True if the text should be editable.
     */
    private void createControl(boolean editable) {
        int textStyle = SWT.BORDER | SWT.SINGLE;
        if (!editable) {
            textStyle |= SWT.READ_ONLY;
        }

        text = new Text(this, textStyle);
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
     * Set the value represented by this object button.
     *
     * @param newValue The new value.
     */
    public void setValue(Object newValue) {
        value = newValue;
        this.text.setText(parser.objectToText(newValue));
    }

    public void setMultipleValue(Object[] newValue) {
        value = newValue;

        // Concatenate the values together (NB they cannot be null as
        // the dialog's initial population was non-null)
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < newValue.length; i++) {
            buffer.append(parser.objectToText(newValue[i]));
            if (i < newValue.length - 1) {
                buffer.append(' ');
            }
        }

        this.text.setText(buffer.toString());
    }

    /**
     * Get the value represented by this object button.
     *
     * @return The value.
     */
    public Object getValue() {
        return value;
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

    //javadoc inherited
    public boolean canProvideObject() {
        return true;
    }

    //javadoc inherited
    public void addStateChangeListener(StateChangeListener listener) {
        //todo implement this method
    }

    //javadoc inherited
    public void removeStateChangeListener(StateChangeListener listener) {
        //todo implement this method
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
