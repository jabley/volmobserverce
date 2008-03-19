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

import com.volantis.mcs.eclipse.common.ResourceUnits;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import java.util.List;

/**
 * An SWT combo which facilitates the selection of a style unit name: these are
 * the units for length, time and angle used in style markup.  This component
 * provides all the units specified in the typesage enumeration ResourceUnits.
 */
public class UnitsCombo extends Composite {
    /**
     * Resource prefix for the FontFamilytSelectionDialog.
     */
    private final static String RESOURCE_PREFIX = "UnitsCombo.";

    /**
     * The name of the combo box.
     */
    private static final String UNITS_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "units");

    /**
     * A flag that will be used to determine whether the combo control
     * contains an empty item in order to allow a unit to be unselected
     */
    private boolean isEmptyItemRequired;

    /**
     * The wrapped Combo control
     */
    private Combo theCombo;

    /**
     * Creates a new UnitsCombo and populate it with a list of localized unit
     * names
     *
     * @param parent a composite widget to add this widget to.
     * @param localizedUnits the list of units
     * @param isEmptyItemRequired  A flag that will be used to determine
     * whether the combo control contains an empty item in order to allow a
     * unit to be unselected
     */
    public UnitsCombo(Composite parent,
                      List localizedUnits,
                      boolean isEmptyItemRequired) {

        super(parent, SWT.NONE);
        this.isEmptyItemRequired = isEmptyItemRequired;
        setLayout(new FormLayout());

        theCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);

        // Populate the combo from the properties file
        populateWithUnits(localizedUnits);

        initAccessible();
    }

    /**
     * Creates a new UnitsCombo and populate it with a list of localized unit
     * names
     *
     * @param parent a composite widget to add this widget to.
     * @param localizedUnits the list of units
     */
    public UnitsCombo(Composite parent,
                      List localizedUnits) {
        this(parent, localizedUnits, false);
    }

    /**
     * Add a selction listener to this control.
     *
     * @param listener the listener to Combo selections
     */
    public void addSelectionListener(SelectionListener listener) {
        theCombo.addSelectionListener(listener);
    }

    /**
     * Remove a previously added selection listener.
     *
     * @param listener The listener to remove.
     */
    public void removeSelectionListener (SelectionListener listener) {
        theCombo.removeSelectionListener(listener);
    }

    /**
     * Populate this UnitsCombo control with a list LocalizedUnit objects.
     * <p>
     * This method checks that each name in the list is a valid localized name
     * for a markup unit (according to ResourceUnits)
     *
     *
     * @param localizedUnits the units to include in the combo
     * @see com.volantis.mcs.eclipse.common.ResourceUnits
     */
    private void populateWithUnits(List localizedUnits) {

        if (isEmptyItemRequired) {
            theCombo.add("");
        }
        for (int i = 0; i < localizedUnits.size(); i++) {
            // get the next object and th list and check that it really
            // is a LocalizedUnit.
            Object o = localizedUnits.get(i);
            if ( ! (o instanceof ResourceUnits) ){
                throw new IllegalArgumentException("Object number " + i +
                        " on the list supplied to UnitsCombo was not a " +
                        "ResourceUnits instance.");
            }

            String lunitName = ((ResourceUnits) o).getLocalized();

            theCombo.add(lunitName);
        }
    }

    /**
     * Return the standard unit name of the control
     * @return the currently selected FileFilter.
     */
    public String getSelectedUnit() {
        String lunit = null;    // localized name from control
        String sunit = null;           // standard name
        int idx = theCombo.getSelectionIndex();
        if (idx > -1) {
            lunit = theCombo.getItem(idx);
            // the return from this method does not need to be checked because
            // the control can only be populated with valid
            if ("".equals(lunit)) {
                sunit = "";
            } else {
                sunit = ResourceUnits.getUnitFromLocalizedName(lunit);
            }
        }
        return sunit;
    }

    /**
     * Set the selected unit using the standard unit name.
     *
     * This method throws an IllegalArgumentException if the supplied name
     * does not correspond to a unit which has been specified for this control.
     *
     * @param unitName The standard (non localized) name for the unit to be
     * selected
     */
    public void setSelectedUnit(String unitName){
        boolean found = false;

        if (unitName == null) {
            theCombo.select(0);
        } else {
            if ("".equals(unitName) && isEmptyItemRequired) {
                theCombo.select(0);
            } else {
                String localizedName =
                    ResourceUnits.getLocalizedFromUnitName(unitName);
                if (localizedName != null){
                    String [] items = theCombo.getItems();
                    for (int i = 0; i < items.length; i++ ) {
                        if (items[i].equals(localizedName)) {
                            theCombo.select(i);
                            found = true;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException (
                                "The value supplied is not valid for this " +
                                "UnitsCombo");
                    }
                }
            }
        }
    }

    // javadoc inherited
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        theCombo.setEnabled(b);
    }

    /**
     * Overridden to set the focus to the underlying combo.
     */
    public boolean setFocus() {
        return theCombo.setFocus();
    }

    /**
     * Get the combo control that is to be used to display the units.
     * @return the combo control that is used to display the units.
     */
    public Combo getCombo() {
        return theCombo;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL() {
            public void getValue(AccessibleControlEvent ae) {
                ae.result = theCombo.getText();
            }
        };
        acl.setControl(this);
        acl.setRole(ACC.ROLE_COMBOBOX);
        getAccessible().addAccessibleControlListener(acl);

        StandardAccessibleListener al = new StandardAccessibleListener(theCombo) {
            public void getName(AccessibleEvent event) {
                event.result = UNITS_TEXT;
            }
        };
        theCombo.getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jan-05	6603/1	adrianj	VBM:2004120801 Added names for accessible components

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/2	adrianj	VBM:2004102602 Accessibility support for custom controls

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2526/5	doug	VBM:2003112607 Added the StyleValueComposite control

 14-Jan-04	2526/3	doug	VBM:2003112607 Added the StyleValueComposite control

 09-Dec-03	2170/1	pcameron	VBM:2003102103 Added AssetTypeSection

 28-Nov-03	2013/1	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 17-Nov-03	1903/3	tony	VBM:2003110610 removed (useless) commented out code and println()s

 17-Nov-03	1903/1	tony	VBM:2003110610 fixed merge problems on original commit

 14-Nov-03	1880/5	tony	VBM:2003110705 Fixed typo in exception message for UnitsCombo constructor

 14-Nov-03	1880/3	tony	VBM:2003110705 added removeSelectionListener() and fixed constructor in UnitsCombo

 14-Nov-03	1880/1	tony	VBM:2003110705 UnitsCombo and LayoutUnitsCombo complete per VBM

 ===========================================================================
*/
