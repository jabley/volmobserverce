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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.layout;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.FormatReferenceFinder;
import com.volantis.mcs.expression.FormatReferenceValueImpl;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;


/**
 * A pipeline expression to convert an iterated format specification to a
 * FormatReferenceValue which is a class that encapsulates a FormatReference.
 * The expression is of the type 'namespace:get[Format]Instance(name,idx1,...)'
 * where each index references an embedded iterated format.
 *
 * <p>The resulting FormatReference will contain the correct number of
 * dimensions (i.e. number of indices) for the targeted format, with missing
 * indices set to zero and any surplus indices being discarded.</p>
 */
public class GetIteratedFormatInstanceFunction implements Function {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(GetIteratedFormatInstanceFunction.class);

    /**
     * The namespace to find format instances within.
     */ 
    private FormatNamespace namespace;
    
    /**
     * Initializes the new instance.
     * 
     * @param namespace the namespace to find format instances within.
     */
    public GetIteratedFormatInstanceFunction(FormatNamespace namespace) {
        this.namespace = namespace;
    }

    /**
     * Convert a get[Format]Instance exression into a FormatReference and 
     * return it as a <code>FormatReferenceValue</code> object.
     *
     * @param expressionContext
     *               the context in the expression is called
     * @param values each parameter stored as a Value object
     * @return the format reference value
     */
    public Value invoke(ExpressionContext expressionContext, Value[] values)
        throws ExpressionException {
        // We need at least 1 parameter
        if ((values == null) || (values.length < 1)) {
            logger.error("invalid-getInstance-call", new Object[] {namespace.getName()});

            throw new ExpressionException(
                    "Call to get" +  namespace.getName() + "Instance with no " +
                    "arguments.");
        }

        // Extract the mariner request context from the expression context and
        // the page context from there
        MarinerRequestContext requestContext =
                (MarinerRequestContext)expressionContext.getProperty(
                        MarinerRequestContext.class);
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);

        // The first parameter is the stem name of the format
        String name = values[0].stringValue().asJavaString();

        // Convert the values array int indeces.
        int[] indeces = new int[values.length - 1];
        for (int i = 0; i < indeces.length; i += 1) {
            indeces[i] = getIntValue(values[i + 1]);
        }

        FormatReferenceFinder formatReferenceFinder =
                pageContext.getFormatReferenceFinder();
        FormatReference reference =
                formatReferenceFinder.getFormatReference(name, indeces);

        return new FormatReferenceValueImpl(
                expressionContext.getFactory(), reference);
    }

    /**
     * Read a single Value and return it as an integer.
     *
     * @param value the value to be converted into an integer
     * @return the integer equivalent to the given value
     */
    private int getIntValue(Value value) throws ExpressionException {
        int formatIndex = -1;

        if (value instanceof IntValue) {
            formatIndex = ((IntValue) value).asJavaInt();
        } else if (value instanceof DoubleValue) {
            double d = ((DoubleValue) value).asJavaDouble();

            formatIndex = (int) d;
        } else {
            try {
                formatIndex =
                    Integer.valueOf(value.stringValue().asJavaString()).
                        intValue();
            } catch (NumberFormatException e) {
                formatIndex = -1;
            }
        }

        if (formatIndex == -1) {
            logger.error("invalid-format-index", new Object[]{value.stringValue().asJavaString()});

            throw new ExpressionException(value.stringValue().asJavaString() +
                    " is not a valid format index.");
        }

        return formatIndex;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 30-Jul-04	4713/13	geoff	VBM:2004061004 Support iterated Regions (supermerge again)

 02-Jul-04	4713/11	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/9	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	3999/2	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 07-Jan-04	2389/5	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/2	steve	VBM:2003121701 Pre-test save

 ===========================================================================
*/
