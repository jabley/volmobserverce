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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.diselect;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.expression.functions.diselect.DIDeviceHeightFunction;
import com.volantis.mcs.expression.functions.diselect.DILengthFunction;

/**
 * Sub class which returns specific information about the DILengthFunction
 * for use in the test cases defined in the parent class.
 */
public class DIHeightFunctionTestCase extends DILengthFunctionTestAbstract {

    // Javadoc inherited.
    public DILengthFunction getFunction() {
        return new DIDeviceHeightFunction();
    }

    // Javadoc inherited.
    public String[] getPolicyNames() {
        return new String[]{
            DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS,
            DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM,
            DevicePolicyConstants.USABLE_HEIGHT_IN_PIXELS};
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
