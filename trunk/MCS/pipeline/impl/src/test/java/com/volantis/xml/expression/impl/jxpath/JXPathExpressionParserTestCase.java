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

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import junit.framework.TestCase;

/**
 * TestCase for the JXPathExpressionParser class.
 */
public class JXPathExpressionParserTestCase extends TestCase {
    /**
     * Instance of class that is being tested
     */
    private JXPathExpressionParser parser;

    /**
     * The factory to be used in all object creations
     */
    private ExpressionFactory factory = new JXPathExpressionFactory();

    /**
     * creates a new JXPathExpressionParserTestCase instance 
     * @param name the name 
     */
    public JXPathExpressionParserTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        parser = new JXPathExpressionParser(factory);
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        parser = null;
    }

    /**
     * test that a variable reference can be parsed 
     * @throws Exception if an error occurs
     */
    public void testVariableReferenceParse() throws Exception {
        Expression exp = parser.parse("$variable");
        assertNotNull("JXPathExpressionParser should be able to parse a " +
                      "variable reference expression", exp);
    }

    /**
     * Ensures that a qualified variable can be parsed  
     * @throws Exception if an error occurs
     */
    public void testQualifiedVariableReferenceParse() throws Exception {
        Expression exp = parser.parse("$prefix:myVar");
        assertNotNull("JXPathExpressionParser should be able to parse a " +
                      " qualified variable", exp);
    }

    /**
     * Ensures that the arithmetic operators can be parsed
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testArithmeticOperatorsParse() throws Exception {
        Expression exp = parser.parse("- 1 + 2 * 3 div 4 mod 5 - 6");
        assertNotNull("JXPathExpressionParser should be able to parse the "
                + " arithmetic operators", exp);
    }

    /**
     * Ensures that an ExpressionException is thrown if an xpath location path
     * is parsed
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void testLocationPathParse() throws Exception {
        try {
            parser.parse("/root/leaf");

            fail("should not have successfully parsed a location path " +
                 "expression");
        } catch (ExpressionException e) {
            // Expected condition
        }
    }

    /**
     * Ensures that an xpath function expression can be parsed
     * @throws Exception if an error occurs
     */
    public void testFunctionParse() throws Exception {
        Expression exp = parser.parse("testFunction($myVar)");

        assertNotNull("JXPathExpressionParser should be able to parse a " +
                      "function expression", exp);

    }

    /**
     * Ensures that an ExpressionException is thrown if an xpath
     * or operation is parsed 
     * @throws Exception if an error occurs
     */
    public void testOrOperationParse() throws Exception {
        try {
            parser.parse("/location or $MyVar");

            fail("should not be able to parse an \"or\" operation expression");
        } catch (ExpressionException e) {
            // Expected condition
        }
    }

    /**
     * Ensures that an ExpressionException is thrown if an xpath
     * literal is parsed 
     * @throws Exception if an error occurs
     */
    public void testLiteralParse() throws Exception {
        try {
            parser.parse("literal");

            fail("should not be able to parse a literal expression");
        } catch (ExpressionException e) {
            // Expected condition
        }
    }

    /**
     * Ensures that an ExpressionException is thrown if an invalid
     * xpath reference variable expression is parsed 
     * @throws Exception if an error occurs
     */
    public void testInvalidVariableReferenceParse() throws Exception {
        try {
            parser.parse("$:myVar");

            fail("should not be able to parse a invalid qualified variable");
        } catch (ExpressionException e) {
            // Expected condition
        }
    }

    /**
     * Ensures that an ExpressionException is thrown if an xpath
     * expression with illegal syntax is parsed 
     * @throws Exception if an error occurs
     */
    public void testInvalidSyntaxParse() throws Exception {
        try {
            parser.parse("@@@@@@@@@");

            fail("an exception should be thrown when an illegal " +
                 "expression is parsed");
        } catch (ExpressionException e) {
            // Expected condition
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jan-06	10855/3	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 28-Dec-05	10855/1	pszul	VBM:2005121508 Arithmetic operators +, -, *, div, mod and unary- added to pipeline expressions.

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/5	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jun-03	102/1	sumit	VBM:2003061906 request:getParameter XPath function support

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
