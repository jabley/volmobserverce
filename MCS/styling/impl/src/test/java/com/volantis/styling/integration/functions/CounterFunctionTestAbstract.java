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
package com.volantis.styling.integration.functions;

import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.expressions.EvaluationContextMock;
import com.volantis.styling.impl.functions.counter.CounterFormatterMock;
import com.volantis.styling.impl.functions.counter.CounterFormatterSelectorMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A common superclass for CSS counter function tests.
 */
public abstract class CounterFunctionTestAbstract
        extends TestCaseAbstract {

    protected EvaluationContextMock evaluationContextMock;
    protected CounterFormatterSelectorMock formatterSelectorMock;
    protected CounterFormatterMock formatterMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        evaluationContextMock = new EvaluationContextMock(
                "evaluationContextMock", expectations);

        formatterSelectorMock = new CounterFormatterSelectorMock(
                "formatterSelectorMock", expectations);

        formatterMock =
                new CounterFormatterMock("formatterMock", expectations);

    }

    protected StyleIdentifier getCounterIdentifier(String counterName) {
        return StyleValueFactory.getDefaultInstance().getIdentifier(null, counterName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 ===========================================================================
*/
