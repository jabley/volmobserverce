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
import com.volantis.mcs.themes.StyleSheetFactory;
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
 * A GUI component for creating pseudo selectors.
 */
public abstract class PseudoSelectorProvider extends Composite
        implements ValidatedObjectControl {
    /**
     * A default OK validation.
     */
    private ValidationStatus VALIDATE_OK =
            new ValidationStatus(ValidationStatus.OK, "");

    /**
     * The prefix for resources.
     */
    private static final String RESOURCE_PREFIX = "PseudoSelectorProvider.";

    /**
     * The factory to use for creating new model instances.
     */
    protected static final StyleSheetFactory MODEL_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    /**
     * The text for the pseudo-selector label.
     */
    private static final String PSEUDO_SELECTOR_LABEL =
            ControlsMessages.getString(RESOURCE_PREFIX + "pseudoselector.label");

    /**
     * The text for the parameter label.
     */
    private static final String PARAMETER_LABEL =
            ControlsMessages.getString(RESOURCE_PREFIX + "parameter.label");

    /**
     * The text field containing the parameter (if valid).
     */
    protected Text parameterText;

    /**
     * The combo for selecting selectors.
     */
    protected Combo pseudoSelectorsCombo;

    /**
     * An array of names for the pseudo selectors supported by this provider.
     */
    protected String[] templates;

    /**
     * An array of display values for the pseudo selectors supported by this
     * provider.
     */
    private String[] displayValues;

    /**
     * An array of names for the pseudo selectors supported by this provider
     * that can have parameters associated with them.
     */
    protected String[] parameterised;

    /**
     * Create a new pseudo selector provider.
     *
     * @param parent The parent composite in which the provider will be created
     * @param style The style for the provider
     * @param templates The internal pseudo selector names to use
     * @param displayValues The display pseudo selector names to use
     * @param parameterised The pseudo selector names which can have parameters
     *                      associated with them
     */
    public PseudoSelectorProvider(Composite parent, int style,
                                  String[] templates, String[] displayValues,
                                  String[] parameterised) {
        super(parent, style);
        this.templates = templates;
        this.displayValues = displayValues;
        this.parameterised = parameterised;
        initControl();
    }

    /**
     * The pseudo-selector provider is expected to be used in a read-only
     * environment, and as such any attempt to set a value against it is
     * currently ignored.
     *
     * @param newValue The new value to ignore
     */
    public void setValue(Object newValue) {
    }

    // Javadoc inherited
    public ValidationStatus validate() {
        return VALIDATE_OK;
    }

    /**
     * Build the user interface for this composite.
     */
    private void initControl() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;
        this.setLayout(layout);
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label t1 = new Label(this, SWT.NONE);
        t1.setText(PSEUDO_SELECTOR_LABEL);

        Label t2 = new Label(this, SWT.NONE);
        t2.setText(PARAMETER_LABEL);

        pseudoSelectorsCombo = new Combo(this, SWT.READ_ONLY);
        parameterText = new Text(this, SWT.BORDER);

        for (int i = 0; i < templates.length; i++) {
            pseudoSelectorsCombo.add(displayValues[i]);
        }

        pseudoSelectorsCombo.select(0);

        pseudoSelectorsCombo.addModifyListener(
            new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    try {
                        updateParameterEnabled();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        );

        updateParameterEnabled();

        t1.setLayoutData(new GridData(GridData.FILL_BOTH));
        t2.setLayoutData(new GridData(GridData.FILL_BOTH));
        pseudoSelectorsCombo.setLayoutData(new GridData(GridData.FILL_BOTH));
        parameterText.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /**
     * Checks whether a specified pseudo selector name can have a parameter.
     *
     * @param template The template name to check
     * @return True if the specified selector can have a parameter
     */
    protected boolean isParameterised(String template) {
        boolean isParameterised = false;
        for (int i = 0; i < parameterised.length && !isParameterised; i++) {
            if (parameterised[i].equals(template)) {
                isParameterised = true;
            }
        }
        return isParameterised;
    }

    /**
     * Update the enabled status of the parameter text field based on the pseudo
     * selector currently selected.
     */
    private void updateParameterEnabled() {
        int selectionIndex = pseudoSelectorsCombo.getSelectionIndex();
        // Selection index flips to -1 between selections - ignore these
        // cases, since we'll be getting a proper index immediately afterwards
        if (selectionIndex >= 0) {
            String selectedTemplate = templates[selectionIndex];

            if (isParameterised(selectedTemplate)) {
                parameterText.setEnabled(true);
            } else {
                parameterText.setEnabled(false);
                parameterText.setText("");
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

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 20-Sep-05	9380/3	adrianj	VBM:2005082401 Tidy up and javadoc nth-child support

 14-Sep-05	9380/1	adrianj	VBM:2005082401 GUI support for nth-child

 ===========================================================================
*/
