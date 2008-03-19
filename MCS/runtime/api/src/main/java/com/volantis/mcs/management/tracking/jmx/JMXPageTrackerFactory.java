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

import com.volantis.mcs.management.tracking.PageDetailsManager;
import com.volantis.mcs.management.tracking.PageDetails;
import com.volantis.mcs.management.tracking.CanvasDetails;
import com.volantis.mcs.management.tracking.DefaultPageDetails;
import com.volantis.mcs.management.tracking.CanvasType;
import com.volantis.mcs.management.tracking.DefaultCanvasDetails;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.management.tracking.PageTrackerException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.management.JMRuntimeException;
import javax.management.JMException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.RuntimeOperationsException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * JMX Implementation of the PageDetailsManagerFactory. This factory produces a
 * PageTracker. PageTracker is a Standard MBean that this factory registers with
 * a MBeanServer instance. This allows JMX management functionality to be
 * exposed.
 */
public class JMXPageTrackerFactory implements PageTrackerFactory {


    /**
     * The JMX domain to use.
     */
    public static final String JMX_DOMAIN = "com.volantis.mcs";

    /**
     * The MBean name to use for the Tracker bean.
     */
    public static final String PAGE_TRACKER_NAME =
            JMX_DOMAIN + ":name=PageTracker";

    /**
     * synchronization should be performed on this object.
     */
    private Object mutex = new Object();

    /**
     * reference to the page details manager.
     */
    private PageDetailsManager manager = null;

    /**
     * reference to our MBeanServer.
     */
    private MBeanServer server = null;

    /**
     * Hold the name used to refer to the PageTrackerMBean
     */
    private ObjectName beanName;

    /**
     * javadoc inherited
     */
    public PageDetails createPageDetails(CanvasDetails canvasDetails,
                                         String deviceName) {
        // null parameter checks are performed be DefaultPageDetails class
        return new DefaultPageDetails(canvasDetails, deviceName);
    }

    /**
     * javadoc inherited.
     */
    public CanvasDetails createCanvasDetails(String title, CanvasType canvasType,
                                             String themeName, String layoutName) {
        // null parameter checks are performed be DefaultCanvasDetails class
        return new DefaultCanvasDetails(title, canvasType, themeName, layoutName);
    }

    /**
     * This is a thread safe implementation of createPageDetails. This method
     * creates an MBeanServer with domain <code>JMX_DOMAIN</code>. It then
     * creates a PageDetailsManager, registers it with the MBeanServer and
     * returns it.
     *
     * //rest of javadoc inherited.
     * @return a PageDetailsManager that is also registered as a PageTrackerMBean
     * with its own MBeanServer.
     * @throws PageTrackerException if an error occurs creating an MBeanServer,
     * or the PageTrackerBean cannot be registered with the server, or if the
     * server has been deregistered from the MBeanServerFactory.
     */
    public PageDetailsManager createPageDetailsManager()
            throws PageTrackerException {

        synchronized (mutex) {
            try {
                if(beanName==null) { // create our name
                    beanName = new ObjectName(PAGE_TRACKER_NAME);
                }
                // do the stuff
                initMBeanServer();
            } catch (PageTrackerException pte) {
                // catch any page tracker exceptions so that we can
                // unset the manager and server
                manager = null;
                server = null;
                throw pte; // rethrow the exception
            } catch (MalformedObjectNameException mone) {
                throw new PageTrackerException("The ObjectName provided is malformed:" +
                        " " + PAGE_TRACKER_NAME, mone);
            }
            return manager;
        }
    }

