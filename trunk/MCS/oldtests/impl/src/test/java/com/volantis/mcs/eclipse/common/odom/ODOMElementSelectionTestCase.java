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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.mcs.eclipse.ab.editors.dom.LPDMODOMFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

/**
 * Test case for ODOMElementSelection.
 */
public class ODOMElementSelectionTestCase extends TestCaseAbstract {
    /**
     * Ensure that the constructor only allows ODOMElements in the
     * selection.
     */
    public void testConstructorCreation() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement e1 = (ODOMElement) factory.element("Element1");
        ODOMElement e2 = (ODOMElement) factory.element("Element2");
        ODOMElement e3 = (ODOMElement) factory.element("Element3");

        ArrayList elements = new ArrayList();
        elements.add(e1);
        elements.add(e2);
        elements.add(e3);

        // Test the positive case first.
        new ODOMElementSelection(elements);

        // Now add a non-ODOMElement to the List.
        Element jDOMElement = new Element("jdom");

        elements.add(jDOMElement);

        try {
            new ODOMElementSelection(elements);
            fail("Expected an IllegalArgumentException because of the " +
                    "JDOM element in list of elements.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * Test toList().
     */
    public void testToList() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement el1 = (ODOMElement) factory.element("Element1");
        ODOMElement el2 = (ODOMElement) factory.element("Element2");
        ODOMElement el3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{el1, el2, el3};

        List expected = Arrays.asList(elements);

        ODOMElementSelection selection = new ODOMElementSelection(expected);

        List actual = selection.toList();

        assertEquals("Expected and actual sizes not equal.",
                expected.size(),
                actual.size());

        assertTrue("A different List instance should be returned.",
                expected != actual);

        for (int i = 0; i < expected.size(); i++) {
            assertSame("Expected ODOM element [" + i + "] not same as actual.",
                    expected.get(i),
                    actual.get(i));
        }
    }

    /**
     * Test toArray()
     */
    public void testToArray() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement el1 = (ODOMElement) factory.element("Element1");
        ODOMElement el2 = (ODOMElement) factory.element("Element2");
        ODOMElement el3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{el1, el2, el3};

        ODOMElementSelection selection = new ODOMElementSelection(
                Arrays.asList(elements));

        Object actuals [] = selection.toArray();

        assertEquals("Expected and actual sizes not equal.",
                elements.length,
                actuals.length);

        assertTrue("A different List instance should be returned.",
                elements != actuals);

        for (int i = 0; i < elements.length; i++) {
            assertSame("Expected ODOM element [" + i + "] not same as actual.",
                    elements[i],
                    actuals[i]);
        }
    }

    /**
     * Test toODOMElementArray()
     */
    public void testToODOMElementArray() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement e1 = (ODOMElement) factory.element("Element1");
        ODOMElement e2 = (ODOMElement) factory.element("Element2");
        ODOMElement e3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{e1, e2, e3};

        ODOMElementSelection selection = new ODOMElementSelection(
                Arrays.asList(elements));

        ODOMElement actuals [] = selection.toODOMElementArray();

        assertEquals("Expected and actual sizes not equal.",
                elements.length,
                actuals.length);

        assertTrue("A different List instance should be returned.",
                elements != actuals);

        for (int i = 0; i < elements.length; i++) {
            assertSame("Expected ODOM element [" + i + "] not same as actual.",
                    elements[i],
                    actuals[i]);
        }
    }

    /**
     * Test toODOMElementArray()
     */
    public void testGetFirstElement() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement e1 = (ODOMElement) factory.element("Element1");
        ODOMElement e2 = (ODOMElement) factory.element("Element2");
        ODOMElement e3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{e1, e2, e3};

        ODOMElementSelection selection = new ODOMElementSelection(
                Arrays.asList(elements));

        assertSame("First element should be Element1",
                e1, selection.getFirstElement());
    }

    /**
     * Test iterator().
     */
    public void testIterator() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement e1 = (ODOMElement) factory.element("Element1");
        ODOMElement e2 = (ODOMElement) factory.element("Element2");
        ODOMElement e3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{e1, e2, e3};

        ODOMElementSelection selection = new ODOMElementSelection(
                Arrays.asList(elements));

        Iterator iterator = selection.iterator();

        ODOMElement actuals [] = new ODOMElement[3];
        int i=0;
        while(iterator.hasNext()) {
            actuals[i] = (ODOMElement) iterator.next();
            i++;
        }

        assertEquals("There should be three elements iterated.",
                3, actuals.length);

        for (i = 0; i < elements.length; i++) {
            assertSame("Expected ODOM element [" + i + "] not same as actual.",
                    elements[i],
                    actuals[i]);
        }

        // Test that elements cannot be removed from the Iterator
        iterator = selection.iterator();

        try {
            iterator.remove();
            fail("Expected an UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // Success.
        }
    }

    /**
     * Test size().
     */
    public void testSize() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement e1 = (ODOMElement) factory.element("Element1");
        ODOMElement e2 = (ODOMElement) factory.element("Element2");
        ODOMElement e3 = (ODOMElement) factory.element("Element3");

        ODOMElement[] elements = new ODOMElement[]{e1, e2, e3};

        ODOMElementSelection selection = new ODOMElementSelection(
                Arrays.asList(elements));

        assertEquals("Size should be 3.", 3, selection.size());

        selection = new ODOMElementSelection(null);

        assertEquals("Size should be 0.", 0, selection.size());
    }

    /**
     * Test isEmpty()
     */
    public void testIsEmpty() {
        JDOMFactory factory = new LPDMODOMFactory();
        ODOMElement e1 = (ODOMElement) factory.element("Element1");

        ODOMElement[] elements = new ODOMElement[]{e1};

        ODOMElementSelection selection = new ODOMElementSelection(
                Arrays.asList(elements));

        assertFalse("There should be one element.", selection.isEmpty());

        selection = new ODOMElementSelection(null);

        assertTrue("There are no elements in the selection.",
                selection.isEmpty());
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

 ===========================================================================
*/
