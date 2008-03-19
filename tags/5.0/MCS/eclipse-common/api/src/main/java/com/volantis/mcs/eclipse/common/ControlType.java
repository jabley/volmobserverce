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
package com.volantis.mcs.eclipse.common;

import java.util.HashMap;

/**
 * A type safe enumeration specifying a type of control and associating it
 * with a name for convenience.
 */
public class ControlType {
    /**
     * A Background Component Control Type
     */
    public static final ControlType BACKGROUND_COMPONENT =
            new ControlType("BackgroundComponent"); //$NON-NLS-1$

    /**
     * A CheckBox Iterations ControlType
     */
    public static final ControlType CHECK_BOX =
            new ControlType("CheckBox"); //$NON-NLS-1$

    /**
     * A Cell Iterations ControlType
     */
    public static final ControlType CELL_ITERATIONS =
            new ControlType("CellIterations"); //$NON-NLS-1$

    /**
     * A ColorSelector ControlType.
     */
    public static final ControlType COLOR_SELECTOR =
            new ControlType("ColorSelector"); //$NON-NLS-1$

    /**
     * A DeviceSelector ControlType
     */
    public static final ControlType DEVICE_SELECTOR =
            new ControlType("DeviceSelector"); //$NON-NLS-1$

    /**
     * An editable Combo ControlType.
     */
    public static final ControlType EDITABLE_COMBO =
            new ControlType("EditableCombo"); //$NON-NLS-1$

    /**
     * An editable Combo ControlType.
     */
    public static final ControlType EDITABLE_COMBO_VIEWER =
            new ControlType("EditableComboViewer"); //$NON-NLS-1$

    /**
     * A LayoutNumberUnits ControlType
     */
    public static final ControlType LAYOUT_NUMBER_UNITS =
            new ControlType("LayoutNumberUnits"); //$NON-NLS-1$

    /**
     * A PolicySelector ControlType.
     */
    public static final ControlType POLICY_SELECTOR =
            new ControlType("PolicySelector"); //$NON-NLS-1$


    /**
     * A read only Combo ControlType.
     */
    public static final ControlType READ_ONLY_COMBO =
            new ControlType("ReadOnlyCombo"); //$NON-NLS-1$

    /**
     * A read only Combo ControlType.
     */
    public static final ControlType READ_ONLY_COMBO_VIEWER =
            new ControlType("ReadOnlyComboViewer"); //$NON-NLS-1$

    /**
     * A Text ControlType.
     */
    public static final ControlType TEXT = new ControlType("Text"); //$NON-NLS-1$

    /**
     * A TypeDefinition ControlType
     */
    public static final ControlType TEXT_DEFINITION =
            new ControlType("TextDefinition"); //$NON-NLS-1$

    /**
     * A TimeSelector ControlType
     */
    public static final ControlType TIME_SELECTOR =
            new ControlType("TimeSelector"); //$NON-NLS-1$

    /**
     * A StyleSelector ControlType
     */
    public static final ControlType STYLE_SELECTOR =
            new ControlType("StyleSelector");

    /**
     * A URI Fragment control type.
     */
    public static final ControlType URI_FRAGMENT =
            new ControlType("URIFragment");

    /**
     * A Map for looking up ControlTypes.
     */
    private static HashMap controlTypes;

    /**
     * The name of the control.
     */
    public final String name;


    /**
     * Construct a new ControlType with the specified name.
     */
    private ControlType(String name) {
        this.name = name;
        if(controlTypes == null) {
            controlTypes = new HashMap();
        }
        controlTypes.put(name, this);
    }

    /**
     * Convenience method to get the ControlType for a particular name.
     */
    public static ControlType getControlType(String name) {
        return (ControlType) controlTypes.get(name);
    }

    /**
     * @return the name of this ControlType
     */
    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Mar-05	7426/1	philws	VBM:2004121405 Port URI fragment asset value encoding and decoding from 3.3

 15-Mar-05	7374/1	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 16-Dec-03	2213/2	allan	VBM:2003121401 More editors and fixes for presentable values.

 27-Nov-03	2013/3	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
