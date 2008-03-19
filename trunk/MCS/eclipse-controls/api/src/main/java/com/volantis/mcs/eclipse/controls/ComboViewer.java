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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.PresentableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;


/**
 * Creates a Combo control that uses PresentationItems to allow the
 * real values contained in the combo to be seen be the user as a
 * different view.
 */
public class ComboViewer extends Composite {

    /**
     * Resource prefix for the FontFamilytSelectionDialog.
     */
    private final static String RESOURCE_PREFIX = "ComboViewer.";

    /**
     * The Combo widget used by the control.
     */
    private Combo combo;

    /**
     * The name of the combo box.
     */
    private static final String DROP_DOWN_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "dropDown");

    /**
     * The PresentableItems for this ComboViewer.
     */
    private PresentableItem presentableItems [];

    /**
     * Creates the ComboViewer control.
     * @param parent the parent Composite
     * @param style the style of the Combo
     * @param presentableItems the content presentableItems for the ComboViewer.
     * Cannot be null or empty.
     */
    public ComboViewer(Composite parent, int style,
                       PresentableItem[] presentableItems) {
        super(parent, SWT.NONE);
        if (presentableItems == null || presentableItems.length == 0) {
            throw new IllegalArgumentException("Cannot be null " +
                    "or empty: presentableItems.");
        }
        this.presentableItems = presentableItems;
        createCombo(style);
        initAccessible();
    }

    /**
     * Creates the Combo control for ComboViewer.
     * @param style the style for the Combo, supplied
     * at construction time
     */
    private void createCombo(int style) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        this.setLayout(gridLayout);
        combo = new Combo(this, style);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(gridData);
        for (int i = 0; i < presentableItems.length; i++) {
            combo.add(presentableItems[i].presentableValue);
        }
    }

    /**
     * Sets the items for this ComboViewer.
     * @param items an array of PresentableItems for the ComboViewer. Cannot
     * be null or empty.
     * @throws IllegalArgumentException if items is null or empty
     */
    public void setItems(PresentableItem[] items) {
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    "items");
        }
        combo.removeAll();
        presentableItems = items;
        for (int i = 0; i < presentableItems.length; i++) {
            combo.add(presentableItems[i].presentableValue);
        }
    }

    /**
     * Gets the underlying value for the current selection in
     * the Combo.
     * @return The real value of the value presented or null if no
     * value is presented.
     */
    public Object getValue() {
        int i = combo.getSelectionIndex();
        Object value = null;
        if (i == -1) {
            // value does not match an entry in the combos list so the
            // user must have typed in the value manually
            value = combo.getText();
        } else {
            // the value was found in the list so return the real value.
            value = presentableItems[i].realValue;
        }
        return value;
    }

    /**
     * Sets the currently selected value for the ComboViewer.
     * If the style is read-only, then if the
     * given value is not available among the PresentationItems then
     * no value will be displayed in the text part of the Combo.
     * If the style is not read-only, then the given value will be
     * displayed regardless.
     * @param value The value.
     */
    public void setValue(Object value) {
        // Default to empty, not null, text
        String text = "";

        int i;
        boolean found = false;
        for (i = 0; i < presentableItems.length && !found; i++) {
            found = presentableItems[i].realValue.equals(value);
        }
        if (found) {
            text = presentableItems[i - 1].presentableValue;
        } else if ((getStyle() & SWT.READ_ONLY) == 0 && value != null) {
            // Not read only, so use the parameter value as long as it is
            // not null (in which case "" will be used)
            text = value.toString();
        }
        combo.setText(text);
    }

    // javadoc inherited
    public int getStyle() {
        // The underlying Combo contains the style bitset for this widget.
        return combo.getStyle();
    }

    /**
     * Adds a selection listener to the Combo control.
     * @param listener the listener to add
     */
    public void addSelectionListener(SelectionListener listener) {
        combo.addSelectionListener(listener);
    }

    /**
     * Removes a selection listener from the Combo control.
     * @param listener the listener to remove
     */
    public void removeSelectionListener(SelectionListener listener) {
        combo.removeSelectionListener(listener);
    }

    /**
     * Adds a modify listener to the Combo control.
     * @param listener the listener to add
     */
    public void addModifyListener(ModifyListener listener) {
        combo.addModifyListener(listener);
    }

    /**
     * Removes a modify listener from the Combo control.
     * @param listener the listener to remove
     */
    public void removeModifyListener(ModifyListener listener) {
        combo.removeModifyListener(listener);
    }

    /**
     * Overridden to enable or disable the Combo control.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        combo.setEnabled(enabled);
    }

    /**
     * Overridden to set the focus for the Combo control.
     */
    public boolean setFocus() {
        return combo.setFocus();
    }

    /**
     * Getter for the combo control.
     *
     * @return the combo control.
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL() {
            public void getValue(AccessibleControlEvent ae) {
                ae.result = combo.getText();
            }
        };
        acl.setControl(this);
        acl.setRole(ACC.ROLE_COMBOBOX);
        getAccessible().addAccessibleControlListener(acl);

        StandardAccessibleListener al = new StandardAccessibleListener(combo) {
            public void getName(AccessibleEvent event) {
                event.result = DROP_DOWN_TEXT;
            }
        };
        combo.getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-May-05	8452/1	matthew	VBM:2005011006 Add accesibility name to ComboViewer

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 04-May-04	4121/2	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 05-Jan-04	2393/3	pcameron	VBM:2004010204 ComboViewer now also uses ModifyListeners

 04-Jan-04	2364/3	doug	VBM:2004010401 Fixed problem with ComboViewer set/getValue()

 02-Jan-04	2332/1	richardc	VBM:2003122902 Property name changes and associated knock-ons

 14-Dec-03	2208/4	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/2	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 09-Dec-03	2170/1	pcameron	VBM:2003102103 Added AssetTypeSection

 ===========================================================================
*/
