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
import com.volantis.mcs.expression.functions.diselect.DIMonochromeFunction;
import com.volantis.mcs.expression.functions.diselect.DIColorFunctionTestCase;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;

/**
 * Defines tests which should be run for {@link DIMonochromeFunction}. Extends
 * {@link com.volantis.mcs.expression.functions.diselect.DIColorFunctionTestCase}.
 */
public class DIMonochromeFunctionTestCase extends DIColorFunctionTestCase {

    /**
     * Test the execute method using the supplied parameters.
     *
     * @param renderMode        whether the render mode is monochrome or not
     * @param defaultValue      the default value that should be used
     * @param expectedResult    expected result of executing the function
     */
    public void doTestExecute(String renderMode, Value defaultValue,
            int expectedResult) {

        // set up expectations
        accessorMock.expects.getDependentPolicyValue(
                DevicePolicyConstants.RENDER_MODE).returns(renderMode);

        DIMonochromeFunction function = new DIMonochromeFunction();
        Value result = function.execute(exprContext, accessorMock,
                defaultValue);
        assertTrue(result instanceof SimpleIntValue);
        assertTrue(expectedResult == ((SimpleIntValue)result).asJavaInt());
    }

    /**
     * Test that the function evaluates to the pixel depth in the repository
     * if the render mode (from the repository) is "grayscale".
     */
    public void testExecuteWithMonochromeRenderMode() {
        // set up expectations
        accessorMock.expects.getDependentPolicyValue(
                DevicePolicyConstants.PIXEL_DEPTH).returns("16");
        doTestExecute(DIMonochromeFunction.GRAYSCALE, VALID_DEFAULT, 16);
    }

    /**
     * Test that the function evaluates to the supplied default value if the
     * render mode (from the repository) is not "grayscale".
     */
    public void testExecuteWithColourRenderModeAndValidDefault() {

        doTestExecute("not monochrome", VALID_DEFAULT,
                VALID_DEFAULT.asJavaInt());
    }

    /**
     * Test that the function evaluates to zero if the render mode (from the
     * repository) is not "grayscale" and the supplied default is invalid.
     */
    public void testExecuteWithColourRenderModeAndInValidDefault() {

        doTestExecute("not monochrome", INVALID_DEFAULT,
                UNKNOWN_VALUE.asJavaInt());
    }

    /**
     * Test that the function evaluates to zero if the render mode (from the
     * repository) is null and the supplied default is invalid.
     */
    public void testExecuteWithColourRenderModeAndNullDefault() {

        doTestExecute("not monochrome", null, UNKNOWN_VALUE.asJavaInt());
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
