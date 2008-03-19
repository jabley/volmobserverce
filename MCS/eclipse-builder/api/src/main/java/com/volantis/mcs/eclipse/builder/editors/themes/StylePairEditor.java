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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.EditorPropertyParser;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.types.StyleFractionType;
import com.volantis.mcs.themes.types.StylePairType;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.styling.properties.StyleProperty;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is a composite editor for two style values, allowing both separate
 * parts to be edited in a single unit. It is used to edit {@link StyleFraction}
 * and {@link StylePair}s. StylePairEditors are composed of two
 * {@link StyleValueEditor}s and labels, and can display the individual
 * components on the same, or subsequent lines.
 * <p/>
 * Their behaviour is straightforward for StylePairs - the first value is
 * edited via the first editor, the second via the second.
 * <p/>
 * StyleFractions are slightly more complex. The first editor is used to
 * display all the allowable values for this property, but the second is only
 * enabled if representing a StyleFraction. For example, mcs-marquee-speed can
 * be a length, keyword, fraction or inherit. The first editor would display
 * either a length, keyword, inherit or the numerator of the fraction. The
 * second can only display the denominator of the fraction.
 */
public class StylePairEditor extends Composite {

    private static final String RESOURCE_PREFIX = "StylePairEditor.";

    /**
     * An editor for the first property.
     */
    private StyleValueEditor first;

    /**
     * An editor for the second property.
     */
    private StyleValueEditor second;

    /**
     * A checkbox to specify the priority of the property being edited.
     */
    private Button importantCheckbox;

    /**
     * A list of change listeners.
     */
    private ListenerList listeners = new ListenerList();

    /**
     * Flag to indicate whether events are currently being transmitted.
     */
    private boolean transmittingEvents = true;

    /**
     * List of remaining controls in this editor.
     */
    private ArrayList otherControls;

    /**
     * The style property being edited by this editor.
     */
    private StyleProperty property;

    /**
     * The default separator strings for {@link StylePair} and
     * {@link StyleFraction} respectively.
     */
    private static final String PAIR_SEPARATOR = " ";
    private static final String FRACTION_SEPARATOR = " / ";

    /**
     * The string which should be used as a separator when rendering.
     */
    private String separatorString;

    /**
     * Listener to pass on value changes in the editors for the underlying
     * style editors.
     */
    private ModifyListener childModificationListener = new ModifyListener() {
        public void modifyText(ModifyEvent event) {
            updatePriorityCheckbox();
            valueChanged();
        }
    };

    /**
     * Listener to pass on value changes in the important checkbox for the
     * underlying style editors.
     */
    private SelectionListener priorityListener = new SelectionListener() {
        // Javadoc inherited
        public void widgetSelected(SelectionEvent event) {
            valueChanged();
        }

        // Javadoc inherited
        public void widgetDefaultSelected(SelectionEvent event) {
            valueChanged();
        }
    };

    /**
     * Listener which updates the second editor based on changes to the first.
     */
    private ModifyListener firstModificationListener = new ModifyListener() {
        public void modifyText(ModifyEvent event) {
            // Only do something if the first changed to a non empty value
            if (first != null && !"".equals(first.getValue())) {
                PropertyValue value = getPropertyValue();
                if (value != null && (value.getValue() instanceof StyleKeyword
                        || value.getValue() instanceof StyleInherit)) {
                    // Only reset (and hence cause redraw) if the first value
                    // was a style keyword or style inherit and the second
                    // value needs to be cleared.
                    setValue(value);
                } else if (!second.isEnabled()) {
                    // If the first value is not a keyword or inherit and the
                    // second editor is not enabled, then it should now be!
                    second.setEnabled(true);
                }
            }
        }
    };

