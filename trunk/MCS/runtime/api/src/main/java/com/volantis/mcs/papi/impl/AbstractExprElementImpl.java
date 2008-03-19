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
 * $Header: /src/voyager/com/volantis/mcs/papi/BlockElement.java,v 1.15 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.ExprAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;

/**
 * This abstract class implements functionality which is common for all elements
 * which take an 'expr' attribute.
 */
public abstract class AbstractExprElementImpl
        extends AbstractStyledElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory
                    .createExceptionLocalizer(AbstractExprElementImpl.class);

    /**
     * Flag which indicates whether the elementStart method returned
     * SKIP_ELEMENT_BODY.
     */
    private boolean skipped;

    /**
     * Create a new <code>BlockElement</code>.
     */
    public AbstractExprElementImpl() {
    }

    /**
     * Create a new <code>BlockElement</code>.
     *
     * @param isStylingEnabled Flag which indicates whether the styling
     *                         should be performed or not.
     */
    public AbstractExprElementImpl(boolean isStylingEnabled) {
        super(isStylingEnabled);
    }

    /**
     * As we now have a new elementStart() method that handles expressions, code
     * that use to go in elementStart() can now go here.
     */
    protected abstract int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    /**
     * As we now have a new elementEnd() method that handles expressions, code
     * that use to go in elementEnd() can now go here.
     */
    protected abstract int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    // javadoc inherited.
    public int styleElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        int returnCode;

        if (evaluateExpression(context, papiAttributes)) {
            skipped = false;
            returnCode = exprElementStart(context, papiAttributes);
        } else {
            skipped = true;
            returnCode = SKIP_ELEMENT_BODY;
        }

        return returnCode;
    }

    // javadoc inherited.
    public int styleElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {
        int returnCode;

        if (!skipped) {
            returnCode = exprElementEnd(context, papiAttributes);
        } else {
            returnCode = CONTINUE_PROCESSING;
        }
        return returnCode;
    }

    /**
     * Evaluate an expression and return the result as a boolean.
     * <p>If the the expr attribute is present and the expression it contains
     * evaluates to a non-null, non-empty string that is not equal to false
     * then we return true</p><p>If the expr attribute is not present, true is
     * returned.
     *
     * @param context        The MarinerRequestContext within which this element is
     *                       being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the implementation of PAPIElement.
     * @return boolean true if the expression evaluates to true, false otherwise
     * @throws PAPIException If there was a problem processing the element.
     */
    boolean evaluateExpression(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        boolean result;

        // Get the expression to be evaluated
        ExprAttributes attributes = (ExprAttributes) papiAttributes;
        String expression = attributes.getExpr();

        if ("".equals(expression)) {
            throw new PAPIException(exceptionLocalizer.format(
                    "cannot-evaluate-empty-expression"));
        } else if (expression != null) {
            try {
                Expression expr = MCSExpressionHelper.createUnquotedExpression(
                        expression, context);
                result = MCSExpressionHelper.evaluateBooleanExpression(expr,
                        context);
            } catch (ExpressionException e) {
                // MCSPA0007X="Could not evaluate expression: {1}"
                throw new PAPIException(
                        exceptionLocalizer.format(
                                "expression-evaluation-failure",
                                expression),
                        e);
            }

        } else {
            result = true;
        }
        return result;
    }

    protected String getPlainText(TextAssetReference object) {
        return object == null ? null : object.getText(TextEncoding.PLAIN);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Jun-05	8878/5	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 23-Jun-05	8483/8	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/6	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/3	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	958/4	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 ===========================================================================
*/
