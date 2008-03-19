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
package com.volantis.mcs.servlet.http.proxy;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestCookie;

/**
 * Tests the functionality of the target HTTP session.
 */
public class TargetHTTPProxySessionTestCase extends TestCaseAbstract {
    /**
     * Tests that state change handling works correctly.
     */
    public void testStateChangeCallback() throws Exception {
        final TargetHTTPProxySession session = new TargetHTTPProxySession();

        TestSessionStateChangeHandler handler =
                new TestSessionStateChangeHandler() ;

        session.setSessionStateChangeHandler(handler);

        // Ensure that state-changing methods inform notifier.
        handler.reset();
        session.invalidate();
        assertEquals("invalidate() should trigger change notification", 1,
                handler.getChanges());

        handler.reset();
        session.setEncoding(HTTPProxySessionEncodingEnum.COOKIE);
        assertEquals("setEncoding() should trigger change notification", 1,
                handler.getChanges());

        handler.reset();
        session.setSessionIDParam("test");
        assertEquals("setSessionIdParam() should trigger change notification",
                1, handler.getChanges());

        handler.reset();
        session.receivedSessionIDPath("test");
        assertEquals("receivedSessionIDPath() should trigger change " +
                "notification", 1, handler.getChanges());

        handler.reset();
        session.receivedSessionIDParam("test");
        assertEquals("receivedSessionIDParam() should trigger change " +
                "notification", 1, handler.getChanges());

        handler.reset();
        WebRequestCookie cookie = new WebRequestCookie();
        cookie.setName("henry");
        cookie.setValue("cookie");
        session.receivedSessionIDCookie(cookie);
        assertEquals("receivedSessionIDCookie() should trigger change " +
                "notification", 1, handler.getChanges());

        handler.reset();
        session.encodeUrl(new StringBuffer("http://www.hamster-rock.com/"));
        assertEquals("encodeURL() should trigger change notification", 1,
                handler.getChanges());

        // Ensure that state preserving changes do not trigger notification.
        handler.reset();
        session.toString();
        assertEquals("toString() should not trigger change notification", 0,
                handler.getChanges());
    }

    /**
     * Simple change handler for checking notification takes place the right
     * number of times.
     */
    private class TestSessionStateChangeHandler
            implements SessionStateChangeHandler {
        /**
         * The number of changes that have been notified.
         */
        int changes = 0;

        /**
         * Reset the change count to 0.
         */
        public void reset() {
            changes = 0;
        }

        /**
         * Retrieve the number of changes.
         *
         * @return The number of changes
         */
        public int getChanges() {
            return changes;
        }

        /**
         * Increment the number of changes logged when the session state
         * changes.
         *
         * @param changedObject The object that changed in the session
         */
        public void sessionStateChanged(Object changedObject) {
            changes += 1;
        }
    }
}
