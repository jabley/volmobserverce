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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;
import org.jdom.input.JDOMFactory;

import java.util.List;
import java.util.Map;

/**
 * Test case for {@link ODOMElement}.
 */
public class ODOMElementTestCase extends TestCaseAbstract {

    /**
     * Empty listener implementation.
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

    /**
     *
     * @throws Exception
     */
    public void testMoveListeners() throws Exception {
        final JDOMFactory factory = new ODOMFactory();

        ODOMElement fromRoot = (ODOMElement) factory.element("root");
        ODOMElement fromChild1 = (ODOMElement) factory.element("child");
        ODOMElement fromChild2 = (ODOMElement) factory.element("child");
        fromRoot.addContent(fromChild1);
        fromRoot.addContent(fromChild2);
        factory.document(fromRoot);

        ODOMChangeListener rootListener = createODOMChangeListener();
        fromRoot.addChangeListener(rootListener);
        fromRoot.addChangeListener(rootListener, ChangeQualifier.NAME);

        ODOMChangeListener child1Listener = createODOMChangeListener();
        fromChild1.addChangeListener(child1Listener);
        fromChild1.addChangeListener(child1Listener, ChangeQualifier.NAME);

        ODOMChangeListener child2Listener = createODOMChangeListener();
        fromChild2.addChangeListener(child2Listener);
        fromChild2.addChangeListener(child2Listener, ChangeQualifier.NAME);

        ODOMElement toRoot = (ODOMElement) factory.element("root");
        ODOMElement toChild1 = (ODOMElement) factory.element("child");
        toRoot.addContent(toChild1);
        factory.document(toRoot);

        fromRoot.moveListeners(toRoot);

        assertEquals("There should be no listeners in fromRoot.", 0,
                getNumListeners(fromRoot));
        assertEquals("There should be no qualified listeners in fromRoot.", 0,
                getNumQListeners(fromRoot));
        assertEquals("There should be 1 listener in toRoot.", 1,
                getNumListeners(toRoot));
        assertEquals("There should be 1 qualified listener in toRoot.", 1,
                getNumQListeners(toRoot));

        assertEquals("There should be no listeners in fromChild1.", 0,
                getNumListeners(fromChild1));
        assertEquals("There should be no qualified listeners in fromChild1.", 0,
                getNumQListeners(fromChild1));
        assertEquals("There should be 1 listener in toChild1.", 1,
                getNumListeners(toChild1));
        assertEquals("There should be 1 qualified listener in toChild1.", 1,
                getNumQListeners(toChild1));

        assertEquals("There should be no listeners in fromChild2.", 0,
                getNumListeners(fromChild2));
        assertEquals("There should be no qualified listeners in fromChild2.", 0,
                getNumQListeners(fromChild2));

        assertSame("The listener in toRoot should be rootListener", rootListener,
                getChangeSupport(toRoot).getChangeListeners().get(0));

        assertSame("The listener in toChild1 should be child1Listener",
                child1Listener,
                getChangeSupport(toChild1).getChangeListeners().get(0));

    }

    /**
     * Create an ODOMChangeListener that does nothing.
     * @return an ODOMChangeListener
     */
    private ODOMChangeListener createODOMChangeListener() {
        return new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
            }
        };
    }

    /**
     *
     * @param element
     * @return
     */
    private int getNumListeners(ODOMElement element) throws Exception {
        List listeners = null;
        ODOMChangeSupport support = getChangeSupport(element);
        if (support != null) {
            listeners =
                    (List) PrivateAccessor.getField(support, "listeners");
        }
        return listeners == null ? 0 : listeners.size();
    }

    /**
     * Provide the change support for a given ODOMElement.
     * @param element
     * @return
     * @throws NoSuchFieldException
     */
    private ODOMChangeSupport getChangeSupport(ODOMElement element)
            throws NoSuchFieldException {
        ODOMChangeSupport support = (ODOMChangeSupport)
                PrivateAccessor.getField(element, "changeSupport");
        return support;
    }

    /**
     *
     * @param element
     * @return
     */
    private int getNumQListeners(ODOMElement element) throws Exception {
        Map listeners = null;
        ODOMChangeSupport support = (ODOMChangeSupport)
                PrivateAccessor.getField(element, "changeSupport");
        if (support != null) {
            listeners =
                    (Map) PrivateAccessor.getField(support,
                            "qualifiedListeners");
        }
        return listeners == null ? 0 : listeners.size();
    }

    /**
     * This tests to detachObservable method.  Whilst this is tested from the
     * point of view of the command pattern it uses, this test case exists to
     * exercise the recursive nature of the implementation of the method in
     * the <code>ODOMElement</code> class.
     */
    public void testRecursiveDetach() throws Exception {
        final JDOMFactory factory = new ODOMFactory();

        // Build some test data
        ODOMElement fromRoot = (ODOMElement) factory.element("root");
        fromRoot.setName("Root");
        ODOMElement fromChild1 = (ODOMElement) factory.element("child");
        fromChild1.setName("Child1");
        ODOMElement fromChild2 = (ODOMElement) factory.element("child");
        fromChild2.setName("Child2");
        fromRoot.addContent(fromChild1);
        fromRoot.addContent(fromChild2);

        ODOMChangeListener rootListener = createODOMChangeListener();
        fromRoot.addChangeListener(rootListener);
        fromRoot.addChangeListener(rootListener, ChangeQualifier.NAMESPACE);

        ODOMChangeListener child1Listener = createODOMChangeListener();
        fromChild1.addChangeListener(child1Listener);
        fromChild1.addChangeListener(child1Listener, ChangeQualifier.TEXT);

        ODOMChangeListener child2Listener = createODOMChangeListener();
        fromChild2.addChangeListener(child2Listener);
        fromChild2.addChangeListener(child2Listener, ChangeQualifier.NAME);

        // Test detachObservable
        fromRoot.detachObservable();
        ODOMChangeSupport support =
                (ODOMChangeSupport) PrivateAccessor.getField(fromRoot,
                                                             "changeSupport");

        // Check test results
        assertTrue("Should be no unqualified listeners (1)",
                   support.getChangeListeners().size() == 0);
        assertTrue("Should be no qualified listeners (1)",
                   support.getQualifiedChangeListeners().size() == 0);
        support = (ODOMChangeSupport) PrivateAccessor.getField(fromChild1,
                                                               "changeSupport");
        assertTrue("Should be no unqualified listeners (2)",
                   support.getChangeListeners().size() == 0);
        assertTrue("Should be no qualified listeners (2)",
                   support.getQualifiedChangeListeners().size() == 0);
        support = (ODOMChangeSupport) PrivateAccessor.getField(fromChild2,
                                                               "changeSupport");
        assertTrue("Should be no unqualified listeners (3)",
                   support.getChangeListeners().size() == 0);
        assertTrue("Should be no qualified listeners (3)",
                   support.getQualifiedChangeListeners().size() == 0);
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

 18-May-04	4429/3	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 11-May-04	4250/3	pcameron	VBM:2004051005 Added Restore Defaults button and changed ODOMElement and StandardElementHandler to deal with listener removal

 ===========================================================================
*/
