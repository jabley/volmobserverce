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

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionParser;
import junit.framework.TestCase;

/**
 * Unit test for the JXPathExpressionFactory class
 */ 
public class JXPathExpressionFactoryTestCase extends TestCase {
    
    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * Reference to the instance of the JXPathExpressionFactory class that
     * is being tested.
     */ 
    private JXPathExpressionFactory factory;
    
    /**
     * Creates a new JXPathExpressionFactoryTestCase instance
     * @param name the name
     */ 
    public JXPathExpressionFactoryTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = new JXPathExpressionFactory();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the createParser() method 
     * @throws Exception if an  error is encountered
     */ 
    public void testCreateParser() throws Exception {
        // ensure the correct object is created        
        ExpressionParser parser = factory.createExpressionParser();
        assertTrue("JXPathExpressionFactory createParser() should create " +
                   "JXPathExpressionParser instance", 
                   (parser instanceof JXPathExpressionParser));
    }
        
    /**
     * Tests the createContext() method 
     * @throws Exception if an  error is encountered
     */
    public void testCreateContext() throws Exception {
        // ensure the correct object is created
        // @todo fix parameters
        ExpressionContext context = factory.createExpressionContext(null, null);
        assertTrue("JXPathExpressionFactory createContext() should create " +
                   "JXPathExpressionContext instance", 
                   (context instanceof JXPathExpressionContext));
    }    
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
