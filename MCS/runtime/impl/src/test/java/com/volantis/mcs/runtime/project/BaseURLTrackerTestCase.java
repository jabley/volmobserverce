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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link BaseURLTracker}.
 */
public class BaseURLTrackerTestCase
        extends TestCaseAbstract {

    /**
     * Test that tracker works properly.
     *
     * @throws Exception
     */
    public void testTracking() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        BaseURLTracker tracker = new BaseURLTracker(null);
        assertNull("Base URL starts off as null", tracker.getBaseURL());
        assertTrue("Base URL must have changed",
                tracker.startElement("file:/a/b.xml"));
        {
            assertEquals("Current URL", new MarinerURL("file:/a/b.xml"),
                    tracker.getBaseURL());
            assertFalse("Base URL has not changed",
                    tracker.startElement("file:/a/b.xml"));
            {
                assertEquals("Current URL", new MarinerURL("file:/a/b.xml"),
                        tracker.getBaseURL());
                assertTrue("Base URL must have changed",
                        tracker.startElement("file:/q/r/s.xml"));
                {
                    assertEquals("Current URL", new MarinerURL(
                            "file:/q/r/s.xml"),
                            tracker.getBaseURL());
                }
                tracker.endElement("file:/q/r/s.xml");

                assertEquals("Current URL", new MarinerURL("file:/a/b.xml"),
                        tracker.getBaseURL());
            }
            tracker.endElement("file:/a/b.xml");

            assertEquals("Current URL", new MarinerURL("file:/a/b.xml"),
                    tracker.getBaseURL());
        }
        tracker.endElement("file:/a/b.xml");

        assertNull("Base URL ends off as null", tracker.getBaseURL());
    }

    /**
     * Ensure that the tracker detects a mismatch between the system ids
     * provided on matching start and end elements.
     */
    public void testDetectStartEndMismatch() {
        BaseURLTracker tracker = new BaseURLTracker(null);
        tracker.startElement("file:/a/b.xml");
        try {
            tracker.endElement("file:/q/r/s.xml");
            fail("Did not detect mismatch between start and end system ids");
        } catch (IllegalStateException expected) {

        }
    }

    /**
     * Test normalisation of URL's
     */
    public void testURLNormalisation() {
        BaseURLTracker tracker = new BaseURLTracker(null);
        tracker.startElement(
                "file:/opt/jboss-4.0.2/server/default/tmp/deploy/" +
                "tmp23758mcs.war/welcome/welcome.xdime");
        //This URL should be the same even with the extra ./
        tracker.endElement(
                "file:/opt/jboss-4.0.2/server/default/./tmp/deploy/" +
                "tmp23758mcs.war/welcome/welcome.xdime");
    }

}
