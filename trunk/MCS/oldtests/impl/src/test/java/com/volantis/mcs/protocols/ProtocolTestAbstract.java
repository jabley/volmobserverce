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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/ProtocolTestAbstract.java,v 1.2 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Jan-03    Geoff           VBM:2003012101 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.context.CurrentProjectProviderMock;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.servlet.ServletEnvironmentContext;
import com.volantis.mcs.testtools.request.mocks.MockMarinerServletRequestContext;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mocks.MockHTTPServletRequest;
import com.volantis.testtools.mocks.MockHTTPServletResponse;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import junit.framework.TestCase;

import javax.servlet.http.HttpServletRequest;

/**
 * Parent for all {@link VolantisProtocol} and it's subclasses; this defines
 * the structures which ensure the inheritance of test case fixtures works
 * properly.
 * <p>
 * Each subclass of this class must:
 * <ul>
 * <li>Implement the {@link #createTestableProtocol} method to return the 
 * subtype of {@link VolantisProtocol} that it is testing.
 * <li>Override the {@link #setTestableProtocol} method, saving a reference to
 * the created protocol into an instance variable and calling the superclass
 * version as well to allow it to get a reference to the created class.
 * <li>Avoid redefining the {@link TestCase#setUp} method since the behaviour
 * already defined here is required for this to all work.
 * </ul>
 * <p>
 * Note that it is not possible for subclasses to use the 
 * {@link TestCase#setUp} method as normal since each test case must set up
 * independently of it's subclasses, and there is no way to model this with a
 * protected method, even given the super() method.  
 */ 
public abstract class ProtocolTestAbstract extends TestCaseAbstract {

    protected InternalDevice internalDevice;

    public ProtocolTestAbstract(String s) {
        super(s);
    }

    /**
     * The {@link TestCase#setUp} for <b>all</b> test cases in the heirarchy.
     * 
     * Note that one cannot currently include protocol configuration code
     * in here as each TestCase expects the protocol to be configured 
     * differently.
     */ 
    protected void setUp() throws Exception {
        super.setUp();

        internalDevice = InternalDeviceTestHelper.createTestDevice();

        // All test cases need a common way of creating the protocol they are
        // to test against.
        VolantisProtocol protocol = createTestableProtocol(internalDevice);
        // All test cases need to have a reference to the protocol once it is
        // created.
        VolantisProtocolTestable testable = (VolantisProtocolTestable) protocol;
        setTestableProtocol(protocol, testable);
        // Prevent any null pointers when using devices or css emulation
        // as these will be delegated to the page context
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.setDeviceName("Unknown Device");
        context.setDevice(internalDevice);
        MarinerRequestContext requestContext;

//        Volantis volantisBean = new Volantis();
        requestContext = initialiseMarinerRequestContext(expectations);

        context.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(context);
        protocol.initialise();
        context.setProtocol(protocol);
    }

    public static MarinerRequestContext initialiseMarinerRequestContext(
            ExpectationBuilder expectations) {
        MarinerServletRequestContext requestContext;
        HttpServletRequest servletRequest = new MockHTTPServletRequest();

        requestContext = new MockMarinerServletRequestContext(servletRequest, new MockHTTPServletResponse());

        Project project = new RuntimeProjectMock("projectMock", expectations);
//        requestContext.pushProject(project);

        EnvironmentContext environmentContext = new ServletEnvironmentContext(
                requestContext);
        ContextInternals.setEnvironmentContext(requestContext,
                environmentContext);

        ExpressionFactory expressionFactory = ExpressionFactory.getDefaultInstance();

        ExpressionContext expressionContext =
                expressionFactory.createExpressionContext(
                        null,
                        NamespaceFactory.getDefaultInstance()
                        .createPrefixTracker());

        expressionContext.setProperty(MarinerRequestContext.class,
                                      requestContext, false);

        final CurrentProjectProviderMock projectProviderMock =
                new CurrentProjectProviderMock("projectProviderMock",
                        expectations);
        projectProviderMock.expects.getCurrentProject().returns(project).any();

        // Add a project provider in for resolving relative policy expressions
        // without a project into absolute IDs containing a project.
        expressionContext.setProperty(CurrentProjectProvider.class,
                projectProviderMock, false);

        environmentContext.setExpressionContext(expressionContext);
        return requestContext;
    }

    /**
     * Create the protocol that is to be tested. 
     * <p>
     * Implementations of this method must not call super().
     * 
     * @return a protocol to be tested.
     * @param internalDevice
     */ 
    protected abstract VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice);

    /**
     * Save a reference to the protocol that is to be tested.
     * <p>
     * Implementations of this method must call super().
     * 
     * @param protocol the protocol that has been created.
     * @param testable the testable interface of the protocol that has been 
     *      created.
     */ 
    protected abstract void setTestableProtocol(VolantisProtocol protocol, 
            VolantisProtocolTestable testable);

    /**
     * Create a set of mutable property values.
     *
     * @return A newly created set of mutable property values.
     */
    protected MutablePropertyValues createPropertyValues() {
        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        return StylingFactory.getDefaultInstance()
                .createPropertyValues(definitions);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 ===========================================================================
*/
