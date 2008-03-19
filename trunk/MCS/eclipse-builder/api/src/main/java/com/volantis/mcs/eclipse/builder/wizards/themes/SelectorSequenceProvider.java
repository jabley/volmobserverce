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
package com.volantis.mcs.eclipse.builder.wizards.themes;

import com.volantis.mcs.eclipse.builder.editors.themes.ThemesMessages;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.controls.AttributeSelectorProviderFactory;
import com.volantis.mcs.eclipse.controls.ClassSelectorProviderFactory;
import com.volantis.mcs.eclipse.controls.ComboViewer;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import com.volantis.mcs.eclipse.controls.ObjectListSelector;
import com.volantis.mcs.eclipse.controls.PseudoClassProviderFactory;
import com.volantis.mcs.eclipse.controls.PseudoElementProviderFactory;
import com.volantis.mcs.eclipse.controls.ValidatedObjectControl;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.eclipse.controls.events.StateChangeListenerCollection;
import com.volantis.mcs.eclipse.validation.IndependentValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.Validator;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.ElementSelector;
import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.ThemeConstants;
import com.volantis.mcs.themes.TypeSelector;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import com.volantis.mcs.xdime.XDIMESchemata;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GUI control for providing a selector sequence.
 */
public class SelectorSequenceProvider extends Composite
        implements ValidatedObjectControl {
    /**
     * The prefix for resource messages in the selector sequence provider
     */
    private static final String RESOURCE_PREFIX = "SelectorSequenceProvider.";

    /**
     * The prefix for resource messages in the pseudo element selector
     */
    private static final String PSEUDO_ELEMENT_SELECTOR_RESOURCE_PREFIX =
            "PseudoElementSelector.";

    /**
     * The prefix for resource messages in the pseudo class selector
     */
    private static final String PSEUDO_CLASS_SELECTOR_RESOURCE_PREFIX =
            "PseudoClassSelector.";

    /**
     * A factory for object parsers (for converting between DOM and model
     * representations of theme objects).
     */
    private static final ObjectParserFactory PARSER_FACTORY =
            ObjectParserFactory.getDefaultInstance();

    /**
     * A validation message builder.
     */
    private ValidationMessageBuilder VALIDATION_MESSAGE_BUILDER =
            new ValidationMessageBuilder(
                    ThemesMessages.getResourceBundle(),
                    new HashMap(), new String[0]);

    /**
     * A simple dummy validator implementation that always validates
     * successfully.
     */
    private final Validator DUMMY_VALIDATOR =
            new Validator() {
                public ValidationStatus validate(Object object,
                                      ValidationMessageBuilder messageBuilder) {
                    return VALIDATE_OK;
                }
            };

    /**
     * In the new themes GUI, validation is handled in a different way (by
     * implementing com.volantis.mcs.model.Validatable). Eventually, the rest
     * of the GUI will be migrated to use the same framework. However until
     * that point, this dummy validator (which considers everything valid) will
     * be used when a validator is required by the old code.
     */
    private final IndependentValidator DUMMY_INDEPENDENT_VALIDATOR =
            new IndependentValidator(DUMMY_VALIDATOR,
                    VALIDATION_MESSAGE_BUILDER) {
                // Javadoc inherited.
                public ValidationStatus validate(Object o) {
                    return VALIDATE_OK;
                }
            };

    /**
     * Default validation status
     */
    private static final ValidationStatus VALIDATE_OK =
            new ValidationStatus(ValidationStatus.OK, "");

    /**
     * Label for this controls group box label
     */
    private static final String GROUP_LABEL =
                ThemesMessages.getString(RESOURCE_PREFIX + "label");

    /**
     * The {@link PresentableItem}s that will be used to populate the drop-down
     * list of elements.
     */
    private static final PresentableItem[] ELEMENTS;
    static {
        List deprecated = new ArrayList(ThemeConstants.DEPRECATED_TYPES.length);
        for (int i = 0; i < ThemeConstants.DEPRECATED_TYPES.length; i++) {
            deprecated.add(ThemeConstants.DEPRECATED_TYPES[i]);
        }
        ELEMENTS = new PresentableItem[ThemeConstants.TYPE_LIST.length + 2];
        ELEMENTS[0] = new PresentableItem("", "");
        ELEMENTS[1] = new PresentableItem(ThemeConstants.UNIVERSAL_SELECTOR,
                ThemeConstants.UNIVERSAL_SELECTOR);
        for (int i = 0; i < ThemeConstants.TYPE_LIST.length; i++) {
            String type = ThemeConstants.TYPE_LIST[i];
            String presentation = type;
            if (deprecated.contains(type)) {
                String format = ThemesMessages.getString(RESOURCE_PREFIX + "deprecatedType");
                presentation = MessageFormat.format(format, new Object[] { type });
            }
            ELEMENTS[i + 2] = new PresentableItem(type, presentation);
        }
    }

    /**
     * The {@link PresentableItem}s that will be used to populate the drop-down
     * list of namespace prefixes.
     */
    private static final PresentableItem[] NAMESPACE_PREFIXES;
    static {
        String format = ThemesMessages.getString(RESOURCE_PREFIX + "prefixNamespace");
        NAMESPACE_PREFIXES = new PresentableItem[] {
            new PresentableItem(null, ThemesMessages.getString(
                    RESOURCE_PREFIX + "defaultNamespace")),
            new PresentableItem("*",
                    ThemesMessages.getString(RESOURCE_PREFIX + "anyNamespace")),
            new PresentableItem(XDIMESchemata.DEFAULT_CDM_PREFIX,
                    MessageFormat.format(format, new Object[] {
                        XDIMESchemata.DEFAULT_CDM_PREFIX,
                        XDIMESchemata.CDM_NAMESPACE
                    })),
            new PresentableItem(XDIMESchemata.DEFAULT_XHTML2_PREFIX,
                    MessageFormat.format(format, new Object[] {
                        XDIMESchemata.DEFAULT_XHTML2_PREFIX,
                        XDIMESchemata.XHTML2_NAMESPACE
                    })),
            new PresentableItem(XDIMESchemata.DEFAULT_XFORMS_PREFIX,
                    MessageFormat.format(format, new Object[] {
                        XDIMESchemata.DEFAULT_XFORMS_PREFIX,
                        XDIMESchemata.XFORMS_NAMESPACE
                    })),
            new PresentableItem(XDIMESchemata.DEFAULT_WIDGETS_PREFIX,
                    MessageFormat.format(format, new Object[] {
                        XDIMESchemata.DEFAULT_WIDGETS_PREFIX,
                        XDIMESchemata.WIDGETS_NAMESPACE
                    })),
            new PresentableItem(XDIMESchemata.DEFAULT_TICKER_PREFIX,
                    MessageFormat.format(format, new Object[] {
                        XDIMESchemata.DEFAULT_TICKER_PREFIX,
                        XDIMESchemata.TICKER_NAMESPACE
                    })),
            new PresentableItem(XDIMESchemata.DEFAULT_GALLERY_PREFIX,
                    MessageFormat.format(format, new Object[] {
                        XDIMESchemata.DEFAULT_GALLERY_PREFIX,
                        XDIMESchemata.GALLERY_NAMESPACE
                    })),
        };
    }

    /**
     * Factory for theme model objects.
     */
    private static final StyleSheetFactory MODEL_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    /**
     * An array containing each type of pseudo-class.
     */
    private static final String[] PSEUDO_CLASS_TEMPLATES = {
        "first-child",
        "nth-child",
        "link",
        "visited",
        "active",
        "focus",
        "hover",
        "mcs-concealed",
        "mcs-unfolded",
        "mcs-invalid",
        "mcs-normal",
        "mcs-busy",
        "mcs-failed",
        "mcs-suspended",
        "mcs-disabled"
    };

    /**
     * An array containing each type of pseudo-element.
     */
    private static final String[] PSEUDO_ELEMENT_TEMPLATES = {
        "first-line",
        "first-letter",
        "mcs-shortcut",
        "after",
        "before",
        "marker",
        "mcs-next",
        "mcs-previous",
        "mcs-reset",
        "mcs-cancel",
        "mcs-complete",
        "mcs-item",
        "mcs-between"
    };

    /**
     * An array containing the pseudo-selectors that can have parameters
     * associated with them.
     */
    private static final String[] PARAMETERISED_PSEUDO_SELECTORS = {
        "nth-child",
    };

    /**
     * The combo for selecting element selectors.
     */
    private ComboViewer elementsCombo;

    /**
     * The combo for selecting namespace selectors.
     */
    private ComboViewer namespaceCombo;

    /**
     * The text field for entering ID selectors.
     */
    private Text idText;

    /**
     * The object list selector for selecting attribute selectors.
     */
    private ObjectListSelector attributeSelector;

    /**
     * The object list selector for selecting class selectors.
     */
    private ObjectListSelector classSelector;

    /**
     * The object list selector for selecting pseudo-class selectors.
     */
    private ObjectListSelector pseudoClassSelector;

    /**
     * The object list selector for selecting pseudo-element selectors.
     */
    private ObjectListSelector pseudoElementSelector;

    /**
     * Collection of listener objects to be notified if the provider state
     * changes
     */
    private StateChangeListenerCollection stateChangeListenerCollection =
            new StateChangeListenerCollection();

    /**
     * Create a SelectorSequenceProvider with the specified parent and style
     * (the project is not used).
     *
     * @param parent The parent composite
     * @param style The style of this composite
     * @param project The project
     */
    public SelectorSequenceProvider(Composite parent, int style,
                                    IProject project) {
        super(parent, style);
        createControl();
    }

    /**
     * Creates the actual selector control.
     */
    private void createControl() {
        // create the layout
        GridLayout layout = new GridLayout(2, false);

        layout.marginHeight = ControlsMessages.getInteger(RESOURCE_PREFIX +
                "marginHeight").intValue();
        layout.marginWidth = ControlsMessages.getInteger(RESOURCE_PREFIX +
                "marginWidth").intValue();
        layout.verticalSpacing = layout.marginHeight;

        layout.marginWidth = 0;
        layout.marginHeight = 0;
        setLayout(layout);

        Group group = new Group(this, SWT.NONE);
        group.setText(GROUP_LABEL);
        group.setLayout(new GridLayout(2, false));
        GridData data = new GridData(GridData.FILL_BOTH);
        group.setLayoutData(data);

        addLabel(group, "elementSelector");
        elementsCombo = addCombo(group, ELEMENTS);

        addLabel(group, "namespaceSelector");
        namespaceCombo = addCombo(group, NAMESPACE_PREFIXES);

        // Select the first item.
        namespaceCombo.getCombo().select(0);

        addLabel(group, "classSelector");
        classSelector = addClassSelector(group);

        addLabel(group, "idSelector");
        idText = addTextField(group);

        addLabel(group, "attributeSelector");
        attributeSelector = addAttributeSelector(group);

        addLabel(group, "pseudoClassSelector");

        pseudoClassSelector = addPseudoClassSelector(group,
                PSEUDO_CLASS_TEMPLATES, PSEUDO_CLASS_TEMPLATES,
                PARAMETERISED_PSEUDO_SELECTORS,
                DUMMY_INDEPENDENT_VALIDATOR);

        addLabel(group, "pseudoElementSelector");

        pseudoElementSelector = addPseudoElementSelector(group,
                PSEUDO_ELEMENT_TEMPLATES, PSEUDO_ELEMENT_TEMPLATES,
                PARAMETERISED_PSEUDO_SELECTORS,
                DUMMY_INDEPENDENT_VALIDATOR);

        addValueChangeListener();

        layout();
    }

    // Javadoc inherited
    public Object getValue() {
        SelectorSequence sequence =
                MODEL_FACTORY.createSelectorSequence();

        String namespace = (String) namespaceCombo.getValue();
        String element = (String) elementsCombo.getValue();
        if (element != null && element.length() > 0) {
            ElementSelector elementSelector = null;
            if (ThemeConstants.UNIVERSAL_SELECTOR.equals(element)) {
                elementSelector = MODEL_FACTORY.createUniversalSelector();
            } else {
                TypeSelector typeSelector =
                        MODEL_FACTORY.createTypeSelector();
                typeSelector.setType(element);
                elementSelector = typeSelector;
            }
            if (elementSelector != null) {
                if (namespace != null && namespace.length() > 0) {
                    elementSelector.setNamespacePrefix(namespace);
                }
                sequence.addSelector(elementSelector);
            }
        }

        if (idText.getText().trim().length() > 0) {
            String newId = idText.getText().trim();
            IdSelector idSelector = MODEL_FACTORY.createIdSelector(newId);
            sequence.addSelector(idSelector);
        }        

        Object[] classes = classSelector.getSelectedObjects();
        if (classes != null && classes.length > 0) {
            for (int i = 0; i < classes.length; i++) {
                if (classes[i] instanceof ClassSelector) {
                    sequence.addSelector((Selector)classes[i]);
                } else {
                    throw new IllegalStateException("Expected class selector");
                }
            }
        }

        Object[] attributes = attributeSelector.getSelectedObjects();
        if (attributes != null && attributes.length > 0) {
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i] instanceof AttributeSelector) {
                    sequence.addSelector((Selector) attributes[i]);
                } else {
                    throw new IllegalStateException(
                            "Expected attribute selector");
                }
            }
        }

        Object[] pseudoClasses = pseudoClassSelector.getSelectedObjects();
        if (pseudoClasses != null && pseudoClasses.length > 0) {
            for (int i = 0; i < pseudoClasses.length; i++) {
                Object selector = pseudoClasses[i];
                if (selector instanceof PseudoClassSelector) {
                    PseudoClassSelector pseudoClassSelector =
                            (PseudoClassSelector) selector;
                    sequence.addSelector(pseudoClassSelector);
                } else {
                    throw new IllegalStateException(
                            "Expected pseudo class selector");
                }
            }
        }

        Object[] pseudoElements = pseudoElementSelector.getSelectedObjects();
        if (pseudoElements != null && pseudoElements.length > 0) {
            for (int i = 0; i < pseudoElements.length; i++) {
                if (pseudoElements[i] instanceof PseudoElementSelector) {
                    sequence.addSelector((Selector) pseudoElements[i]);
                } else {
                    throw new IllegalStateException(
                            "Expected pseudo element selector");
                }
            }
        }

        return sequence;
    }

    /**
     * The ability to set a value for the SelectorSequenceProvider is currently
     * unsupported.
     *
     * @param newValue The value to set
     * @throws UnsupportedOperationException In all cases
     */
    public void setValue(Object newValue) {
        if (newValue == null) {
            elementsCombo.getCombo().select(0);
            namespaceCombo.getCombo().select(0);
            idText.setText("");
            attributeSelector.setSelectedObjects(new Object[0]);
            classSelector.setSelectedObjects(new Object[0]);
            pseudoClassSelector.setSelectedObjects(new Object[0]);
            pseudoElementSelector.setSelectedObjects(new Object[0]);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Perform validation.
     *
     * @todo Implement validation. Current implementation assumes OK, since any values entered are considered acceptable.
     * @return The appropriate validation status for the validated object
     */
    public ValidationStatus validate() {
        return VALIDATE_OK;
    }

    /**
     * Adds a label with its text specified by the specified key.
     *
     * @param parent The parent composite
     * @param key The message key to use for the label text
     */
    private void addLabel(Composite parent, String key) {
        String text = ControlsMessages.getString(
                RESOURCE_PREFIX + key + ".label");
        new Label(parent, SWT.NONE).setText(text);
    }

    /**
     * Add a simple drop-down combo box representing a series of presentable
     * items.
     *
     * @param parent The parent composite for the combo box
     * @param values The values to display in the combo box
     * @return The created {@link ComboViewer}
     */
    private ComboViewer addCombo(Composite parent, PresentableItem[] values) {
        int style = (SWT.READ_ONLY | SWT.DROP_DOWN);
        final ComboViewer combo =
                new ComboViewer(parent, style, values);

        GridData comboGridData = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(comboGridData);

        return combo;
    }

    /**
     * Add a simple text field.
     *
     * @param parent The parent composite for the text field
     * @return The created {@link Text}
     */
    private Text addTextField(Composite parent) {
        final Text text = new Text(parent, SWT.BORDER | SWT.SINGLE);

        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(textGridData);

        return text;
    }

    /**
     * Add a object list selector for class selectors. Class selectors are all
     * created with a default (empty) class selector, and are populated by
     * editing them in-place.
     *
     * @param parent The parent composite for the object list selector
     * @return The created {@link ObjectListSelector} control
     */
    private ObjectListSelector addClassSelector(Composite parent) {
        final ObjectListSelector selector = new ObjectListSelector(parent,
                SWT.NORMAL, PARSER_FACTORY.createClassSelectorParser(),
                new ClassSelectorProviderFactory(), null, true,
                "ClassSelector.");

        return selector;
    }

    /**
     * Add an object list selector for pseudo-element selectors.
     *
     * @param parent The parent composite for the object list selector
     * @param selectorTemplates The internal values to use for the selector names
     * @param selectorDisplay The display values to use for the selector names
     * @param selectorParameterised The selectors that should be allowed to
     *                              specify parameters
     * @param validator The {@link IndependentValidator} to use for validation
     * @return The created {@link ObjectListSelector} control
     */
    private ObjectListSelector addPseudoElementSelector(Composite parent,
                                               String[] selectorTemplates,
                                               String[] selectorDisplay,
                                               String[] selectorParameterised,
                                               IndependentValidator validator) {
        final ObjectListSelector selector =
                new ObjectListSelector(parent, SWT.NORMAL,
                        PARSER_FACTORY.createPseudoElementSelectorParser(),
                        new PseudoElementProviderFactory(selectorTemplates,
                                selectorDisplay, selectorParameterised),
                        validator, false,
                        PSEUDO_ELEMENT_SELECTOR_RESOURCE_PREFIX);

        GridData objectSelectorGridData = new GridData(GridData.FILL_HORIZONTAL);
        selector.setLayoutData(objectSelectorGridData);

        return selector;
    }

    /**
     * Add an object list selector for pseudo-class selectors.
     *
     * @param parent The parent composite for the object list selector
     * @param selectorTemplates The internal values to use for the selector names
     * @param selectorDisplay The display values to use for the selector names
     * @param selectorParameterised The selectors that should be allowed to
     *                              specify parameters
     * @param validator The {@link IndependentValidator} to use for validation
     * @return The created {@link ObjectListSelector} control
     */
    private ObjectListSelector addPseudoClassSelector(Composite parent,
                                               String[] selectorTemplates,
                                               String[] selectorDisplay,
                                               String[] selectorParameterised,
                                               IndependentValidator validator) {
        final ObjectListSelector selector =
                new ObjectListSelector(parent, SWT.NORMAL,
                        PARSER_FACTORY.createPseudoClassSelectorParser(),
                        new PseudoClassProviderFactory(selectorTemplates,
                                selectorDisplay, selectorParameterised),
                        validator, false,
                        PSEUDO_CLASS_SELECTOR_RESOURCE_PREFIX);

        GridData objectSelectorGridData = new GridData(GridData.FILL_HORIZONTAL);
        selector.setLayoutData(objectSelectorGridData);

        return selector;
    }

    /**
     * Add a object list selector for attribute selectors. Uses the attribute
     * selector provider to generate attribute selectors, which are
     * non-editable thereafter.
     *
     * @param parent The parent composite for the object list selector
     * @return The created {@link ObjectListSelector} control
     */
    private ObjectListSelector addAttributeSelector(Composite parent) {
        final ObjectListSelector selector = new ObjectListSelector(parent,
                SWT.NORMAL, PARSER_FACTORY.createAttributeSelectorParser(),
                new AttributeSelectorProviderFactory(), null, false,
                "AttributeSelector.");

        GridData objectSelectorGridData =
                new GridData(GridData.FILL_HORIZONTAL);
        selector.setLayoutData(objectSelectorGridData);

        return selector;
    }

    /**
     * add listeners to the objects
     */
    private void addValueChangeListener() {
        //create a listener for modify events
        ModifyListener listener = new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                notifyStateChangeListener();
            }
        };

        //create a state change listener
        StateChangeListener stateChangeListener = new StateChangeListener() {
            public void stateChanged() {
                notifyStateChangeListener();
            }
        };

        //add the listeners to the components
        elementsCombo.addModifyListener(listener);
        namespaceCombo.addModifyListener(listener);
        classSelector.addStateChangeListener(stateChangeListener);
        idText.addModifyListener(listener);
        attributeSelector.addStateChangeListener(stateChangeListener);
        pseudoClassSelector.addStateChangeListener(stateChangeListener);
        pseudoElementSelector.addStateChangeListener(stateChangeListener);

    }


    /**
     * check that at least one of the components has a value
     * @return true iff one of the components has a value
     */
    public boolean canProvideObject() {
        boolean result = false;
        if ((elementsCombo.getValue() != null &&
                ((String) elementsCombo.getValue()).length() > 0)||
            (namespaceCombo.getValue() != null &&
                ((String) namespaceCombo.getValue()).length() > 0) ||
            (classSelector.getText() != null &&
                classSelector.getText().getText().length() > 0) ||
            (idText.getText() != null &&
                idText.getText().length() > 0) ||
            (attributeSelector.getText() != null &&
                attributeSelector.getText().getText().length() > 0) ||
            (pseudoClassSelector.getText() != null &&
                pseudoClassSelector.getText().getText().length() > 0) ||
            (pseudoElementSelector.getText() != null &&
                pseudoElementSelector.getText().getText().length() > 0)) {

                result = true;
        }

        return result;
    }

    //javadoc inherited
    public void addStateChangeListener(StateChangeListener listener) {
        stateChangeListenerCollection.addStateChangeListener(listener);
    }

    //javadoc inherited
    public void removeStateChangeListener(StateChangeListener listener) {
        stateChangeListenerCollection.removeStateChangeListener(listener);
    }

    /**
     * notify the listener that the state has changed
     */
    private void notifyStateChangeListener() {
        stateChangeListenerCollection.notifyListeners();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/3	emma	VBM:2005111705 Interim commit

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 08-Nov-05	10195/1	adrianj	VBM:2005101803 Display deprecated elements in selector wizard

 07-Nov-05	10179/1	adrianj	VBM:2005101803 Show deprecated elements in selector group wizard

 02-Nov-05	10084/1	adrianj	VBM:2005110109 Fix for selector wizard

 02-Nov-05	10077/1	adrianj	VBM:2005110109 Fix for new selector dialog

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
