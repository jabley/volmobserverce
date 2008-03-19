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
package com.volantis.mcs.eclipse.common;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.net.URL;

/**
 * Tests {@link EclipseLogAppender}.
 */
public class EclipseLogAppenderTestCase extends TestCaseAbstract {
    // javadoc unnecessary
    public void testAppendNoILog() throws Exception {
        EclipseLogAppender appender = createEclipseLogAppender();

        try {
            appender.append(
                    new LoggingEvent("category",
                                     new Logger("category") {},
                                     Priority.ERROR,
                                     "error message",
                                     null));

            fail("Should have raised an exception");
        } catch (Exception e) {
            // Expected case
        }
    }

    // javadoc unnecessary
    public void testAppendFatal() throws Exception {
        doTestAppend(Priority.FATAL, Status.ERROR);
    }

    // javadoc unnecessary
    public void testAppendError() throws Exception {
        doTestAppend(Priority.ERROR, Status.ERROR);
    }

    // javadoc unnecessary
    public void testAppendWarn() throws Exception {
        doTestAppend(Priority.WARN, Status.WARNING);
    }

    // javadoc unnecessary
    public void testAppendInfo() throws Exception {
        doTestAppend(Priority.INFO, Status.INFO);
    }

    // javadoc unnecessary
    public void testAppendDebug() throws Exception {
        doTestAppend(Priority.DEBUG, Status.INFO);
    }

    /**
     * Shared code for testing the {@link EclipseLogAppender#append} method
     * with different priorities.
     *
     * @param priority the priority of log event to append
     * @param expectedSeverity
     *                 the expected Eclipse log severity
     */
    private void doTestAppend(final Priority priority,
                              final int expectedSeverity) {
        EclipseLogAppender appender = createEclipseLogAppender();
        final boolean[] called = new boolean[1];
        called[0] = false;
        LoggingEvent event = new LoggingEvent("category",
                                              new Logger("category") {},
                                              priority,
                                              "message",
                                              null);
        ILog log = new ILog() {
            // javadoc inherited
            public void addLogListener(ILogListener listener) {
            }

            // javadoc inherited
            public Bundle getBundle() {
                return new Bundle() {
                    // javadoc inherited
                    public int getState() {
                        return 0;
                    }

                    // javadoc inherited
                    public void start() throws BundleException {
                    }

                    // javadoc inherited
                    public void stop() throws BundleException {
                    }

                    // javadoc inherited
                    public void update() throws BundleException {
                    }

                    // javadoc inherited
                    public void update(InputStream in) throws BundleException {
                    }

                    // javadoc inherited
                    public void uninstall() throws BundleException {
                    }

                    // javadoc inherited
                    public Dictionary getHeaders() {
                        return null;
                    }

                    // javadoc inherited
                    public long getBundleId() {
                        return 0;
                    }

                    // javadoc inherited
                    public String getLocation() {
                        return null;
                    }

                    // javadoc inherited
                    public ServiceReference[] getRegisteredServices() {
                        return new ServiceReference[0];
                    }

                    // javadoc inherited
                    public ServiceReference[] getServicesInUse() {
                        return new ServiceReference[0];
                    }

                    // javadoc inherited
                    public boolean hasPermission(Object permission) {
                        return false;
                    }

                    // javadoc inherited
                    public URL getResource(String name) {
                        return null;
                    }

                    // javadoc inherited
                    public Dictionary getHeaders(String localeString) {
                        return null;
                    }

                    // javadoc inherited
                    public String getSymbolicName() {
                        return "plugin-name";
                    }

                    // javadoc inherited
                    public Class loadClass(String name) throws ClassNotFoundException {
                        return null;
                    }

                    // javadoc inherited
                    public Enumeration getEntryPaths(String path) {
                        return null;
                    }

                    // javadoc inherited
                    public URL getEntry(String name) {
                        return null;
                    }
                };
            }

            // javadoc inherited
            public void log(IStatus status) {
                called[0] = true;

                assertEquals("severity for " + priority.toString() + "not as",
                             expectedSeverity,
                             status.getSeverity());
            }

            // javadoc inherited
            public void removeLogListener(ILogListener listener) {
            }
        };

        appender.setLog(log);
        appender.append(event);

        assertTrue("Log call was not made",
                   called[0]);
    }

    /**
     * Returns a new testable appender instance.
     *
     * @return new testable appender instance
     */
    protected EclipseLogAppender createEclipseLogAppender() {
        return new EclipseLogAppender();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Nov-04	6272/1	philws	VBM:2004111803 Output error and fatal Log4J messages to the Eclipse Log

 ===========================================================================
*/
