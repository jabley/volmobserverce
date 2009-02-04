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

package com.volantis.styling.unit.engine.sheet;

import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerIterateeMock;
import com.volantis.styling.impl.sheet.StylerMock;
import com.volantis.styling.impl.engine.sheet.CompiledStyleSheetImpl;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.util.ArrayList;
import java.util.List;

public class CompiledStyleSheetTestCase
        extends TestCaseAbstract {

    public void test() {
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jul-05	9029/9	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 18-Jul-05	9029/6	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 18-Jul-05	9029/4	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
