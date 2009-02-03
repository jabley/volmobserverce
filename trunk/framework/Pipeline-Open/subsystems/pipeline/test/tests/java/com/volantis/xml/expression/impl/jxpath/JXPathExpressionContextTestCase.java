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
package com.volantis.xml.expression.impl.jxpath;


import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import junit.framework.TestCase;

/**
 * TestCase for the JXPathExpressionContext class.
 */
public class JXPathExpressionContextTestCase extends TestCase {
    /**
     * Instance of the class that we are testing
     */
    private JXPathExpressionContext context;

    /**
     * The factory to use in all object creations
     */
    private ExpressionFactory factory = new JXPathExpressionFactory();

    /**
     * An Value reference.
     */ 
    private Value result;

    
    /**
     * Creates a new JXPathExpressionContextTestCase instance.
     * @param name the name
     */
    public JXPathExpressionContextTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        // @todo fix parameters
        context = new JXPathExpressionContext(
            factory,
            null,
            new DefaultNamespacePrefixTracker());
        result = createExpressionValue();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * factory method for creating Value instances.
     * @return An Value instance.
     */ 
    protected Value createExpressionValue() {
        return factory.createStringValue("TestValue");
    }

    /**
     * Test the pushStackFrame() method.
     * @throws Exception if an error occurs.
     */
    public void testPushStackFrame() throws Exception {
        JXPathQualifiedVariables variables =
            (JXPathQualifiedVariables)
                context.getJXPathContext().getVariables();

        assertTrue(
                "variable stack frame count should have initial value of 0",
                variables.getStackFrameCount() == 0);

        context.pushStackFrame();

        assertTrue(
                "variable stack frame count should increment by 1",
                variables.getStackFrameCount() == 1);

    }

    /**
     * Test the popStackFrame() method.
     * @throws Exception if an error occurs.
     */
    public void testPopStackFrame() throws Exception {
        JXPathQualifiedVariables variables =
            (JXPathQualifiedVariables)
                context.getJXPathContext().getVariables();

        // ensure that an IllegalStateException is thrown if we
        // try to pop an empty stack
        try {
            context.popStackFrame();

            fail("An exception should have been thrown when popping an " +
                 "empty stack");
        } catch (IllegalStateException e) {
            // Expected condition
        }

        context.pushStackFrame();

        assertTrue(
                "variable stack frame count should increment by 1",
                variables.getStackFrameCount() == 1);

        context.popStackFrame();

        assertTrue(
                "variable stack frame count should decrement by 1",
                variables.getStackFrameCount() == 0);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/6	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/4	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 18-Jun-03	100/1	sumit	VBM:2003061602 Converted all references to org.apache to our.apache

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
