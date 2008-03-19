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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommandTestAbstract;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;

import java.util.LinkedList;
import java.util.List;


/**
 * This class exists so that the class hierarchy of the Test cases matches the
 * hierarchy of the classes being tested. It also provides a convienient place
 * to add tests which ensure that the correct number of ODOMSelectionEvents
 * are fired when modifications are made to the DOM.
 */
public abstract class LayoutActionCommandTestAbstract
        extends ODOMActionCommandTestAbstract {

    /**
     * The number of selection events expected when an action command is run.
     */
    private int numRunSelectionsExpected = 1;

    /**
     * The number of selection events exected when an action command is asked
     * if it can enable itself
     */
    private int numEnableSelectionsExpected = 0;

    /**
     * This method runs the tests but allows you to specify the number of
     * selection events you expect from that test.
     *
     * @param data the test data containing the configuration to be tested
     * @param expectedXml the expected document structure
     * @param numSelectionsExpected
     * @throws Exception
     * rest of javadoc inherited
     */
    protected void doTestRun(TestData data, String expectedXml,
                             int numSelectionsExpected) throws Exception {
        int tmp = numRunSelectionsExpected;
        numRunSelectionsExpected = numSelectionsExpected;
        try {
            doTestRun(data, expectedXml);
        } finally {
            numRunSelectionsExpected = tmp;
        }
    }

    /**
     * Used to perform "run" tests with selection event counting.
     *
     * @param data the test data containing the configuration to be tested
     * @param expectedXml the expected document structure
     * rest of javadoc inherited
     */
    protected void doTestRun(TestData data,
                             String expectedXml) throws Exception {

        ODOMSelectionManager selManager = data.getSelectionManager();
        ODOMESCountingListener listener = new ODOMESCountingListener();
        selManager.addSelectionListener(listener, null);
        super.doTestRun(data, expectedXml);
        assertEquals("One selection event was expected. ",
                numRunSelectionsExpected, listener.getCount());
        selManager.removeSelectionListener(listener, null);

    }

    /**
     * Used to perform "Enable" tests with selection counting. This method
     * allows you to specify how many selection events should be fired when
     * enable is called. Ideally no selections should be fired.
     *
     * @param numSelectionsExpected The number of selection events you expect.
     * rest of javadoc inherited
     */
    protected void doTestEnable(TestData data, String testName,
                                boolean expected, int numSelectionsExpected)
            throws Exception {
        int tmp = numEnableSelectionsExpected;
        numEnableSelectionsExpected = numSelectionsExpected;
        try {
            doTestEnable(data, testName, expected);
        } finally {
            numEnableSelectionsExpected = tmp;
        }

    }

    /**
     * Used to perform "Enable" tests with selection event counting
     * No selections should be fired under ideal circumstances.
     * rest of javadoc inherited
     */
    protected void doTestEnable(TestData data,
                                String testName,
                                boolean expected) throws Exception {
        ODOMSelectionManager selManager = data.getSelectionManager();
        ODOMESCountingListener listener = new ODOMESCountingListener();
        selManager.addSelectionListener(listener, null);
        super.doTestEnable(data, testName, expected);
        assertEquals("No selection event was expected.",
                numEnableSelectionsExpected, listener.getCount());
        selManager.removeSelectionListener(listener, null);
    }


    /**
     * Utility method to create an ODOMSelectionManager
     * @param observe the element to observe
     * @return
     */
    protected ODOMSelectionManager createSelectionManager(ODOMElement observe) {
        ODOMSelectionFilter filter = new ODOMSelectionFilter(null, null,
                new ODOMSelectionFilterConfiguration(true, false));
        filter.include(observe);
        return new ODOMSelectionManager(filter);
    }

    /**
     * Utility class that can be used to count the number of selection events
     * that occur. This is useful for testing that the layout related
     * ODOMActionCommands only cause a single selection event to be fired.
     */
    public static class ODOMESCountingListener
            implements ODOMElementSelectionListener {

        /**
         * A count of all the selectionEvents this listener receives
         */
        public int count = 0;

        /**
         * Counts the number of selection events that have been received
         * rest of javadoc inherited
         */
        public void selectionChanged(ODOMElementSelectionEvent event) {
            count++;
        }

        /**
         * @return the number of selection events this listener has recieved
         */
        public int getCount() {
            return count;
        }

        /**
         * Reset the count this has made of selection events
         */
        public void resetCount() {
            count = 0;
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

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 ===========================================================================
*/
