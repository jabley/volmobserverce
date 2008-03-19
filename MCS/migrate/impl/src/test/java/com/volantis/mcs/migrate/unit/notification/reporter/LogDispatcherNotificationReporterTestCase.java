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
package com.volantis.mcs.migrate.unit.notification.reporter;

import com.volantis.mcs.migrate.impl.notification.SimpleMessageNotification;
import com.volantis.mcs.migrate.impl.notification.reporter.LogDispatcherNotificationReporter;
import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.synergetics.log.LogDispatcherMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link com.volantis.mcs.migrate.impl.notification.reporter.LogDispatcherNotificationReporter}.
 * <p/>
 * </p>
 */
public class LogDispatcherNotificationReporterTestCase  extends TestCaseAbstract {

    private ExpectationBuilder expectations;

    private LogDispatcherMock loggerMock;

    protected void setUp() throws Exception {
        expectations = mockFactory.createUnorderedBuilder();
        loggerMock = new LogDispatcherMock("loggerMock", expectations);
    }

    /**
     * Test reporting "Error"
     */
    public void testReportError()
            throws Exception {

        // ==================================================================
        // Create mocks.
        // ==================================================================
        //
        // ==================================================================
        // Create expectations.
        // ==================================================================
        loggerMock.expects.isErrorEnabled().returns(true);
        loggerMock.expects.error("error-notification-reporter","message");
        // ==================================================================
        // Do the test.
        // ==================================================================

        LogDispatcherNotificationReporter reporter = new LogDispatcherNotificationReporter(loggerMock);
        Notification notification = new SimpleMessageNotification(NotificationType.ERROR, "message");
        reporter.reportNotification(notification);
    }

    /**
     * Test reporting "Info"
     */
    public void testReportInfo()
            throws Exception {

        // ==================================================================
        // Create mocks.
        // ==================================================================
        //
        // ==================================================================
        // Create expectations.
        // ==================================================================
        loggerMock.expects.isInfoEnabled().returns(true);
        loggerMock.expects.info("info-notification-reporter","message");
        // ==================================================================
        // Do the test.
        // ==================================================================

        LogDispatcherNotificationReporter reporter = new LogDispatcherNotificationReporter(loggerMock);
        Notification notification = new SimpleMessageNotification(NotificationType.INFO, "message");
        reporter.reportNotification(notification);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - post-review amendments: new reporter type

 15-Nov-05	10098/2	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 ===========================================================================
*/
