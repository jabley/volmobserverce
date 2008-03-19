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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.layout;

import com.volantis.mcs.context.FormatReferenceFinderMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.expression.FormatReferenceValue;
import com.volantis.mcs.expression.functions.request.ExpressionFunctionTestAbstract;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.FormatReferenceMock;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.functions.AbstractFunction;


/**
 * Tests the servlet request function for an XPath value like so:
 * <pipeline:expr value="%{layout:getPaneInstance("fred",1,2,3)}"/>
 */
public class GetIteratedFormatInstanceFunctionTestCase
        extends ExpressionFunctionTestAbstract {

    static String FUNCTION_NAME = "getPaneInstance";

    static String FUNCTION_QNAME = "layout:getPaneInstance";

    /**
     * The page context
     */
    private MarinerPageContextMock pageContextMock;

    private FormatReferenceFinderMock formatReferenceFinderMock;
    private FormatReferenceMock formatReferenceMock;

    // javadoc inherited
    protected String getFunctionQName() {
        return FUNCTION_QNAME;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FUNCTION_NAME;
    }

    // javadoc inherited
    protected String getURI() {
        return LAYOUT_URI;
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        pageContextMock = new MarinerPageContextMock("pageContextMock",
                expectations);

        formatReferenceFinderMock = new FormatReferenceFinderMock(
                "formatReferenceFinderMock", expectations);

        formatReferenceMock = new FormatReferenceMock(
                "formatReferenceMock", expectations);

        requestContextMock.expects.getMarinerPageContext()
                .returns(pageContextMock).any();
        pageContextMock.expects.getRequestContext()
                .returns(requestContextMock).any();

        pageContextMock.expects.getFormatReferenceFinder()
                .returns(formatReferenceFinderMock).any();
    }

    /**
     * Test invoke with a missing entity and no default.
     *
     * @throws Exception
     */
    public void testInvokeMissing() throws Exception {
        Expression expression = parser.parse(getFunctionQName() + "('name')");

        formatReferenceFinderMock.expects
                .getFormatReference("name", new int[]{})
                .returns(formatReferenceMock);

        doTestFunction(expression);
    }

    /**
     * Name and single index
     */
    public void testInvokeSingleIndex() throws Exception {
        Expression expression = parser.parse(getFunctionQName() + "('name',1)");

        formatReferenceFinderMock.expects
                .getFormatReference("name", new int[]{1})
                .returns(formatReferenceMock);

        doTestFunction(expression);
    }

    /**
     * Name and multiple indexes
     */
    public void testInvokeMultipleIndexes() throws Exception {
        Expression expression =
                parser.parse(getFunctionQName() + "('name',1,2,3,4)");

        formatReferenceFinderMock.expects
                .getFormatReference("name", new int[]{1,2,3,4})
                .returns(formatReferenceMock);

        doTestFunction(expression);
    }

    private void doTestFunction(Expression expression)
            throws ExpressionException {
        Value result = expression.evaluate(expressionContext);

        assertTrue("Result not a format reference",
                result instanceof FormatReferenceValue);

        FormatReference ref = ((FormatReferenceValue) result).asFormatReference();
        assertSame(formatReferenceMock, ref);
    }

    // javadoc inheritied
    protected AbstractFunction
            createTestableFunction(ExpressionFactory factory) {
        return new AbstractPaneInstanceFunction();
    }

    private class AbstractPaneInstanceFunction
            extends AbstractFunction {

        GetIteratedFormatInstanceFunction func;

        public AbstractPaneInstanceFunction() {
            func = new GetIteratedFormatInstanceFunction(FormatNamespace.PANE);
        }

        public String getFunctionName() {
            return FUNCTION_NAME;
        }

        public Value invoke(ExpressionContext context, Value[] values)
                throws ExpressionException {
            return func.invoke(context, values);
        }
    }


    /**
     * These have no relavence to this test
     */
    public void testDefaultInvokeWithNumericPredicateOutOfBounds()
            throws Exception {
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 08-Jan-04	2461/2	steve	VBM:2003121701 Patch pane name changes from Proteus2

 07-Jan-04	2389/1	steve	VBM:2003121701 Enhanced pane referencing

 ===========================================================================
*/
