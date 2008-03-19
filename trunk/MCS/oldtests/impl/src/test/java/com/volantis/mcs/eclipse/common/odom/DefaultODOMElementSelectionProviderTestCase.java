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

import junit.framework.TestCase;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ConcurrentModificationException;

import org.jdom.Element;
import com.volantis.mcs.xml.xpath.XPath;

/**
 * Test the DefaultODOMElementSelectionProvider object.
 * @author sfound
 */
public class DefaultODOMElementSelectionProviderTestCase extends TestCase {
    private int eventCount = 0;

    /**
     * ODOMFactory for creating ODOMObservables
     */
    private ODOMFactory factory = new ODOMFactory();

    /**
     * Constructor for ODOMElementSelectionImplTestCase.
     * @param arg0
     */
    public DefaultODOMElementSelectionProviderTestCase(String arg0) {
        super(arg0);
    }

    /**
     * Test a selector with no filters or XPath resolver.
     * In this instance, all elements should simply be passed on to
     * any listeners if they have changed.
     */
    public void testNoFilters() {

        ODOMSelectionFilter f = new ODOMSelectionFilter(null, null);
        DefaultODOMElementSelectionProvider selector =
                new DefaultODOMElementSelectionProvider(f);

        selector.addSelectionListener(new FiremenODOMElementSelectionListener());

        List elements = new ArrayList();
        elements.add(factory.element("cuthbert"));
        elements.add(factory.element("dibble"));
        elements.add(factory.element("grubb"));

        eventCount = 0;
        selector.update(elements);
        assertEquals("Too many events", 1, eventCount);

        selector.addSelectionListener(new FiremenODOMElementSelectionListener());
        assertEquals("Too many events", 2, eventCount);

        // No change so no event expected
        selector.update(elements);
        assertEquals("Too many events", 2, eventCount);

        // Add two elements
        elements.add(factory.element("hugh"));
        elements.add(factory.element("pugh"));

        // 2 events expected as we have two listeners
        selector.update(elements);
        assertEquals("Too many events", 4, eventCount);
    }

    /**
     * Test the udpate method when an empty list is passed through to the update
     * method (which should clear the event list).
     */
    public void testUpdateClearingEvents() {

        String[] filterNames = {
            "fred",
            "barney",
            "wilma",
            "betty",
            "dino"
        };

        ODOMSelectionFilter filter = new ODOMSelectionFilter(null, filterNames);

        DefaultODOMElementSelectionProvider provider =
                new DefaultODOMElementSelectionProvider(filter);

        List elements = new ArrayList();
        elements.add(factory.element("fred")); // filtered out
        elements.add(factory.element("wilma")); // filtered out
        elements.add(factory.element("cuthbert"));
        elements.add(factory.element("dibble"));
        elements.add(factory.element("barney")); // filtered out
        elements.add(factory.element("grubb"));

        eventCount = 0;
        ElementsODOMElementSelectionListener listener =
                new ElementsODOMElementSelectionListener();
        provider.addSelectionListener(listener);
        provider.update(elements);

        assertEquals("Too many events", 1, eventCount);

        // Test the update now with an empty list. The expected names should
        // be cleared so that the number of events will be zero in the
        // checkElements method.
        eventCount = 0;
        listener.getExpectedNames().clear();
        provider.update(new ArrayList());
        assertEquals("Too many events", 1, eventCount);
    }

