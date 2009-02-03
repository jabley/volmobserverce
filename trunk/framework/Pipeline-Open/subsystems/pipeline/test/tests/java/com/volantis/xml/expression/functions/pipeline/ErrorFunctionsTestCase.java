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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions.pipeline;

import java.util.Map;
import java.util.HashMap;

import com.volantis.xml.expression.functions.FunctionTestAbstract;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.impl.SimpleExpressionContext;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.impl.operations.tryop.TryProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.shared.environment.SimpleEnvironmentInteractionTracker;

/**
 * Test for {@link ErrorMessageFunction}.
 */
public class ErrorFunctionsTestCase extends FunctionTestAbstract {

    /**
    * Function.
    */
    private Function errorMessageFunction = new ErrorMessageFunction();
    private Function errorCodeNameFunction = new ErrorCodeNameFunction();
    private Function errorCodeURIfunction = new ErrorCodeURIFunction();
    private Function errorInfoFunction = new ErrorInfoFunction();
    private Function errorSourceIDfunction = new ErrorSourceIDFunction();

    private ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
    
    private final static String ERROR_MESSAGE  = "Error message.";
    private final static String ERROR_CODENAME = "Error code name.";
    private final static String ERROR_CODEURI  = "Error code URI.";
    private final static String ERROR_SOURCEID = "Error source ID.";

    private final static String STRING_PROPERTY_NAME = "String property name.";
    private final static String INT_PROPERTY_NAME = "Int property name.";
    private final static String STRING_PROPERTY_VALUE = "String property value.";
    private final static Integer INT_PROPERTY_VALUE = new Integer(71830);

    /**
    * Expression context.
    */
    private SimpleExpressionContext context = (SimpleExpressionContext) factory
           .createExpressionContext(new SimpleEnvironmentInteractionTracker(),
                   new DefaultNamespacePrefixTracker());

    // javadoc inherited
    public void setUp() throws Exception {
        super.setUp();
        Map errorProperties = new HashMap();

        errorProperties.put(STRING_PROPERTY_NAME, STRING_PROPERTY_VALUE);
        errorProperties.put(INT_PROPERTY_NAME, INT_PROPERTY_VALUE);

        TryProcess process = new TryProcess(null);
        XMLPipelineException e = new XMLPipelineException(ERROR_MESSAGE, null);
        e.initErrorInfo(ERROR_SOURCEID, ERROR_CODEURI, ERROR_CODENAME, errorProperties);
        process.addException(e);
        context.pushObject(process, false);
    }

    protected void tearDown() throws Exception {
        context.popObject();
        super.tearDown();
    }

    /**
    * Tests invocation.
    */
    public void testErrorMessage() throws Exception {

       StringValue expected = factory.createStringValue(ERROR_MESSAGE);
       Value result =
           errorMessageFunction.invoke(context, new Value[0]);
       assertEquals(expected, result);
    }

    /**
    * Tests error code name.
    */
    public void testErrorCodeName() throws Exception {

       StringValue expected = factory.createStringValue(ERROR_CODENAME);
       Value result =
           errorCodeNameFunction.invoke(context, new Value[0]);
       assertEquals(expected, result);
    }

    /**
    * Tests error code URI.
    */
    public void testErrorCodeURI() throws Exception {

       StringValue expected = factory.createStringValue(ERROR_CODEURI);
       Value result =
           errorCodeURIfunction.invoke(context, new Value[0]);
       assertEquals(expected, result);
    }

    /**
    * Tests error info.
    */
    public void testErrorInfo() throws Exception {
        StringValue propertyName = factory.createStringValue(STRING_PROPERTY_NAME);
        Value result =
            errorInfoFunction.invoke(context, new Value[]{propertyName});
        assertEquals(factory.createStringValue(STRING_PROPERTY_VALUE), result);

        propertyName = factory.createStringValue(INT_PROPERTY_NAME);
        result =
            errorInfoFunction.invoke(context, new Value[]{propertyName});
        assertEquals(factory.createStringValue(INT_PROPERTY_VALUE.toString()), result);
    }

    /**
    * Tests error source ID.
    */
    public void testErrorSourceID() throws Exception {

       StringValue expected = factory.createStringValue(ERROR_SOURCEID);
       Value result =
           errorSourceIDfunction.invoke(context, new Value[0]);
       assertEquals(expected, result);
    }

    /**
    * Test outside try.
    */
    public void testOutsideTry() throws Exception {
        //Simulates end of try block
        context.popObject();

        try {
            errorSourceIDfunction.invoke(context, new Value[0]);
            fail("Exception was expected");
        } catch (ExpressionException x) {
            //OK
        }

        try {
            errorCodeURIfunction.invoke(context, new Value[0]);
            fail("Exception was expected");
        } catch (ExpressionException x) {
            //OK
        }

        try {
            errorCodeNameFunction.invoke(context, new Value[0]);
            fail("Exception was expected");
        } catch (ExpressionException x) {
            //OK
        }

        try {
            errorMessageFunction.invoke(context, new Value[0]);
            fail("Exception was expected");
        } catch (ExpressionException x) {
            //OK
        }

        StringValue propertyName = factory.createStringValue(STRING_PROPERTY_NAME); 
        try {
            errorInfoFunction.invoke(context, new Value[]{propertyName});
            fail("Exception was expected");
        } catch (ExpressionException x) {
            //OK
        }

    }
}
