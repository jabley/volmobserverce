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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions;

import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.context.CurrentProjectProviderMock;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.DevicePolicyValueAccessorMock;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespaceFactory;

/**
 * Unit test for the {@link AbstractFunction} class
 */
public abstract class AbstractFunctionTestAbstract
        extends TestCaseAbstract {

    /**
     * Branding namespace URI
     */
    private static final String BRANDING_URI =
        "http://www.volantis.com/xmlns/mcs/branding";

    /**
     * Request namespace URI
     */
    protected static final String REQUEST_URI =
        "http://www.volantis.com/xmlns/mariner/request";

    /**
     * Device namespace URI
     */
    protected static final String DEVICE_URI =
            "http://www.volantis.com/xmlns/mariner/device";

    /**
     * Layout namespace URI
     */
    protected static final String LAYOUT_URI =
            "http://www.volantis.com/xmlns/mariner/layout";

    /**
     * Service namespace URI
     */
    protected static final String SERVICE_URI =
            "http://www.volantis.com/xmlns/mariner/service";


    /**
     * Mock project.
     */
    private Project project;

    /**
     * ExpressionContext used in various tests
     */
    protected ExpressionContext expressionContext;

    /**
     * Parser for parsing expressions
     */
    protected ExpressionParser parser;

    protected DevicePolicyValueAccessorMock accessorMock;

    /**
     * ExpressionFactory for creating expression type objects
     */
    private ExpressionFactory expressionFactory;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        project = new RuntimeProjectMock("projectMock", expectations);

        expressionFactory = ExpressionFactory.getDefaultInstance();

        expressionContext = expressionFactory.createExpressionContext(
                null,
                NamespaceFactory.getDefaultInstance().createPrefixTracker());

        final CurrentProjectProviderMock projectProviderMock =
                new CurrentProjectProviderMock("projectProviderMock",
                        expectations);
        projectProviderMock.expects.getCurrentProject().returns(project).any();

        // Add a project provider in for resolving relative policy expressions
        // without a project into absolute IDs containing a project.
        expressionContext.setProperty(CurrentProjectProvider.class,
                projectProviderMock, false);

        expressionContext.getNamespacePrefixTracker().
                startPrefixMapping("branding",
                                   BRANDING_URI);

        expressionContext.getNamespacePrefixTracker().
                startPrefixMapping("request",
                                   REQUEST_URI);

        expressionContext.getNamespacePrefixTracker().
                startPrefixMapping("device",
                                   DEVICE_URI);

        expressionContext.getNamespacePrefixTracker().
                startPrefixMapping("layout",
                                   LAYOUT_URI);

        expressionContext.getNamespacePrefixTracker().
                startPrefixMapping("service",
                                   SERVICE_URI);


        expressionContext.registerFunction(
                new ImmutableExpandedName(getURI(),
                                          getFunctionName()),
                createTestableFunction(expressionFactory));

        accessorMock = new DevicePolicyValueAccessorMock(
                "accessorMock", expectations);

        // Allow XPath expressions access to the device
        expressionContext.setProperty(
                DevicePolicyValueAccessor.class, accessorMock,
                false);

        parser = expressionFactory.createExpressionParser();

    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        parser = null;

        expressionContext = null;

        super.tearDown();
    }

    /**
     * Create a concrete instance of a Function
     * @param factory The ExpressionFactory.
     */
    protected abstract AbstractFunction
            createTestableFunction(ExpressionFactory factory);

    /**
     * Provide the name of the function call and its prefix as a string.
     * @return the function prefix followed by the name e.g. request:getHeader
     */
    protected abstract String getFunctionQName();

    /**
     * Provide the URI for the function.
     * @return The URI for the function.
     */
    protected abstract String getURI();

    /**
     * Provide the name of the function.
     * @return the name of the function.
     */
    protected abstract String getFunctionName();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 12-Aug-04	5181/1	allan	VBM:2004081106 Support branding post MCS 3.0.

 08-Jan-04	2461/1	steve	VBM:2003121701 Patch pane name changes from Proteus2

 ===========================================================================
*/