    public void testUpdateWithResolver() throws Exception {
        ODOMSelectionFilter filter = new ODOMSelectionFilter(
                new XPath("ancestor-or-self::family"),
                null);
        ODOMElementSelectionProvider provider =
                new DefaultODOMElementSelectionProvider(filter);

        Element root = factory.element("families");
        Element partridge = factory.element("family");
        Element brady = factory.element("family");

        Element shirley = factory.element("shirley");
        Element keith = factory.element("keith");
        Element laurie = factory.element("laurie");
        Element danny = factory.element("danny");
        Element christopher = factory.element("christopher");
        Element tracy = factory.element("tracy");

        Element carol = factory.element("carol");
        Element marcia = factory.element("marcia");
        Element jan = factory.element("jan");
        Element cindy = factory.element("cindy");
        Element thomas = factory.element("thomas");
        Element greg = factory.element("greg");
        Element peter = factory.element("peter");
        Element bobby = factory.element("bobby");

        root.addContent(partridge);
        root.addContent(brady);

        partridge.setAttribute("name", "partridge");
        partridge.addContent(shirley);
        partridge.addContent(keith);
        partridge.addContent(laurie);
        partridge.addContent(danny);
        partridge.addContent(christopher);
        partridge.addContent(tracy);

        brady.setAttribute("name", "brady");
        brady.addContent(carol);
        brady.addContent(marcia);
        brady.addContent(jan);
        brady.addContent(cindy);
        brady.addContent(thomas);
        brady.addContent(greg);
        brady.addContent(peter);
        brady.addContent(bobby);

        // This checker will automatically throw assertion failures if the
        // expected selections list doesn't match the event's content
        SelectionChecker checker = new SelectionChecker();

        List actualSelections = new ArrayList();
        List expectedSelections = new ArrayList();

        eventCount = 0;

        // Only add brady bunch actual selections
        actualSelections.add(carol);
        actualSelections.add(greg);
        actualSelections.add(bobby);

        provider.update(actualSelections);

        // Thus, we only expect to find the brady family element
        expectedSelections.add(brady);

        checker.setExpected(expectedSelections);

        assertEquals("Event count not as",
                0,
                eventCount);

        // This should automatically trigger the initial invocation of the
        // listener
        provider.addSelectionListener(checker);

        assertEquals("Event count not as",
                1,
                eventCount);

        // Update with just brady family members again
        actualSelections.add(marcia);
        actualSelections.add(cindy);
        actualSelections.remove(greg);

        // This should do nothing
        provider.update(actualSelections);

        assertEquals("Event count after no-op update not as",
                1,
                eventCount);

        // Add a member of the partridge family...
        actualSelections.add(danny);
        expectedSelections.add(partridge);

        provider.update(actualSelections);

        assertEquals("Event count not as",
                2,
                eventCount);

        // Empty all selections...
        actualSelections.clear();
        expectedSelections.clear();

        provider.update(actualSelections);

        assertEquals("Event count not as",
                3,
                eventCount);

        // Add the partridge family itself and the root node, which should
        // be ignored
        actualSelections.add(partridge);
        actualSelections.add(root);

        expectedSelections.add(partridge);

        provider.update(actualSelections);

        assertEquals("Event count not as",
                4,
                eventCount);

        // Add partridge family members for a no-op change
        actualSelections.add(shirley);
        actualSelections.add(keith);
        actualSelections.add(laurie);
        actualSelections.add(christopher);
        actualSelections.add(tracy);

        provider.update(actualSelections);

        assertEquals("Event count after no-op update not as",
                4,
                eventCount);

        // Add in the last few bradys
        actualSelections.add(jan);
        actualSelections.add(thomas);
        actualSelections.add(peter);

        expectedSelections.add(brady);

        provider.update(actualSelections);

        assertEquals("Event count not as",
                5,
                eventCount);
    }

    /**
     * Test adding a listener after update() is called to ensure that an
     * event fires for that listener.
     */
    public void testNoInitialListeners() {
        String[] filters = new String[5];
        filters[0] = "fred";
        filters[1] = "barney";
        filters[2] = "wilma";
        filters[3] = "betty";
        filters[4] = "dino";

        ODOMSelectionFilter f = new ODOMSelectionFilter(null, filters);

        DefaultODOMElementSelectionProvider selector =
                new DefaultODOMElementSelectionProvider(f);

        List elements = new ArrayList();
        elements.add(factory.element("fred"));
        elements.add(factory.element("wilma"));
        elements.add(factory.element("cuthbert"));
        elements.add(factory.element("dibble"));
        elements.add(factory.element("barney"));
        elements.add(factory.element("grubb"));

        eventCount = 0;
        selector.update(elements);

        selector.addSelectionListener(new ElementsODOMElementSelectionListener());
        assertEquals("Too many events", 1, eventCount);
    }

