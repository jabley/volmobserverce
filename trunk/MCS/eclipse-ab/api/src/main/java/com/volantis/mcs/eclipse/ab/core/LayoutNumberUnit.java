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
package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.ab.editors.layout.LayoutUnitsCombo;
import com.volantis.mcs.eclipse.controls.SecondaryControl;
import com.volantis.mcs.eclipse.controls.StandardAccessibleListener;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import java.util.List;

/**
 * A composite control that allows the user to input a value and select the unit type.
 * <p>
 * This control is designed for use with Layout editors.
 *
 */
public class LayoutNumberUnit extends Composite
        implements SecondaryControl {
    /**
     * Resource prefix for the layout number unit.
     */
    private final static String LAYOUT_NUMBER_UNIT_PREFIX =
            "LayoutNumberUnit.";

    /**
     * The accessible name for the value text field.
     */
    private static final String TEXT_VALUE =
            ControlsMessages.getString(LAYOUT_NUMBER_UNIT_PREFIX +
            "valueText");

    /**
     * The accessible name for the units combo box.
     */
    private static final String TEXT_UNITS =
            ControlsMessages.getString(LAYOUT_NUMBER_UNIT_PREFIX +
            "unitsText");

    private LayoutUnitsCombo theCombo;
    private Text theText;

    /**
     * Construct a new instance of this control.<p>
     *
     * The localizedUnits is passed in to the constuctor to provide flexibility
     * to provide more/less localized units defined in:
     * {@link com.volantis.mcs.eclipse.common.ResourceUnits}
     *
     * @param parent The SWT parent Composite
     * @param localizedUnits A list of LocalizedUnit
     */
    public LayoutNumberUnit(Composite parent, List localizedUnits) {
        super(parent, SWT.NONE);
        theText = new Text(this, SWT.BORDER);
        theCombo = new LayoutUnitsCombo(this, localizedUnits);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        theText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        initAccessible();
    }

    /**
     * Getter for the text control.
     *
     * @return the text control.
     */
    public Text getText() {
        return theText;
    }

    /**
     * Set the value that appears in the number Text field
     * @param numString The number String.  LayoutNumberUnit does not
     * check this value.
     */
    public void setNumber(String numString) {
        if (numString==null){
            theText.setText("");
        } else {
            theText.setText(numString);
        }
    }
    /**
     * Return the current contents of the numeric input field.
     * @return Contents of the numeric input field.  This value is not checked
     * by this control.
     */
    public String getNumber() {
        return theText.getText();
    }

    /**
     * Set the unit currently displayed by this control.
     * @param unitName The unit to display in standard layout format.
     */
    public void setUnit(String unitName) {
        theCombo.setSelectedUnit(unitName);

    }

    /**
     * Get the current selected unit name.
     * @return The unit name in standard layout format.
     */
    public String getUnit() {
        return theCombo.getSelectedUnit();
    }

    /**
     * Overridden to set the focus to the Text.
     */
    public boolean setFocus() {
        return theText.setFocus();
    }

    /**
     * Adds a listener that is called when the contents of the text control is
     * modified.
     *
     * @param listener for modifications
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            theText.addModifyListener(listener);
        }
    }

    /**
     * Removes a control modification listener
     *
     * @param listener for modifications
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            theText.removeModifyListener(listener);
        }
    }

    /**
     * Adds a listener that is called when the selection in the combo control
     * changes.
     *
     * @param listener for selection changes.
     */
    public void addSelectionListener(SelectionListener listener) {
        if (listener != null) {
            theCombo.addSelectionListener(listener);
        }
    }

    /**
     * Removes the selection listener from the combo box.
     *
     * @param listener for selection changes.
     */
    public void removeSelectionListener(SelectionListener listener) {
        if (listener != null) {
            theCombo.removeSelectionListener(listener);
        }
    }

    // javadoc inherited
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        theCombo.setEnabled(enabled);
        theText.setEnabled(enabled);
    }

    // javadoc inherited
    public void setSecondaryVisible(boolean visible) {
        if (theCombo != null) {
            theCombo.setVisible(visible);
        }
    }

    // javadoc inherited.
    public void setSecondaryEnabled(boolean enabled) {
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener unitsListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = TEXT_UNITS;
                    }
                };
        unitsListener.setControl(theCombo);
        theCombo.getAccessible().addAccessibleListener(unitsListener);

        StandardAccessibleListener valueListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = TEXT_VALUE;
                    }
                };
        valueListener.setControl(theText);
        theText.getAccessible().addAccessibleListener(valueListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/2	adrianj	VBM:2004102602 Accessibility support for custom controls

 19-Aug-04	5264/2	allan	VBM:2004081008 Remove invalid plugin dependencies

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: hide units control if element is a grid

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 22-Jan-04	2540/2	byron	VBM:2003121505 Added main formats attribute page

 28-Nov-03	2013/1	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 17-Nov-03	1903/2	tony	VBM:2003110610 fixed merge problems on original commit

 ===========================================================================
*/
