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
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.dynamic.PassThroughController;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * XMLProcess that evaluates attribute values that are defined as XPath
 * type expressions. If an attribute value is an expression that value
 * is updated with the evaluated expression string.
 */
public class ExpressionProcess
        extends XMLProcessImpl
        implements PassThroughController {

    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * Flag that indicates whether this process is in pass through mode.
     */
    private boolean inPassThroughMode;

    /**
     * ExpressionParser used to parse xpath like expressions
     */
    private ExpressionParser parser;

    /**
     * Possible prefixes for expressions.
     */
    private final String[] exprPrefixes;

    /**
     * Possible suffixes for expressions.
     */
    private final String[] exprSuffixes;

    /**
     * Possible excaped prefixes for expressions.
     */
    private final String[] exprEscapedPrefixes;

    /**
     * Possible excaped suffixes for expressions.
     */
    private final String[] exprEscapedSuffixes;

    /**
     * Initializes a new instance of this class, using default expression
     * markup.
     */
    public ExpressionProcess() {
        this(EMPTY_STRING_ARRAY,
             EMPTY_STRING_ARRAY,
             EMPTY_STRING_ARRAY,
             EMPTY_STRING_ARRAY);
    }

    /**
     * Initializes a new instance of this class using the given prefix and
     * suffix information.
     *
     * @param exprPrefixes possible prefixes for expressions.
     * @param exprSuffixes possible suffixes for expressions.
     * @param exprEscapedPrefixes possible escaped prefixes for expressions.
     * @param exprEscapedSuffixes possible escaped suffixes for expressions.
     */
    public ExpressionProcess(String[] exprPrefixes,
                             String[] exprSuffixes,
                             String[] exprEscapedPrefixes,
                             String[] exprEscapedSuffixes) {
        this.inPassThroughMode = false;

        if ((exprPrefixes.length != exprSuffixes.length) ||
            (exprEscapedPrefixes.length != exprEscapedSuffixes.length)) {
            throw new IllegalArgumentException("Prefixes and suffixes must" +
                                               " be the same length");
        }

        this.exprPrefixes = copyStringArray(exprPrefixes);
        this.exprSuffixes = copyStringArray(exprSuffixes);
        this.exprEscapedPrefixes = copyStringArray(exprEscapedPrefixes);
        this.exprEscapedSuffixes = copyStringArray(exprEscapedSuffixes);
    }

    // javadoc inherited
    public void startPassThrough() {
        inPassThroughMode = true;
    }

    // javadoc inherited
    public void stopPassThrough() {
        inPassThroughMode = false;
    }

    // javadoc inherited from interface
    public void startProcess() throws SAXException {
        // initalize the parser that will be use to parse XPath expressions
        XMLPipelineFactory pipelineFactory =
                getPipelineContext().getPipelineFactory();
        ExpressionFactory expressionFactory =
                pipelineFactory.getExpressionFactory();
        parser = expressionFactory.createExpressionParser();
    }

    // javadoc inherited from interface
    public void stopProcess() throws SAXException {
        parser = null;
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {

        // if we are in pass through mode we do not want to process the
        // attributes
        Attributes attributes =
                (inPassThroughMode) ? atts : processAttributes(atts);

        // pass this event on to the next process with the potentially
        // processed attributes
        super.startElement(namespaceURI,
                           localName,
                           qName,
                           attributes);
    }

    /**
     * Processes the <code>Attributes</code> provided. This processing
     * involves determining whether the attribute is represented via an
     * Expression, if it is the expression is evaluated and the result
     * is used to overwrite the attributes existing value
     * @param input the Attributes to process
     * @return an Attributes instance
     * @throws SAXException if an error occurs
     */
    public Attributes processAttributes(Attributes input) throws SAXException {

        // Take a look at each attribute to see if they are xpath expressions
        AttributesImpl output = null;
        String value = null;

        // we can potentially remove an attribute(s) from the Attributes
        // object. To avoid having to adjust the index whilst iterating over
        // the Attributes instance we will start at the end Attributes and
        // iterate backwards.
        for (int i = input.getLength() - 1; i >= 0; i--) {
            value = input.getValue(i);
            // an expression is always encoded as follows
            // %{expression}
            if (PipelineExpressionHelper.isPipelineQuotedExpression(
                    value, exprPrefixes, exprSuffixes)) {
                // The current attribute is an xpath expression that
                // needs to be evaluated.
                if (null == output) {
                    // the Attributes instance passed in is immutable. As
                    // we need to modify the current attribute copy all the
                    // attributes into an AttributesImpl instance
                    // (AttributesImpl instances are not immutable).
                    output = new AttributesImpl(input);
                }
                // strip off the %{ } so that we are left with the xpath
                String expressionStr =
                        PipelineExpressionHelper.removePipelineQuoting(
                                value, exprPrefixes, exprSuffixes);
                XMLPipelineContext pipelineContext = getPipelineContext();
                try {
                    // parse the xpath

                    Expression expression = parser.parse(expressionStr);
                    // obtain the ExpressionContext from the PipelineContext
                    // the ExpressionContext is needed in order to evaluate the
                    // expression parsed
                    ExpressionContext expressionContext =
                            getPipelineContext().getExpressionContext();

                    // evaluate the experssion
                    Value result = expression.evaluate(expressionContext);
                    if (result.getSequence().getLength() == 0) {
                        // if the sequence is empty then we must remove the
                        // attriubute.
                        output.removeAttribute(i);
                    } else {
                        // update the current attribute with the string
                        // representation of the evaluated expression.
                        output.setValue(i, result.stringValue().asJavaString());
                    }
                }
                catch (Exception e) {
                    // something went wrong when evaluating the xpath.
                    // Send an error down the pipeline
                    Locator locator = pipelineContext.getCurrentLocator();
                    XMLPipelineException se = new XMLPipelineException(
                            "Could not evaluate the expression " +
                            expressionStr,
                            locator,
                            e);

                    fatalError(se);
                }
            } else if (PipelineExpressionHelper.isEscapedPipelineQuotedExpression(
                    value, exprEscapedPrefixes, exprEscapedSuffixes)) {
                // found an escaped %{} remove the escape character '\'
                if (null == output) {
                    output = new AttributesImpl(input);
                }
                output.setValue(
                        i, PipelineExpressionHelper.getUnescapedPipelineQuotedExpression(value));
            }
        }
        // if we haven't evaluated any expressions then return the attributes
        // that were passed in to this method. Otherwise return the
        // modified attributes
        return (null != output) ? output : input;
    }

    // javadoc inherited
    public void release() {
        inPassThroughMode = false;
        parser = null;
    }

    /**
     * Copies the array into a new array.
     * @param sArray the array to copy.
     * @return a copy of sArray.
     */
    private static String[] copyStringArray(String[] sArray) {
        String[] copy = new String[sArray.length];
        System.arraycopy(sArray,
                         0,
                         copy,
                         0,
                         copy.length);
        return copy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8751/3	schaloner	VBM:2005060711 Updated code style

 15-Jun-05	8751/1	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Oct-03	435/2	doug	VBM:2003091902 Expressions that evaluate to an empty sequence are represented with null rather than empty string

 11-Aug-03	275/3	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 01-Aug-03	258/3	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 19-Jun-03	90/1	adrian	VBM:2003061606 Added Expression support to Tag attributes

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