    /**
     * Test adding a listener before update() is called to ensure that an
     * event fires for that listener.
     */
    public void testWithInitialListener() {
        String[] filters = new String[5];
        filters[0] = "fred";
        filters[1] = "barney";
        filters[2] = "wilma";
        filters[3] = "betty";
        filters[4] = "dino";

        ODOMSelectionFilter f = new ODOMSelectionFilter(null, filters);

        DefaultODOMElementSelectionProvider selector =
                new DefaultODOMElementSelectionProvider(f);

        selector.addSelectionListener(new ElementsODOMElementSelectionListener());

        List elements = new ArrayList();
        elements.add(factory.element("fred"));
        elements.add(factory.element("wilma"));
        elements.add(factory.element("cuthbert"));
        elements.add(factory.element("dibble"));
        elements.add(factory.element("barney"));
        elements.add(factory.element("grubb"));

        eventCount = 0;
        selector.update(elements);
        assertEquals("Too many events", 1, eventCount);
    }

    /**
     * Test to ensure that if a listener adds another listener to the
     * DefaultODOMElementSelectionProvider when it is notified that a
     * ConcurrentModificationException does not occur.
     */
    public void testConcurrentModificationException() {
        String[] filters = new String[5];
        filters[0] = "fred";
        filters[1] = "barney";
        filters[2] = "wilma";
        filters[3] = "betty";
        filters[4] = "dino";

        ODOMSelectionFilter f = new ODOMSelectionFilter(null, filters);

        final DefaultODOMElementSelectionProvider selector =
                new DefaultODOMElementSelectionProvider(f);

        // this listener adds a new listener to the
        // DefaultODOMElementSelectionProvider when its selectionChanged method
        // is called.
        selector.addSelectionListener(new ODOMElementSelectionListener() {
            /**
             * add a new selectionListener when notified
             */
            public void selectionChanged(ODOMElementSelectionEvent event) {
                selector.addSelectionListener(new ODOMElementSelectionListener(){
                    public void selectionChanged(ODOMElementSelectionEvent event) {
                        // do nothing in here
                    }
                });
            }
        });

        List elements = new ArrayList();
        elements.add(factory.element("fred"));
        elements.add(factory.element("grubb"));
        try {
            selector.update(elements);
            // an exception should not be thrown
        } catch (ConcurrentModificationException cme) {
            fail("ConcurrentModificationException should not be thrown here");
        }
    }

    /**
     * Main test. Adds and removes listeners and elements and ensures that the
     * right number of events have fired for the different scenarios.
     */
    public void testFiddleWithListeners() {
        String[] filters = new String[5];
        filters[0] = "fred";
        filters[1] = "barney";
        filters[2] = "wilma";
        filters[3] = "betty";
        filters[4] = "dino";

        ODOMSelectionFilter f = new ODOMSelectionFilter(null, filters);

        DefaultODOMElementSelectionProvider selector =
                new DefaultODOMElementSelectionProvider(f);

        // This listener should be called with first update
        ODOMElementSelectionListener listener1 =
                new ElementsODOMElementSelectionListener();

        selector.addSelectionListener(listener1);

        List elements = new ArrayList();
        elements.add(factory.element("fred"));
        elements.add(factory.element("wilma"));
        elements.add(factory.element("cuthbert"));
        elements.add(factory.element("dibble"));
        elements.add(factory.element("barney"));
        elements.add(factory.element("grubb"));

        eventCount = 0;
        selector.update(elements);
        assertEquals("Too many events", 1, eventCount);

        // This listener will be called when it is added, but the first
        // listener will not be recalled.
        // This listener should be called with first update
        ODOMElementSelectionListener listener2 =
                new ElementsODOMElementSelectionListener();

        selector.addSelectionListener(listener2);
        assertEquals("Too many events", 2, eventCount);

        // Another update should not trigger any events
        selector.update(elements);
        assertEquals("Too many events", 2, eventCount);

        // Adding a filtered element should not trigger any events
        elements.add(factory.element("dino"));
        selector.update(elements);
        assertEquals("Too many events", 2, eventCount);

        // Remove the two current listeners
        selector.removeSelectionListener(listener1);
        selector.removeSelectionListener(listener2);

        // Add a new unfiltered element
        elements.add(factory.element("hugh"));

        // Update should do nothing as we have no listeners
        selector.update(elements);
        assertEquals("Too many events", 2, eventCount);

        // Add another listener with the new element
        selector.addSelectionListener(new FiremenODOMElementSelectionListener());
        assertEquals("Too many events", 3, eventCount);

        // Add a new unfiltered element
        elements.add(factory.element("pugh"));

        // This update should trigger an event
        selector.update(elements);
        assertEquals("Too many events", 4, eventCount);
    }

