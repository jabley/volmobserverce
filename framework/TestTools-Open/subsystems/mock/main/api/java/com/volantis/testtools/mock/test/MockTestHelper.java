/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.test;

import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.proxy.ProxyMockObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A helper class that is used when writing tests.
 *
 * <p>This class is only intended for use by the mock framework and the 
 * generated mocks, not user code. It is only public in order to allow access
 * by the mocks but for all other code this should be treated as if it does
 * not exist.</p>
 */
public class MockTestHelper {

    /**
     * The default factory.
     */
    private static MockFactory mockFactory = MockFactory.getDefaultInstance();

    /**
     * A thread local variable used to store per thread state, mainly in order
     * to detect and prevent reentrancy.
     */
    private static ThreadLocal mockTestHelperState = new ThreadLocal();

    /**
     * Begin a test involving mock objects.
     */
    public static void begin() {
        if (getState() != null) {
            throw new IllegalStateException("Nested tests not allowed");
        }

        // We need to initialize the state like this, since there is a
        // re-entrant call to addExpectations
        final MockTestHelperState state = new MockTestHelperState();
        setState(state);

        // Now that the state is available for use, create the global
        // ExpectationContainer.
        state.createGlobalExpectationContainer();
    }

    private static MockTestHelperState getState() {
        return (MockTestHelperState) mockTestHelperState.get();
    }

    private static MockTestHelperState getState(boolean create) {
        MockTestHelperState state =
                (MockTestHelperState) mockTestHelperState.get();
        if (create && state == null) {
            state = new MockTestHelperState();
            mockTestHelperState.set(state);
        }

        return state;
    }

    private static void setState(MockTestHelperState state) {
        mockTestHelperState.set(state);
    }

    /**
     * Return a <code>Map</code> of the Proxies.
     *
     * @return a Map
     */
    private static Map getProxies() {
        return getState().getProxies();
    }

    /**
     * Return a <code>List</code> of the expectations.
     *
     * @return a <code>List</code>.
     */
    private static List getExpectations() {
        return getState().getExpectations();
    }

    /**
     * End the test.
     *
     * @param verify If true then all the mock objects used within the test
     *               will be verified, otherwise they will not. This should only be false if
     *               the test failed in some other way.
     */
    public static void end(boolean verify) {
        if (getState() == null) {
            throw new IllegalStateException("Test was not begun");
        }

        try {
            if (verify) {
                // Verify all expectations.
                for (Iterator iterator = getExpectations().iterator(); iterator.hasNext();)
                {
                    ExpectationContainer container =
                            (ExpectationContainer) iterator.next();
                    container.verify();
                }
            }
        } finally {
            // Clear the state.
            setState(null);
        }
    }

    public static void addExpectations(ExpectationContainer container) {
        if (getState() == null) {
            throw new IllegalStateException
                    ("Mock test was not begin, must call MockTestHelper.begin()" +
                            " before constructing any mock objects.");
        }
        getExpectations().add(container);
    }

    public static void insideFramework() {
        if (isInsideFramework()) {
            throw new IllegalStateException("Already inside framework");
        }

        setInsideFramework(true);
    }

    /**
     * Record whether the current thread is inside the framework or not.
     *
     * @param state True if the current thread is entering the framework,
     *              false if it is exiting the framework.
     */
    private static void setInsideFramework(boolean state) {
        // Get the state, making sure that it is created if not present and
        // then mark that it is inside the framework.
        getState(true).setInsideFramework(state);
    }

    /**
     * Check to see whether the current thread is inside the framework.
     *
     * <p>The current thread is inside the framework iff thread local state
     * has been created and it has been marked as being inside the
     * framework.</p>
     *
     * @return True if it is, false if it is not.
     */
    public static boolean isInsideFramework() {
        return getState() != null && getState().isInsideFramework();
    }

    public static void outsideFramework() {
        if (!isInsideFramework()) {
            throw new IllegalStateException("Not inside framework");
        }

        setInsideFramework(false);
    }

    public static ExpectationContainer getGlobalExpectationContainer() {
        //System.out.println("Getting global - " + globalContainer);
        return getState() != null ? getState().getGlobalExpectationContainer() : null;
    }

    public static ProxyMockObject getProxyObject(
            Class clazz, String identifier) {

        ProxyMockObject object = (ProxyMockObject) getProxies().get(identifier);
        if (object == null) {
            object = mockFactory.createProxyMockObject(clazz, identifier);
            getProxies().put(identifier, object);
        }

        return object;
    }

    /**
     * Class to hold object state. Encapsulating state in this manner makes it
     * easier to make the MockTestHelper thread-safe.
     */
    private static class MockTestHelperState {

        private final List expectations;

        private ExpectationContainer globalContainer;

        private boolean insideFramework;

        private final Map proxies;

        MockTestHelperState() {
            expectations = new ArrayList();
            proxies = new HashMap();
            insideFramework = false;
        }

        public boolean isInsideFramework() {
            return insideFramework;
        }

        public Map getProxies() {
            return proxies;
        }

        public List getExpectations() {
            return expectations;
        }

        public void setInsideFramework(boolean state) {
            this.insideFramework = state;
        }

        public ExpectationContainer getGlobalExpectationContainer() {
            return globalContainer;
        }

        /**
         * Initialise the globalContainer. This cannot be a final field which
         * gets assigned as part of the constructor, since the
         * {@link com.volantis.testtools.mock.MockFactory#createUnorderedBuilder()}
         * implementation calls
         * {@link MockTestHelper#addExpectations(com.volantis.testtools.mock.ExpectationContainer)};
         * i.e. it is re-entrant.
         */
        public void createGlobalExpectationContainer() {
            globalContainer = mockFactory.createUnorderedBuilder();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/3	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 11-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
