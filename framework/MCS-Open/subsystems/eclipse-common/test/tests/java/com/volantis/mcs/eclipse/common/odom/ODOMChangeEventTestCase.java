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

import org.jdom.Document;
import org.jdom.Element;


/**
 * Test case for {@link ODOMChangeEvent}. Only non-trivial methods are tested.
 */
public class ODOMChangeEventTestCase extends TestCaseAbstract {
    // javadoc unnecessary
    public ODOMChangeEventTestCase(String name) {
        super(name);
    }


    /**
     * Test constructors.
     */
    public void testODOMChangeEvent() throws Exception {
        ODOMChangeEvent event;
        ODOMObservable source = new ODOMObservableStub();
        Object oldValue = new Object();
        Object newValue = new Object();

        event = ODOMChangeEvent.createNew(source,
                oldValue,
                newValue,
                ChangeQualifier.NONE);

        assertSame("source not as",
                source,
                event.getSource());

        assertSame("oldValue not as",
                oldValue,
                event.getOldValue());

        assertSame("newValue not as",
                newValue,
                event.getNewValue());

        assertSame("Expected qualifier to be NONE",
                ChangeQualifier.NONE,
                event.getChangeQualifier());

        //check for assertion
        try {
            ODOMChangeEvent.createNew(null,oldValue,newValue,ChangeQualifier.NONE);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) { /* OK */ }
    }


    public void testODOMHierarchyChangeEvent() throws Exception {

        Element rootElem = new ODOMElement("root");
        Document document = new Document(rootElem);
        ODOMElement child = new ODOMElement("child");
        rootElem.addContent(child);

        ODOMChangeEvent event = ODOMChangeEvent.createNew(child,
                null,
                rootElem,
                ChangeQualifier.HIERARCHY);

        assertSame("source not as",
                child,
                event.getSource());

        assertNull("oldValue not null",
                event.getOldValue());

        assertSame("newValue not as",
                rootElem,
                event.getNewValue());

        assertSame("Expected qualifier to be HIERARCHY",
                ChangeQualifier.HIERARCHY,
                event.getChangeQualifier());

        //check for assertions
        try {
            ODOMChangeEvent.createNew(child,
                    null,
                    null,
                    ChangeQualifier.HIERARCHY);
            fail("IllegalArgumentException expected - two null values");
        } catch (IllegalArgumentException expected) { /*OK */
        }

        try {
            ODOMChangeEvent.createNew(child,
                    rootElem,
                    rootElem,
                    ChangeQualifier.HIERARCHY);
            fail("IllegalArgumentException expected - two non null values");
        } catch (IllegalArgumentException expected) { /*OK */
        }
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

 29-Jan-04	2689/2	eduardo	VBM:2003112407 undo/redo manager for ODOM

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