    /**
     * Check the elements that are passed in an event
     *
     * @param evt           the event
     * @param expectedNames a List of strings holding the names of all the
     *                      expected elements.
     */
    private void checkElements(ODOMElementSelectionEvent evt,
                               List expectedNames) {
        eventCount++;

        assertEquals("Not enough elements", expectedNames.size(),
                evt.getSelection().size());

        Iterator iter = evt.getSelection().iterator();
        while (iter.hasNext()) {
            ODOMElement odomElement = (ODOMElement) iter.next();
            String name = odomElement.getName();
            assertTrue("Unexpected element name " + name,
                    expectedNames.contains(name));
        }
    }

    /**
     * Check the names of elements that are camberwick green firemen
     * @param evt the event
     */
    private void checkFiremen(ODOMElementSelectionEvent evt) {
        eventCount++;

        String validNames = "hugh pugh cuthbert dibble grubb";
        Iterator iter = evt.getSelection().iterator();
        while (iter.hasNext()) {
            ODOMElement el = (ODOMElement) iter.next();
            String name = el.getName();
            assertEquals("Unexpected element name " + name, true,
                    (validNames.indexOf(name) > -1));
        }
    }

    /**
     * Helper Listener class
     */
    private class FiremenODOMElementSelectionListener
            implements ODOMElementSelectionListener {
        public void selectionChanged(ODOMElementSelectionEvent evt) {
            checkFiremen(evt);
        };
    }

    /**
     * Helper Elements Listener class
     */
    private class ElementsODOMElementSelectionListener
            implements ODOMElementSelectionListener {

        final List expectedNames = new ArrayList(3);

        ElementsODOMElementSelectionListener() {
            expectedNames.add("cuthbert");
            expectedNames.add("dibble");
            expectedNames.add("grubb");
        }

        public List getExpectedNames() {
            return expectedNames;
        }

        public void selectionChanged(ODOMElementSelectionEvent evt) {
            checkElements(evt, expectedNames);
        };
    }

    /**
     * Helps verify that the number of selections in an event is as expected
     * and that the expected elements are in the event.
     */
    private class SelectionChecker implements ODOMElementSelectionListener {
        /**
         * Initial empty list to handle initial condition.
         */
        List expected = new ArrayList();

        /**
         * Retains a reference to the given list to avoid the need to call
         * this method repeatedly each time the expected results change.
         * @param elements
         */
        public void setExpected(List elements) {
            expected = elements;
        }

        /**
         * Checks the given event's elements against the expected elements
         * list and, if all OK, increments the event count.
         */
        public void selectionChanged(ODOMElementSelectionEvent event) {
            Assert.assertEquals("Number of selections not as",
                    expected.size(),
                    event.getSelection().size());

            List selection = event.getSelection().toList();
            for (int i = 0; i < selection.size(); i++) {
                Assert.assertTrue("Element " +
                        ((Element) selection.get(i)).getName() +
                        " not in the expected set",
                        expected.contains(selection.get(i)));
            }

            eventCount++;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 11-May-04	4262/1	matthew	VBM:2004051009 DefaultODOMElementSelectionProvider modified to avoid ConcurrentModificationException

 15-Jan-04	2618/1	allan	VBM:2004011510 Provide an IStructuredSelection for selected ODOMElements.

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 29-Nov-03	2064/1	philws	VBM:2003112901 Correct the result of resolved selections

 25-Nov-03	2005/1	byron	VBM:2003112006 Eclipse to ODOM events - fixed javadoc and updated testcases

 23-Nov-03	1974/1	steve	VBM:2003112006 ODOM Selection changes

 20-Nov-03	1960/4	steve	VBM:2003111902 Classes Renamed

 20-Nov-03	1960/1	steve	VBM:2003111902 Pre-XPath save

 ===========================================================================
*/
