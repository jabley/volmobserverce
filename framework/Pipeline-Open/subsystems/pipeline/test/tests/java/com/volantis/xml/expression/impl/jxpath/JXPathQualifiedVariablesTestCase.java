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
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import junit.framework.TestCase;

/**
 * TestCase for the JXPathQualifiedVaraibles class. 
 */ 
public class JXPathQualifiedVariablesTestCase extends TestCase {
    /**
     * Reference to an instance of the class being tested
     */ 
    private JXPathQualifiedVariables variables;

    /**
     * The factory needed by the variables instance
     */
    private ExpressionFactory expressionFactory;

    /**
     * The namespace prefix tracker needed by the variables instance
     */
    private NamespacePrefixTracker namespacePrefixTracker;

    /**
     * Creates a new JXPathQualifiedVariablesTestCase instance.
     * @param name
     */ 
    public JXPathQualifiedVariablesTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        // @todo later could use mock factory and namespace prefix tracker
        expressionFactory = new JXPathExpressionFactory();
        namespacePrefixTracker = new DefaultNamespacePrefixTracker();
        variables = new JXPathQualifiedVariables(expressionFactory,
                                                 namespacePrefixTracker);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        variables = null;
        namespacePrefixTracker = null;
        expressionFactory = null;
    }

    /**
     * Test the pushVariableStackFrame() method
     * @throws Exception if an error occurs
     */ 
    public void testPushVariableStackFrame() throws Exception {
        
        assertTrue("Stack frame count should be 0", 
                   variables.getStackFrameCount() == 0);
        
        variables.pushStackFrame();
        
        assertTrue("Stack frame should count should increment by 1", 
                   variables.getStackFrameCount() == 1);
    }
    
    /**
     * Test the popVariableStackFrame() method
     * @throws Exception if an error occurs
     */ 
    public void testPopVariableStackFrame() throws Exception {
        assertTrue("Stack frame count should be 0",
                   variables.getStackFrameCount() == 0);
        
        try {
            variables.popStackFrame();

            fail("Should have had an exception trying to pop a frame");
        } catch (IllegalStateException e) {
            // Expected condition
        }
        
        variables.pushStackFrame();

        assertTrue("Stack frame should count should increment by 1",
                   variables.getStackFrameCount() == 1);
        
        variables.popStackFrame();
        
        assertTrue("Stack frame should count should decrement by 1", 
                   variables.getStackFrameCount() == 0);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
