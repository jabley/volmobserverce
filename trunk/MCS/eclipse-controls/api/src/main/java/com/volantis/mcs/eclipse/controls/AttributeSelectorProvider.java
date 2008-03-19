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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.AttributeSelectorActionEnum;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.constraints.Constraint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A GUI component for creating attribute selectors.
 */
public class AttributeSelectorProvider extends Composite
        implements ValidatedObjectControl {
    /**
     * The theme model factory to use for creating the attribute selector.
     */
    private static final StyleSheetFactory MODEL_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    /**
     * The prefix for resources.
     */
    private static final String RESOURCE_PREFIX = "AttributeSelectorProvider.";

    /**
     * The text for the attribute label.
     */
    private static final String ATTRIBUTE_LABEL =
            ControlsMessages.getString(RESOURCE_PREFIX + "attribute.label");

    /**
     * The text for the action label.
     */
    private static final String ACTION_LABEL =
            ControlsMessages.getString(RESOURCE_PREFIX + "action.label");

    /**
     * The text for the value label.
     */
    private static final String VALUE_LABEL =
            ControlsMessages.getString(RESOURCE_PREFIX + "value.label");

    /**
     * The actions available for attribute selectors.
     */
    private static final AttributeSelectorActionEnum[] ACTIONS = {
        AttributeSelectorActionEnum.SET,
        AttributeSelectorActionEnum.EQUALS,
        AttributeSelectorActionEnum.CONTAINS_WORD,
        AttributeSelectorActionEnum.LANGUAGE_MATCH,
        AttributeSelectorActionEnum.STARTS_WITH,
        AttributeSelectorActionEnum.ENDS_WITH,
        AttributeSelectorActionEnum.CONTAINS,
    };

    /**
     * The text field containing the attribute name.
     */
    private Text attributeText;

    /**
     * The combo for selecting actions.
     */
    private Combo actionsCombo;

    /**
     * The text field containing the attribute value.
     */
    private Text valueText;

    /**
     * Create a new attribute selector provider.
     *
     * @param parent The parent composite in which the provider will be created
     * @param style The style for the provider
     */
    public AttributeSelectorProvider(Composite parent, int style) {
        super(parent, style);
        initControl();
    }

    /**
     * A default OK validation.
     */
    private ValidationStatus VALIDATE_OK =
            new ValidationStatus(ValidationStatus.OK, "");

    // Javadoc inherited
    public Object getValue() {
        String attribute = attributeText.getText();
        AttributeSelectorActionEnum action =
                ACTIONS[actionsCombo.getSelectionIndex()];
        String value = valueText.getText();

        AttributeSelector selector = MODEL_FACTORY.createAttributeSelector();
        selector.setName(attribute);
        selector.setConstraint(action, value);
        return selector;
    }

    /**
     * Sets the value of the attribute selector.
     *
     * @param newValue The new value to set
     */
    public void setValue(Object newValue) {
        if (newValue == null || !(newValue instanceof AttributeSelector)) {
            attributeText.setText("");
            valueText.setText("");
            actionsCombo.select(0);
            updateValueEnabled();
        } else {
            AttributeSelector newSelector = (AttributeSelector) newValue;
            attributeText.setText(newSelector.getName());
            Constraint constraint = newSelector.getConstraint();
            valueText.setText(constraint.getValue());
            int selectedAction = -1;
            final AttributeSelectorActionEnum action =
                    AttributeSelectorActionEnum.
                    getAttributeSelectorActionEnum(constraint);

            for (int i = 0; selectedAction == -1 && i < ACTIONS.length; i++) {
                if (ACTIONS[i].equals(action)) {
                    selectedAction = i;
                }
            }
            if (selectedAction == -1) {
                selectedAction = 0;
            }
            actionsCombo.select(selectedAction);
            updateValueEnabled();
        }
    }

    // Javadoc inherited
    public ValidationStatus validate() {
        return VALIDATE_OK;
    }

    /**
     * Build the GUI control.
     */
    private void initControl() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = true;
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));


        Label t1 = new Label(this, SWT.NONE);
        t1.setText(ATTRIBUTE_LABEL);

        Label t2 = new Label(this, SWT.NONE);
        t2.setText(ACTION_LABEL);

        Label t3 = new Label(this, SWT.NONE);
        t3.setText(VALUE_LABEL);

        attributeText = new Text(this, SWT.BORDER);
        actionsCombo = new Combo(this, SWT.READ_ONLY);
        valueText = new Text(this, SWT.BORDER);

        for (int i = 0; i < ACTIONS.length; i++) {
            actionsCombo.add(ACTIONS[i].toString());
        }

        actionsCombo.select(0);

        actionsCombo.addModifyListener(
            new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    try {
                        updateValueEnabled();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        );

        updateValueEnabled();

        t1.setLayoutData(new GridData(GridData.FILL_BOTH));
        t2.setLayoutData(new GridData(GridData.FILL_BOTH));
        t3.setLayoutData(new GridData(GridData.FILL_BOTH));
        attributeText.setLayoutData(new GridData(GridData.FILL_BOTH));
        actionsCombo.setLayoutData(new GridData(GridData.FILL_BOTH));
        valueText.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /**
     * Update the enabled status of the value text field based on the action
     * currently selected.
     */
    private void updateValueEnabled() {
        int selectionIndex = actionsCombo.getSelectionIndex();
        // Selection index flips to -1 between selections - ignore these
        // cases, since we'll be getting a proper index immediately afterwards
        if (selectionIndex >= 0) {
            AttributeSelectorActionEnum selectedAction =
                    ACTIONS[selectionIndex];
            if (AttributeSelectorActionEnum.SET == selectedAction) {
                valueText.setEnabled(false);
                valueText.setText("");
            } else {
                valueText.setEnabled(true);
            }
        }
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

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
