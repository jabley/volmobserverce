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

import com.volantis.mcs.management.tracking.CanvasDetails;
import com.volantis.mcs.management.tracking.CanvasType;
import com.volantis.mcs.management.tracking.DefaultCanvasDetailsTestCase;
import com.volantis.mcs.management.tracking.DefaultPageDetailsTestCase;
import com.volantis.mcs.management.tracking.PageDetails;
import com.volantis.mcs.management.tracking.PageDetailsManager;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.util.ArrayList;


/**
 * Perform tests to ensure that the JMXPageDetailsManagerFactory is
 * working as expected.
 */
public class JMXPageTrackerFactoryTestCase extends TestCaseAbstract {

    /**
     * Standard test case constructor.
     * @param name Test name
     */
    public JMXPageTrackerFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Simple tests to ensure the JMXPageDetailsManagerFactory is working as
     * expected.
     * @throws Exception
     */
    public void testCreatePageDetailsManager() throws Exception {
        PageTrackerFactory managerFactory =
                new JMXPageTrackerFactory();

        // ensure we have a clean slate
        JMXTestUtilities.removeAllMBeanServers();

        // test multiple calls to manager Factory
        PageDetailsManager pdManager1 =
                managerFactory.createPageDetailsManager();
        PageDetailsManager pdManager2 =
                managerFactory.createPageDetailsManager();

        ArrayList mBeanServers = MBeanServerFactory.findMBeanServer(null);
        assertEquals("There should only be one MBeanServer",
                1, mBeanServers.size());

        assertSame("These two PageDetailsManager objects should be the same",
                pdManager1, pdManager2);

    }

    /**
     * Check that the correct mbean is registered with MBeanServerFactory.
     * This is done by calling the MBeans methods.
     * @throws Exception
     */
    public void testMBeanIsAvailable() throws Exception {

        // ensure we have a clean slate
        JMXTestUtilities.removeAllMBeanServers();
        PageTrackerFactory managerFactory =
                new JMXPageTrackerFactory();

        // this should have registered an MBean with a server.
        PageDetailsManager pdManager1 =
                managerFactory.createPageDetailsManager();

        PageTrackerFactory ptFactory = new JMXPageTrackerFactory();
        CanvasDetails canvasDetails = ptFactory.createCanvasDetails("TITLE",
                CanvasType.INCLUSION, "FAKE_THEME", "FAKE_LAYOUT");
        PageDetails pageDetails = ptFactory.createPageDetails(canvasDetails, "WIBBLE");
        pdManager1.addPageDetails(pageDetails);

        ArrayList mBeanServers = MBeanServerFactory.findMBeanServer(null);

        // we know this is the only MBeanServer.
        // testCreatePageDetailManager() tests for it.

        // count the number of beans. There should be the one I created and
        // a "default".
        MBeanServer server = (MBeanServer) mBeanServers.get(0);
        assertEquals("There should be two beans", 2,
                server.getMBeanCount().intValue());

        ObjectName beanName =
                new ObjectName(JMXPageTrackerFactory.PAGE_TRACKER_NAME);

        PageDetails[] results = invokeRetrievePageDetails(server, beanName);
        assertEquals("The number of PageDetails objects should be 1",
                1, results.length);
        assertSame("Is the page details object the one I inserted",
                pageDetails, results[0]);

        // call the flushPageDetails method
        try {
            server.invoke(beanName, "flushPageDetails", null, null);
            // success
        } catch (Exception e) {
            fail("Exception should not be thrown here");
        }

        // we have now flushed the MBean queue.
        results = invokeRetrievePageDetails(server, beanName);
        assertEquals("The number of PageDetails objects should be 0",
                0, results.length);
    }

    /**
     * utility method to retrive the PageDetails array.
     *
     * @param server the server to which the MBean belongs
     * @param beanName the name of the bean from which to retrieve the details.
     * @return the retrieved PageDetails[].
     */
    private PageDetails[] invokeRetrievePageDetails(MBeanServer server,
                                                    ObjectName beanName) {
        try {
            PageDetails[] result = (PageDetails[]) server.invoke(
                    beanName, "retrievePageDetails", null, null);
            return result;
            // success
        } catch (Exception e) {
            fail("Error invoking retrievePageDetails");
        }
        return null;
    }

    /**
     * Perform tests to ensure that a valid CanvasDetails object is returned.
     * @throws Exception
     */
    public void testCanvasDetails() throws Exception {
        PageTrackerFactory ptFactory = new JMXPageTrackerFactory();

        String title = "myTitle";
        CanvasType canvasType = CanvasType.INCLUSION;
        String themeName = "myTheme";
        String layoutName = "layoutName";

        // use factory to create CanvasDetails object
        CanvasDetails canvasDetails = ptFactory.createCanvasDetails(title,
                canvasType, themeName, layoutName);

        // compare to the parameters passed to the factory.
        DefaultCanvasDetailsTestCase.doComparison(canvasDetails, title,
                canvasType, themeName, layoutName);
    }

    /**
     * Perform tests to ensure that a valid Page details object is returned
     * @throws Exception
     */
    public void testPageDetails() throws Exception {

        PageTrackerFactory ptFactory = new JMXPageTrackerFactory();
        String title = "myTitle";
        CanvasType canvasType = CanvasType.INCLUSION;
        String themeName = "myTheme";
        String layoutName = "layoutName";
        String deviceName = "fakeDevice";

        // use factory to create CanvasDetails object
        CanvasDetails canvasDetails = ptFactory.createCanvasDetails(title,
                canvasType, themeName, layoutName);
        PageDetails pageDetails =
                ptFactory.createPageDetails(canvasDetails, deviceName);
        DefaultPageDetailsTestCase.
                doComparison(pageDetails, canvasDetails, deviceName);
    }


    /**
     * Ensure that the JMXPageTrackerFactory returns the same instance
     * of the PageDetailsManager each time it is called.
     * @throws Exception
     */
    public void testPageDetailsManager() throws Exception {
        PageTrackerFactory ptFactory = new JMXPageTrackerFactory();
        PageDetailsManager manager1 = ptFactory.createPageDetailsManager();
        PageDetailsManager manager2 = ptFactory.createPageDetailsManager();
        assertSame("The two PageDetailsManager objects returned should be the same",
                manager1, manager2);


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