    /**
     * Create a new two style editor.
     *
     * @param composite         parent composite in which to create the editor
     * @param style             The SWT style for the editor
     * @param property          The name of the property being edited
     * @param firstType         The type of the property being edited
     * @param secondType        The type of the property being edited
     * @param acceptsImportant  True if the 'important' checkbox should be
     *                          displayed
     * @param context           The editor context shared by components editing
     *                          this theme
     * @param multiline         true if this editor should be rendered over two
     *                          lines, false otherwise
     * @param firstLabelText  String label for the first editor
     * @param secondLabelText String label for the second editor
     * @param separator
     */
    public StylePairEditor(Composite composite, int style,
                           StyleProperty property, StyleType firstType,
                           StyleType secondType, boolean acceptsImportant,
                           EditorContext context, boolean multiline,
                           String firstLabelText, String secondLabelText,
                           String separator) {
        super(composite, style);
        this.property = property;
        this.separatorString = separator;
        otherControls = new ArrayList();

        // Determine how many columns are required to display this editor.
        int columns = calculateColumnsRequired(multiline, acceptsImportant, 
                firstLabelText, secondLabelText);

        // Configure this editor's layout.
        GridLayout layout = new GridLayout(columns, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        this.setLayoutData(data);
       
        // Create the label for the first editor (if required) and add it to
        // the otherControls list.
        createLabel(firstLabelText);

        // Create the first editor.
        first = new StyleValueEditor(this, SWT.NONE,
                property, property.getName() + ".first",
                firstType, false, null, context);
        data = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        first.setLayoutData(data);
        setControlColour(first);

        if (!multiline) {
            // Add a separatorLabel label if rendering on one line
            Label separatorLabel = new Label(this, SWT.NONE);
            if (!PAIR_SEPARATOR.equals(separatorString)) {
                separatorLabel.setText(separatorString);
            }
            data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
            separatorLabel.setLayoutData(data);
            otherControls.add(separatorLabel);
            setControlColour(separatorLabel);
        } else if (acceptsImportant) {
            // Add an important checkbox between the editors if multiline.
            createImportant(multiline);
        }

        // Create the label for the second editor (if required) and add it to
        // the otherControls list.
        createLabel(secondLabelText);

        // Create the second editor.
        second = new StyleValueEditor(this, SWT.NONE,
                property, property.getName() + ".second",
                secondType, false, null, context);
        data = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        second.setLayoutData(data);
        setControlColour(second);

        // Add the important checkbox at the end of the line.
        if (acceptsImportant && !multiline) {
            createImportant(multiline);
        }

        updatePriorityCheckbox();
        
        pack();

        // Register a listener for modifications to the underlying editors.
        first.addModifyListener(childModificationListener);
        second.addModifyListener(childModificationListener);

        // The second editor should be updated when the first is updated (as
        // well as when it's modified directly), so to avoid duplication of
        // effort we should only let the containing editor know there's been a
        // change when the second changes.
        first.addModifyListener(firstModificationListener);
    }

    /**
     * Create a new style pair editor for a {@link StyleFractionType}.
     *
     * @param parent            parent composite in which to create the editor
     * @param style             The SWT style for the editor
     * @param property          The name of the property being edited
     * @param fractionType      fraction type describing the allowable types of
     *                          the property being edited
     * @param acceptsImportant  True if the 'important' checkbox should be
     *                          displayed
     * @param context           The editor context shared by components editing
     */
    public StylePairEditor(Composite parent,
                           int style,
                           StyleProperty property,
                           StyleType type,
                           StyleFractionType fractionType,
                           boolean acceptsImportant,
                           EditorContext context) {

        this(parent, style, property, type, fractionType.getDenominatorType(),
                acceptsImportant,context, false, null, null, FRACTION_SEPARATOR);
    }

    /**
     * Create a new style pair editor for a {@link StylePairType}.
     *
     * @param parent            parent composite in which to create the editor
     * @param style             The SWT style for the editor
     * @param property          The name of the property being edited
     * @param pairType          pair type describing the allowable types of
     *                          the property being edited
     * @param acceptsImportant  True if the 'important' checkbox should be
     *                          displayed
     * @param context           The editor context shared by components editing
     */
    public StylePairEditor (Composite parent,
                            int style,
                            StyleProperty property,
                            StylePairType pairType,
                            boolean acceptsImportant,
                            EditorContext context,
                            String firstLabel,
                            String secondLabel) {
        this(parent, style, property, pairType.getFirstType(),
                pairType.getSecondType(), acceptsImportant, context, true,
                firstLabel, secondLabel, PAIR_SEPARATOR);
    }

    /**
     * Determine how many columns are required to display this pair editor.
     *
     * @param multiline         true if the pair should be displayed over
     *                          two lines
     * @param acceptsImportant  true if the pair should have a checkbox to
     *                          indicate whether a value is important
     * @param firstLabelText    label for the first style value editor
     * @param secondLabelText   label for the second style value editor
     * @return int the number of colums required to display this editor
     */ 
    private int calculateColumnsRequired(boolean multiline, 
                                         boolean acceptsImportant,
                                         String firstLabelText,
                                         String secondLabelText) {
        // Determine how many columns are required to display this pair editor
        // (will always need at least one column for the style value editor).
        int columns = 1;

        // Don't need a column for the label if no label text was supplied.
        if (firstLabelText != null) {
            columns = ++columns;
        }

        // Handle any special behaviour if the pair editor should all fit
        // on one line.
        if (!multiline) {
            // Need columns for both first and second editors.
            columns = columns*2;
            // Correct for one of the labels being null.
            if (firstLabelText != null && secondLabelText == null) {
                columns = -+columns;
            } else if (firstLabelText == null && secondLabelText != null) {
                columns = ++columns;
            }
            // Add a column for the separator label if one is required
            // (e.g. / for mcs-marquee-speed).
            if (separatorString != null) {
                columns = ++columns;
            }
        }

        // Add a column for the 'important' checkbox (to indicate whether the
        // property value is important) if required. 
        columns = acceptsImportant ? ++columns : columns;
        
        return columns;
    }
    
    /**
     * Create a {@link Label} with the supplied label text and add it to this
     * editor's list of controls . If the label text is null, no label will be
     * created.
     * <p/>
     * NB: Be aware that when you call this method dictates where the label
     * will appear.
     *
     * @param labelText     String text of the label
     * @return Label the label that was created. May be null if the label text
     * passed in was null.
     */ 
    private Label createLabel(String labelText) {
        Label label = null;
        if (labelText != null) {
            label = new Label(this, SWT.NONE);
            label.setText(labelText);
            GridData  data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
            label.setLayoutData(data);
            setControlColour(label);
            otherControls.add(label);
        }
        return label;            
    }
    
    /**
     * Create a checkbox which can be used to indicate whether or not this
     * property is important.
     *
     * @param multiline     true if important checkbox should apply to more
     *                      than one line, false otherwise
     */
    private void createImportant(boolean multiline) {

        // Create the checkbox.
        GridData data;
        importantCheckbox = new Button(this, SWT.CHECK);
        importantCheckbox.setText(EditorMessages.getString(
                RESOURCE_PREFIX + "important.label"));
        data = new GridData(GridData.VERTICAL_ALIGN_CENTER);

        // Centre it between lines if this editor spans multiple lines.
        if (multiline) {
            data.verticalSpan = 2;
        }
        importantCheckbox.setLayoutData(data);
        setControlColour(importantCheckbox);
            importantCheckbox.addSelectionListener(priorityListener);
    }

    /**
     * Get the string values of the editor as an array.
     *
     * @return String[] values of this editor as a string array
     */
    public String[] getValues() {
        String[] values = new String[2];
        values[0] = first.getValue();
        values[1] = second.getValue();
        return values;
    }

    /**
     * Get the string values of the editor. If both values are non null, they
     * will be concatenated together with the supplied separatorString. If the
     * second value is unset, the first will be returned. An empty string will
     * be returned if neither are set.
     *
     * @return String representing this editor's values
     */
    public String getStringValue() {
        final StringBuffer textValue = new StringBuffer();

        if (first != null && !first.getValue().equals("")) {
            textValue.append(first.getValue());
            if (second != null && !second.getValue().equals("")) {
                textValue.append(separatorString).append(second.getValue());
            }
        }
        return textValue.toString();
    }

    /**
     * Get the {@link PropertyValue} which represents the values edited by
     * this editor.
     *
     * @return PropertyValue
     */
    public PropertyValue getPropertyValue() {
        // Check if the first could actually be a StyleKeyword
        final String firstValue = first.getValue();
        final EditorPropertyParser parser = new EditorPropertyParser();
        PropertyValue value = parser.parsePropertyValue(property,
                firstValue, isImportant());

        // Ignore the second value if the first is a keyword or inherit.
        if (value == null || !(value.getValue() instanceof StyleKeyword ||
                value.getValue() instanceof StyleInherit)) {
            // If it's not then parse the whole fraction.
            value = parser.parsePropertyValue(property,getStringValue(),
                    isImportant());
        }
        return value;
    }

    /**
     * Helper method to set the colour of a control to the default for this
     * UI ({@link org.eclipse.swt.SWT.COLOR_LIST_BACKGROUND}).
     *
     * @param control The control for which the colour is being set
     */
    private void setControlColour(Control control) {
        control.setBackground(getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));
    }

