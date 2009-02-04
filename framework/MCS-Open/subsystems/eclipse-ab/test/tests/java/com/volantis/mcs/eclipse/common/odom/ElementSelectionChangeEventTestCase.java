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
import com.volantis.mcs.eclipse.ab.editors.ElementSelectionChangeEvent;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.Element;

/**
 * Test the ElementSelectionChangeEvent class
 */
public class ElementSelectionChangeEventTestCase extends TestCaseAbstract {

    /**
     * ODOMfactory for factoring ODOMObservables
     */
    JDOMFactory factory = new ODOMFactory();
    
    /**
     * Test the event with a valid JDOM Element.
     */
    public void testEventWithJDOMElement() throws Exception {
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        Element source = factory.element("testElement");
        ElementSelectionChangeEvent event =
                new ElementSelectionChangeEvent(source);

        String mustBeNull = "Value should be null";
        assertNull(mustBeNull, event.getNewValue());
        assertNull(mustBeNull, event.getOldValue());

        String match = "Values should match";
        assertEquals(match, ChangeQualifier.NONE, event.getChangeQualifier());
        assertEquals(match, source, event.getSource());
    }

    /**
     * Test the event with a valid ODOMChangeEvent object.
     */
    public void testEventWithODOMChangeEvent() throws Exception {
        ODOMElement source = (ODOMElement)factory.element("test");
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        Element oldObject = factory.element("oldObject");
        Element newObject = factory.element("newObject");
        ODOMChangeEvent event = ODOMChangeEvent.createNew(source, oldObject, newObject,
                ChangeQualifier.NAMESPACE);

        ElementSelectionChangeEvent selectionChangeEvent =
                new ElementSelectionChangeEvent(event);

        final String match = "Values should match";
        assertEquals(match, newObject, selectionChangeEvent.getNewValue());
        assertEquals(match, oldObject, selectionChangeEvent.getOldValue());
        assertEquals(match, ChangeQualifier.NAMESPACE, event.getChangeQualifier());
        assertEquals(match, source, event.getSource());
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

 29-Jan-04	2689/1	eduardo	VBM:2003112407 undo/redo manager for ODOM

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 06-Nov-03	1808/1	byron	VBM:2003110406 ElementSelectionChange event handling

 ===========================================================================
*/
