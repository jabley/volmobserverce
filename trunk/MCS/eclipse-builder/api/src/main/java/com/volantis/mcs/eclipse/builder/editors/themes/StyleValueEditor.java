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
import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.mcs.eclipse.common.ResourceUnits;
import com.volantis.mcs.eclipse.controls.ColorButton;
import com.volantis.mcs.eclipse.controls.UnitsCombo;
import com.volantis.mcs.themes.CustomStyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleUserAgentDependent;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.AllowableKeywordsAccessor;
import com.volantis.mcs.themes.types.StyleAngleType;
import com.volantis.mcs.themes.types.StyleChoiceType;
import com.volantis.mcs.themes.types.StyleColorType;
import com.volantis.mcs.themes.types.StyleComponentURIType;
import com.volantis.mcs.themes.types.StyleFractionType;
import com.volantis.mcs.themes.types.StyleFrequencyType;
import com.volantis.mcs.themes.types.StyleFunctionCallType;
import com.volantis.mcs.themes.types.StyleIdentifierType;
import com.volantis.mcs.themes.types.StyleInheritType;
import com.volantis.mcs.themes.types.StyleIntegerType;
import com.volantis.mcs.themes.types.StyleKeywordsType;
import com.volantis.mcs.themes.types.StyleLengthType;
import com.volantis.mcs.themes.types.StyleListType;
import com.volantis.mcs.themes.types.StyleNumberType;
import com.volantis.mcs.themes.types.StyleOrderedSetType;
import com.volantis.mcs.themes.types.StylePairType;
import com.volantis.mcs.themes.types.StylePercentageType;
import com.volantis.mcs.themes.types.StyleStringType;
import com.volantis.mcs.themes.types.StyleTimeType;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.mcs.themes.types.StyleTypeVisitor;
import com.volantis.mcs.themes.types.StyleURIType;
import com.volantis.mcs.themes.types.StyleTranscodableURIType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.styling.properties.StyleProperty;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Editor for single style values.
 */