    public void addModifyListener(ModifyListener listener) {
        listeners.add(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        listeners.remove(listener);
    }

    private void valueChanged() {
        if (transmittingEvents) {
            Event event = new Event();
            event.widget = this;
            String[] values = getValues();
            event.data = values;
            event.text = values[0] + " " + values[1];
            ModifyEvent modifyEvent = new ModifyEvent(event);
            Object interested[] = listeners.getListeners();
            for (int i = 0; i < interested.length; i++)
            if (interested[i] != null) {
                ((ModifyListener) interested[i]).modifyText(modifyEvent);
            }
        }
    }

    /**
     * Returns true if the important checkbox is displayed and selected.
     *
     * @return True if the important checkbox is displayed and selected
     */
    public boolean isImportant() {
        return importantCheckbox != null && importantCheckbox.getSelection();
    }

    public void setValue(PropertyValue newPropertyValue) {
        try {
            transmittingEvents = false;

            boolean handled = false;
            if (newPropertyValue != null) {
                StyleValue newValue = newPropertyValue.getValue();
                if (newValue instanceof StylePair) {
                    StylePair pairValue = (StylePair) newValue;
                    updateEditorValue(first, pairValue.getFirst());
                    updateEditorValue(second, pairValue.getSecond());
                    Priority priority = newPropertyValue.getPriority();
                    boolean important = priority == Priority.IMPORTANT;
                    importantCheckbox.setSelection(important);
                    handled = true;
                } else if (newValue instanceof StyleFraction) {
                    StyleFraction fractionValue = (StyleFraction) newValue;
                    updateEditorValue(first, fractionValue.getNumerator());
                    updateEditorValue(second, fractionValue.getDenominator());
                    second.setEnabled(true);
                    Priority priority = newPropertyValue.getPriority();
                    boolean important = priority == Priority.IMPORTANT;
                    importantCheckbox.setSelection(important);
                    handled = true;
                } else if (newValue instanceof StyleKeyword ||
                        newValue instanceof StyleInherit) {
                    updateEditorValue(first, newValue);
                    // The second value should not be enabled in this case.
                    updateEditorValue(second, null);
                    second.setEnabled(false);
                    Priority priority = newPropertyValue.getPriority();
                    boolean important = priority == Priority.IMPORTANT;
                    importantCheckbox.setSelection(important);
                    handled = true;
                }
            }

            if (!handled) {
                // TODO later Handle this situation better - can't do anything about it in the current GUI, can we?
                updateEditorValue(first, null);
                updateEditorValue(second, null);
                importantCheckbox.setSelection(false);
            }
        } finally {
            transmittingEvents = true;
        }
    }

    // Javadoc inherited
    public void setEnabled(boolean newEnabled) {
        if (first != null) {
            first.setEnabled(newEnabled);
        }
        if (second != null) {
            second.setEnabled(newEnabled);
        }

        for (Iterator i = otherControls.iterator(); i.hasNext();) {
            final Control control = (Control) i.next();
            control.setEnabled(newEnabled);
        }
    }
    
    /**
     * Convenience method which updates the child {@link StyleValueEditor} and
     * shared priority checkbox without firing events to listeners.
     *
     * @param editor    whose value to update
     * @param newValue
     */
    public void updateEditorValue(StyleValueEditor editor, StyleValue newValue) {
        editor.updateEditor(newValue, false);
        // the priority checkbox is shared between the child style value
        // editors - need to determine if it should be disabled every time an
        // editor value changes.
        updatePriorityCheckbox();
    }
 
    /**
     * Examine the values of the child style value editors and disable/enable
     * the priority checkbox accordingly.
     */
    private void updatePriorityCheckbox() {
 
        String[] values = getValues();
        boolean isEmpty = true;
        for (int i=0; isEmpty && i<values.length;i++) {
            String value = values[i];
            if (value != null && !value.equals("") ) {
                isEmpty = false;
            }
        }
        importantCheckbox.removeSelectionListener(priorityListener);
        importantCheckbox.setEnabled(!isEmpty);
        if (isEmpty) {
            importantCheckbox.setSelection(false);
        }
        importantCheckbox.addSelectionListener(priorityListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10617/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10589/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Nov-05	9886/5	adrianj	VBM:2005101811 New theme GUI

 01-Nov-05	10062/1	adrianj	VBM:2005101811 New theme GUI

 01-Nov-05	9886/5	adrianj	VBM:2005101811 New theme GUI

 01-Nov-05	9886/4	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
