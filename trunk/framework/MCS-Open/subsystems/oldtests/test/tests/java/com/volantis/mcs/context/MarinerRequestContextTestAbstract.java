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
package com.volantis.mcs.context;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.internal.DefaultInternalApplicationContextFactory;
import com.volantis.mcs.runtime.TestableVolantis;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.servlet.DefaultServletApplicationContextFactory;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.environment.SimpleEnvironmentInteractionTracker;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.ServletContextStub;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;

/**
 * This class provides the framework for all underlying test cases for the
 * MarinerRequestContext hierarchy.
 */
public abstract class MarinerRequestContextTestAbstract
        extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Encoding manager is slow to initialise so do it only once.
     */
    protected static EncodingManager encodingManager = new EncodingManager();

    /**
     * The Volantis bean needed by the tests.
     */
    protected Volantis volantis;

    /**
     * The servlet context used for testing.
     */
    protected ServletContextStub servletContext;

    // javadoc inherited
    protected MarinerRequestContextTestAbstract(String title) {
        super(title);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        // Register the default application.
        ApplicationRegistry ar = ApplicationRegistry.getSingleton();
        ApplicationRegistryContainer arc =
                new ApplicationRegistryContainer(
                        new DefaultInternalApplicationContextFactory(),
                        new DefaultServletApplicationContextFactory());

        ar.registerApplication(
                ApplicationRegistry.DEFAULT_APPLICATION_NAME, arc);

        servletContext = new ServletContextStub();
        volantis = new TestableVolantis() {
            // Javadoc inherited.
            public MarinerPageContext createMarinerPageContext() {
                return new TestMarinerPageContext();
            }
        };
    }

    /**
     * Helper method that sets up the contexts and creates and sets an
     * expression context into the environment context.
     * @return the MarinerPageContext
     */
    protected TestMarinerPageContext createContexts() throws Exception {
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        pageContext.setVolantis(volantis);
        TestEnvironmentContext envContext = new TestEnvironmentContext();

        DefaultDevice dev = new DefaultDevice(null, null, null);
        dev.setName("myDevice");
        dev.setPolicyValue("protocol", "XHTMLBasic");
        MarinerSessionContext session = envContext.getSessionContext();
        InternalDevice internalDevice =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(dev);
        session.setDevice(internalDevice);
        pageContext.setDevice(session.getDevice());

        // Create an expression context for the environment context.
        final ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        final EnvironmentInteractionTracker simpleTracker =
                new SimpleEnvironmentInteractionTracker();
        final ExpressionContext exprContext = factory.
                createExpressionContext(simpleTracker,
                        new DefaultNamespacePrefixTracker());

        // Associate the expression context with the environment context.
        envContext.setExpressionContext(exprContext);

        // Create the initial request context.
        MarinerRequestContext requestContext =
                createInitialRequestContext(envContext);

        ApplicationContext appContext = new ApplicationContext(requestContext);
        appContext.setEncodingManager(encodingManager);

        pageContext.pushRequestContext(requestContext);
        ContextInternals.setEnvironmentContext(requestContext, envContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setApplicationContext(requestContext, appContext);

        return pageContext;
    }

    /**
     * Creates an initial MarinerRequestContext.
     * @param initialEnvContext the environment context to use
     * @return the new MarinerRequestContext
     * @throws Exception if there were problems creating the reuqest context
     */
    protected abstract MarinerRequestContext
            createInitialRequestContext(EnvironmentContext initialEnvContext)
            throws Exception;

    /**
     * Helper method that creates the nested context and tests that an
     * expression context is copied.
     */
    protected void doNestedContextsWithExpression() throws Exception {
        // Get the page context for the test.
        TestMarinerPageContext pageContext = createContexts();

        // Get the initial environment and expression contexts from the page
        // context.
        final TestEnvironmentContext initialEnvContext =
                (TestEnvironmentContext) pageContext.getEnvironmentContext();
        final ExpressionContext initialExprContext =
                initialEnvContext.getExpressionContext();

        // Retrieve the initial MarinerRequestContext.
        final MarinerRequestContext initialRequestContext =
                pageContext.getRequestContext();

        assertSame("Expression context should be copied to first nested " +
                "context",
                initialRequestContext.getEnvironmentContext().
                getExpressionContext(),
                initialExprContext);

        // Initially, the  expression context is associated with the page
        // context's request context.
        assertSame("MRC class property should be the same for the page " +
                "context's request context",
                initialExprContext.getProperty(MarinerRequestContext.class),
                initialRequestContext);

        // Create the first nested context.
        MarinerRequestContext nestedContext1 =
                initialRequestContext.createNestedContext();

        // Test that the initial expression context has been copied to the
        // first nested context.
        assertSame("Expression context should be copied to first nested " +
                "context",
                nestedContext1.getEnvironmentContext().getExpressionContext(),
                initialExprContext);

        // The expression context is now associated with the
        // MarinerServletRequestContext.
        assertSame("MRC class property should be the same on first nested " +
                "context",
                initialExprContext.getProperty(MarinerRequestContext.class),
                nestedContext1);

        // Create the second nested context.
        MarinerRequestContext nestedContext2 =
                nestedContext1.createNestedContext();

        // Test that the initial expression context has been copied to the
        // second nested context.
        assertSame("Expression context should be copied to second nested " +
                "context",
                nestedContext2.getEnvironmentContext().getExpressionContext(),
                initialExprContext);

        // The expression context is now associated with the second nested
        // context.
        assertSame("MRC class property should be the same on second nested " +
                "context",
                initialExprContext.getProperty(MarinerRequestContext.class),
                nestedContext2);

        // Clean up the context's resources; in particular the expression
        // context should be "reset".
        nestedContext2.release();

        // After releasing, the expression context should now be re-associated
        // with the first nested context.
        assertSame("MRC class property should be the same on first nested " +
                "context after second nested context release",
                initialExprContext.getProperty(MarinerRequestContext.class),
                nestedContext1);

        // Clean up the context's resources; in particular the expression
        // context should be "reset".
        nestedContext1.release();

        // After releasing, the expression context should now be re-associated
        // with the internal request context.
        assertSame("MRC class property should be the same on " +
                "MarinerServletRequestContext after first nested release",
                initialExprContext.getProperty(MarinerRequestContext.class),
                initialRequestContext);
    }
}
