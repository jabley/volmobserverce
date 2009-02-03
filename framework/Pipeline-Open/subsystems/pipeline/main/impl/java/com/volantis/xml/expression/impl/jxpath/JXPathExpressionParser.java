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

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.impl.jxpath.compiler.JXPathCompiler;
import our.apache.commons.jxpath.JXPathException;
import our.apache.commons.jxpath.ri.Compiler;
import our.apache.commons.jxpath.ri.JXPathCompiledExpression;
import our.apache.commons.jxpath.ri.parser.ParseException;
import our.apache.commons.jxpath.ri.parser.TokenMgrError;
import our.apache.commons.jxpath.ri.parser.XPathParser;

import java.io.StringReader;

/**
 * JXPath implementation of the ExpressionParser interface.
 */
public class JXPathExpressionParser implements ExpressionParser {

    /**
     * Logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(JXPathExpressionParser.class);

    /**
     * The JXPath compiler that will be use to compile XPaths.
     * OK to declare as static as the compiler retains no state and
     * is thread safe.
     */
    private static Compiler COMPILER = new JXPathCompiler();

    /**
     * The factory by which this parser was created.
     */
    private ExpressionFactory factory;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory the factory by which this parser was created
     */
    protected JXPathExpressionParser(ExpressionFactory factory) {
        this.factory = factory;
    }

    // javadoc inherited
    public Expression parse(String expression) throws ExpressionException {
        try {
            // Parse the expression and return an API expression wrapping the
            // JXPath compiled expression
// commented out while we investigate why it causes lock ups under some circumstances.
//            our.apache.commons.jxpath.ri.compiler.Expression e =
//                    (our.apache.commons.jxpath.ri.compiler.Expression)
//                    Parser.parseExpression(expression, getCompiler());

            XPathParser parser = new XPathParser(new StringReader(expression));
            parser.setCompiler(getCompiler());
            our.apache.commons.jxpath.ri.compiler.Expression e =
                    (our.apache.commons.jxpath.ri.compiler.Expression)
                            parser.parseExpression();

            // return the Expression object
            return new JXPathExpression(
                    factory,
                    new JXPathCompiledExpression(expression, e));
        } catch (TokenMgrError tme) {
            // if parsing failed then throw an expression exception
            throw new ExpressionException(tme);
        } catch (JXPathException e) {
            // if parsing failed then throw an expression exception
            throw new ExpressionException(e);
        } catch (ParseException e) {
            LOGGER.error("exception-during-expression-parse", expression, e);
            throw new ExpressionException(e);
        }
    }

    /**
     * Factory method to create a compiler that can be used to compile XPath
     * expressions.
     *
     * @return a Compiler instance.
     */
    protected Compiler getCompiler() {
        return COMPILER;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/4	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 27-Jun-03	102/4	sumit	VBM:2003061906 Defunct ServletRequestFunction

 25-Jun-03	102/2	sumit	VBM:2003061906 request:getParameter XPath function support

 18-Jun-03	100/1	sumit	VBM:2003061602 Converted all references to org.apache to our.apache

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