    /**
     * Initialise the volantis MBeanServer. If it does not exist or
     * is not registered with the MBeanServer factory then create
     * a new
     */
    private void initMBeanServer() throws PageTrackerException {
        if (server == null) {
            // create a registered MBeanServer
            try {
                server = MBeanServerFactory.createMBeanServer(JMX_DOMAIN);
                // thats right. There is no common base class for the
                // exceptions this throws so catch them all.
            } catch (SecurityException se) {
                throw new PageTrackerException("You do not have permission to" +
                        "create an MBeanServer", se);
            } catch (JMRuntimeException jre) {
                throw new PageTrackerException("Could not create MBeanServer. " +
                        "This can occur for two reasons.\n1) the property " +
                        "\"javax.management.builder.initial\" is set but " +
                        "the specified class cannot be instanciated.\n2) " +
                        "The instanciated class is returning null from " +
                        "calls to its \"newMBeanServerDelegate\" or" +
                        "\"newMBeanServer\" method.", jre);
            } catch (ClassCastException cce) {
                throw new PageTrackerException("The class specified in " +
                        "\"javax.management.builder.initial\" is" +
                        "not assignment compatible with MBeanServerBuilder",
                        cce);
            }
        } else {
            // if our MBeanServer is not registered then throw an exception
            // as someone else has deregistered it.
            if (!isMBeanServerRegistered(server)) {
                String errorMessage = "The Volantis MBeanServer does not " +
                        "appear to be registered with the factory. It is " +
                        "likely that someone else has deregistered it.";
                throw new PageTrackerException(errorMessage);
            }
        }

        registerBeanWithServer();
    }

    /**
     * Create a PageTracker object and register it with the server that
     * was found/created.
     * @throws PageTrackerException if an error occurs when registering the
     * PageTrackerBean with the MBeanServer
     */
    private void registerBeanWithServer()
            throws PageTrackerException {
        try {

            // create a new PageTracker if we don't have one registered
            if (manager == null) {
                manager = new PageTracker();
            }

            if (!server.isRegistered(beanName)) {
                server.registerMBean(manager, beanName);
            }
            // catch all the exceptions known to man and provide helpful
            // messages. PageTrackerExceptions from isRegistered are passed
            // through
            // @todo later internationalize these
        } catch (InstanceAlreadyExistsException iaee) {
            throw new PageTrackerException("Could not register bean: " + manager +
                    " (" + beanName + ") with server " + server +
                    " as instance already exists", iaee);
        } catch (MBeanRegistrationException mbre) {
            throw new PageTrackerException("Could not register bean: " + manager +
                    " (" + beanName + ") with server " + server +
                    " as preregister threw an exception", mbre);
        } catch (NotCompliantMBeanException ncmbe) {
            throw new PageTrackerException("Could not register object: " + manager +
                    " (" + beanName + ") with server " + server +
                    " as the object is not a complient MBean", ncmbe);
        } catch (RuntimeOperationsException roe) {
            throw new PageTrackerException("Could not register bean: " + manager +
                    " (" + beanName + ") with server " + server +
                    " as an illegal argument was provided.", roe);
        }
    }


    /**
     * Return true if the specified MBeanServer is registered with the
     * MBeanServerFactory.
     *
     * @param server The server to find.
     * @return true if the server is one of those registered with the
     * MBeanServerFactory. False otherwise.
     * @throws PageTrackerException if you do not have permission to
     * query the MBeanServerFactory.
     */
    private static boolean isMBeanServerRegistered(MBeanServer server)
            throws PageTrackerException {
        // ensure the server is still registered with the
        // MBeanServerFactory by searching though all registered Servers
        ArrayList servers = null;
        try {
            servers = MBeanServerFactory.findMBeanServer(null);
        } catch (SecurityException se) {
            throw new PageTrackerException("You do not have permission to " +
                    "execute the findMBeanServer method.", se);
        }

        Iterator serverIt = servers.listIterator();
        boolean foundServer = false;
        while (serverIt.hasNext()) {
            if (serverIt.next() == server) {
                foundServer = true;
                break;
            }
        }
        return foundServer;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 01-Jul-04	4702/7	matthew	VBM:2004061402 merge problems

 24-Jun-04	4702/5	matthew	VBM:2004061402 rework JMXPageTrackerFactory error handling

 21-Jun-04	4702/3	matthew	VBM:2004061402 rework PageTracking

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
