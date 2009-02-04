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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import org.jdom.input.JDOMFactory;

import java.util.List;
import java.util.Arrays;


/**
 * Test case for ODOMElementSelectionEvent.
 */
public class ODOMElementSelectionEventTestCase extends TestCaseAbstract {

    /**
     * Test getSelection(). This test is a bit pointless but since we
     * already have this test class then we need at least one test.
     */
    public void testGetSelection() {

        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement el1 = (ODOMElement) factory.element("Element1");
        ODOMElement el2 = (ODOMElement) factory.element("Element2");
        ODOMElement el3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{el1, el2, el3};

        List expected = Arrays.asList(elements);

        ODOMElementSelection selection = new ODOMElementSelection(expected);
        ODOMElementSelectionEvent event =
                new ODOMElementSelectionEvent(selection);

        assertSame("The returned selection should be the same as that " +
                "provided at construction time.", selection,
                event.getSelection());
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 15-Jan-04	2618/1	allan	VBM:2004011510 Provide an IStructuredSelection for selected ODOMElements.

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 20-Nov-03	1939/7	steve	VBM:2003111802 Enhance testsuite, code format and add new constructor to Event class

 19-Nov-03    1939/5    steve    VBM:2003111802 Next attempt to commit

 19-Nov-03    1939/3    steve    VBM:2003111802 ODOM Selection events

 ===========================================================================
*/
