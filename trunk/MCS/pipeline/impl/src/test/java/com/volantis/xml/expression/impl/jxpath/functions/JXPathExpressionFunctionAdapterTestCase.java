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
 
package com.volantis.xml.expression.impl.jxpath.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.impl.jxpath.JXPathExpressionFactory;
import com.volantis.xml.expression.sequence.Item;
import junit.framework.TestCase;
import our.apache.commons.jxpath.JXPathContext;
import our.apache.commons.jxpath.Pointer;

import java.util.List;

/**
 * Tests the functionality of the adapter between our ExpressionFunction 
 * class and the JXPathFunctions class. 
 */
public class JXPathExpressionFunctionAdapterTestCase extends TestCase {
    /**
     * Simple test function
     */
    private class TestFunction implements Function {
        ExpressionFactory factory;

        public TestFunction(ExpressionFactory factory) {
            this.factory = factory;
        }

        public Value invoke(ExpressionContext context,
                            Value[] parameters) {
            try {
                StringBuffer result = new StringBuffer();

                for (int i = 0;
                     i < parameters.length;
                     i++ ) {
                    result.append(parameters[i].stringValue().asJavaString());

                    if (i < parameters.length - 1) {
                        result.append(':');
                    }
                }

                return factory.createStringValue(result.toString());
            } catch (ExpressionException e) {
                throw new IllegalArgumentException(
                    "had an expression exception " + e);
            }
        }
    }

    /**
     * Supporting JXPath expression context
     */
    private class TestExpressionContext implements
            our.apache.commons.jxpath.ExpressionContext {
        public JXPathContext getJXPathContext() {
            return null;
        }

        public Pointer getContextNodePointer() {
            return null;
        }

        public List getContextNodeList() {
            return null;
        }

        public int getPosition() {
            return 0;
        }
    }
    
    public void testParameters() throws Exception {
        ExpressionFactory factory = new JXPathExpressionFactory();
        TestFunction testFunction = new TestFunction(factory);
        // @todo later fix parameters
        ExpressionContext context = factory.createExpressionContext(null, null);
        JXPathExpressionFunctionAdapter adapter =
                new JXPathExpressionFunctionAdapter(factory,
                                                    testFunction,
                                                    context);
        Item[] items = {factory.createIntValue(1),
                        factory.createIntValue(2),
                        factory.createStringValue("3")};
        int[] ints = {42, 255, 7};
        String[] strings = {"ho", "ho", "little", "boy"};
        Object[] paramList = {new Integer(0),
                              "fred",
                              new Double(2.4),
                              new String(),
                              new Boolean(true),
                              factory.createBooleanValue(false),
                              factory.createSequence(items),
                              factory.createStringValue("abc"),
                              ints,
                              strings,
                              items};
        Value result = (Value)adapter.invoke(
            new TestExpressionContext(), paramList);

        assertEquals("result of parameters call not as",
                     "0" + ":" +
                     "fred" + ":" +
                     "2.4" + ":" +
                     "" + ":" +
                     "true" + ":" +
                     "false" + ":" +
                     "1 2 3" + ":" +
                     "abc" + ":" +
                     "42 255 7" + ":" +
                     "ho ho little boy" + ":" +
                     "1 2 3",
                     result.stringValue().asJavaString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jun-03	102/1	sumit	VBM:2003061906 request:getParameter XPath function support

 ===========================================================================
*/
