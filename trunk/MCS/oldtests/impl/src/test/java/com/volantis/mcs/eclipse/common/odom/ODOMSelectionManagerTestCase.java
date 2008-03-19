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
import com.volantis.mcs.utilities.BooleanObject;
import junitx.util.PrivateAccessor;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

/**
 * Test the ODOMSelectionManager object.
 */
public class ODOMSelectionManagerTestCase
        extends TestCaseAbstract {

    /**
     * ODOMfactory for factoring ODOMObservables
     */
    JDOMFactory factory = new ODOMFactory();

    protected ODOMSelectionManager manager;

    protected ISelectionProvider sp;

    protected void setUp() throws Exception {
        super.setUp();
        manager = new ODOMSelectionManager(null);
        sp = new ISelectionProvider() {
            public void addSelectionChangedListener(
                    ISelectionChangedListener iSelectionChangedListener) {
            }

            public ISelection getSelection() {
                return null;
            }

            public void removeSelectionChangedListener(
                    ISelectionChangedListener iSelectionChangedListener) {
            }

            public void setSelection(ISelection iSelection) {
            }

        };
    }

    /**
     * Helper method.
     */
    private Map getFilterToProviderMap() throws Exception {
        return (Map) PrivateAccessor.getField(manager, "filterToProviderMap");
    }

    /**
     * Test that the adding of selection listener works as expected.
     */
    public void testAddSelectionListener() throws Exception {
        manager.addSelectionListener(null, null);
        Map map = getFilterToProviderMap();
        assertNotNull(map);
        assertEquals("Size should match", 1, map.size());

        // Add a null listener.
        ODOMSelectionFilter filter = new ODOMSelectionFilter(null, null);
        manager.addSelectionListener(null, filter);
        map = getFilterToProviderMap();
        assertNotNull(map);
        assertEquals("Size should match", 2, map.size());
        Object provider = map.get(filter);
        assertNotNull("Value shouldn't be null", provider);
        assertTrue("Type should match",
                provider instanceof DefaultODOMElementSelectionProvider);

        // Adding the same listener again shouldn't change the size of the map.
        manager.addSelectionListener(null, filter);
        map = getFilterToProviderMap();
        assertNotNull(map);
        assertEquals("Size should match", 2, map.size());
        provider = map.get(filter);
        assertNotNull("Value shouldn't be null", provider);
        assertTrue("Type should match",
                provider instanceof DefaultODOMElementSelectionProvider);
    }

    /**
     * Test that the removing of selection listener works as expected.
     */
    public void testRemoveSelectionListener() throws Exception {
        manager.removeSelectionListener(null, null);

        ODOMSelectionFilter filter = new ODOMSelectionFilter(null, null);
        ODOMElementSelectionListener listener =
                new ODOMElementSelectionListener() {
                    public void selectionChanged(ODOMElementSelectionEvent event) {
                    }
                };
        try {
            manager.removeSelectionListener(null, filter);
            fail("Expected an IllegalStateException");
        } catch (Exception e) {
        }
        manager.addSelectionListener(listener, filter);
        Map map = getFilterToProviderMap();
        assertNotNull(map);
        assertEquals("Size should match", 2, map.size());
        Object provider = map.get(filter);
        assertNotNull("Value shouldn't be null", provider);
        assertTrue("Type should match",
                provider instanceof DefaultODOMElementSelectionProvider);
        List listeners = (List) PrivateAccessor.getField(provider, "listeners");
        assertEquals("Listeners should contain ", 1, listeners.size());
        assertEquals("Listener value should match ", listener, listeners.get(0));

        manager.removeSelectionListener(listener, filter);
        map = getFilterToProviderMap();
        assertNotNull(map);
        assertEquals("Size should match", 2, map.size());
        provider = map.get(filter);
        assertNotNull("Value shouldn't be null", provider);
        assertTrue("Type should match",
                provider instanceof DefaultODOMElementSelectionProvider);
        listeners = (List) PrivateAccessor.getField(provider, "listeners");
        // Listener should've been removed.
        assertEquals("Listeners should contain ", 0, listeners.size());
    }

    /**
     * Test the result of a selection change event.
     */
    public void testSelectionChangedExceptionConditions() throws Exception {
        try {
            manager.selectionChanged(null);
            fail("Expected and IllegalArgument exception.");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    /**
     * Test the result of a simple selection change event.
     */
    public void testSelectionChangedSimple() throws Exception {
        doSelectionChangedTest(new StructuredSelection(),
                getMockProviderFromFilter(null), 0);
    }

    /**
     * Test the result of a complex erroneious selection change event.
     */
    public void testSelectionChangedComplexWithIllegalArgs() throws Exception {
        Object objectElements[] = {
            factory.element("odomElement"),
            new Element("spannerInTheWorksElement"),
            factory.text("spannerInTheWorksTextNode")
        };
        StructuredSelection structuredSelection =
                new StructuredSelection(objectElements);

        List elements = structuredSelection.toList();
        assertNotNull("List shouldn't be null", elements);
        assertEquals("List size should match", objectElements.length,
                elements.size());

        // Should throw an IllegalArgumentException (the element list should
        // only contain ODOMElements).
        try {
            SelectionChangedEvent event =
                    new SelectionChangedEvent(sp, structuredSelection);
            manager.selectionChanged(event);
            fail("Expected and IllegalArgumentException");
        } catch (Exception e) {
        }
    }

    /**
     * test the setEnabled method of the ODOMSelectionManager.
     * @throws Exception
     */
    public void testSetEnabled() throws Exception {
        StructuredSelection structuredSelection = new StructuredSelection();
        MockDefaultODOMElementSelectionProvider provider =
                getMockProviderFromFilter(null);

        SelectionChangedEvent event =
                new SelectionChangedEvent(sp, structuredSelection);

        // check the manager is working
        manager.selectionChanged(event);
        assertTrue("Update should have been called.", provider.updateCalled);
        provider.updateCalled = false;

        // disable the manager
        manager.setEnabled(false);
        manager.selectionChanged(event);
        assertFalse("Update should not have been called as the manager is " +
                "disabled", provider.updateCalled);
        provider.updateCalled = false;

        // renable the manager and see if it is still working
        manager.setEnabled(true);
        manager.selectionChanged(event);
        assertTrue("Update should have been called again as the manager has " +
                "been renabled", provider.updateCalled);
    }

    /**
     * Helper method.
     */
    private void doSelectionChangedTest(StructuredSelection structuredSelection,
                                        MockDefaultODOMElementSelectionProvider provider,
                                        int expectedElementSize)
            throws Exception {

        List elements = structuredSelection.toList();
        assertNotNull("List shouldn't be null", elements);
        assertEquals("List size should match", expectedElementSize,
                elements.size());

        assertFalse("Update should not have been called.", provider.updateCalled);
        // Should use the listeners' providers update method(s). Testing this
        // behaviour should be covered by the DefaultODOMSelectionProvderTestCase.
        SelectionChangedEvent event =
                new SelectionChangedEvent(sp, structuredSelection);
        manager.selectionChanged(event);
        assertTrue("Update should have been called.", provider.updateCalled);
    }

    /**
     * Update the defaultProvider by replacing it with a new Mock provider
     * object (so that we can verify that update() has actually been called).
     */
    private MockDefaultODOMElementSelectionProvider getUpdatedMockDefaultProvider()
            throws NoSuchFieldException {
        DefaultODOMElementSelectionProvider provider =
                (DefaultODOMElementSelectionProvider) PrivateAccessor.getField
                (manager, "defaultProvider");

        MockDefaultODOMElementSelectionProvider mockProvider =
                new MockDefaultODOMElementSelectionProvider(
                        provider.getSelectionFilter());
        PrivateAccessor.setField(manager, "defaultProvider", mockProvider);
        return mockProvider;
    }

    /**
     * Update the filterToProvider by replacing the provider in the map associated
     * with the filter with a new Mock provider object (so that we can verify
     * that update() has actually been called).
     */
    private MockDefaultODOMElementSelectionProvider getMockProviderFromFilter(
            ODOMSelectionFilter filter) throws Exception {

        Map map = getFilterToProviderMap();
        DefaultODOMElementSelectionProvider provider =
                (DefaultODOMElementSelectionProvider) map.get(filter);
        MockDefaultODOMElementSelectionProvider mock =
                new MockDefaultODOMElementSelectionProvider(
                        provider.getSelectionFilter());
        map.put(filter, mock);
        return mock;
    }

    /**
     * Test the result of a complex selection change event.
     */
    public void testSelectionChangedComplex() throws Exception {
        Object objectElements[] = {
            factory.element("odomElement1"),
            factory.element("odomElement2")
        };
        StructuredSelection structuredSelection =
                new StructuredSelection(objectElements);

        doSelectionChangedTest(structuredSelection,
                getMockProviderFromFilter(null), objectElements.length);
    }

    /**
     * Test the result of a complex selection change event.
     */
    public void testSelectionChangedComplexWithFilters() throws Exception {
        Object objectElements[] = {
            factory.element("odomElement1"),
            factory.element("odomElement2")
        };
        StructuredSelection structuredSelection =
                new StructuredSelection(objectElements);

        List elements = structuredSelection.toList();
        assertNotNull("List shouldn't be null", elements);
        assertEquals("List size should match", objectElements.length,
                elements.size());

        ODOMSelectionFilter filter = new ODOMSelectionFilter(null, null);
        manager.addSelectionListener(null, filter);

        MockDefaultODOMElementSelectionProvider mock =
                getMockProviderFromFilter(filter);
        doSelectionChangedTest(structuredSelection, mock, objectElements.length);
    }

    /**
     * Gets the list of ISelectionChahngedListeners currently registered with
     * the ODOMSelectionManager under test.
     */
    private ListenerList getSelectionChangedListenersList() throws Exception {
        return (ListenerList) PrivateAccessor.getField(manager,
                "selectionChangedListeners");
    }

    /**
     * Test that the adding of selection changed listeners works as expected.
     */
    public void testAddSelectionChangedListener() throws Exception {
        // Add a null listener.
        manager.addSelectionChangedListener(null);
        ListenerList listeners = getSelectionChangedListenersList();
        assertNotNull(listeners);
        assertTrue("List should be empty", listeners.isEmpty());

        // Add a non-null listener.
        final ISelectionChangedListener listener =
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                    }
                };

        manager.addSelectionChangedListener(listener);
        listeners = getSelectionChangedListenersList();
        assertNotNull(listeners);
        assertEquals("List size should match", 1, listeners.size());

        // Adding the same listener again shouldn't change the size.
        manager.addSelectionChangedListener(listener);
        listeners = getSelectionChangedListenersList();
        assertNotNull(listeners);
        assertEquals("List size should match", 1, listeners.size());
    }

    /**
     * Test that the removing of selection changed listeners works as expected.
     */
    public void testRemoveSelectionChangedListener() throws Exception {
        // Remove a null listener.
        manager.removeSelectionChangedListener(null);

        // Add a non-null listener.
        final ISelectionChangedListener listener =
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                    }
                };

        manager.addSelectionChangedListener(listener);
        ListenerList listeners = getSelectionChangedListenersList();
        assertNotNull(listeners);
        assertEquals("List size should match", 1, listeners.size());


        manager.removeSelectionChangedListener(listener);
        listeners = getSelectionChangedListenersList();
        assertNotNull(listeners);
        assertTrue("List should be empty", listeners.isEmpty());

        // Removing the same listener again shouldn't change the size or
        // cause any exceptions.
        manager.removeSelectionChangedListener(listener);
        listeners = getSelectionChangedListenersList();
        assertNotNull(listeners);
        assertTrue("List should be empty", listeners.isEmpty());
    }

    /**
     * Tests that setting the selection works as expected.
     */
    public void testSetSelection() throws Exception {
        // Set an empty selection.
        manager.setSelection(new ODOMElementSelection(Collections.EMPTY_LIST));
        ISelection returnedSelection = manager.getSelection();
        assertNotNull(returnedSelection);
        assertTrue("Selection should be empty", returnedSelection.isEmpty());

        // Set a selection with two elements.
        final List selection = new ArrayList();

        ODOMElement aElement = (ODOMElement) factory.element("a");
        ODOMElement bElement = (ODOMElement) factory.element("b");

        selection.add(aElement);
        selection.add(bElement);
        manager.setSelection(new ODOMElementSelection(selection));

        List odomSelection =
                ((ODOMElementSelection) manager.getSelection()).toList();
        assertNotNull(odomSelection);
        assertEquals("List size should match", 2, odomSelection.size());
        assertTrue("List should contain element a",
                odomSelection.contains(aElement));
        assertTrue("List should contain element b",
                odomSelection.contains(bElement));
    }

    /**
     * Tests that changing the selection fires the appropriate change events.
     */
    public void testFireSelectionChangedEvent() throws Exception {
        final BooleanObject eventFired = new BooleanObject();
        // Add a non-null listener.
        final ISelectionChangedListener listener =
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        eventFired.setValue(true);
                    }
                };

        manager.addSelectionChangedListener(listener);

        ODOMElement aElement = (ODOMElement) factory.element("a");
        ODOMElement bElement = (ODOMElement) factory.element("b");

        // Set a selection with two elements.
        final List selection = new ArrayList();
        selection.add(aElement);
        selection.add(bElement);
        manager.setSelection(new ODOMElementSelection(selection));

        assertTrue("SelectionChangeEvent should have been fired",
                eventFired.getValue());
    }

    /**
     * Mock DefaultODOMElementSelectionProvider object that is used to determine
     * whether or not the update method has been called or not.
     */
    class MockDefaultODOMElementSelectionProvider
            extends DefaultODOMElementSelectionProvider {

        boolean updateCalled = false;

        public MockDefaultODOMElementSelectionProvider(ODOMSelectionFilter filter) {
            super(filter);
        }

        public void update(List elements) {
            super.update(elements);
            updateCalled = true;
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

 02-Aug-04	5030/3	pcameron	VBM:2004070705 Using ODOMElementSelection

 02-Aug-04	5030/1	pcameron	VBM:2004070705 Implemented ISelectionProvider on ODOMSelectionManager and DeviceEditor

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 11-May-04	4262/1	matthew	VBM:2004051009 DefaultODOMElementSelectionProvider modified to avoid ConcurrentModificationException

 15-Dec-03	2160/2	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 25-Nov-03	2005/5	byron	VBM:2003112006 Eclipse to ODOM events - replaced defaultProvider with default value in map

 25-Nov-03	2005/3	byron	VBM:2003112006 Eclipse to ODOM events - fixed javadoc and updated testcases

 24-Nov-03	2005/1	byron	VBM:2003112006 Eclipse to ODOM events

 ===========================================================================
*/