public class StyleValueEditor extends Composite {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    StyleValueEditor.class);

    private static final String RESOURCE_PREFIX = "StyleValueEditor.";
    /**
     * The string for the inherit entry in the editable combo. This does not
     * come from a resource bundle as it should never be localised as it is
     * a W3C keyword
     */
    private final static String INHERIT_KEYWORD = "inherit";

    private int controlCount;
    private boolean colorButtonRequired;
    private boolean browseButtonRequired;
    private boolean unitsComboRequired;
    private boolean importantRequired;

    private ColorButton colorButton;
    private Button browseButton;
    private UnitsCombo unitsCombo;
    private Combo editableCombo;
    private Button importantCheckbox;

    private StylePropertyBrowseAction browseAction;

    private StyleProperty property;

    /**
     * This may be different to {@link property#getName} in some cases - e.g.
     * when used in a pair editor.
     */
    private String propertyName;

    private List valueTypes;
    
    private EditorContext context;

    private static final AllowableKeywordsAccessor ALLOWABLE_KEYWORDS_ACCESSOR =
            new AllowableKeywordsAccessor();

    private ModifyListener editableComboListener;

    private ListenerList listeners = new ListenerList();

    private boolean transmittingEvents = true;

    private SelectionListener changeSelectionListener = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent event) {
            valueChanged();
        }
    };

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param composite         parent composite
     * @param i                 style - see superclass explanation
     * @param property          which is being edited by this editor
     * @param propertyType      type which this editor can contain
     * @param acceptsImportant  indicates whether this property supports css
     *                          important
     * @param browseAction      if non null, defines what the browse button of
     *                          this editor should do.
     * @param context           in which this editor should operate
     */
    public StyleValueEditor(Composite composite, int i, StyleProperty property,
                            StyleType propertyType, boolean acceptsImportant,
                            StylePropertyBrowseAction browseAction,
                            EditorContext context) {
        this(composite, i, property, property.getName(), propertyType,
                acceptsImportant, browseAction, context);
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param composite         parent composite
     * @param i                 style - see superclass explanation
     * @param property          which is being edited by this editor
     * @param propertyName      in most cases this will be property#getName,
     *                          however for editors which are part of a
     *                          {@link StylePairEditor} it may be
     *                          property#getName +".first" (or ".second")
     * @param propertyType      type which this editor can contain
     * @param acceptsImportant  indicates whether this property supports css
     *                          important
     * @param browseAction      if non null, defines what the browse button of
     *                          this editor should do.
     * @param context           in which this editor should operate
     */
    public StyleValueEditor(Composite composite, int i, StyleProperty property,
                            String propertyName, StyleType propertyType,
                            boolean acceptsImportant,
                            StylePropertyBrowseAction browseAction,
                            EditorContext context) {
        super(composite, i);

        this.context = context;

        controlCount = 1;
        this.importantRequired = acceptsImportant;
        this.property = property;
        this.propertyName = propertyName;

        if (browseAction != null) {
            browseButtonRequired = true;
            this.browseAction = browseAction;
        }

        valueTypes = new ArrayList();

        propertyType.accept(new StyleTypeVisitor() {
            // Javadoc inherited
            public void visitStyleInheritType(StyleInheritType type) {
                valueTypes.add(StyleValueType.INHERIT);
            }

            // Javadoc inherited
            public void visitStyleLengthType(StyleLengthType type) {
                valueTypes.add(StyleValueType.LENGTH);
                unitsComboRequired = true;
            }

            // Javadoc inherited
            public void visitStyleURIType(StyleURIType type) {
                valueTypes.add(StyleValueType.URI);
            }

            // Javadoc inherited
            public void visitStyleKeywordsType(StyleKeywordsType type) {
                valueTypes.add(StyleValueType.KEYWORD);
            }

            // Javadoc inherited
            public void visitStylePercentageType(StylePercentageType type) {
                valueTypes.add(StyleValueType.PERCENTAGE);
                unitsComboRequired = true;
            }

            // Javadoc inherited
            public void visitStyleTimeType(StyleTimeType type) {
                valueTypes.add(StyleValueType.TIME);
                unitsComboRequired = true;
            }

            // Javadoc inherited
            public void visitStyleAngleType(StyleAngleType type) {
                valueTypes.add(StyleValueType.ANGLE);
                unitsComboRequired = true;
            }

            // Javadoc inherited
            public void visitStylePairType(StylePairType type) {
                valueTypes.add(StyleValueType.PAIR);
            }

            // Javadoc inherited
            public void visitStyleColorType(StyleColorType type) {
                valueTypes.add(StyleValueType.COLOR);
                colorButtonRequired = true;
            }

            // Javadoc inherited
            public void visitStyleFunctionCallType(StyleFunctionCallType type) {
                valueTypes.add(StyleValueType.FUNCTION_CALL);
            }

            // Javadoc inherited
            public void visitStyleListType(StyleListType type) {
                valueTypes.add(StyleValueType.LIST);
            }

            // Javadoc inherited
            public void visitStyleIdentifierType(StyleIdentifierType type) {
                valueTypes.add(StyleValueType.IDENTIFIER);
            }

            // Javadoc inherited
            public void visitStyleNumberType(StyleNumberType type) {
                valueTypes.add(StyleValueType.NUMBER);
            }

            // Javadoc inherited
            public void visitStyleIntegerType(StyleIntegerType type) {
                valueTypes.add(StyleValueType.INTEGER);
            }

            // Javadoc inherited
            public void visitStyleComponentURIType(StyleComponentURIType type) {
                valueTypes.add(StyleValueType.COMPONENT_URI);
            }

            // Javadoc inherited
            public void visitStyleTranscodableURIType(
                    final StyleTranscodableURIType type) {
                valueTypes.add(StyleValueType.TRANSCODABLE_URI);
            }

            // Javadoc inherited
            public void visitStyleChoiceType(StyleChoiceType type) {
                List choices = type.getTypes();
                Iterator it = choices.iterator();
                while (it.hasNext()) {
                    StyleType optionType = (StyleType) it.next();
                    optionType.accept(this);
                }
            }

            // Javadoc inherited
            public void visitStyleOrderedSetType(StyleOrderedSetType type) {
                valueTypes.add(StyleValueType.LIST);
            }

            // Javadoc inherited
            public void visitStyleStringType(StyleStringType type) {
                valueTypes.add(StyleValueType.STRING);
            }

            // Javadoc inherited.
            public void visitStyleFractionType(StyleFractionType type) {
                type.getNumeratorType().accept(this);
                type.getDenominatorType().accept(this);
            }

            // Javadoc inherited.
            public void visitStyleFrequencyType(StyleFrequencyType type) {
                valueTypes.add(StyleValueType.FREQUENCY);
                unitsComboRequired = true;
            }
        });

        if (browseButtonRequired) {
            controlCount += 1;
        }

        if (colorButtonRequired) {
            controlCount += 1;
        }

        if (unitsComboRequired) {
            controlCount += 1;
        }

        if (importantRequired) {
            controlCount += 1;
        }

        createComponents();
    }

    public void addModifyListener(ModifyListener listener) {
        listeners.add(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        listeners.remove(listener);
    }

    private void createComponents() {
        GridLayout layout = new GridLayout(controlCount, false);
        this.setLayout(layout);

        if (colorButtonRequired) {
            colorButton = new ColorButton(this, SWT.PUSH);
            // We must register a listener so that when a color is selected
            // the editable combo is updated.
            ModifyListener colorButtonListener = new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    handleColorButtonSelection();
                }
            };
            colorButton.addModifyListener(colorButtonListener);
        }

        addEditableCombo();

        if (unitsComboRequired) {
            addUnitsCombo();
        }

        if (browseButtonRequired) {
            browseButton = new Button(this, SWT.PUSH);
            browseButton.setText(EditorMessages.getString(
                    RESOURCE_PREFIX + "browse.label"));
            browseButton.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent event) {
                    doBrowseAction();
                }

                public void widgetDefaultSelected(SelectionEvent event) {
                    doBrowseAction();
                }
            });
        }

        if (importantRequired) {
            importantCheckbox = new Button(this, SWT.CHECK);
            importantCheckbox.setText(EditorMessages.getString(
                    RESOURCE_PREFIX + "important.label"));
            // register a listener with the checkbox. When a selection is made
            // we want to fire a modified event.
            importantCheckbox.addSelectionListener(changeSelectionListener);
            importantCheckbox.setBackground(getDisplay().
                    getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        }

        pack();
        // if we are using a color button we need to force its height
        if (colorButtonRequired) {
            int comboHeight = editableCombo.getBounds().height;
            colorButton.setSize(comboHeight, comboHeight);
            colorButton.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            // Do the layout again, since the button has been resized
            pack();
        }
    }

    private void doBrowseAction() {
        if (browseAction != null) {
            String initialValue = getValue();
            String browsed = browseAction.doBrowse(initialValue, this, context);
            if (browsed != null && !browsed.equals(initialValue)) {
                editableCombo.setText(browsed);
            }
        }
    }
    
    /**
     * Method that will be invoked whenever the ColorButton control is clicked
     */
    private void handleColorButtonSelection() {
         // Get the RGB value that has been selected
        RGB rgb = colorButton.getColor();
        // set the text in the editable combo the the hex representatation
        // of the color that has been selected
        editableCombo.setText(Convertors.RGBToHex(rgb));
        // renable the editable combo listener
        if (unitsCombo != null) {
            // disable the untis combo
            unitsCombo.setEnabled(false);
        }
        // notify listeners of the change
        valueChanged();
    }

    private AllowableKeywords getAllowableKeywords() {
        AllowableKeywords allowableKeywords = null;
        if (propertyName != null) {
            allowableKeywords = ALLOWABLE_KEYWORDS_ACCESSOR
                    .getAllowableKeywords(propertyName);
        }
        return allowableKeywords;
    }

    /**
     * Adds an editable combo to this Composite
     */
    private void addEditableCombo() {
        editableCombo = new Combo(this, SWT.NONE);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        editableCombo.setLayoutData(data);
        // if inherit type is supported then we have to add the inherit
        // entry to the editable combo
        if (valueTypes.contains(StyleValueType.INHERIT)) {
            // inherit entry allways comes first
            editableCombo.add(INHERIT_KEYWORD, 0);
        }
        // if the keyword type is supported then we have to add all the
        // keywords to the editable combo
        if (valueTypes.contains(StyleValueType.KEYWORD)) {
            AllowableKeywords allowableKeywords = getAllowableKeywords();
            // add the keywords to the combo. Note: keywords are not localized
            // as the are part of the CSS spec.
            if (allowableKeywords != null) {
                List keywords = allowableKeywords.getKeywords();
                for (int i = 0; i < keywords.size(); i++) {
                    StyleKeyword keyword = (StyleKeyword) keywords.get(i);
                    if (!keyword.isInternal()) {
                        editableCombo.add(keyword.getName());
                    }
                }
            }
        }
        if (colorButtonRequired) {
            // add the predefined list of colors to the Combo
            NamedColor colors[] = NamedColor.getAllColors();
            for (int i = 0; i < colors.length; i++) {
                editableCombo.add(colors[i].getName());
            }
        }
        // register a listener with the editable combo box. When notified this
        // listener will if required (depending on the text enetered) update
        // the state of the other optional controls
        editableComboListener = new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                handleEditableComboTextInput();
            }
        };
        editableCombo.addModifyListener(editableComboListener);
    }

    /**
     * Method that will be invoked whenever the editable combo receives input
     */
    private void handleEditableComboTextInput() {
        // retrieve the text that has been entered
        String text = editableCombo.getText();
        if (colorButton != null) {
            // if the color button has been enabled we need to determine if
            // the value entered represents a color. If it does we need to
            // update the color button so that it display the correct color.
            String hexRGBCandidate = NamedColor.getHex(text);
            if (hexRGBCandidate == null) {
                // text was not a named color. Set the candidate to the text
                // it may be a hexadecimal color representation
                hexRGBCandidate = text;
            }
            RGB rgb = null;
            try {
                rgb = (hexRGBCandidate == null)
                        ? null : Convertors.hexToRGB(hexRGBCandidate);
            } catch (NumberFormatException nfe) {
                // If the candidate could not be parsed, then we leave the
                // RGB value as null, and no further action is required.
            }
            // Set the color buttons color. Note - if null is passed in the
            // button will display no color
            // To avoid event loops, only set the value if it has changed
            RGB oldRGB = colorButton.getColor();
            if (rgb == null ? oldRGB != null : !rgb.equals(oldRGB)) {
                colorButton.setColor(rgb);
            }
        }

        if (unitsCombo != null) {
            // if the text in the editable combo is not a double value then
            // the unitsCombo should be disabled.
            boolean enableUnitsCombo = true;
            try {
                Double.parseDouble(text);
            } catch (NumberFormatException e) {
                enableUnitsCombo = false;
            }
            unitsCombo.removeSelectionListener(changeSelectionListener);
            unitsCombo.setEnabled(enableUnitsCombo);
            unitsCombo.addSelectionListener(changeSelectionListener);
        }

        if (importantCheckbox != null) {
            // Disable the important checkbox if the editable value is an
            // empty string.
            boolean enabled = true;
            if (text.equals("")) {
                enabled = false;
            }
            importantCheckbox.removeSelectionListener(changeSelectionListener);
            importantCheckbox.setEnabled(enabled);
            importantCheckbox.addSelectionListener(changeSelectionListener);
        }

        // notify interested clients of the change in value.
        valueChanged();
    }

    /**
     * Adds a UnitsCombo to this composite.
     */
    private void addUnitsCombo() {
        List units = new ArrayList();
        // if the angle type is supported then we have to add the angle units
        // to the units list.
        if (valueTypes.contains(StyleValueType.ANGLE)) {
            units.add(ResourceUnits.DEGREE);
            units.add(ResourceUnits.GRAD);
            units.add(ResourceUnits.RADIAN);
        }
        // if the length type is supported then we have to add the length units
        // to the units list.
        if (valueTypes.contains(StyleValueType.LENGTH)) {
            units.add(ResourceUnits.PIXEL);
            units.add(ResourceUnits.EM);
            units.add(ResourceUnits.EX);
            units.add(ResourceUnits.PICA);
            units.add(ResourceUnits.POINT);
            units.add(ResourceUnits.INCH);
            units.add(ResourceUnits.CENTIMETRE);
            units.add(ResourceUnits.MILLIMETRE);
        }
        // if the percentage type is supported then we have to add the percentage
        // units to the units list.
        if (valueTypes.contains(StyleValueType.PERCENTAGE)) {
            units.add(ResourceUnits.PERCENT);
        }
        // if the time type is supported then we have to add the time
        // units to the units list.
        if (valueTypes.contains(StyleValueType.TIME)) {
            units.add(ResourceUnits.MILLISECOND);
            units.add(ResourceUnits.SECOND);
        }

        // if the frequency type is supported then we have to add the frequency
        // units to the units list.
        if (valueTypes.contains(StyleValueType.FREQUENCY)) {
            units.add(ResourceUnits.HERTZ);
            units.add(ResourceUnits.KILOHERTZ);
        }
        // if number or integers types are supported we will have to add a
        // blank entry to the units combo (if the untis combo is required at
        // all).
        boolean requireBlankUnitsEntry =
            (valueTypes.contains(StyleValueType.INTEGER) ||
             valueTypes.contains(StyleValueType.NUMBER));
        // create the actual UnitCombo object.
        unitsCombo = new UnitsCombo(this, units, requireBlankUnitsEntry);
        unitsCombo.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // register a listener with the combo. When a selection is made
        // we want to fire a modified event.
        unitsCombo.addSelectionListener(changeSelectionListener);
    }

    /**
     * Returns a string representation of the style value that the controls
     * currently represent
     */
    public String getValue() {
        // If this control has a UnitsCombo return the concatenation of the
        // text in the editable combo and the selected unit, otherwise just
        // return the value of the editable combo.
        // note: not using a StringBuffer as only concatenating 2 strings.
        boolean unitsEnabled = unitsCombo != null && unitsCombo.isEnabled();
        final String text = editableCombo.getText();
        // Only append the unit string if:
        // a) the unit combo is enabled
        // b) the unit string is non null
        // c) the editable combo text is not null or empty
        if (text != null && !text.equals("") &&
                unitsEnabled && unitsCombo.getSelectedUnit() != null) {
            return text + unitsCombo.getSelectedUnit();
        } else {
            return text;
        }
    }

    public boolean isImportant() {
        return importantCheckbox != null && importantCheckbox.getSelection();
    }

    private String styleValueToString(StyleValue value) {
        return value.getStandardCSS();
    }

    public void setPropertyValue(PropertyValue newPropertyValue) {
        StyleValue newValue;
        boolean important;
        if (newPropertyValue == null) {
            newValue = null;
            important = false;
        } else {
            newValue = newPropertyValue.getValue();
            important = newPropertyValue.getPriority() == Priority.IMPORTANT;
        }
        updateEditor(newValue, important);
    }

    void updateEditor(StyleValue newValue, boolean important) {
        try {
            transmittingEvents = false;
            if (newValue == null) {
                editableCombo.setText("");
                if (colorButton != null) {
                    colorButton.setColor(null);
                }
                if (importantCheckbox != null) {
                    importantCheckbox.setSelection(false);
                }
                if (unitsCombo != null) {
                    unitsCombo.setSelectedUnit(null);
                }
            } else {
                newValue.visit(new EditorUpdater(), null);
                if (importantCheckbox != null) {
                    importantCheckbox.setSelection(important);
                }
            }
        } finally {
            transmittingEvents = true;
        }
    }

    private void valueChanged() {
        if (transmittingEvents) {
            Event event = new Event();
            event.widget = this;
            String value = getValue();
            event.data = value;
            event.text = value;
            ModifyEvent modifyEvent = new ModifyEvent(event);
            Object interested[] = listeners.getListeners();
            for (int i = 0; i < interested.length; i++)
            if (interested[i] != null) {
                ((ModifyListener) interested[i]).modifyText(modifyEvent);
            }
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (editableCombo != null) {
            editableCombo.setEnabled(b);
        }

        if (importantCheckbox != null) {
            importantCheckbox.setEnabled(b);
        }

        if (unitsCombo != null) {
            if (!b) {
                // Should always be able to disable the unitsCombo.
                unitsCombo.setEnabled(b);
            } else if (editableCombo != null) {                
                String text = editableCombo.getText();
                // the units combo should not be enabled if the editable combo
                // contains a value which doesn't support a type (e.g.
                // StyleKeyword) or if it's empty (because it will be enabled when
                // the editable combo associated with it is edited).
                if (!"".equals(text)) {
                    EditorPropertyParser parser = new EditorPropertyParser();
                    PropertyValue propertyValue = parser.parsePropertyValue(
                            property, text, isImportant());
                    StyleValue value = propertyValue.getValue();
                    if (!(value instanceof StyleKeyword ||
                            value instanceof StyleInherit)) {
                        unitsCombo.setEnabled(b);
                    }
                }
            }
        }

        if (browseButton != null) {
            browseButton.setEnabled(b);
        }

        if (colorButton != null) {
            colorButton.setEnabled(b);
        }
    }

    private class EditorUpdater implements StyleValueVisitor {
        public void visit(StyleAngle value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleColorName value, Object object) {
            editableCombo.setText(value.getName());
        }

        public void visit(StyleColorPercentages value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleColorRGB value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleComponentURI value, Object object) {
            editableCombo.setText(value.getExpressionAsString());
        }

        public void visit(StyleTranscodableURI value, Object object) {
            editableCombo.setText(value.getUri());
        }

        public void visit(StyleFrequency value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleFunctionCall value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleIdentifier value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleInherit value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleInteger value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleInvalid value, Object object) {
            editableCombo.setText(value.getValue());
        }

        public void visit(StyleKeyword value, Object object) {
            String identifier = value.getName();
            if (identifier != null) {
                editableCombo.setText(identifier);
            }
        }

        public void visit(StyleLength value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleList value, Object object) {
            editableCombo.setText(
                    value.getStandardCSS(
                            StylePropertyMetadata.getListSeperator(
                                    propertyName)));
        }

        public void visit(StyleNumber value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StylePair value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StylePercentage value, Object object) {
            editableCombo.setText(String.valueOf(value.getPercentage()));
            unitsCombo.setSelectedUnit(ResourceUnits.PERCENT.getUnit());
        }

        public void visit(StyleString value, Object object) {
            editableCombo.setText(value.getString());
        }

        public void visit(StyleURI value, Object object) {
            editableCombo.setText(value.getURI());
        }

        public void visit(StyleUserAgentDependent value, Object object) {
            throw new UnsupportedOperationException(
                    EXCEPTION_LOCALIZER.format("style-value-not-updateable",
                            new String[]{"StyleUserAgentDependent",
                                         "User agent dependent values not " +
                    "supported by GUI"}));
        }

        public void visit(StyleTime value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleFraction value, Object object) {
            throw new UnsupportedOperationException(
                    EXCEPTION_LOCALIZER.format("style-value-not-updateable",
                            new Object[]{"StyleFractions",
                                         "Fractions should be updated using " +
                    "the TwoStyleEditor, which will update the fraction's " +
                    "components individually"}));
        }

        public void visit(CustomStyleValue value, Object object) {
            throw new UnsupportedOperationException();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/2	adrianj	VBM:2005111601 Add style rule view

 06-Dec-05	10652/1	adrianj	VBM:2005112110 Add context menu for StyleCategoriesComposite

 06-Dec-05	10625/4	adrianj	VBM:2005112110 Support synchronizable categories

 06-Dec-05	10625/1	adrianj	VBM:2005112110 Support synchronizable categories

 06-Dec-05	10610/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 06-Dec-05	10608/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Dec-05	10512/1	pduffin	Quick commit for GUI fixes

 01-Dec-05	10523/3	ianw	VBM:2005112406 Fix uo XDIMCP Title element

 01-Dec-05	10514/1	ianw	VBM:2005112406 Fixed XDIMECP Title elemement

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Nov-05	9992/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 31-Oct-05	9886/8	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/4	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
