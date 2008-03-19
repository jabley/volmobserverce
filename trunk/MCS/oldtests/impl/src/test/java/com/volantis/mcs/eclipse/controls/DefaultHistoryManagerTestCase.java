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
package com.volantis.mcs.eclipse.controls;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.ResourceStub;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import java.util.HashMap;


/**
 * Tests the DefaultHistoryManager methods.
 * Methods which require interraction with Eclipse sessions are tested by making use of a
 * mock Resource
 */
public class DefaultHistoryManagerTestCase extends TestCaseAbstract {

    /**
     * The session property prefix for HistoryManager session properties.
     */
    private static final String SESSION_PROPERTY_PREFIX = "HistoryManagerTestCase.";

    /**
     * The qualifier for the QualifiedName for this history managers session key
     */
    private static final String QUALIFIER =
            "com.volantis.mcs.eclipse.controls.DefaultHistoryManager";

    /**
     * A Mock Resource which is used to emulate an Eclipse session resource.
     * This variable needs to be static because the method we are overriding, and using
     * this variable in, is called by the constructor of DefaultHistoryManager and as thus,
     * is part of the construction process. It is static so that it is available before
     * this process has been fully executed.
     */
    private static MyResource mockResource;

    /**
     * Performs Initialization before each test is run
     */
    protected void setUp() throws Exception {
        super.setUp();
        mockResource = new MyResource();
    }

    /**
     * Performs clean up after each test is run
     */
    protected void tearDown() throws Exception {
        mockResource = null;
        super.tearDown();
    }

    /**
     * Gets a DefaultHistoryManager object, which overrrides the getResource
     * method so that we can use our own ResourceStub.
     * @param key - The key for the session object
     * @return a DefaultHistoryManager
     */
    protected DefaultHistoryManager getManager(String key) {
        QualifiedName qualifiedId =
                new QualifiedName(QUALIFIER, SESSION_PROPERTY_PREFIX + key);
        return new DefaultHistoryManager(qualifiedId) {
            // we override this so that we can use the mock resource instead of
            // the Eclipse resource
            public IResource getResource() {
                return mockResource;
            }
        };
    }

    /**
     * Tests that the History Manager is successfully instantiated when there
     * is no value stored.
     * @throws Exception
     */
    public void testHistoryManagerConstructsWithNoSessionArray() throws Exception {
        DefaultHistoryManager manager = getManager("testKey");
        // test that we can construct the manager properly
        String[] history = manager.getHistory();
        assertTrue(history != null);
        assertTrue(history.length == 0);
    }

    /**
     * Tests that the History Manager is successfully instantiated when there
     * is an empty array stored in the session.
     * @throws Exception
     */
    public void testHistoryManagerConstructsWithEmptySessionArray() throws Exception {
        // first save an empty history into the session
        DefaultHistoryManager manager = getManager("testKey");
        manager.saveHistory();

        // test that we can construct the manager properly
        manager = getManager("testKey");
        String[] history = manager.getHistory();
        assertTrue(history != null);
        assertTrue(history.length == 0);
    }

    /**
     * Tests that the History Manager is successfully instantiated when there
     * is an existing array (containing data) stored in the session.
     * @throws Exception
     */
    public void testHistoryManagerConstructsWithSessionArray() throws Exception {
        DefaultHistoryManager manager = getManager("testKey");

        //load some history into the session
        manager.updateHistory("preload1");
        manager.updateHistory("preload2");
        manager.updateHistory("preload3");
        manager.saveHistory();

        // now see if this history loads when constructing the history manager
        manager = getManager("testKey");

        String preloadHistory[] = manager.getHistory();
        assertTrue(preloadHistory != null);
        assertTrue(preloadHistory[0].equals("preload3"));
        assertTrue(preloadHistory[1].equals("preload2"));
        assertTrue(preloadHistory[2].equals("preload1"));
    }

    /**
     * Tests to see if the manager can update history correctly, new entries
     * should be stored at the front of the array, pre-existing entries
     * should have moved to the front of the array.
     * @throws Exception
     */
    public void testUpdateHistory() throws Exception {
        DefaultHistoryManager manager = getManager("testKey");
        String[] history = manager.getHistory();
        // now lets test the update method -
        // history 4 should be in 2nd position and history 1 should occur in the
        // first position only
        manager.updateHistory("history1");
        manager.updateHistory("history2");
        manager.updateHistory("history3");
        manager.updateHistory("history4");
        manager.updateHistory("history1");

        history = manager.getHistory();

        assertTrue(history.length == 4);
        assertTrue(history[0].equals("history1"));
        assertTrue(history[1].equals("history4"));
        assertTrue(history[2].equals("history3"));
        assertTrue(history[3].equals("history2"));
    }

    /**
     * Tests that the History Manager can save the currently stored history
     * and retrieve it again.
     * @throws Exception
     */
    public void testSaveHistory() throws Exception {
        DefaultHistoryManager manager = getManager("testKey");

        // load some history into the session
        manager.updateHistory("preload1");
        manager.updateHistory("preload2");
        manager.updateHistory("preload3");
        manager.saveHistory();

        // now see if this history loads when constructing the history manager
        manager = getManager("testKey");

        String preloadHistory[] = manager.getHistory();
        assertTrue(preloadHistory != null);
        assertTrue(preloadHistory[0].equals("preload3"));
        assertTrue(preloadHistory[1].equals("preload2"));
        assertTrue(preloadHistory[2].equals("preload1"));
    }

    /**
     * Tests that the History Manager can save the currently stored history
     * and retrieve it again.
     * @throws Exception
     */
    public void testHistoryIsUnique() throws Exception {
        DefaultHistoryManager manager = getManager("myKey");

        // load some history into the session
        manager.updateHistory("preload1");
        manager.updateHistory("preload2");
        manager.updateHistory("preload3");
        manager.saveHistory();

        // now see if this history loads when constructing the history manager
        manager = getManager("yourKey");

        String preloadHistory[] = manager.getHistory();
        assertTrue(preloadHistory != null);
        assertTrue(preloadHistory.length == 0);
    }

    /**
     * Private Inner Class for resource stub
     */
    private class MyResource extends ResourceStub {

        /**
         * A Map which stores objects and associates them with a key for later retrieval
         */
        private HashMap map = new HashMap();

        /**
         * Overrides the getSessionProperty to get the session property with the
         * provided key.
         * @param qualifiedName - The Key to get access the session object.
         * @return the session object associated with the provided key or null if no
         * object was found with this key.
         * @throws CoreException
         */
        public Object getSessionProperty(QualifiedName qualifiedName)
                throws CoreException {
            return map.get(qualifiedName);
        }


        /**
         * Overrides the setSessionProperty to set the session property with the
         * provided key
         * @param qualifiedName - The Key to get access the session object.
         * @param o - The Object to store with the provided key.
         * @throws CoreException
         */
        public void setSessionProperty(QualifiedName qualifiedName, Object o)
                throws CoreException {
            map.put(qualifiedName, o);
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

 16-Jul-04	4888/9	tom	VBM:2004070605 Created DefaultHistoryManager
 ===========================================================================
*/
