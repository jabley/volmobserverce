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
package com.volantis.xml.expression.functions;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespaceFactory;

/**
 * Unit test for the {@link AbstractFunction} class
 */
public class AbstractFunctionTestCase
        extends TestCaseAbstract {

    /**
     * Branding namespace URI
     */
    private static final String BRANDING_URI =
        "http://www.volantis.com/xmlns/mcs/branding";

    /**
     * Request namespace URI
     */
    private static final String REQUEST_URI =
        "http://www.volantis.com/xmlns/mariner/request";

    /**
     * Device namespace URI
     */
    private static final String DEVICE_URI =
            "http://www.volantis.com/xmlns/mariner/device";

    /**
     * Layout namespace URI
     */
    private static final String LAYOUT_URI =
            "http://www.volantis.com/xmlns/mariner/layout";


    /**
     * ExpressionContext used in various tests
     */
    private ExpressionContext expressionContext;

    /**
     * ExpressionFactory for creating expression type objects
     */
    private ExpressionFactory expressionFactory;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        expressionFactory = ExpressionFactory.getDefaultInstance();

        expressionContext = expressionFactory.createExpressionContext(
                null,
                NamespaceFactory.getDefaultInstance().createPrefixTracker());

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


        expressionContext.registerFunction(
                new ImmutableExpandedName("test",
                        "test"),
                createTestableFunction());
    }

    // javadoc inherited
    protected void tearDown() throws Exception {

        expressionContext = null;

        super.tearDown();
    }

    /**
     * Create a concrete instance of a Function
     */
    private AbstractFunction createTestableFunction() {
        return new AbstractFunction() {
            protected String getFunctionName() {
                return "test";
            }

            public Value invoke(ExpressionContext context, Value[] arguments) {
                return null;
            }
        };
    }

    /**
     * Tests the {@link AbstractFunction#assertArgumentCount} method by
     * ensuring that an exception is NOT thrown if the value array is null
     * and the expected count is zero.
     * @throws Exception if an error occurs
     */
    public void testAssertArgumentCountNullArgs() throws Exception {
        try {
            // factor a function
            AbstractFunction function
                    = createTestableFunction();
            function.assertArgumentCount(null, 0);
        } catch (ExpressionException e) {
            fail("assertArgumentCount should not have thrown an exception");
        }
    }

    /**
     * Tests the {@link AbstractFunction#assertArgumentCount} method by
     * ensuring that an exception is thrown if a null value array and
     * non zero expected count is provided
     * @throws Exception if an error occurs
     */
    public void testAssertArgumentCountNullArgsInvalidExpected()
            throws Exception {
        try {
            // factor a function
            AbstractFunction function =
                    createTestableFunction();
            function.assertArgumentCount(null, 1);
            fail("assertArgumentCount should have thrown an exception");
        } catch (ExpressionException e) {
            // expected condition
        }
    }

    /**
     * Tests the {@link AbstractFunction#assertArgumentCount} method by
     * ensuring that an exception is thrown if the value array size differs
     * from the expected count provided
     * @throws Exception if an error occurs
     */
    public void testAssertArgumentCountInvalidExpected()
            throws Exception {
        try {
            // factor a function
            AbstractFunction function
                    = createTestableFunction();

            Value[] args = new Value[2];
            args[0] = expressionFactory.createStringValue("test");
            args[1] = expressionFactory.createStringValue("test");

            function.assertArgumentCount(args, 1);
            fail("assertArgumentCount should have thrown an exception");
        } catch (ExpressionException e) {
            // expected condition
        }
    }

    /**
     * Tests the {@link AbstractFunction#assertArgumentCount} method by
     * ensuring that an exception is NOT thrown if the value array size is the
     * same as the expected count
     * @throws Exception if an error occurs
     */
    public void testAssertArgumentCountValidExpected()
            throws Exception {
        try {
            // factor a function
            AbstractFunction function
                    = createTestableFunction();

            Value[] args = new Value[2];
            args[0] = expressionFactory.createStringValue("test");
            args[1] = expressionFactory.createStringValue("test");

            function.assertArgumentCount(args, 2);
        } catch (ExpressionException e) {
           fail("assertArgumentCount should not have thrown an exception");
        }
    }

    /**
     * Tests the {@link AbstractFunction#getFunctionName} method
     * @throws Exception if an error occurs
     */
    public void testGetFunctionName() throws Exception {
        // factor a function
        AbstractFunction function
                    = createTestableFunction();

        // ensure the correct name is returned
        assertEquals("getFunctionName() returned an incorrect name",
                "test",
                     function.getFunctionName());
    }
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
