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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl.osgi;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.method.MethodIdentifier;
import mock.org.osgi.framework.BundleContextMock;

import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class ManagedServiceFactoryTestCase extends TestCaseAbstract {

    /**
     * Override to return the ManagedServiceFactory to test
     *
     * @return the ManagedServiceFactory to test.
     */
    public ManagedServiceFactory createManagedServiceFactory() throws NoSuchMethodException {

        final BundleContextMock contextMock =
            new BundleContextMock("contextMock", expectations);
        final MockFactory mockFactory = MockFactory.getDefaultInstance();
        final Method registerServiceMethod = BundleContext.class.getMethod(
            "registerService",
            new Class[]{String.class, Object.class, Dictionary.class});
        final MethodIdentifier identifier =
            MethodIdentifier.getMethodIdentifier(registerServiceMethod);
        contextMock.fuzzy.registerService(
            identifier,
            EventHandler.class.getName(),
            mockFactory.expectsInstanceOf(ReportEventHandler.class),
            mockFactory.expectsInstanceOf(Dictionary.class)).returns(null).any();

        return new AbstractManagedServiceFactory(contextMock) {

            protected ReportEventHandler createEventHandler(
                Dictionary configuration)
                throws ConfigurationException {
                ReportEventHandler result = new ReportEventHandler() {


                    public void setConfiguration(Dictionary configuration)
                    throws ConfigurationException {
                        
                    }

                    public void handleEvent(Event event) {

                    }
                };
                result.setConfiguration(configuration);
                return result;
            }


            public String getName() {
                return "My name is Earl";
            }
        };
    }

    public void testCreateManagedServiceFactory() throws Exception {
        ManagedServiceFactory sf = createManagedServiceFactory();


        assertNotNull("name should not be null", sf.getName());
        {
            try {
                sf.updated("bob", null);
                fail("An Exception should have been thrown");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }

        {
            try {
                sf.updated(null, new Hashtable());
                fail("An Exception should have been thrown");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
        {
            try {
                sf.deleted(null);
                fail("An Exception should have been thrown");
            } catch (IllegalArgumentException iae) {
                // success
            }
        }
    }

}
