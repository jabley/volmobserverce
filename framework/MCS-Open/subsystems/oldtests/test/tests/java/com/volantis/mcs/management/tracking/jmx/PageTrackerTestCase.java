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
package com.volantis.mcs.management.tracking.jmx;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.management.tracking.PageDetails;
import com.volantis.mcs.management.tracking.CanvasDetails;
import com.volantis.mcs.management.tracking.CanvasType;

/**
 * Test the JMX PageTracker behaves as expected.
 */
public class PageTrackerTestCase extends TestCaseAbstract {

    private static final int NUM_OBJECTS = 10;

    /**
     * factory used when running the tests
     */
    private  PageTrackerFactory ptFactory;

    /**
     * Standard test case constructor.
     */
    public PageTrackerTestCase(String name){
        super(name);


    }

    public void setUp() throws Exception {
         ptFactory = new JMXPageTrackerFactory();
    }

    /**
     * Test the retrievePageDetails and addPageDetails methods of PageTracker.
     * This simple test ensures that the same number of PageDetails objects are
     * returned from the PageTracker as were added to it.
     *
     * @throws Exception
     */
    public void testRetrievePageDetails() throws Exception {
        PageTracker pageTracker = new PageTracker();

        PageDetails[] details = pageTracker.retrievePageDetails();
        assertNotNull("should be a zero length arrray", details);
        assertEquals("Incorrect number of Details objects found ",
                0, details.length);

        // set up a pageTracker with a number of details objects
        for(int i=0;i<NUM_OBJECTS;i++) {
            pageTracker.addPageDetails(createPageDetails("PageDetails:"+i));
        }
        details = pageTracker.retrievePageDetails();

        assertEquals("Incorrect number of Details objects found ",
                 NUM_OBJECTS, details.length);
    }

    /**
     * Simple test to ensure that flushPageDetails removes all PageDetails
     * objects from the tracker.
     *
     * @throws Exception
     */
    public void testFlushPageDetails() throws Exception {
        PageTracker pageTracker = new PageTracker();
        // set up a pageTracker with a number of details objects
        for(int i=0;i<NUM_OBJECTS;i++) {
            pageTracker.addPageDetails(createPageDetails("PageDetails:"+i));
        }
        // ensuring the PageTracker was actually filled in the first place is
        // carried out in the testRetrievePageDetails test.

        pageTracker.flushPageDetails();
        PageDetails[] details = pageTracker.retrievePageDetails();
        assertEquals("Incorrect number of Details objects found. None",
                0, details.length);

    }


    /**
     * Create a PageDetails object for testing.
     *
     * @param deviceName the device name to associate with the created page
     * details object.
     * @return a new PageDetails object.
     */
    protected PageDetails createPageDetails(String deviceName) {

        CanvasDetails canvasDetails = ptFactory.createCanvasDetails("TITLE",
                CanvasType.INCLUSION, "FAKE_THEME", "FAKE_LAYOUT");
        return ptFactory.createPageDetails(canvasDetails, deviceName);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4702/5	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
