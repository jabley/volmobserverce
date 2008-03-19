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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.common.ResourceUnits;
import com.volantis.mcs.eclipse.controls.UnitsCombo;
import com.volantis.mcs.layouts.FormatConstants;
import org.eclipse.swt.widgets.Composite;

import java.util.List;

/**
 * This class specializes UnitsCombo for use in the LayoutEditor.  This returns pixel
 * and percent units that are compatible with the Volantis markup used for layouts.
 *
 */
public class LayoutUnitsCombo extends UnitsCombo {
    /**
     * construct a new instance of LayoutUnitsCombo
     * @param parent The swt container for this component.
     * @param localizedUnits A list of units, in their localized form to display.
     */
    public LayoutUnitsCombo (Composite parent, List localizedUnits){
        super (parent, localizedUnits, true);
    }

    /**
     * Return the layout specific name for the selected unit.
     * @return the currently selected unit name in layout specific form. ie return the
     * standard form for the unit unless it is pixel or percent in which case layout
     * specific overrides apply
     */
    public String getSelectedUnit() {

        String sunit = super.getSelectedUnit();           // standard unit name

        if (sunit.equals(ResourceUnits.PIXEL.getUnit())){
            sunit = FormatConstants.WIDTH_UNITS_VALUE_PIXELS;
        } else if (sunit.equals(ResourceUnits.PERCENT.getUnit())) {
            sunit = FormatConstants.WIDTH_UNITS_VALUE_PERCENT;
        }
        return sunit;
    }

    /**
     * Set the selected unit using the standard unit name as specialised for
     * layouts (same as standard names except for PIXEL and PERCENT)
     *
     * This method throws an IllegalArgumentException if the supplied name
     * does not correspond to a unit which has been specified for this control.
     * @param unitName
     */
    public void setSelectedUnit(String unitName) {

        if (unitName.equals(FormatConstants.WIDTH_UNITS_VALUE_PIXELS)){
            unitName = ResourceUnits.PIXEL.getUnit();
        } else if (unitName.equals(FormatConstants.WIDTH_UNITS_VALUE_PERCENT)){
            unitName = ResourceUnits.PERCENT.getUnit();
        }
        super.setSelectedUnit(unitName);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7133/1	pcameron	VBM:2004091014 Fixed behaviour of width units in Layout editor

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Dec-03	2170/1	pcameron	VBM:2003102103 Added AssetTypeSection

 17-Nov-03	1903/1	tony	VBM:2003110610 fixed merge problems on original commit

 14-Nov-03	1880/1	tony	VBM:2003110705 UnitsCombo and LayoutUnitsCombo complete per VBM

 ===========================================================================
*/
