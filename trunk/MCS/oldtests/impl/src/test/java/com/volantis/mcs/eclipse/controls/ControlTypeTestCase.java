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
package com.volantis.mcs.eclipse.controls;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.common.ControlType;
import junitx.util.PrivateAccessor;

import java.util.HashMap;

/**
 * Testcase for ControlType typesafe enumerator.
 */
public class ControlTypeTestCase extends TestCaseAbstract {

    private static final String TYPE_ARRAY [] = {
        "BackgroundComponent",
        "CellIterations",
        "CheckBox",
        "ColorSelector",
        "DeviceSelector",
        "EditableCombo",
        "EditableCombo",
        "LayoutNumberUnits",
        "PolicySelector",
        "ReadOnlyCombo",
        "ReadOnlyComboViewer",
        "Text",
        "TextDefinition",
        "TimeSelector",
        "StyleSelector",
        "URIFragment"
    };

    /**
     * Test that there is a ControlType for each known type.
     */
    public void testControlTypes() {
        for (int i = 0; i < TYPE_ARRAY.length; i++) {
            assertNotNull("Expected an ControlType of: " +
                    TYPE_ARRAY[i] + ".",
                    ControlType.getControlType(TYPE_ARRAY[i]));
        }
    }

    /**
     * Test that the number of ControlTypes is the same as the number known.
     */
    public void testNumberOfControlTypes() throws Exception {
        HashMap controlTypes = (HashMap)
                PrivateAccessor.getField(ControlType.class, "controlTypes");
        assertEquals("TYPE_ARRAY and controlTypes should be the same size",
                TYPE_ARRAY.length, controlTypes.size());
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

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
