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

import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;
import com.volantis.mcs.expression.functions.diselect.DIAspectRatioFunction;
import com.volantis.mcs.expression.functions.diselect.DIAspectRatioHeightFunction;
import com.volantis.mcs.expression.functions.diselect.DIAspectRatioFunctionTestCase;

/**
 * Defines tests which should be run for {@link DIAspectRatioHeightFunction}
 */
public class DIAspectRatioHeightFunctionTestCase
        extends DIAspectRatioFunctionTestCase {

   /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is invalid, the function evaluates to zero.
     */
    public void testExecuteWithInvalidDefaultValue() {
       doTestExecute(null, null, INVALID_DEFAULT, UNKNOWN_INT_VALUE,
               DIAspectRatioFunction.AspectRatioOutputFormat.HEIGHT);
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to zero.
     */
    public void testExecuteWithNoDefaultValue() {
       doTestExecute(null, null, null, UNKNOWN_INT_VALUE,
               DIAspectRatioFunction.AspectRatioOutputFormat.HEIGHT);
    }

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is valid, the function evaluates to the default value.
     */
    public void testExecuteWithValidDefaultValue() {
        doTestExecute(null, null, VALID_INT_DEFAULT, VALID_INT_DEFAULT,
               DIAspectRatioFunction.AspectRatioOutputFormat.HEIGHT);
    }

    /**
     * Verify that when the value retrieved from the repository is non null and
     * valid, the function evaluates to it.
     */
    public void testExecuteValueWithValidRepositoryValue() {
        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH,
                VALID_INT_DEFAULT, new SimpleIntValue(exprFactory, VALID_HEIGHT),
                DIAspectRatioFunction.AspectRatioOutputFormat.HEIGHT);
    }

    /**
     * Verify that when the value retrieved from the repository is invalid (i.e.
     * doesn't map to a numeric), the function evaluates to the default value.
     */
    public void testExecuteValueWithInvalidRepositoryValue() {
        doTestExecute(INVALID_VALUE, VALID_PIXEL_WIDTH, VALID_INT_DEFAULT,
                VALID_INT_DEFAULT,
                DIAspectRatioFunction.AspectRatioOutputFormat.HEIGHT);
    }

    /**
     * Return the {@link DIAspectRatioFunction} that should be used in tests.
     *
     * @return DIAspectRatioFunction to be used in tests.
     */
    public DIAspectRatioFunction getFunction() {
        return new DIAspectRatioHeightFunction();
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
