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
package com.volantis.mcs.expression;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;

/**
 * This class provides JSP specific helper methods for the creation and
 * manipulation of {@link Expression}s.
 */
public class MCSExpressionHelper {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MCSExpressionHelper.class);

    /**
     * Create an {@link Expression} from the provided unquoted expression based
     * upon the contextual expression information retrieved from the specified
     * MarinerRequestContext.
     *
     * @param expression The unquoted string expression to get as an {@link
     *      Expression}. See AN025 for the definition of an unquoted expression.
     * @param requestContext The MarinerRequestContext from which we retrieve
     *      the contextual pipeline information required to create an
     *      Expression.
     * @return An {@link Expression} created from the specified String.
     * @todo later Should not be creating an ExpressionParser every time
     */
    public static Expression createUnquotedExpression(String expression,
            MarinerRequestContext requestContext)
            throws ExpressionException {

        ExpressionContext expressionContext =
                getExpressionContext(requestContext);

        return createUnquotedExpression(expressionContext, expression);
    }

    /**
     * Create an {@link Expression} from the provided unquoted expression based
     * upon the contextual expression information retrieved from the specified
     * MarinerRequestContext.
     *
     * @param expression The unquoted string expression to get as an {@link
     *      Expression}. See AN025 for the definition of an unquoted expression.
     * @param expressionContext The expression context from which we retrieve
     *      the contextual pipeline information required to create an
     *      Expression.
     * @return An {@link Expression} created from the specified String.
     * @todo later Should not be creating an ExpressionParser every time
     */
    private static Expression createUnquotedExpression(
            ExpressionContext expressionContext, String expression)
            throws ExpressionException {

        Expression result = null;

        if (expressionContext != null) {
            ExpressionFactory expressionFactory =
                expressionContext.getFactory();

            ExpressionParser parser =
                expressionFactory.createExpressionParser();

            result = parser.parse(expression);
        } else {
            logger.warn("missing-expression-context");
        }

        return result;
    }

    /**
     * Evaluate the given expression to a boolean.
     *
     * @param expr The expression
     * @param requestContext The MarinerRequestContext
     * @return The boolean evaluation
     * @throws ExpressionException A problem with the evaluation.
     */
    public static boolean evaluateBooleanExpression(Expression expr,
        MarinerRequestContext requestContext) throws ExpressionException {

        boolean result;

        ExpressionContext exprContext =
            getExpressionContext(requestContext);
        // Evaluate the expression
        Value value = expr.evaluate(exprContext);

        // Convert the expression to a boolean.
        result =
            PipelineExpressionHelper.equals(BooleanValue.TRUE.getSequence(),
                                    value.getSequence());

        if (logger.isDebugEnabled()) {
            logger.debug("expression: " + expr + " result:"
                 + result);
        }

        return result;
    }

    /**
     * Helper method that returns the ExpressionContext from the request's
     * environment context.
     * @param requestContext The MarinerRequestContext
     * @return ExpressionContext
     */
    public static ExpressionContext getExpressionContext(
                MarinerRequestContext requestContext) {
        // This method was created to aid code readability.
        final EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(requestContext);

        // Make this a bit forgiving for the many tests that do not set up
        // an environment context. A hack really but no time to fix them all.
        if (environmentContext != null) {
            return environmentContext.getExpressionContext();
        } else {
            return null;
        }
    }

    /**
     * Helper method that stores the ExpressionContext in the request's
     * environment context. This allows a request's ExpressionContext to be
     * shared across all sub-requests. Note that a request always has an
     * environment context.
     *
     * @param requestContext The <code>MarinerRequestContext</code>.
     * @param expressionContext The ExpressionContext that is to be associated
     *                          with the request context
     */
    public static void setExpressionContext(
            MarinerRequestContext requestContext,
            ExpressionContext expressionContext) {

        // The expression context needs to set this property so that expressions
        // work when rendered at runtime. 
        expressionContext.setProperty(MarinerRequestContext.class,
                                      requestContext,
                                      false);
        // There is always an environment context associated with a request in
        // a properly functioning system!
        // This method was created to aid code readability.
        ContextInternals.getEnvironmentContext(requestContext).
                setExpressionContext(expressionContext);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 10-Mar-05	6852/1	geoff	VBM:2005020206 R821: Branding using Projects (Umbrella)

 17-Feb-05	6957/3	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/2	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 07-Oct-04	5239/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 07-Oct-04	5237/4	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/4	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	2966/2	ianw	VBM:2004011923 Added mcsi:policy function

 11-Aug-03	1013/3	chrisw	VBM:2003080806 Refactored expressions to be jsp independent

 05-Aug-03	928/1	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 ===========================================================================
*/
