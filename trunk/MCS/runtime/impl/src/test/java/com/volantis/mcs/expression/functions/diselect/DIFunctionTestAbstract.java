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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.expression.functions.diselect;

import com.volantis.mcs.expression.DevicePolicyValueAccessorMock;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContextMock;
import com.volantis.xml.expression.ExpressionFactory;

public abstract class DIFunctionTestAbstract
        extends TestCaseAbstract {

    protected static final ExpressionFactory exprFactory =
            ExpressionFactory.getDefaultInstance();

    /**
     * Mock objects.
     */
    protected ExpressionContextMock exprContext;
    protected DevicePolicyValueAccessorMock accessorMock;

    protected void setUp() throws Exception {
        super.setUp();

        // set up mock objects.
        exprContext = new ExpressionContextMock("exprMock", expectations);
        accessorMock = new DevicePolicyValueAccessorMock("accessorMock",
                expectations);

        exprContext.expects.getFactory().returns(exprFactory).any();

        exprContext.expects.getProperty(DevicePolicyValueAccessor.class)
                .returns(accessorMock).any();
    }
}
