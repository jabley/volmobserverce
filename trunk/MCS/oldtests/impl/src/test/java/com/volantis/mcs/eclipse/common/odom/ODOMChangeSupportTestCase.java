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
import junitx.util.PrivateAccessor;

import java.util.ArrayList;
import java.util.Map;

/**
 * Test case for {@link ODOMChangeSupport}.
 */
public class ODOMChangeSupportTestCase extends TestCaseAbstract {
    /**
     * The target for testing.
     */
    ODOMChangeSupport target;

    /**
     * The observable source for which the change support is instanced.
     */
    ODOMObservable source;

    /**
     * Class used to track the number of event notifications received
     */
    class EventCounter implements ODOMChangeListener {
        protected int eventCount;

        // javadoc inherited
        public void changed(ODOMObservable node,
                            ODOMChangeEvent event) {
            eventCount++;
        }

        // javadoc unnecessary
        public int getEventCount() {
            return eventCount;
        }

        public void reset() {
            eventCount = 0;
        }
    }

    // javadoc unnecessary
    public ODOMChangeSupportTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        source = createSource();
        target = createTarget(source);
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        target = null;
        source = null;

        super.tearDown();
    }

    /**
     * Factory method that allows different observables to be constructed if
     * needed.
     *
     * @return an ODOMObservable implementation instance
     */
    protected ODOMObservable createSource() {
        return new ODOMObservableStub();
    }

    /**
     * Factory method that allows a ODOMChangeSupport specializations to be
     * constructed if needed.
     *
     * @param source the source with which to construct the change support
     *               instance
     * @return a new ODOMChangeSupport (or specialization) instance
     */
    protected ODOMChangeSupport createTarget(ODOMObservable source) {
        return new ODOMChangeSupport(source);
    }

    // javadoc unnecessary
    public void testAddChangeListener() throws Exception {
        EventCounter counter = new EventCounter();

        target.addChangeListener(counter);

        target.fireChange(ODOMChangeEvent.createNew(source, source, null,ChangeQualifier.NONE));

        assertEquals("Number of events received not as",
                     1,
                     counter.getEventCount());

        counter.reset();

        // Adding the same object again will do nothing
        target.addChangeListener(counter);

        target.fireChange(ODOMChangeEvent.createNew(source,
                                              source,
                                              null,
                                              ChangeQualifier.HIERARCHY));

        // Even though the event was qualified, the unqualified registration
        // of the counter should mean it still gets notified
        assertEquals("Number of events received[2] not as",
                     1,
                     counter.getEventCount());
    }

    /**
     * Test the use of {@link
     * ODOMChangeSupport#addChangeListener(ODOMChangeListener,ChangeQualifier)
     */
    public void testAddChangeListenerQualified() throws Exception {
        EventCounter unqualified = new EventCounter();
        EventCounter qualified = new EventCounter();

        target.addChangeListener(unqualified);
        target.addChangeListener(qualified, ChangeQualifier.HIERARCHY);

        target.fireChange(ODOMChangeEvent.createNew(source, source, null,ChangeQualifier.NONE));

        assertEquals("Number of events received by unqualified not as",
                     1,
                     unqualified.getEventCount());

        assertEquals("Number of events received by qualified not as",
                     0,
                     qualified.getEventCount());

        unqualified.reset();

        // This should notify both registered counters
        target.fireChange(ODOMChangeEvent.createNew(source,
                                              source,
                                              null,
                                              ChangeQualifier.HIERARCHY));

        assertEquals("Number of events received by unqualified[2] not as",
                     1,
                     unqualified.getEventCount());

        assertEquals("Number of events received by qualified[2] not as",
                     1,
                     qualified.getEventCount());

        unqualified.reset();
        qualified.reset();

        // This should demonstrate that the HIERARCHY qualified registration
        // isn't notified
        target.fireChange(ODOMChangeEvent.createNew(source,
                                              source,
                                              null,
                                              ChangeQualifier.NAME));

        assertEquals("Number of events received by unqualified[3] not as",
                     1,
                     unqualified.getEventCount());

        assertEquals("Number of events received by qualified[3] not as",
                     0,
                     qualified.getEventCount());
    }

    // javadoc unnecessary
    public void testRemoveChangeListener() throws Exception {
        EventCounter counter = new EventCounter();

        // This should simply do nothing since the counter has not yet been
        // registered
        target.removeChangeListener(counter);

        target.addChangeListener(counter);
        target.addChangeListener(counter, ChangeQualifier.HIERARCHY);

        // This should remove one registration of the counter
        target.removeChangeListener(counter);

        target.fireChange(ODOMChangeEvent.createNew(source,
                                              source,
                                              null,
                                              ChangeQualifier.HIERARCHY));

        assertEquals("Number of events received not as",
                     1,
                     counter.getEventCount());
    }

    /**
     * Test the use of {@link
     * ODOMChangeSupport#removeChangeListener(ODOMChangeListener,ChangeQualifier)}
     */
    public void testRemoveChangeListenerQualified() throws Exception {
        EventCounter counter = new EventCounter();

        target.addChangeListener(counter);
        target.addChangeListener(counter, ChangeQualifier.HIERARCHY);

        // This should not remove anything (and should not report any problems)
        target.removeChangeListener(counter, ChangeQualifier.NAME);

        target.fireChange(ODOMChangeEvent.createNew(source,
                                              source,
                                              null,
                                              ChangeQualifier.HIERARCHY));

        assertEquals("Number of events received not as",
                     2,
                     counter.getEventCount());

        counter.reset();

        // This should remove one registration of the counter
        target.removeChangeListener(counter, ChangeQualifier.HIERARCHY);

        target.fireChange(ODOMChangeEvent.createNew(source,
                                              source,
                                              null,
                                              ChangeQualifier.HIERARCHY));

        assertEquals("Number of events received[2] not as",
                     1,
                     counter.getEventCount());
    }

    /**
     * The other tests use fireChange for the case where the value has changed
     * so this test only needs to ensure that no events are fired if the value
     * remains the same.
     */
    public void testFireChange() throws Exception {
        EventCounter counter = new EventCounter();

        target.addChangeListener(counter);

        target.fireChange(
                ODOMChangeEvent.createNew(source,
                                        null,
                                        null,
                                        ChangeQualifier.NONE));

        assertEquals("Number of events fired not as",
                     0,
                     counter.getEventCount());

        target.fireChange(
                ODOMChangeEvent.createNew(source,
                                        source,
                                        source,
                                        ChangeQualifier.NONE ));

        assertEquals("Number of events[2] fired not as",
                     0,
                     counter.getEventCount());
    }

    /**
     * This tests the removeAllListeners method.  It checks that they are
     * successfully added, then removes them and checks that they have been
     * removed as expected.
     */
    public void testRemoveAllListeners() throws Exception {
        target.addChangeListener(createODOMChangeListener("One"));
        target.addChangeListener(createODOMChangeListener("Two"));
        target.addChangeListener(createODOMChangeListener("Three"));
        target.addChangeListener(createODOMChangeListener("A"),
                                 ChangeQualifier.NAMESPACE);
        target.addChangeListener(createODOMChangeListener("B"),
                                 ChangeQualifier.HIERARCHY);
        target.addChangeListener(createODOMChangeListener("C"),
                                 ChangeQualifier.TEXT);

        ArrayList listeners =
                (ArrayList) PrivateAccessor.getField(target, "listeners");
        Map qualifiedListeners =
                (Map) PrivateAccessor.getField(target, "qualifiedListeners");

        assertNotNull("Listeners should be initialised", listeners);
        assertNotNull("Qualified listeners should be initialised",
                      qualifiedListeners);
        assertTrue("Listeners should contain some values",
                   listeners.size() != 0);
        assertTrue("Qualified listeners should contain some values",
                   qualifiedListeners.entrySet().size() != 0);

        target.removeAllListeners();

        listeners = (ArrayList) PrivateAccessor.getField(target, "listeners");
        qualifiedListeners = (Map) PrivateAccessor.getField(target,
                                                            "qualifiedListeners");
        assertNull("Listeners should be null", listeners);
        assertNull("Qualified listeners should be null", qualifiedListeners);
    }

    /**
     * This tests the removeAllListeners method.  It checks that they are
     * successfully added, then removes them and checks that they have been
     * removed as expected.
     */
    public void testOutputListenerWarnings() throws Exception {
        target.addChangeListener(createODOMChangeListener("One"));
        target.addChangeListener(createODOMChangeListener("Two"));
        target.addChangeListener(createODOMChangeListener("Three"));
        target.addChangeListener(createODOMChangeListener("A"),
                                 ChangeQualifier.NAMESPACE);
        target.addChangeListener(createODOMChangeListener("B"),
                                 ChangeQualifier.HIERARCHY);
        target.addChangeListener(createODOMChangeListener("C"),
                                 ChangeQualifier.TEXT);

        ArrayList listeners =
                (ArrayList) PrivateAccessor.getField(target, "listeners");
        Map qualifiedListeners =
                (Map) PrivateAccessor.getField(target, "qualifiedListeners");

        assertNotNull("Listeners should be initialised", listeners);
        assertNotNull("Qualified listeners should be initialised",
                      qualifiedListeners);
        assertTrue("Listeners should contain some values",
                   listeners.size() != 0);
        assertTrue("Qualified listeners should contain some values",
                   qualifiedListeners.entrySet().size() != 0);

        target.outputListenerWarnings();
    }

    /**
     * A utility method that creates a named instance of an ODOM change
     * listener for use in creating test data.
     *
     * @param name The name of the listener
     * @return A suitably initialised <code>ODOMChangeListener</code>.
     */
    private ODOMChangeListener createODOMChangeListener(final String name) {
        return new ODOMChangeListener() {
            String listenerName = name;
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
            }
        };
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4735/1	matthew	VBM:2004062104 Ensure that listener warning information works correctly for qualifiedListeners

 18-May-04	4429/1	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 29-Jan-04	2689/1	eduardo	VBM:2003112407 undo/redo manager for ODOM

 27-Nov-03	2046/1	philws	VBM:2003112603 Clarify contract on ODOMObservable methods and update ODOMChangeSupport to follow this contract

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
